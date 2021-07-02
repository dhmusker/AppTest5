package com.sm.project.framework.data.entity;

import com.sm.project.framework.data.AbstractEntity;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Organisation extends AbstractEntity {


    private Integer id;
    private String name;
    private Integer ownerId;
    private Integer lastModifiedBy;
    private LocalDateTime lastModifiedDateTime;

    public Organisation() {

    }

    public Organisation(
            String name,
            Integer ownerId,
            Integer lastModifiedBy,
            LocalDateTime lastModifiedDateTime ) {
        this.name = name;
        this.ownerId = ownerId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
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

    public void setLastModifiedDateTime(LocalDateTime lastModifiedDateTime) {
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

}
