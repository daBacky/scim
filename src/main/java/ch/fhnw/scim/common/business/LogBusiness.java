package ch.fhnw.scim.common.business;

import ch.fhnw.scim.common.log.config.RouteEndpoints;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogBusiness {

    @Autowired
    private CamelContext camelContext;

    public void info(String message) {
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(message);

        ProducerTemplate template = camelContext.createProducerTemplate();
        template.send(RouteEndpoints.LOG_ROUTE_INFO.uri(), exchange);
    }

    public void error(String message) {
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(message);

        ProducerTemplate template = camelContext.createProducerTemplate();
        template.send(RouteEndpoints.LOG_ROUTE_ERROR.uri(), exchange);
    }
}
