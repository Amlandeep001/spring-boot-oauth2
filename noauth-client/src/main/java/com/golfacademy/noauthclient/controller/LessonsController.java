package com.golfacademy.noauthclient.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
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
	private final URI baseUrl;

	public LessonsController(RestClient.Builder builder, @Value("${resource.server.baseUrl}") URI baseUrl)
	{
		this.baseUrl = baseUrl;
		this.restClient = builder
				.baseUrl(baseUrl)
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
				.uri("/lessons")
				.retrieve()
				.body(String.class);
	}
}