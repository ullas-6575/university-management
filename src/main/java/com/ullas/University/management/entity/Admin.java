package com.ullas.University.management.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "admins")
public class Admin extends Person {
    @Column(nullable = false, length = 100)
    private String office;

    public String getOffice() {
        return office;
    }

    public void setOffice(String value) {
        this.office = value;
    }
}
