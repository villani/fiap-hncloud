package br.com.qrdapio.cucumber;

import br.com.qrdapio.QrDapioApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = QrDapioApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
