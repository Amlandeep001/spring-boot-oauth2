package com.golfacademy.noauthclient.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class LessonsController
{
	private final RestClient restClient;

	public LessonsController(RestClient.Builder builder)
	{
		this.restClient = builder
				.baseUrl("http://localhost:8081")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) ->
				{
					if(response.getStatusCode() == HttpStatus.UNAUTHORIZED)
					{
						throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access to lessons API");
					}
					throw new ResponseStatusException(response.getStatusCode(), "Client error occurred");
				})
				.defaultStatusHandler(HttpStatusCode::is5xxServerError, (request, response) ->
				{
					throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
							"Downstream service error: " + response.getStatusCode());
				})
				.build();
	}

	@GetMapping("/lessons")
	public String fetchLessons()
	{
		return restClient.get()
				.uri("http://localhost:8081/lessons")
				.retrieve()
				.body(String.class);
	}
}