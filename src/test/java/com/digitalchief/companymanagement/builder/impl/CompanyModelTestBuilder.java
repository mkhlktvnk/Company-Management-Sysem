package com.digitalchief.companymanagement.builder.impl;

import com.digitalchief.companymanagement.builder.TestBuilder;
import com.digitalchief.companymanagement.model.CompanyModel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.LocalDate;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aCompanyModel")
public class CompanyModelTestBuilder implements TestBuilder<CompanyModel> {

    private Long id = 0L;
    private String name = "";
    private String description = "";
    private LocalDate dateOfCreation = LocalDate.now();
    private Long numberOfEmployees = 0L;

    @Override
    public CompanyModel build() {
        CompanyModel companyModel = new CompanyModel();

        companyModel.setId(id);
        companyModel.setName(name);
        companyModel.setDescription(description);
        companyModel.setDateOfCreation(dateOfCreation);
        companyModel.setNumberOfEmployees(numberOfEmployees);

        return companyModel;
    }
}
