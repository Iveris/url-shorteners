package com.warneriveris.urlshorteners.service;

import java.time.Duration;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.warneriveris.urlshorteners.WebException;
import com.warneriveris.urlshorteners.shorteners.UrlShortener;

import io.netty.handler.timeout.TimeoutException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class ShorterUrlService {

	private final WebClient client;
	
	public ShorterUrlService(UrlShortener shortener) {
		this.client = WebClient
				.builder()
				.baseUrl(shortener.getWebClientBaseUrl())
				.filter(ExchangeFilterFunction.ofResponseProcessor(response -> exchangeFilterResponseProcessor(response)))
				.build();
	}
	
	public String getShortenedUrl() {
		return client
				.get()
				.retrieve()
				.bodyToMono(String.class)
				.retryWhen(getRetryPolicy())
				.timeout(Duration.ofSeconds(2))
				.doOnError(TimeoutException.class, ex -> System.out.println("Timed out"))
				.block();
	}
	
	
	private Mono<ClientResponse> exchangeFilterResponseProcessor(ClientResponse response) {
		
		HttpStatus status = response.statusCode();
		
		if( status.is4xxClientError() ) {
			return response.bodyToMono(String.class).flatMap(body -> Mono.error(
					new WebException("Client Error " + response.statusCode() + "\n" + body, status)));
		}
		if( status.is5xxServerError() ) {
			return response.bodyToMono(String.class).flatMap(body -> Mono.error(
					new WebException("Client Error " + response.statusCode() + "\n" + body, status)));
		}
		
		return Mono.just(response);
	}
	
	private Retry getRetryPolicy() {
		return Retry.backoff(2, Duration.ofMillis(1500)).jitter(0.5)
				.filter(throwable -> throwable instanceof WebException )
				.onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
					new WebException("Retries Exhausted " + HttpStatus.SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE));
	}
	
}
