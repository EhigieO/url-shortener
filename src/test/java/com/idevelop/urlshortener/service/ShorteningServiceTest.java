package com.idevelop.urlshortener.service;

import com.idevelop.urlshortener.UnknownShortenedUrlException;
import com.idevelop.urlshortener.UrlShorteningException;
import com.idevelop.urlshortener.dao.RegisteredUrlDao;
import com.idevelop.urlshortener.generator.UrlIdentifierGenerator;
import com.idevelop.urlshortener.model.RegisteredUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

class ShorteningServiceTest {
    @Mock
    private UrlIdentifierGenerator urlIdentifierGenerator;

    @Mock
    private RegisteredUrlDao registeredUrlDao;

    private URL baseUrl;

    @InjectMocks
    private ShorteningService service;

    @BeforeEach
    public void setUp() throws Exception {
        service = new ShorteningService();
        baseUrl = new URL("http://cl.ip");
        service.baseUrl = baseUrl;
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_generateShortenedUrl_when_urlIsValidAndDoesNotExist(){
        //given
        URL urlToShorten = null;
        try {
            urlToShorten = new URL("https://google.fr");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String simpleId = "qwerty1234";
        when(urlIdentifierGenerator.generate()).thenReturn(simpleId);
        try {
            when(registeredUrlDao.findRegisteredUrl(urlToShorten)).thenThrow(new UnknownShortenedUrlException(""));
        } catch (UnknownShortenedUrlException e) {
            e.printStackTrace();
        }

        // When
        URL shortenedUrl = null;
        try {
            assert urlToShorten != null;
            shortenedUrl = service.shortenUrl(urlToShorten.toString());
        } catch (UrlShorteningException e) {
            e.printStackTrace();
        }

        // Then
        assertNotNull(shortenedUrl);
        assertEquals(shortenedUrl.toString(),(baseUrl + "/" + simpleId));
    }

    @Test
    public void should_notGenerateNewShortenedUrl_when_urlAlreadyExists() {
        // Given
        RegisteredUrl registeredUrl = null;
        try {
            registeredUrl = new RegisteredUrl("qwerty1234", new URL("http://google.fr"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            when(registeredUrlDao.findRegisteredUrl(registeredUrl.getUrl())).thenReturn(registeredUrl);
        } catch (UnknownShortenedUrlException e) {
            e.printStackTrace();
        }

        // When
        URL shortenedUrl = null;
        try {
            shortenedUrl = service.shortenUrl(registeredUrl.getUrl().toString());
        } catch (UrlShorteningException e) {
            e.printStackTrace();
        }

        // Then
        assertNotNull(shortenedUrl);
        assertEquals(shortenedUrl.getPath(),("/" + registeredUrl.getId()));
    }

    @Test
    public void should_throwException_when_urlIsNotValid() {
        // Given
        String urlToShorten = "BadProtocol:/~B@dH0st]::BadPort";

        // When
        assertThrows(UrlShorteningException.class, () -> {
            service.shortenUrl(urlToShorten);
        });
    }

    @Test
    public void should_resolveShortenedUrl_when_urlIdExists() {
        // Given
        String urlId = "qwerty1234";
        URL url = null;
        try {
            url = new URL("http://google.fr");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            when(registeredUrlDao.getRegisteredUrlById(urlId)).thenReturn(new RegisteredUrl(urlId, url));
        } catch (UnknownShortenedUrlException e) {
            e.printStackTrace();
        }

        // When
        URL originalUrl = null;
        try {
            originalUrl = service.resolveUrl(urlId);
        } catch (UnknownShortenedUrlException e) {
            e.printStackTrace();
        }

        // Then
        assertNotNull(originalUrl);
        assertEquals(originalUrl,url);
    }
}