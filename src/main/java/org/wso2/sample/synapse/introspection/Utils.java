package org.wso2.sample.synapse.introspection;

import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.transport.TransportUtils;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.transport.passthru.Pipe;

public class Utils {
    public static String getBufferValue(final Pipe pipe) {
        byte[] byteArr = pipe.getBuffer().array();

        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < byteArr.length; ++i) {
            char c = (char) Integer.valueOf(byteArr[i]).intValue();

            buffer.append(c);
        }

        return buffer.toString();
    }

    public static void handleUnsupportedContentType(MessageContext mc) {
        org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) mc).getAxis2MessageContext();
        String contentType = (String) axis2MessageContext.getProperty(IntrospectConstants.CONTENT_TYPE_PROPERTY);

        AxisConfiguration configuration = axis2MessageContext.getConfigurationContext().getAxisConfiguration();

        // Check if message builder exists for given Content Type and force default message builder if it does not exist
        if (contentType != null) {
            String derivedContentType = TransportUtils.getContentType(contentType, axis2MessageContext);

            if (configuration.getMessageBuilder(derivedContentType) == null) {
                mc.setProperty(IntrospectConstants.UNSUPPORTED_CONTENT_TYPE_PROPERTY, contentType);
                axis2MessageContext.setProperty(IntrospectConstants.CONTENT_TYPE_PROPERTY, "type/default");
            }
        }

    }

    public static void restoreOriginalContentType(MessageContext mc) {
        String originalContentType = (String) mc.getProperty(IntrospectConstants.UNSUPPORTED_CONTENT_TYPE_PROPERTY);

        if (originalContentType != null && originalContentType.isEmpty() == false) {
            org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) mc).getAxis2MessageContext();
            axis2MessageContext.setProperty(IntrospectConstants.CONTENT_TYPE_PROPERTY, originalContentType);
            mc.setProperty(IntrospectConstants.UNSUPPORTED_CONTENT_TYPE_PROPERTY, ""); // Reset property
        }
    }
}
