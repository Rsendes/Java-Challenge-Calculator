package com.wit.calculator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.wit.calculator.kafka.CalcRequestListener;


@SpringBootTest
class CalculatorApplicationTests {

	@MockBean
    private CalcRequestListener listener;

	@Test
	void contextLoads() {
	}

}
