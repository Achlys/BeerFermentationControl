package org.xer.beerfermcontrol.core.bean;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;

/**
 *
 * @author Achlys
 */
public class Hydrom implements Serializable {
    
    private Integer id;
    private Integer configId;
    @NotBlank
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
