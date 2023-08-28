package org.xer.beerfermcontrol.core.util;

/**
 *
 * @author Achlys
 */
public class CoreConstants {
    
    public static final String TPLINK_TYPE_COLD = "C";
    public static final String TPLINK_TYPE_WARM = "W";
    public static final String TPLINK_ON_MESSAGE = "{\"system\":{\"set_relay_state\":{\"state\":1}}}";
    public static final String TPLINK_OFF_MESSAGE = "{\"system\":{\"set_relay_state\":{\"state\":0}}}";
    public static final Integer TPLINK_TCP_PORT = 9999;
    
}
