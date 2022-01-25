package com.idevelop.urlshortener.generator;

import org.springframework.stereotype.Component;

@Component
public interface UrlIdentifierGenerator {
    String generate();
}
