package com.warneriveris.urlshorteners.shorteners;

import com.warneriveris.urlshorteners.service.ShorterUrlService;

public class TinyUrl implements UrlShortener{

	private final String baseUrl= "http://tinyurl.com/api-create.php";
	private String urlToShorten;
	private ShorterUrlService shorterUrlService;
	
	public TinyUrl(String urlToShorten) {
		setUrl(urlToShorten);
		shorterUrlService = new ShorterUrlService(this);
	}
	
	private void setUrl(String URL) {
		this.urlToShorten = baseUrl + "?url=" + URL;
	}

	public String getWebClientBaseUrl() {
		return this.urlToShorten;
	}

	@Override
	public String getShortenedUrl() {
		return shorterUrlService.getShortenedUrl();
	}

}
