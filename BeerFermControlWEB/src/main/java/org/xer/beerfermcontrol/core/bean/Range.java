package org.xer.beerfermcontrol.core.bean;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Achlys
 */
public class Range implements Serializable {
    
    private Integer id;
    private Integer configId;
    @NotNull
    private Double topGravity;
    @NotNull
    private Double bottomGravity;
    @NotNull
    private Double aimedTemp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getConfigId() {
        return configId;
    }

    public void setConfigId(Integer configId) {
        this.configId = configId;
    }

    public Double getTopGravity() {
        return topGravity;
    }

    public void setTopGravity(Double topGravity) {
        this.topGravity = topGravity;
    }

    public Double getBottomGravity() {
        return bottomGravity;
    }

    public void setBottomGravity(Double bottomGravity) {
        this.bottomGravity = bottomGravity;
    }

    public Double getAimedTemp() {
        return aimedTemp;
    }

    public void setAimedTemp(Double aimedTemp) {
        this.aimedTemp = aimedTemp;
    }
    
}
