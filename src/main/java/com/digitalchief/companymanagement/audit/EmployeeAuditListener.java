package com.digitalchief.companymanagement.audit;

import com.digitalchief.companymanagement.entity.Company;
import com.digitalchief.companymanagement.entity.Employee;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;

public class EmployeeAuditListener {

    @PostPersist
    public void afterEmployeePersist(Employee employee) {
        Company company = employee.getDepartment().getCompany();
        company.setNumberOfEmployees(company.getNumberOfEmployees() + 1);
    }

    @PostRemove
    public void afterEmployeeRemove(Employee employee) {
        Company company = employee.getDepartment().getCompany();
        company.setNumberOfEmployees(company.getNumberOfEmployees() - 1);
    }

}
