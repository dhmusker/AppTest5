package com.sm.project.framework.data.entity;

import com.sm.project.framework.data.AbstractEntity;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class User extends AbstractEntity {

    private Integer id;
    private Integer organisation;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateEnrolled;
    private String title;
    private Boolean userActive;
    private Integer lastModifiedBy;
    private LocalDateTime lastModifiedDateTime;

    public User () {

    }

    public User ( Integer organisation,
                  String firstName,
                String lastName,
                String email,
                LocalDate dateEnrolled,
                String title,
                Boolean userActive,
                Integer lastModifiedBy,
                LocalDateTime lastModifiedDateTime ) {
        this.organisation = organisation;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateEnrolled = dateEnrolled;
        this.title = title;
        this.userActive = userActive;
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
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public LocalDate getDateEnrolled() { return dateEnrolled; }
    public String getTitle() { return title; }
    public Boolean isUserActive() { return userActive; }
    public Boolean getUserActive() { return userActive; }
    public void setDateEnrolled(LocalDate dateEnrolled) {
        this.dateEnrolled = dateEnrolled;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setUserActive(Boolean userActive) {
        this.userActive = userActive;
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
    public void setLastModifiedDateTime(LocalDateTime lastModifiedDateTime) { this.lastModifiedDateTime = LocalDateTime.now(); }

    public String getName() {
        return firstName + " " + lastName;
    }

}
