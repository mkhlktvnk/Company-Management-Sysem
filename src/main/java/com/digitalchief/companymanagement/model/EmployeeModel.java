package com.digitalchief.companymanagement.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Employee Model")
public class EmployeeModel {

    @Schema(
            description = "ID of the employee",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotEmpty
    @Schema(description = "First name of the employee", example = "John")
    @JsonProperty(value = "firstname")
    private String firstname;

    @NotEmpty
    @Schema(description = "Last name of the employee", example = "Doe")
    @JsonProperty(value = "lastname")
    private String lastname;

    @NotEmpty
    @Schema(description = "Position of the employee", example = "Manager")
    @JsonProperty(value = "position")
    private String position;

    @Positive
    @Schema(description = "Age of the employee", example = "30")
    @JsonProperty(value = "age")
    private Integer age;

    @Email
    @Schema(description = "Email address of the employee", example = "john.doe@example.com")
    @JsonProperty(value = "email")
    private String email;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(description = "Date of employment of the employee", example = "2022-01-01")
    @JsonProperty(value = "dateOfEmployment")
    private LocalDate dateOfEmployment;

    @Digits(integer = 19, fraction = 2)
    @Schema(description = "Salary per month of the employee", example = "5000.00")
    @JsonProperty(value = "salaryPerMonth")
    private BigDecimal salaryPerMonth;

}

