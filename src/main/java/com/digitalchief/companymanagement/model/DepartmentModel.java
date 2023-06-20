package com.digitalchief.companymanagement.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Department Model")
public class DepartmentModel {

    @Schema(
            description = "ID of the department",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotEmpty
    @Schema(description = "Name of the department", example = "Sales")
    @JsonProperty(value = "name")
    private String name;

    @NotEmpty
    @Schema(description = "Description of the department", example = "Handles sales operations")
    @JsonProperty(value = "description")
    private String description;

    @Positive
    @Digits(integer = 19, fraction = 2)
    @Schema(description = "Annual budget of the department", example = "1000000.00")
    @JsonProperty(value = "annualBudget")
    private BigDecimal annualBudget;
}

