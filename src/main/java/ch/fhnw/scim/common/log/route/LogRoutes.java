package ch.fhnw.scim.common.log.route;

import ch.fhnw.scim.common.log.config.RouteEndpoints;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class LogRoutes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        onException(Exception.class)
                .logStackTrace(true)
                .handled(true)
                .stop();

        from(RouteEndpoints.LOG_ROUTE_INFO.uri()).routeId(RouteEndpoints.LOG_ROUTE_INFO.routeId())
                .log(LoggingLevel.INFO, "${body}").id("InfoLogger")
                .end();

        from(RouteEndpoints.LOG_ROUTE_ERROR.uri()).routeId(RouteEndpoints.LOG_ROUTE_ERROR.routeId())
                .log(LoggingLevel.ERROR, "${body}").id("ErrorLogger")
                .end();

    }
}