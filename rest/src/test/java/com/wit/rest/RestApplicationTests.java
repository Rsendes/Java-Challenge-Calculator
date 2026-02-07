package com.wit.rest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.wit.rest.kafka.TestProducer;


@SpringBootTest
class RestApplicationTests {

	@MockBean
    private TestProducer testProducer;

	@Test
	void contextLoads() {
	}

}
