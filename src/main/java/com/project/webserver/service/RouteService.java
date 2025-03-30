package com.project.webserver.service;
import com.project.webserver.model.RouteRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class RouteService {
	private static final String GOOGLE_MAPS_API_URL = "https://maps.googleapis.com/maps/api/directions/json";

	public String createRoute(RouteRequest routeRequest) {
		String origin = routeRequest.getOriginLatitude() + "," + routeRequest.getOriginLongitude();
		String destination = routeRequest.getDestinationLatitude() + "," + routeRequest.getDestinationLongitude();
		String apiKey = "YOUR_API_KEY";

		String url = UriComponentsBuilder.fromHttpUrl(GOOGLE_MAPS_API_URL)
				.queryParam("origin", origin)
				.queryParam("destination", destination)
				.queryParam("key", apiKey)
				.toUriString();

		RestTemplate restTemplate = new RestTemplate();
		String response = restTemplate.getForObject(url, String.class);
		System.out.println("Start Respomnse:"+response);
		// Return the entire response, which will include the polyline
		return response;
	}
}