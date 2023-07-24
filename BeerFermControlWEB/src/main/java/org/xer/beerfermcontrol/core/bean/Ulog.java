package org.xer.beerfermcontrol.core.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author Achlys
 */
public class Ulog implements Serializable {
    
    private Timestamp moment;
    private Integer configId;
    private String event;

    public Timestamp getMoment() {
        return moment;
    }

    public void setMoment(Timestamp moment) {
        this.moment = moment;
    }

    public Integer getConfigId() {
        return configId;
    }

    public void setConfigId(Integer configId) {
        this.configId = configId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
    
}
