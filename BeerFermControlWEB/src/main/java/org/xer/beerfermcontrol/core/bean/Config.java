package org.xer.beerfermcontrol.core.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Achlys
 */
public class Config implements Serializable {
    
    private Integer id;
    private Integer userId;
    private String name;
    private Double tolerance;
    private Date startDate;
    private Date endDate;
    private Tplink tplinkHot;
    private Tplink tplinkCold;
    private Hydrom hydrom;
    private List<Range> ranges;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTolerance() {
        return tolerance;
    }

    public void setTolerance(Double tolerance) {
        this.tolerance = tolerance;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Tplink getTplinkHot() {
        return tplinkHot;
    }

    public void setTplinkHot(Tplink tplinkHot) {
        this.tplinkHot = tplinkHot;
    }

    public Tplink getTplinkCold() {
        return tplinkCold;
    }

    public void setTplinkCold(Tplink tplinkCold) {
        this.tplinkCold = tplinkCold;
    }

    public Hydrom getHydrom() {
        return hydrom;
    }

    public void setHydrom(Hydrom hydrom) {
        this.hydrom = hydrom;
    }

    public List<Range> getRanges() {
        return ranges;
    }

    public void setRanges(List<Range> ranges) {
        this.ranges = ranges;
    }
    
}
