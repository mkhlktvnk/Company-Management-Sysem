package com.digitalchief.companymanagement.builder.impl;

import com.digitalchief.companymanagement.builder.TestBuilder;
import com.digitalchief.companymanagement.entity.Company;
import com.digitalchief.companymanagement.entity.Department;
import com.digitalchief.companymanagement.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aDepartment")
public class DepartmentTestBuilder implements TestBuilder<Department> {

    private Long id = 0L;

    private String name = "";

    private String description = "";

    private BigDecimal annualBudget = BigDecimal.ZERO;

    private Company company = CompanyTestBuilder.aCompany().build();

    private List<Employee> employees = new ArrayList<>();

    @Override
    public Department build() {
        Department department = new Department();

        department.setId(id);
        department.setName(name);
        department.setDescription(description);
        department.setAnnualBudget(annualBudget);
        department.setCompany(company);
        department.setEmployees(employees);

        return department;
    }
}
