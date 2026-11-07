package com.ullas.University.management.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "teachers")
public class Teacher extends Person {
    @Column(nullable = false, length = 100)
    private String designation;

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String value) {
        this.designation = value;
    }
}
