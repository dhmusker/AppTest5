package com.sm.project.data.entity;

import com.sm.project.data.AbstractEntity;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
public class User extends AbstractEntity {

    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateEnrolled;
    private String title;
    private boolean userActive;


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
    public boolean isUserActive() { return userActive; }
    public boolean getUserActive() { return userActive; }

    public void setDateEnrolled(LocalDate dateEnrolled) {
        this.dateEnrolled = dateEnrolled;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserActive(boolean userActive) {
        this.userActive = userActive;
    }

}
