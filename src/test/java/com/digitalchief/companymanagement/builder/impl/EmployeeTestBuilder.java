package com.digitalchief.companymanagement.builder.impl;

import com.digitalchief.companymanagement.builder.TestBuilder;
import com.digitalchief.companymanagement.entity.Department;
import com.digitalchief.companymanagement.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "anEmployee")
public class EmployeeTestBuilder implements TestBuilder<Employee> {

    private Long id = 0L;

    private String firstname = "";

    private String lastname = "";

    private String position = "";

    private Integer age = 0;

    private String email = "";

    private Date dateOfEmployment = Date.valueOf(LocalDate.now());

    private BigDecimal salaryPerMonth = BigDecimal.ONE;

    private Department department = DepartmentTestBuilder.aDepartment().build();

    @Override
    public Employee build() {
        Employee employee = new Employee();

        employee.setId(id);
        employee.setFirstname(firstname);
        employee.setLastname(lastname);
        employee.setPosition(position);
        employee.setAge(age);
        employee.setEmail(email);
        employee.setDateOfEmployment(dateOfEmployment);
        employee.setSalaryPerMonth(salaryPerMonth);
        employee.setDepartment(department);

        return employee;
    }
}
