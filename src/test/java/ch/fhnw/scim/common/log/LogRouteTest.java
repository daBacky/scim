package ch.fhnw.scim.common.log;

import ch.fhnw.scim.Application;
import ch.fhnw.scim.common.log.config.RouteEndpoints;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(classes = Application.class)
public class LogRouteTest {

    @EndpointInject(uri = "mock:logger")
    private MockEndpoint loggerEndpoint;

    @Autowired
    private CamelContext camelContext;

    private ProducerTemplate template;

    private static final String BODY = "Test Content";

    @Before
    public void setUp() throws Exception {
        prepareRouteMock();
        template = camelContext.createProducerTemplate();
    }

    @Test
    public void skalRouteTilKvitteringKoe() throws Exception {
        loggerEndpoint.expectedMessageCount(1);

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(BODY);
        template.send(RouteEndpoints.LOG_ROUTE_INFO.uri(), exchange);

        loggerEndpoint.assertIsSatisfied();

        String body = loggerEndpoint.getReceivedExchanges().get(0).getIn().getBody(String.class);
        Assert.assertEquals(BODY, body);
    }

    private void prepareRouteMock() throws Exception {
        camelContext.getRouteDefinition(RouteEndpoints.LOG_ROUTE_ERROR.routeId()).adviceWith(camelContext,
            new AdviceWithRouteBuilder() {
                @Override
                public void configure() throws Exception {
                    weaveById("Logger")
                        .replace()
                        .to(loggerEndpoint);
                }
            });
    }
}
