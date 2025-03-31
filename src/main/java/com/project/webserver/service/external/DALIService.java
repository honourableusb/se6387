package com.project.webserver.service.external;

import com.project.webserver.model.dali.Coordinate;
import com.project.webserver.model.dali.DALIAgent;
import com.project.webserver.model.dali.LightColor;
import com.project.webserver.service.FirebaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.project.webserver.service.Constants.DALI_AGENT_COUNT;

public class DALIService {

    Map<String, DALIAgent> agents;
    FirebaseService firebaseService;

    public DALIService() {
        populateAgents();
        this.firebaseService = new FirebaseService();
    }

    public ResponseEntity getControllers() {
        if (agents != null) {
            return ResponseEntity.ok(agents);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No DALI Agents registered");
        }
    }

    public ResponseEntity getController(String id) {
        DALIAgent agent = agents.get(id);
        if (agent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agent %s does not exist".formatted(id));
        }
        return ResponseEntity.ok(agent);
    }

    public ResponseEntity updateController(String id, String username, Coordinate coordinate) {
        DALIAgent agent = agents.get(id);
        if (agent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agent %s does not exist".formatted(id));
        }
        //TODO validate user exists do we want this?
        if (!userExists(username)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
        }
        agent.addVehicle(username, coordinate);
        return ResponseEntity.ok("Successfully added user to agent queue");
    }

    public boolean userExists(String username) {
        return this.firebaseService.getUser(username) != null;
    }

    private void populateAgents() {
        agents = new HashMap<>();
        for(int i = 0; i < DALI_AGENT_COUNT; i++){
            DALIAgent agent = new DALIAgent();
            agent.setId("DALI#" + i);
            agent.setVehicles(new HashMap<>());
            agent.setColor(LightColor.RED);
            agent.setTimings(generateTimings());
            agent.setTimeRemaining();
            agent.run();
            agents.put(agent.getId(), agent);
            //TODO put initial timings in

        }
    }

    private Map<LightColor, Integer> generateTimings() {
        Random rand = new Random();
        Map<LightColor, Integer> timings = new HashMap<>();
        timings.put(LightColor.GREEN, rand.nextInt(30, 61));
        timings.put(LightColor.YELLOW, rand.nextInt(3, 7));
        timings.put(LightColor.RED, rand.nextInt(27, 68));
        return timings;
    }
}
