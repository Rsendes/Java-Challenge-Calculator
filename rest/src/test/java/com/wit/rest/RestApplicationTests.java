package com.wit.rest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@SpringBootTest
class RestApplicationTests {

    @Configuration
    @Import({}) // no beans that depend on Kafka
    static class TestConfig {
        // empty on purpose
    }

    @Test
    void contextLoads() {
        // Spring context can start without any Kafka beans
    }
}
