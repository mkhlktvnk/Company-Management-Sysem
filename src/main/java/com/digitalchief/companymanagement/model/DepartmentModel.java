package com.digitalchief.companymanagement.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepartmentModel {

    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotEmpty
    @JsonProperty(value = "name")
    private String name;

    @NotEmpty
    @JsonProperty(value = "description")
    private String description;

    @Positive
    @Digits(integer = 19, fraction = 2)
    @JsonProperty(value = "annualBudget")
    private BigDecimal annualBudget;
}
