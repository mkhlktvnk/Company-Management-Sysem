package com.digitalchief.companymanagement.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyModel {

    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotEmpty
    @JsonProperty(value = "name")
    private String name;

    @NotEmpty
    @JsonProperty(value = "description")
    private String description;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonProperty(value = "dateOfCreation")
    private LocalDate dateOfCreation;
}
