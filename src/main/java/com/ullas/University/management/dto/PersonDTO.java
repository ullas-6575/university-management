package com.ullas.University.management.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public abstract class PersonDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotBlank
    @Size(max = 100)
    private String name;
    @NotBlank(groups = ValidationGroups.Create.class)
    @Pattern(regexp = "[a-z0-9][a-z0-9._-]{2,39}", message = "use 3-40 lowercase letters/digits, dot, underscore, or hyphen")
    private String username;
    @NotBlank
    @Email
    @Size(max = 254)
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(groups = ValidationGroups.Create.class)
    @Pattern(regexp = "[ -~]{12,64}", message = "use 12-64 printable ASCII characters, or omit on update")
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
