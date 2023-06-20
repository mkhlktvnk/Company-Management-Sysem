package com.digitalchief.companymanagement.service.impl;

import com.digitalchief.companymanagement.entity.Department;
import com.digitalchief.companymanagement.entity.Employee;
import com.digitalchief.companymanagement.mapper.EmployeeMapper;
import com.digitalchief.companymanagement.message.key.CompanyMessageKey;
import com.digitalchief.companymanagement.message.key.DepartmentMessageKey;
import com.digitalchief.companymanagement.message.key.EmployeeMessageKey;
import com.digitalchief.companymanagement.message.source.MessagesSource;
import com.digitalchief.companymanagement.repository.CompanyRepository;
import com.digitalchief.companymanagement.repository.DepartmentRepository;
import com.digitalchief.companymanagement.repository.EmployeeRepository;
import com.digitalchief.companymanagement.service.DepartmentService;
import com.digitalchief.companymanagement.service.EmployeeService;
import com.digitalchief.companymanagement.service.exception.EntityNotFoundException;
import com.digitalchief.companymanagement.service.exception.EntityNotUniqueException;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;
    private final DepartmentService departmentService;
    private final MessagesSource messages;
    private final EmployeeMapper mapper = Mappers.getMapper(EmployeeMapper.class);

    @Override
    public List<Employee> findAllByCompanyAndDepartmentIdWithPagination(
            Long companyId, Long departmentId, Pageable pageable) {
        return employeeRepository.findAllByDepartmentIdAndId(companyId, departmentId, pageable);
    }

    @Override
    public Employee findByCompanyAndDepartmentAndEmployeeId(Long companyId, Long departmentId, Long employeeId) {
        if (!companyRepository.existsById(companyId)) {
            throw new EntityNotUniqueException(messages.getMessage(CompanyMessageKey.NOT_FOUND_BY_ID, companyId));
        }
        return employeeRepository.findByDepartmentIdAndId(departmentId, employeeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        messages.getMessage(EmployeeMessageKey.EMPLOYEE_NOT_FOUND_BY_ID, employeeId)
                ));
    }

    @Override
    @Transactional
    public Employee createEmployeeInDepartment(Employee employee, Long companyId, Long departmentId) {
        if (!companyRepository.existsById(companyId)) {
            throw new EntityNotFoundException(messages.getMessage(CompanyMessageKey.NOT_FOUND_BY_ID, companyId));
        }
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new EntityNotUniqueException(
                    messages.getMessage(EmployeeMessageKey.EMPLOYEE_ALREADY_EXISTS_BY_EMAIL,
                            employee.getEmail())
            );
        }
        Department departmentToCreateEmployeeIn = departmentService.findByCompanyAndDepartmentId(companyId, departmentId);
        employee.setDepartment(departmentToCreateEmployeeIn);

        return employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public void updateEmployeeInDepartmentById(Long companyId, Long departmentId, Long employeeId,
                                               Employee updateEmployee) {
        if (!companyRepository.existsById(companyId)) {
            throw new EntityNotFoundException(messages.getMessage(CompanyMessageKey.NOT_FOUND_BY_ID, companyId));
        }
        if (employeeRepository.existsByEmail(updateEmployee.getEmail())) {
            throw new EntityNotUniqueException(
                    messages.getMessage(EmployeeMessageKey.EMPLOYEE_ALREADY_EXISTS_BY_EMAIL,
                            updateEmployee.getEmail())
            );
        }
        Employee employeeToUpdate = employeeRepository.findByDepartmentIdAndId(departmentId, employeeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        messages.getMessage(EmployeeMessageKey.EMPLOYEE_NOT_FOUND_BY_ID, employeeId)
                ));
        mapper.copyAllFields(employeeToUpdate, updateEmployee);
        employeeRepository.save(employeeToUpdate);
    }

    @Override
    @Transactional
    public void updateEmployeeInDepartmentPartiallyById(Long companyId, Long departmentId, Long employeeId,
                                                        Employee updateEmployee) {
        if (!companyRepository.existsById(companyId)) {
            throw new EntityNotFoundException(messages.getMessage(CompanyMessageKey.NOT_FOUND_BY_ID, companyId));
        }
        if (employeeRepository.existsByEmail(updateEmployee.getEmail())) {
            throw new EntityNotUniqueException(
                    messages.getMessage(EmployeeMessageKey.EMPLOYEE_ALREADY_EXISTS_BY_EMAIL,
                            updateEmployee.getEmail())
            );
        }
        Employee employeeToUpdate = employeeRepository.findByDepartmentIdAndId(departmentId, employeeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        messages.getMessage(EmployeeMessageKey.EMPLOYEE_NOT_FOUND_BY_ID, employeeId)
                ));
        mapper.copyNotNullFields(employeeToUpdate, updateEmployee);
        employeeRepository.save(employeeToUpdate);
    }

    @Override
    @Transactional
    public void deleteEmployeeFromDepartmentById(Long companyId, Long departmentId, Long employeeId) {
        if (!companyRepository.existsById(companyId)) {
            throw new EntityNotFoundException(messages.getMessage(CompanyMessageKey.NOT_FOUND_BY_ID, companyId));
        }
        Employee employeeToDelete = employeeRepository.findByDepartmentIdAndId(departmentId, employeeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        messages.getMessage(EmployeeMessageKey.EMPLOYEE_NOT_FOUND_BY_ID,
                                employeeId)
                ));
        employeeRepository.delete(employeeToDelete);
    }
}
