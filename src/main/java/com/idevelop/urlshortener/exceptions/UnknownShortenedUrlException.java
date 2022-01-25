package com.idevelop.urlshortener;

import java.net.URL;

public class UnknownShortenedUrlException extends Throwable {
    public UnknownShortenedUrlException(String id) {
        super("Unknown URL with ID: " + id);
    }
    public UnknownShortenedUrlException(URL url){
        super("Unknown URL: " + url);
    }
}
