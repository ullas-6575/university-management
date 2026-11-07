package com.ullas.University.management.dto;

import jakarta.validation.constraints.*;

public class TeacherDTO extends PersonDTO {
    @NotBlank
    @Size(max = 100)
    private String designation;

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String value) {
        this.designation = value;
    }
}
