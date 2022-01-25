package com.idevelop.urlshortener.generator;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

class Base62TimeStampUrlIdentifierGeneratorTest {

    @Test
    public void should_generateShorterThan10AlphanumericCharactersUniqueIds() throws Exception {
        // Given
        Base62TimeStampUrlIdentifierGenerator generator = new Base62TimeStampUrlIdentifierGenerator();
        Set<String> urlIds = new HashSet<>();
        int numberOfIds = 10_000;

        // When
        for (int i = 0; i < numberOfIds; i++) {
            urlIds.add(generator.generate());
        }

        // Then
        assertThat(urlIds).hasSize(numberOfIds);
        assertThat(urlIds).are(new Condition<String>(s -> s.length() <= 10, "shorter than 10 characters"));
        assertThat(urlIds).are(new Condition<String>((String s) -> {
            Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
            return pattern.matcher(s).matches();
        }, "only composed of alphanumeric characters"));
    }
}