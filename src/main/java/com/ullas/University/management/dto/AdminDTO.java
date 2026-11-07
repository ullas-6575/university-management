package com.ullas.University.management.dto;

import jakarta.validation.constraints.*;

public class AdminDTO extends PersonDTO {
    @NotBlank
    @Size(max = 100)
    private String office;

    public String getOffice() {
        return office;
    }

    public void setOffice(String value) {
        this.office = value;
    }
}
