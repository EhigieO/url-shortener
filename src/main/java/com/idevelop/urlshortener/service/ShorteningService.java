package com.idevelop.urlshortener.service;

import com.idevelop.urlshortener.UnknownShortenedUrlException;
import com.idevelop.urlshortener.UrlShorteningException;
import com.idevelop.urlshortener.dao.RegisteredUrlDao;
import com.idevelop.urlshortener.generator.UrlIdentifierGenerator;
import com.idevelop.urlshortener.model.RegisteredUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;

@Service
public class ShorteningService {
    @Autowired
    protected URL baseUrl;

    @Autowired
    private UrlIdentifierGenerator urlIdentifierGenerator;

    @Autowired
    private RegisteredUrlDao registeredUrlDao;

    public URL shortenUrl(String url) throws UrlShorteningException {
        try {
            final URL urlToShorten = new URL(url);
            return retrieveOrCreateRegisteredUrl(urlToShorten);
        } catch (MalformedURLException e) {
            throw new UrlShorteningException("The URL to shorten is invalid: " + url, e);
        }
    }

    public URL retrieveOrCreateRegisteredUrl(URL urlToShorten) throws MalformedURLException {
        try {
            return buildCompleteShortenedUrl(registeredUrlDao.findRegisteredUrl(urlToShorten));
        } catch (UnknownShortenedUrlException e) {
            final RegisteredUrl registeredUrl = new RegisteredUrl(
                    urlIdentifierGenerator.generate(),
                    urlToShorten
            );
            registeredUrlDao.createRegisteredUrl(registeredUrl);
            return buildCompleteShortenedUrl(registeredUrl);
        }
    }

    private URL buildCompleteShortenedUrl(RegisteredUrl registeredUrl) throws MalformedURLException {
        return new URL(baseUrl.getProtocol(), baseUrl.getHost(), baseUrl.getPort(), "/" + registeredUrl.getId());
    }

    public URL resolveUrl(String urlId) throws UnknownShortenedUrlException {
        final RegisteredUrl registeredUrl = registeredUrlDao.getRegisteredUrlById(urlId);
        return registeredUrl.getUrl();
    }
}
