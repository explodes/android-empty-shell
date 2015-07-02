package io.explod.android.emptyshell.network;


public enum ApiEndpoint {

    PRODUCTION("Production", "prod://"),
    STAGING("Staging", "stage://"),
    DEVELOPMENT("Development", "dev://"),
    MOCK_MODE("Mock Mode", "mock://");

    public final String friendlyName;
    public final String serviceHost;

    ApiEndpoint(String friendlyName, String serviceHost) {
        this.friendlyName = friendlyName;
        this.serviceHost = serviceHost;
    }

    @Override
    public String toString() {
        return friendlyName;
    }

}