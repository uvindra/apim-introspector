package org.wso2.sample.synapse.introspection;

import org.apache.axis2.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ProtocolVersion;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.rest.AbstractHandler;
import org.apache.synapse.transport.passthru.Pipe;
import org.apache.synapse.transport.passthru.SourceRequest;
import org.apache.synapse.transport.passthru.TargetResponse;

import java.util.Iterator;
import java.util.Map;


public class APIIntrospectHandler extends AbstractHandler {

    private static final Log log = LogFactory.getLog(APIIntrospectHandler.class);

    @Override
    public boolean handleRequest(MessageContext mc) {
        org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) mc).getAxis2MessageContext();

        SourceRequest sourceRequest = (SourceRequest) axis2MessageContext.getProperty(IntrospectConstants.SOURCE_REQUEST_KEY);

        Map headers = (Map) sourceRequest.getHeaders();

        if (headers.containsKey(IntrospectConstants.INTROSPECT_HEADER) &&
                                        0 == "true".compareToIgnoreCase((String) headers.get(IntrospectConstants.INTROSPECT_HEADER))) {
            mc.setProperty(IntrospectConstants.INTROSPECT_PROPERTY, true);

            String url = sourceRequest.getUri();
            String method = sourceRequest.getMethod();

            String logIDString = IntrospectConstants.LOG_APPENDER + "||" + axis2MessageContext.getLogCorrelationID() + "||";
            mc.setProperty(IntrospectConstants.LOG_MSG_ID_PROPERTY, logIDString);

            log.info(logIDString + IntrospectConstants.LOG_CLIENT_TO_GATEWAY_DIRECTION);
            log.info(logIDString + "===================HTTPHeaders==================================");
            for (final Iterator entries = headers.entrySet().iterator(); entries.hasNext();) {
                Map.Entry entry = (Map.Entry) entries.next();
                log.info(logIDString + entry.getKey() + "=" + entry.getValue());
            }
            log.info(logIDString + "=================================================================");
            log.info(logIDString + "HTTP Method = " + method);
            log.info(logIDString + "URL = " + url);

            Pipe pipe = (Pipe) axis2MessageContext.getProperty(IntrospectConstants.PASSTHROUGH_PIPE_PROPERTY);

            if (null != pipe) {
                log.info(logIDString + "Body = " +  Utils.getBufferValue(pipe));
            }

            log.info(logIDString + IntrospectConstants.LOG_CLIENT_TO_GATEWAY_DIRECTION);

            Map axisHeaders = (Map) ((Axis2MessageContext) mc).getAxis2MessageContext().
                    getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);

            axisHeaders.remove(IntrospectConstants.INTROSPECT_HEADER);
        }

        return true;
    }

    @Override
    public boolean handleResponse(MessageContext mc) {
        if (null != mc.getProperty(IntrospectConstants.INTROSPECT_PROPERTY)) {
            org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) mc).getAxis2MessageContext();

            mc.setProperty(Constants.Configuration.APPLICATION_XML_BUILDER_ALLOW_DTD, true);

            String logIDString = (String) mc.getProperty(IntrospectConstants.LOG_MSG_ID_PROPERTY);

            TargetResponse targetResponse = (TargetResponse) axis2MessageContext.getProperty(IntrospectConstants.TARGET_RESPONSE_KEY);

            Map headers = (Map) axis2MessageContext.getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);

            log.info(logIDString + IntrospectConstants.LOG_BACKEND_TO_GATEWAY_DIRECTION);
            log.info(logIDString + "===================HTTPHeaders==================================");
            for (final Iterator entries = headers.entrySet().iterator(); entries.hasNext(); ) {
                Map.Entry entry = (Map.Entry) entries.next();
                log.info(logIDString + entry.getKey() + "=" + entry.getValue());
            }
            log.info(logIDString + "=================================================================");

            int statusCode = targetResponse.getStatus();
            log.info(logIDString + "Status Code : " + statusCode);

            String statusLine = targetResponse.getStatusLine();
            log.info(logIDString + "Status Line : " + statusLine);

            ProtocolVersion version = targetResponse.getVersion();
            log.info(logIDString + "Protocol Version : " + version);

            Pipe pipe = (Pipe) axis2MessageContext.getProperty(IntrospectConstants.PASSTHROUGH_PIPE_PROPERTY);

            if (null != pipe) {
                log.info(logIDString + "Body = " +  Utils.getBufferValue(pipe));
            }

            log.info(logIDString + IntrospectConstants.LOG_BACKEND_TO_GATEWAY_DIRECTION);

        }

        return true;
    }

}
