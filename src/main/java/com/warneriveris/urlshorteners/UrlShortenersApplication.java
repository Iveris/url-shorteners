package com.warneriveris.urlshorteners;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.warneriveris.urlshorteners.shorteners.TinyUrl;
import com.warneriveris.urlshorteners.shorteners.UrlShortener;

import java.util.Scanner;


@SpringBootApplication
public class UrlShortenersApplication {
	

	public static void main(String[] args) {
		SpringApplication.run(UrlShortenersApplication.class, args);
	
		UrlShortener shortener;
		
		// Get User Input
		Scanner scanner = new Scanner(System.in);
		System.out.println("Choose a URL Shortening Service:\n1) TinyURL.com\n2) is.gd");
		int shortenerChoice = scanner.nextInt();
		
		System.out.println("\nNow type the URL you wish to shorten:");
		String urlToShorten = scanner.next();

		switch(shortenerChoice) {
		case 1: shortener = new TinyUrl(urlToShorten);
		default: shortener = new TinyUrl(urlToShorten);
		}
		
		// Return Shortened URL
		System.out.println(shortener.getShortenedUrl());
		
		// Clean up and close application
		scanner.close();
		System.exit(0);
		
	}

}

