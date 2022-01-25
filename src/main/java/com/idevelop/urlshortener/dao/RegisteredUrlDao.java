package com.idevelop.urlshortener.dao;

import com.idevelop.urlshortener.UnknownShortenedUrlException;
import com.idevelop.urlshortener.model.RegisteredUrl;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
public interface RegisteredUrlDao {
    RegisteredUrl getRegisteredUrlById(String id) throws UnknownShortenedUrlException;
    void createRegisteredUrl(RegisteredUrl registeredUrl);
    RegisteredUrl findRegisteredUrl(URL url) throws UnknownShortenedUrlException;
}
