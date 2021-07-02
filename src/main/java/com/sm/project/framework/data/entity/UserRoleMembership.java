package com.sm.project.framework.data.entity;

import com.sm.project.framework.data.AbstractEntity;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class UserRoleMembership extends AbstractEntity {

    private Integer id;
    private Integer organisation;
    private Integer userId;
    private Integer roleId;
    private Integer requestedBy;
    private Integer approvedBy;
    private LocalDateTime startDateTime;
    private LocalDateTime EndDateTime;
    private Integer lastModifiedBy;
    private LocalDateTime lastModifiedDateTime;

    public UserRoleMembership() {

    }

    public UserRoleMembership(
            Integer organisation,
            Integer userId,
            Integer roleId,
            Integer requestedBy,
            Integer approvedBy,
            LocalDateTime startDateTime,
            LocalDateTime EndDateTime,
            Integer lastModifiedBy,
            LocalDateTime lastModifiedDateTime
    ) {
        this.organisation = organisation;
        this.userId = userId;
        this.roleId = roleId;
        this.requestedBy = requestedBy;
        this.approvedBy = approvedBy;
        this.startDateTime = startDateTime;
        this.EndDateTime = EndDateTime;
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
    public Integer getRoleId() {
        return roleId;
    }
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Integer getRequestedBy() {
        return requestedBy;
    }
    public void setRequestedBy(Integer requestedBy) {
        this.requestedBy = requestedBy;
    }
    public Integer getApprovedBy() {
        return approvedBy;
    }
    public void setApprovedBy(Integer approvedBy) {
        this.approvedBy = approvedBy;
    }
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }
    public LocalDateTime getEndDateTime() {
        return EndDateTime;
    }
    public void setEndDateTime(LocalDateTime endDateTime) {
        EndDateTime = endDateTime;
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

}
