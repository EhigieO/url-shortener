package com.idevelop.urlshortener.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.net.URL;

@Document
@AllArgsConstructor
@Getter
public class RegisteredUrl {
    private final String id;
    private final URL url;
}
