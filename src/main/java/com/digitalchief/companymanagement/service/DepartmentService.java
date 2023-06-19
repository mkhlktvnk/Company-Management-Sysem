package com.digitalchief.companymanagement.service;

import com.digitalchief.companymanagement.entity.Department;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepartmentService {

    List<Department> findAllByPageable(Pageable pageable);

    Department findById(Long departmentId);

    Department createDepartmentInCompany(Department department, Long companyId);

    void updateDepartmentById(Long departmentId, Department updateDepartment);

    void updateDepartmentPartiallyById(Long departmentId, Department updateDepartment);

    void deleteDepartmentById(Long departmentId);

}
