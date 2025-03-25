package com.project.webserver.controller.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class GoogleMapsClient implements IClient{
    static HttpClient client = HttpClient.newHttpClient();
    String root = "https://routes.googleapis.com/directions/v2:computeRoutes";
    public Gson doPost(String url, Map input) {
        ObjectMapper mapper = new ObjectMapper();
        String inputString = "";
        HttpResponse<String> response = null;
        try {
            inputString = mapper.writeValueAsString(input);
        } catch (Exception e) {
            //logger info for failure, throw runtime exception
            e.printStackTrace();
            throw new RuntimeException("Error deserializing input object to string");
        }
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).POST(HttpRequest.BodyPublishers.ofString(inputString))
                .header("Content-Type", "application/json")
                .header("X-Goog-Api-Key", "key-here")
                .header("X-Goog_FieldMask", "routes.duration,routes.distanceMeters,routes.ployline.encodedPolyLine")
                .build();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            //logging exceptions that could have occurred
            //throw runtimeexecption
        }
        int code = response.statusCode();
        if (code == 200) {
            return new Gson().fromJson(response.body(), new TypeToken<HashMap<String, String>>(){}.getType());
        }
        else {
            //returned code x
            //print body
            //throw exception
        }
        return null;
    }

    public Gson getRoute(Map input) {
        return doPost(root, input);
    }

}
