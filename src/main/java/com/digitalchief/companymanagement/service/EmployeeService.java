package com.digitalchief.companymanagement.service;

import com.digitalchief.companymanagement.entity.Employee;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {
    List<Employee> findAllByPageable(Pageable pageable);

    Employee findById(Long id);

    Employee createEmployeeInDepartment(Employee employee, Long id);

    void updateEmployeeById(Long employeeId, Employee updateEmployee);

    void updateEmployeePartiallyById(Long employeeId, Employee updateEmployee);

    void deleteEmployeeById(Long employeeId);
}
