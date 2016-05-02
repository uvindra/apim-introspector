package org.wso2.sample.synapse.introspection;

public class IntrospectConstants {
    public static final String INTROSPECT_PROPERTY = "INTROSPECT_ENABLED";
    public static final String UNSUPPORTED_CONTENT_TYPE_PROPERTY = "UNSUPPORTED_CONTENT_TYPE";
    public static final String LOG_MSG_ID_PROPERTY = "LOG_MSG_ID";

    public static final String SOURCE_REQUEST_KEY = "pass-through.Source-Request";
    public static final String TARGET_RESPONSE_KEY = "pass-through.Target-Response";

    public static final String INTROSPECT_HEADER = "X-Introspect";
    public static final String LOG_APPENDER = "_INTROSPECT_OUTPUT_";
    public static final String PASSTHROUGH_PIPE_PROPERTY = "pass-through.pipe";
    public static final String HOST_HEADER_PROPERTY = "Host";
    public static final String USER_AGENT_HEADER_PROPERTY = "User-Agent";
    public static final String USER_AGENT_GATEWAY_HEADER_VALUE = "Synapse-PT-HttpComponents-NIO";
    public static final String SERVER_HEADER_PROPERTY = "Server";
    public static final String SERVER_GATEWAY_HEADER_VALUE = "WSO2-PassThrough-HTTP";
    public static final String CONTENT_TYPE_PROPERTY = "ContentType";

    public static final String LOG_CLIENT_TO_GATEWAY_DIRECTION = ">>>>>>>>>>>>>>>>>>>>>>>>> CLIENT_TO_GATEWAY >>>>>>>>>>>>>>>>>>>>>>>>>";
    public static final String LOG_GATEWAY_TO_BACKEND_DIRECTION = ">>>>>>>>>>>>>>>>>>>>>>>>> GATEWAY_TO_BACKEND >>>>>>>>>>>>>>>>>>>>>>>>>";
    public static final String LOG_BACKEND_TO_GATEWAY_DIRECTION = "<<<<<<<<<<<<<<<<<<<<<<<<< BACKEND_TO_GATEWAY <<<<<<<<<<<<<<<<<<<<<<<<";
    public static final String LOG_GATEWAY_TO_CLIENT_DIRECTION = "<<<<<<<<<<<<<<<<<<<<<<<<< GATEWAY_TO_CLIENT <<<<<<<<<<<<<<<<<<<<<<<<<";
}
