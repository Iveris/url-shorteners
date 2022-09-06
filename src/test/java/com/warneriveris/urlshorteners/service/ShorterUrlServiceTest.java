package com.warneriveris.urlshorteners.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

import com.warneriveris.urlshorteners.shorteners.UrlShortener;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@ExtendWith(MockitoExtension.class)
public class ShorterUrlServiceTest {
    
    static MockWebServer mockWebServer;
    
    @Mock
    UrlShortener shortener;
    
    final String BASE_URL = "http://localhost:8081";
    
    @BeforeAll
    public static void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(8081);
    }
    
    @AfterAll
    public static void shutdown() throws IOException {
        mockWebServer.shutdown();
    }
    
    
    @Test
    public void ValidUrl_ShorterUrlService_Success() {
        
        when(shortener.getWebClientBaseUrl()).thenReturn(BASE_URL);
        ShorterUrlService shorterUrl = new ShorterUrlService(shortener);
        
        mockWebServer.enqueue(new MockResponse().setBody("www.shorterurl.com/123abc")
                                                .addHeader(HttpHeaders.CONTENT_TYPE, "text/html; charset=UTF-8"));
        
        var response = shorterUrl.getShortenedUrl();      
        assertEquals(response,"www.shorterurl.com/123abc");
    }

}
