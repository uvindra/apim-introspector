package org.wso2.sample.synapse.introspection;

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
}
