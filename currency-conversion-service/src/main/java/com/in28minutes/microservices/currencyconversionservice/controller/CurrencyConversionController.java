package com.in28minutes.microservices.currencyconversionservice.controller;


import com.in28minutes.microservices.currencyconversionservice.CurrencyExchangeProxy;
import com.in28minutes.microservices.currencyconversionservice.model.CurrencyConversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CurrencyConversionController {

    @Autowired
    private Environment environment;

    @Autowired
    private CurrencyExchangeProxy proxy;

    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion retrieveConversionValue(@PathVariable String from,
                                                      @PathVariable String to,
                                                      @PathVariable BigDecimal quantity) {

        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);
        ResponseEntity<CurrencyConversion> response  = new RestTemplate().getForEntity("http://currency-exchange:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class, uriVariables);
        CurrencyConversion currencyExchange = response.getBody();
        CurrencyConversion currencyConversion = new CurrencyConversion(1000l, from, to,
                currencyExchange.getConversionMultiple(), quantity,
                currencyExchange.getConversionMultiple().multiply(quantity),
                currencyExchange.getEnvironment() + "Rest Template");

        return currencyConversion;
    }

    @GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion retrieveConversionValueFeign(@PathVariable String from,
                                                      @PathVariable String to,
                                                      @PathVariable BigDecimal quantity) {

        CurrencyConversion currencyExchange = proxy.retrieveCurrencyExchange(from, to);
        CurrencyConversion currencyConversion = new CurrencyConversion(1000l, from, to,
        currencyExchange.getConversionMultiple(), quantity, currencyExchange.getConversionMultiple().multiply(quantity), currencyExchange.getEnvironment() + "Fegin");

        return currencyConversion;
    }
}
