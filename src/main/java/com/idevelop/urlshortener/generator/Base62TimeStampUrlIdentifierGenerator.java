package com.idevelop.urlshortener.generator;

import com.idevelop.urlshortener.util.Base62;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class Base62TimeStampUrlIdentifierGenerator implements UrlIdentifierGenerator{
    protected AtomicInteger counter = new AtomicInteger();
    @Override
    public String generate() {
        final int counterValue = counter.getAndUpdate((operand) -> (operand + 1) % 1000);
        final long base10id = Long.valueOf(""+ counterValue + System.currentTimeMillis());
        return Base62.fromBase10(base10id);
    }
}
