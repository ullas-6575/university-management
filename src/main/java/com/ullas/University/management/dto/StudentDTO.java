package com.ullas.University.management.dto;

import jakarta.validation.constraints.*;

public class StudentDTO extends PersonDTO {
    @NotBlank
    @Size(max = 30)
    private String roll;

    public String getRoll() {
        return roll;
    }

    public void setRoll(String value) {
        this.roll = value;
    }
}
