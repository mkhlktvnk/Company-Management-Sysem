package com.digitalchief.companymanagement.builder.impl;

import com.digitalchief.companymanagement.builder.TestBuilder;
import com.digitalchief.companymanagement.model.EmployeeModel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;
import java.time.LocalDate;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "anEmployeeModel")
public class EmployeeModelTestBuilder implements TestBuilder<EmployeeModel> {

    private Long id = 0L;
    private String firstname = "";
    private String lastname = "";
    private String position = "";
    private Integer age = 0;
    private String email = "";
    private LocalDate dateOfEmployment = LocalDate.now();
    private BigDecimal salaryPerMonth = BigDecimal.ZERO;

    @Override
    public EmployeeModel build() {
        EmployeeModel employeeModel = new EmployeeModel();

        employeeModel.setId(id);
        employeeModel.setFirstname(firstname);
        employeeModel.setLastname(lastname);
        employeeModel.setPosition(position);
        employeeModel.setAge(age);
        employeeModel.setEmail(email);
        employeeModel.setDateOfEmployment(dateOfEmployment);
        employeeModel.setSalaryPerMonth(salaryPerMonth);

        return employeeModel;
    }
}
