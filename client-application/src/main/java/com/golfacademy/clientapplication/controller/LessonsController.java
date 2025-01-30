package com.golfacademy.clientapplication.controller;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
public class LessonsController
{
	private final RestClient restClient;
	private final URI uri;

	public LessonsController(RestClient restClient, @Value("${resource.server.uri}") URI uri)
	{
		this.restClient = restClient;
		this.uri = uri;
	}

	@GetMapping("/lessons")
	public String fetchLessons()
	{
		return restClient.get()
				.uri(uri)
				.attributes(clientRegistrationId("golf-client"))
				.retrieve()
				.body(String.class);
	}
}