package com.haitaos.microservices.currencyexchangeservice.controller;

import com.haitaos.microservices.currencyexchangeservice.entity.CurrencyExchange;
import com.haitaos.microservices.currencyexchangeservice.repository.CurrencyExchangeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CurrencyExchangeController {

    private CurrencyExchangeRepository currencyExchangeRepository;

    private Environment environment;

    private Logger logger =
            LoggerFactory.getLogger(CircuitBreakerController.class);

    public CurrencyExchangeController(Environment environment,
                                      CurrencyExchangeRepository currencyExchangeRepository) {
        this.currencyExchangeRepository = currencyExchangeRepository;
        this.environment = environment;
    }
    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange retrieveExchangeValue (
            @PathVariable String from,
            @PathVariable String to) {
        this.logger.info("retrieveExchangeValue called with {} to {}", from, to);

        CurrencyExchange currencyExchange = currencyExchangeRepository.findByFromAndTo(from, to);
        if (currencyExchange == null) {
            throw new RuntimeException("Unable to Find data for " + from + " to " + to);
        }
        String port = environment.getProperty("local.server.port");

        String host = environment.getProperty("HOSTNAME");
        String version = "v2";
        currencyExchange.setEnvironment(port + " " + host + " " + version);

        return currencyExchange;
    }
}
