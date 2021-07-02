package com.sm.project.framework.data.entity;

import com.sm.project.framework.data.AbstractEntity;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class MasterReferenceData extends AbstractEntity {

    private Integer id;
    private Integer organisation;
    private String dataset;
    private String refCode;
    private String refValue;
    private Boolean isActive;
    private LocalDate activeFromDate;
    private LocalDate activeToDate;
    private Integer lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    public MasterReferenceData() {

    }

    public MasterReferenceData(Integer organisation,
                               String dataset,
                               String refCode,
                               String refValue,
                               Boolean isActive,
                               LocalDate activeFromDate,
                               LocalDate activeToDate,
                               Integer lastModifiedBy,
                               LocalDateTime lastModifiedDate) {
        this.organisation = organisation;
        this.dataset = dataset;
        this.refCode = refCode;
        this.refValue = refValue;
        this.isActive = isActive;
        this.activeFromDate = activeFromDate;
        this.activeToDate = activeToDate;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public Integer getId() {
        return id;
    }
    @Override
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getOrganisation() {
        return organisation;
    }
    public void setOrganisation(Integer organisation) {
        this.organisation = organisation;
    }
    public String getDataset() {
        return dataset;
    }
    public void setDataset(String dataset) {
        this.dataset = dataset;
    }
    public Boolean getActive() {
        return isActive;
    }
    public void setActive(Boolean active) {
        isActive = active;
    }
    public LocalDate getActiveFromDate() {
        return activeFromDate;
    }
    public void setActiveFromDate(LocalDate activeFromDate) {
        this.activeFromDate = activeFromDate;
    }
    public LocalDate getActiveToDate() {
        return activeToDate;
    }
    public void setActiveToDate(LocalDate activeToDate) {
        this.activeToDate = activeToDate;
    }
    public Integer getLastModifiedBy() {
        return lastModifiedBy;
    }
    public void setLastModifiedBy(Integer lastModifiedBy) { this.lastModifiedBy = lastModifiedBy;    }
    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }
    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    public String getRefCode() {
        return refCode;
    }
    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }
    public String getRefValue() {
        return refValue;
    }
    public void setRefValue(String refValue) {
        this.refValue = refValue;
    }

}

