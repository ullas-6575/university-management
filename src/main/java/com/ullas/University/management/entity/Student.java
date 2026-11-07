package com.ullas.University.management.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student extends Person {
    @Column(nullable = false, length = 30, unique = true)
    private String roll;

    public String getRoll() {
        return roll;
    }

    public void setRoll(String value) {
        this.roll = value;
    }
}
