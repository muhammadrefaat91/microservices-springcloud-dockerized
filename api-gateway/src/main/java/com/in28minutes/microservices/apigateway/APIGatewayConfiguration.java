package com.in28minutes.microservices.apigateway;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class APIGatewayConfiguration {

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        //customize routes if url gets with /get
        Function<PredicateSpec, Buildable<Route>> routeFunc = predicateSpec ->
                predicateSpec
                        .path("/get")
                        .filters(f-> f.addRequestHeader("myheader", "h1"))
                        .uri("http://httpbin.org:80");
        return builder.routes()
                .route(routeFunc)
                //if uri has currency-exchange so direct this uri by naming server
                // find the service name match it and also do load balancing
                .route(p -> p.path("/currency-exchange/**")
                        .uri("lb://currency-exchange"))
                .route(p -> p.path("/currency-conversion-feign/**")
                        .uri("lb://currency-conversion"))
                .route(p -> p.path("/currency-conversion/**")
                        .uri("lb://currency-conversion"))
                .build();
    }

}
