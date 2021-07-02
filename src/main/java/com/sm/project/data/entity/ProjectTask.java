package com.sm.project.data.entity;

import com.sm.project.framework.data.AbstractEntity;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;

@Entity
public class ProjectTask extends AbstractEntity {

    private Integer id;
    private Integer organisation;
    private Integer projectId;
    private String name;
    private String description;
    private String assignedTo;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double minsDuration;
    private Integer numResources;
    private Double minsEffort;
    private Double cost;
    private String category;

    public ProjectTask(
            Integer id,
            Integer organisation,
            Integer projectId,
            String name,
            String description,
            String assignedTo,
            String status,
            LocalDate startDate,
            LocalDate endDate,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Double minsDuration,
            Integer numResources,
            Double minsEffort,
            Double cost,
            String category) {
        this.setId(id);
        this.setOrganisation(organisation);
        this.setProjectId(projectId);
        this.setName(name);
        this.setDescription(description);
        this.setAssignedTo(assignedTo);
        this.setStatus(status);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
        this.setStartTime(startTime);
        this.setEndTime(endTime);
        this.setMinsDuration(minsDuration);
        this.setNumResources(numResources);
        this.setMinsEffort(minsEffort);
        this.setCost(cost);
        this.setCategory(category);
    }

    public ProjectTask(
                Integer id,
                Integer organisation,
                Integer projectId,
                String name,
                String description,
                String assignedTo,
                String status,
                String category) {
            this.setId(id);
            this.setOrganisation(organisation);
            this.setProjectId(projectId);
            this.setName(name);
            this.setDescription(description);
            this.setAssignedTo(assignedTo);
            this.setStatus(status);
            this.setCategory(category);
    }

    public ProjectTask() {

    }


    @Override
    public Integer getId() {        return id;    }
    @Override
    public void setId(Integer id) {        this.id = id;    }
    public Integer getOrganisation() {        return organisation;    }
    public void setOrganisation(Integer organisation) {        this.organisation = organisation;    }
    public Integer getProjectId() {
        return projectId;
    }
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getAssignedTo() {
        return assignedTo;
    }
    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    public LocalDateTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    public Double getMinsDuration() {
        return minsDuration;
    }
    public void setMinsDuration(Double minsDuration) {
        this.minsDuration = minsDuration;
    }
    public Integer getNumResources() {
        return numResources;
    }
    public void setNumResources(Integer numResources) {
        this.numResources = numResources;
    }
    public Double getMinsEffort() {
        return minsEffort;
    }
    public void setMinsEffort(Double minsEffort) {
        this.minsEffort = minsEffort;
    }
    public Double getCost() {
        return cost;
    }
    public void setCost(Double cost) {
        this.cost = cost;
    }
    public String getCategory() {       return category;    }
    public void setCategory(String category) {        this.category = category;    }


    public void applyDefaults(String assignedTo ) {
        this.setAssignedTo(assignedTo);
        this.setStatus("Planned");
        this.setStartDate(LocalDate.now());
        this.setEndDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
        this.setStartTime(LocalDateTime.now());
        this.setEndTime(LocalDateTime.now().plus(1, ChronoUnit.HOURS));
        this.setMinsDuration(60.00);
        this.setNumResources(1);
        this.setMinsEffort(60.00);
        this.setCost(600.00);
        this.setCategory("Skills");
    }

}
