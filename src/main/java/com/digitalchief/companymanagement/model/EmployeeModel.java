package com.digitalchief.companymanagement.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class EmployeeModel {

    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotEmpty
    @JsonProperty(value = "firstname")
    private String firstname;

    @NotEmpty
    @JsonProperty(value = "lastname")
    private String lastname;

    @NotEmpty
    @JsonProperty(value = "position")
    private String position;

    @Positive
    @JsonProperty(value = "age")
    private Integer age;

    @Email
    @JsonProperty(value = "email")
    private String email;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonProperty(value = "dateOfEmployment")
    private LocalDate dateOfEmployment;

    @Digits(integer = 19, fraction = 2)
    @JsonProperty(value = "salaryPerMonth")
    private BigDecimal salaryPerMonth;

}
