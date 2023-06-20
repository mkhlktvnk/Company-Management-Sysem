package com.digitalchief.companymanagement.repository;

import com.digitalchief.companymanagement.entity.Department;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findAllByCompanyId(Long companyId, Pageable pageable);

    List<Department> findAllByCompanyIdAndId(Long companyId, Long departmentId);

    Optional<Department> findByCompanyIdAndId(Long companyId, Long id);
}