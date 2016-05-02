package org.wso2.sample.synapse.introspection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.rest.RESTConstants;
import org.apache.synapse.transport.passthru.PassThroughConstants;
import org.apache.synapse.transport.passthru.Pipe;

import java.util.Iterator;
import java.util.Map;

public class OutsequenceIntrospector extends AbstractMediator {
    private static final Log log = LogFactory.getLog(OutsequenceIntrospector.class);

    @Override
    public boolean mediate(MessageContext mc) {
        if (null != mc.getProperty(IntrospectConstants.INTROSPECT_PROPERTY)) {
            String logIDString = (String) mc.getProperty(IntrospectConstants.LOG_MSG_ID_PROPERTY);

            org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) mc).getAxis2MessageContext();
            Map headers = (Map) axis2MessageContext.getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);

            log.info(logIDString + IntrospectConstants.LOG_GATEWAY_TO_CLIENT_DIRECTION);
            log.info(logIDString + "===================HTTPHeaders==================================");
            for (final Iterator entries = headers.entrySet().iterator(); entries.hasNext(); ) {
                Map.Entry entry = (Map.Entry) entries.next();

                if (null == entry.getValue()) {
                    continue;
                }

                if (0 == ((String) entry.getKey()).compareToIgnoreCase(IntrospectConstants.SERVER_HEADER_PROPERTY)) {
                    log.info(logIDString + entry.getKey() + " = " + IntrospectConstants.SERVER_GATEWAY_HEADER_VALUE);
                }
                else {
                    log.info(logIDString + entry.getKey() + " = " + entry.getValue());
                }

            }
            log.info(logIDString + "=================================================================");

            String requestParameters = (String) mc.getProperty(RESTConstants.REST_SUB_REQUEST_PATH);

            if (null != requestParameters) {
                log.info(logIDString + "Request Parameters = " + requestParameters);
            }

            String httpMethod = (String) mc.getProperty(RESTConstants.REST_METHOD);

            if (null != httpMethod) {
                log.info(logIDString + "HTTP Method = " + httpMethod);
            }

            Integer returnCode = (Integer) axis2MessageContext.getProperty(PassThroughConstants.HTTP_SC);

            if (null != returnCode) {
                log.info(logIDString + "HTTP code = " + returnCode);
            }

            Pipe pipe = (Pipe) axis2MessageContext.getProperty(IntrospectConstants.PASSTHROUGH_PIPE_PROPERTY);

            if (null != pipe) {
                log.info(logIDString + "Body = " + Utils.getBufferValue(pipe));
            }
            log.info(logIDString + IntrospectConstants.LOG_GATEWAY_TO_CLIENT_DIRECTION);
        }

        Utils.restoreOriginalContentType(mc);

        return true;
    }
}
