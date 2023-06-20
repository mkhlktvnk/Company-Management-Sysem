package com.digitalchief.companymanagement.service;

import com.digitalchief.companymanagement.entity.Employee;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {
    List<Employee> findAllByCompanyAndDepartmentIdWithPagination(Long companyId, Long departmentId, Pageable pageable);

    Employee findByCompanyAndDepartmentAndEmployeeId(Long companyId, Long departmentId, Long employeeId);

    Employee createEmployeeInDepartment(Employee employee, Long companyId, Long departmentId);

    void updateEmployeeInDepartmentById(Long companyId, Long departmentId, Long employeeId, Employee updateEmployee);

    void updateEmployeeInDepartmentPartiallyById(Long companyId, Long departmentId, Long employeeId, Employee updateEmployee);

    void deleteEmployeeFromDepartmentById(Long companyId, Long departmentId, Long employeeId);
}
