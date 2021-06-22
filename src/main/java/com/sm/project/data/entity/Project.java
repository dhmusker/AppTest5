package com.sm.project.data.entity;

import com.sm.project.data.AbstractEntity;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
public class Project extends AbstractEntity {

    private Integer id;
    private String title;
    private String manager;
    private String businessCase;
    private LocalDate dateStarted;
    private LocalDate dateEnded;
    private boolean isActive;
    private String category;

    @Override
    public Integer getId() { return id;    }

    @Override
    public void setId(Integer id) {        this.id = id;    }


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
    public String getManager() {
        return manager;
    }
    public void setManager(String manager) {
        this.manager = manager;
    }
    public String getBusinessCase() {
        return businessCase;
    }
    public void setBusinessCase(String businessCase) {
        this.businessCase = businessCase;
    }
    public String getCategory() {       return category;    }
    public void setCategory(String category) {        this.category = category;    }


    public Project() {

    }

    public Project(
            Integer id,
            String title,
            String manager,
            String businessCase,
            LocalDate dateStarted,
            LocalDate dateEnded,
            boolean isActive,
            String category ) {
        this.setId( id );
        this.setTitle( title );
        this.setManager( manager );
        this.setBusinessCase( businessCase );
        this.setDateStarted( dateStarted );
        this.setDateEnded( dateEnded);
        this.setIsActive(isActive);
        this.setCategory(category);
    }
}
