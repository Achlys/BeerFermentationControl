package org.xer.beerfermcontrol.core.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author Achlys
 */
public class Reading implements Serializable {
    
    private Timestamp moment;
    private String hydromName;
    private Double gravity;
    private Double temp;
    private String json;

    public Timestamp getMoment() {
        return moment;
    }

    public void setMoment(Timestamp moment) {
        this.moment = moment;
    }

    public String getHydromName() {
        return hydromName;
    }

    public void setHydromName(String hydromName) {
        this.hydromName = hydromName;
    }

    public Double getGravity() {
        return gravity;
    }

    public void setGravity(Double gravity) {
        this.gravity = gravity;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
    
}
