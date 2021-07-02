package com.sm.project.data.entity;

import com.sm.project.framework.data.AbstractEntity;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
public class Project extends AbstractEntity {

    private Integer id;
    private Integer organisation;
    private String title;
    private Integer projectManagerID;
    private String projectManagerName;
    private String businessCase;
    private LocalDate dateStarted;
    private LocalDate dateEnded;
    private boolean isActive;
    private String categoryCode;
    private String categoryName;

    public Project() {

    }

    public Project(
            Integer id,
            Integer organisation,
            String title,
            Integer projectManagerID,
            String projectManagerName,
            String businessCase,
            LocalDate dateStarted,
            LocalDate dateEnded,
            boolean isActive,
            String categoryCode,
            String categoryName ) {
        this.setId( id );
        this.setOrganisation( organisation );
        this.setTitle( title );
        this.setProjectManagerID( projectManagerID );
        this.setProjectManagerName( projectManagerName );
        this.setBusinessCase( businessCase );
        this.setDateStarted( dateStarted );
        this.setDateEnded( dateEnded);
        this.setIsActive(isActive);
        this.setCategoryCode(categoryCode);
        this.setCategoryName(categoryName);
    }

    @Override
    public Integer getId() { return id; }
    @Override
    public void setId(Integer id) { this.id = id; }
    public Integer getOrganisation() {        return organisation;    }
    public void setOrganisation(Integer organisation) {        this.organisation = organisation;    }
    public LocalDate getDateStarted() { return dateStarted; }
    public void setDateStarted(LocalDate dateStarted) { this.dateStarted = dateStarted; }
    public LocalDate getDateEnded() { return dateEnded; }
    public void setDateEnded(LocalDate dateEnded) { this.dateEnded = dateEnded; }
    public boolean getIsActive() { return isActive; }
    public boolean isActive() { return isActive; }
    public void setIsActive(boolean isActive) { this.isActive = isActive; }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getProjectManagerName() {
        return projectManagerName;
    }
    public void setProjectManagerName(String projectManagerName) {
        this.projectManagerName = projectManagerName;
    }
    public String getBusinessCase() {
        return businessCase;
    }
    public void setBusinessCase(String businessCase) {
        this.businessCase = businessCase;
    }
    public String getCategoryName() {       return categoryName;    }
    public void setCategoryName(String categoryName) {        this.categoryName = categoryName;    }
    public String getCategoryCode() {        return categoryCode;    }
    public void setCategoryCode(String categoryCode) {        this.categoryCode = categoryCode;    }
    public Integer getProjectManagerID() { return projectManagerID; }
    public void setProjectManagerID(Integer projectManagerID) { this.projectManagerID = projectManagerID; }


    public Project( Integer organisation, Integer projectManagerID, String projectManagerName ) {
        this.setOrganisation( organisation );
        this.setProjectManagerID( projectManagerID );
        this.setProjectManagerName( projectManagerName );
        this.setDateStarted( LocalDate.now() );
        this.setDateEnded( LocalDate.now().plus(1, ChronoUnit.DAYS));
        this.setIsActive(false);
        this.setCategoryCode("2");
        this.setCategoryName("Skills");
    }

}
