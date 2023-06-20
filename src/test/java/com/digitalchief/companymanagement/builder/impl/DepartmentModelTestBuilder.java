package com.digitalchief.companymanagement.builder.impl;

import com.digitalchief.companymanagement.builder.TestBuilder;
import com.digitalchief.companymanagement.model.DepartmentModel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aDepartmentModel")
public class DepartmentModelTestBuilder implements TestBuilder<DepartmentModel> {

    private Long id = 0L;
    private String name = "";
    private String description = "";
    private BigDecimal annualBudget = BigDecimal.ZERO;

    @Override
    public DepartmentModel build() {
        DepartmentModel departmentModel = new DepartmentModel();

        departmentModel.setId(id);
        departmentModel.setName(name);
        departmentModel.setDescription(description);
        departmentModel.setAnnualBudget(annualBudget);

        return departmentModel;
    }
}
