package ch.fhnw.scim.common.log.config;

public enum RouteEndpoints {
    LOG_ROUTE_INFO("direct:logInfo", "Write info log"),
    LOG_ROUTE_ERROR("direct:logError", "Write error log");

    private final String uri;
    private final String routeId;

    RouteEndpoints(String uri, String routeId) {
        this.uri = uri;
        this.routeId = routeId;
    }

    public String uri() {
        return this.uri;
    }

    public String routeId() {
        return this.routeId;
    }
}
