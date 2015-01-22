package com.jgefroh.scheduler;

import java.util.Date;



public class Job {
    private Integer id;
    private String name;
    private String description;
    private SchedulingUnit schedulingUnit;
    private Integer frequency;
    private Date lastRun;
    
    
    
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getFrequency() {
        return frequency;
    }
    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }
    
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    
    
    public Date getLastRun() {
        return lastRun;
    }
    public void setLastRun(Date lastRun) {
        this.lastRun = lastRun;
    }
    
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    
    public SchedulingUnit getSchedulingUnit() {
        return schedulingUnit;
    }
    
    public void setSchedulingUnit(SchedulingUnit schedulingUnit) {
        this.schedulingUnit = schedulingUnit;
    }
    
}
