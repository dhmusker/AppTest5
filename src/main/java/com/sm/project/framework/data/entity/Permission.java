package com.sm.project.framework.data.entity;

import com.sm.project.framework.data.AbstractEntity;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class Permission extends AbstractEntity {

    private Integer id;
    private Integer organisation;
    private String name;
    private String purpose;
    private Integer lastModifiedBy;
    private LocalDateTime lastModifiedDateTime;

    public Permission () {}

    public Permission (Integer organisation,
                       String name,
                       String purpose,
                       Integer lastModifiedBy,
                       LocalDateTime lastModifiedDateTime) {
        this.organisation = organisation;
        this.name = name;
        this.purpose = purpose;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDateTime = lastModifiedDateTime;
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
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getLastModifiedBy() {
        return lastModifiedBy;
    }
    public void setLastModifiedBy(Integer lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
    public LocalDateTime getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }
    public void setLastModifiedDateTime(LocalDateTime lastModifiedDateTime) { this.lastModifiedDateTime = lastModifiedDateTime; }
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

}
