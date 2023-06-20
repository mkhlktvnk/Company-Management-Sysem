package com.digitalchief.companymanagement.builder.impl;

import com.digitalchief.companymanagement.builder.TestBuilder;
import com.digitalchief.companymanagement.entity.Company;
import com.digitalchief.companymanagement.entity.Department;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aCompany")
public class CompanyTestBuilder implements TestBuilder<Company> {

    private Long id = 0L;

    private String name = "";

    private String description = "";

    private Date dateOfCreation = Date.valueOf(LocalDate.now());

    private List<Department> departments = new ArrayList<>();

    @Override
    public Company build() {
        Company company = new Company();

        company.setId(id);
        company.setName(name);
        company.setDescription(description);
        company.setDateOfCreation(dateOfCreation);
        company.setDepartments(departments);

        return company;
    }

}
