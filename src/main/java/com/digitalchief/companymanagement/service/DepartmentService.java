package com.digitalchief.companymanagement.service;

import com.digitalchief.companymanagement.entity.Department;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepartmentService {

    List<Department> findAllByCompanyIdWithPagination(Long companyId, Pageable pageable);

    Department findByCompanyAndDepartmentId(Long companyId, Long departmentId);

    Department findById(Long departmentId);

    Department createDepartmentInCompany(Department department, Long companyId);

    void updateDepartmentInCompanyById(Long companyId, Long departmentId, Department updateDepartment);

    void updateDepartmentInCompanyPartiallyById(Long companyId, Long departmentId, Department updateDepartment);

    void  deleteDepartmentFromCompanyById(Long companyId, Long departmentId);

}
