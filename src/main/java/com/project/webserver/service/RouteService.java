package com.project.webserver.service;

import com.project.webserver.model.RouteRequest;
import com.project.webserver.model.TrafficSignalDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class RouteService {

	private static final String GOOGLE_MAPS_API_URL = "https://maps.googleapis.com/maps/api/directions/json";

	private final RestTemplate restTemplate;

	@Value("${dali.base-url}")
	private String daliBaseUrl;

	@Autowired
	public RouteService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public String createRoute(RouteRequest routeRequest) {
		String origin = routeRequest.getOriginLatitude() + "," + routeRequest.getOriginLongitude();
		String destination = routeRequest.getDestinationLatitude() + "," + routeRequest.getDestinationLongitude();
		String apiKey = "";

		String url = UriComponentsBuilder.fromHttpUrl(GOOGLE_MAPS_API_URL)
				.queryParam("origin", origin)
				.queryParam("destination", destination)
				.queryParam("key", apiKey)
				.toUriString();

		String response = restTemplate.getForObject(url, String.class);
		System.out.println("Start Response:" + response);
		return response;
	}

	public TrafficSignalDTO fetchSignalFromDali(String signalId) {
		String url = daliBaseUrl + "/" + signalId;
		return restTemplate.getForObject(url, TrafficSignalDTO.class);
	}
}
