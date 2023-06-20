package com.digitalchief.companymanagement.repository;

import com.digitalchief.companymanagement.entity.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findAllByDepartmentIdAndId(Long departmentId, Long id, Pageable pageable);

    Optional<Employee> findByDepartmentIdAndId(Long departmentId, Long id);

    boolean existsByEmail(String email);
}
