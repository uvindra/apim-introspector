package org.wso2.sample.synapse.introspection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.Mediator;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.endpoints.HTTPEndpoint;
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.mediators.builtin.SendMediator;
import org.apache.synapse.mediators.filters.FilterMediator;
import org.apache.synapse.rest.RESTConstants;
import org.apache.synapse.transport.passthru.Pipe;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InsequenceIntrospector extends AbstractMediator {
    private static final Log log = LogFactory.getLog(InsequenceIntrospector.class);

    @Override
    public boolean mediate(MessageContext mc) {
        if (null != mc.getProperty(IntrospectConstants.INTROSPECT_PROPERTY)) {
            String logIDString = (String) mc.getProperty(IntrospectConstants.LOG_MSG_ID_PROPERTY);

            org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) mc).getAxis2MessageContext();
            Map headers = (Map) axis2MessageContext.getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);

            String endpointURL = getEndpointURL(mc);

            log.info(logIDString + IntrospectConstants.LOG_GATEWAY_TO_BACKEND_DIRECTION);
            log.info(logIDString + "===================HTTPHeaders==================================");
            for (final Iterator entries = headers.entrySet().iterator(); entries.hasNext(); ) {
                Map.Entry entry = (Map.Entry) entries.next();
                if (null == entry.getValue()) {
                    continue;
                }

                if (0 == ((String) entry.getKey()).compareToIgnoreCase(IntrospectConstants.HOST_HEADER_PROPERTY)) {
                    try {
                        if (null != endpointURL) {
                            URL url = new URL(endpointURL);
                            log.info(logIDString + entry.getKey() + " = " + url.getHost());
                        }
                    } catch (MalformedURLException e) {
                        log.error(e);
                    }

                }
                else if (0 == ((String) entry.getKey()).compareToIgnoreCase(IntrospectConstants.USER_AGENT_HEADER_PROPERTY)) {
                    log.info(logIDString + entry.getKey() + " = " + IntrospectConstants.USER_AGENT_GATEWAY_HEADER_VALUE);
                }
                else {
                    log.info(logIDString + entry.getKey() + " = " + entry.getValue());
                }
            }
            log.info(logIDString + "=================================================================");

            if (null != endpointURL) {
                log.info(logIDString + "Endpoint URL = " + endpointURL);
            }

            String requestParameters = (String) mc.getProperty(RESTConstants.REST_SUB_REQUEST_PATH);

            if (null != requestParameters) {
                log.info(logIDString + "Request Parameters = " + requestParameters);
            }

            String httpMethod = (String) mc.getProperty(RESTConstants.REST_METHOD);

            if (null != httpMethod) {
                log.info(logIDString + "HTTP Method = " + httpMethod);
            }

            Pipe pipe = (Pipe) axis2MessageContext.getProperty(IntrospectConstants.PASSTHROUGH_PIPE_PROPERTY);

            if (null != pipe) {
                log.info(logIDString + "Body = " + Utils.getBufferValue(pipe));
            }

            log.info(logIDString + IntrospectConstants.LOG_GATEWAY_TO_BACKEND_DIRECTION);
        }

        Utils.restoreOriginalContentType(mc);

        return true;
    }

    private String getEndpointURL(MessageContext mc) {
        String restAPI = (String) mc.getProperty(RESTConstants.SYNAPSE_REST_API);

        String resource = (String) mc.getProperty(RESTConstants.SYNAPSE_RESOURCE);

        List<Mediator> outerMediatorList = mc.getConfiguration().getAPI(restAPI).getResource(resource).getInSequence().getList();

        for (final Iterator outerMediators = outerMediatorList.iterator(); outerMediators.hasNext(); ) {
            Mediator outerMediator = (Mediator) outerMediators.next();

            if (outerMediator instanceof FilterMediator) {
                FilterMediator filterMediator = (FilterMediator) outerMediator;

                List<Mediator> innerMediatorList = filterMediator.getList();

                for (final Iterator innerMediators = innerMediatorList.iterator(); innerMediators.hasNext(); ) {
                    Mediator innerMediator = (Mediator) innerMediators.next();

                    if (innerMediator instanceof SendMediator) {
                        SendMediator sendMediator = (SendMediator) innerMediator;

                        HTTPEndpoint httpEndpoint = (HTTPEndpoint) sendMediator.getEndpoint();

                        return httpEndpoint.getUriTemplate().getTemplate();
                    }
                }
            }
        }

        return null;
    }
}
