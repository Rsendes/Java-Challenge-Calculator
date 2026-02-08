package com.wit.rest;

import com.wit.common.api.CalcRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest
class RestApplicationTests {

    @MockBean
    private KafkaTemplate<String, CalcRequest> kafkaTemplate;

    @Test
    void contextLoads() {}
}
