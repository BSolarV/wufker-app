package com.messirvedevs.wufker.objects;

import java.util.Date;

public class User {
    private String email;
    private String firstname;
    private String lastname;
    private Boolean isVet;
    private Date birthdate;

    public User(){}

    public User(String email, String firstname, String lastname, Boolean isVet, Date birthdate) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.isVet = isVet;
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Boolean getVet() {
        return isVet;
    }

    public void setVet(Boolean vet) {
        isVet = vet;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }
}
