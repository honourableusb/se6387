package com.project.webserver.service.external;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.webserver.model.dali.*;
import com.project.webserver.service.FirebaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static com.project.webserver.service.Constants.DALI_AGENT_COUNT;
import static com.project.webserver.service.Constants.DALI_COORDINATE_FILE;

public class DALIService {

    Map<String, DALIAgent> agents;
    Map<String, Coordinate> agentLocations;
    FirebaseService firebaseService;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public DALIService() {
        populateAgents();
        this.firebaseService = new FirebaseService();
    }

    public ResponseEntity<Object> getControllers() {
        if (agents != null) {
            logger.info("Getting all DALI controllers");
            return ResponseEntity.ok(agents.values().stream().map(DALIAgent::toString).collect(Collectors.toList()));
        }
        else {
            logger.error("No DALI Agents registered");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No DALI Agents registered");
        }
    }

    public ResponseEntity<Object> getController(String id) {
        DALIAgent agent = agents.get(id);
        if (agent == null) {
            logger.error("Agent %s does not exist".formatted(id));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agent %s does not exist".formatted(id));
        }
        logger.info("Getting DALI controller %s".formatted(id));
        return ResponseEntity.ok(agent.toString());
    }

    public ResponseEntity<Object> updateController(String id, String username, UserLocation location) {
        DALIAgent agent = agents.get(id);
        if (agent == null) {
            logger.error("Agent %s does not exist".formatted(id));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agent %s does not exist".formatted(id));
        }
        if (!userExists(username)) {
            logger.error("User does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
        }
        logger.info("Adding/adjusting vehicle %s in DALI Agent %s's queue".formatted(username, id));
        //check location and heading, determine if vehicle has passed this dali agent
        if (agent.getVehicles().containsKey(username) && agent.hasUserPassed(location, username)) { //vehicle was in the queue already and has passed the agent
            //user has passed the DALI agent, send them the next one
            String next = agent.getNeighbor(location.getHeading());
            UpdateControllerResponse resp = null;
            if (next == null || next.isEmpty()) {
                logger.info("Vehicle has passed and no neighbor exists");
                resp = new UpdateControllerResponse.UpdateControllerResponseBuilder()
                        .message("Vehicle has passed and no neigbor exists.")
                        .status(UpdateStatus.PASSED).build();
            }
            else {
                logger.info("Vehicle has passed DALI agent. Next agent: %s".formatted(next));
                //update next agent
                DALIAgent nextAgent = agents.get(next);
                nextAgent.addVehicle(username, location);
                resp = new UpdateControllerResponse.UpdateControllerResponseBuilder()
                        .message("Vehicle has passed. Next Agent provided.")
                        .nextId(next).status(UpdateStatus.PASSED).build();
            }
            return ResponseEntity.ok(resp);
        }
        else {
            //TODO if vehicle already exists don't adjust any timings. if so, adjust timings
            agent.addVehicle(username, location);
            logger.info("Successfully updated user in agent queue");
            UpdateControllerResponse resp = new UpdateControllerResponse.UpdateControllerResponseBuilder()
                    .message("Successfully updated user in agent queue")
                    .status(UpdateStatus.IN_PROGRESS).build();
            return ResponseEntity.ok(resp);
        }
    }

    public boolean userExists(String username) {
        return this.firebaseService.getUser(username) != null;
    }

    private void populateAgents() {
        Map<String, DALIAgentLocationData> coordinateMap = loadCoordinatesFromResource();
        agents = new HashMap<>();
        for(int i = 0; i < DALI_AGENT_COUNT; i++){
            String id = "DALI#" + i;
            logger.info("Creating DALI agent %s".formatted(id));
            DALIAgentLocationData locationData = coordinateMap.get(id);
            DALIAgent agent = new DALIAgent();
            agent.setId(id);
            agent.setVehicles(new LinkedHashMap<>());
            agent.setColor(LightColor.RED);
            agent.setTimings(generateTimings());
            agent.setTimeRemaining();
            agent.setLocation(locationData.getCoordinate());
            agent.setNeighbors(locationData.getNeighbors());
            agent.start();
            agents.put(agent.getAgentID(), agent);
        }
//        calculateNeighbors();
    }

    private Map<LightColor, Integer> generateTimings() {
        Random rand = new Random();
        Map<LightColor, Integer> timings = new HashMap<>();
        timings.put(LightColor.GREEN, rand.nextInt(30, 61));
        timings.put(LightColor.YELLOW, rand.nextInt(3, 7));
        timings.put(LightColor.RED, rand.nextInt(27, 68));
        return timings;
    }

    private Map<String, DALIAgentLocationData> loadCoordinatesFromResource() {
        Map<String, DALIAgentLocationData> coordinates = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        logger.info("Loading coordinates from resource file %s".formatted(DALI_COORDINATE_FILE));
        try {
            JsonNode node = mapper.readTree(new File(DALI_COORDINATE_FILE)).get("agents");
            coordinates = mapper.convertValue(node, new TypeReference<>(){});
        } catch (Exception e) {
            logger.error("Error loading coordinates from resource file: ", e);
            throw new RuntimeException(e);
        }
        return coordinates;
    }
}
