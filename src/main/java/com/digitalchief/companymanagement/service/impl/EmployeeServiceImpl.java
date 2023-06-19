package com.digitalchief.companymanagement.service.impl;

import com.digitalchief.companymanagement.entity.Department;
import com.digitalchief.companymanagement.entity.Employee;
import com.digitalchief.companymanagement.mapper.EmployeeMapper;
import com.digitalchief.companymanagement.message.key.EmployeeMessageKey;
import com.digitalchief.companymanagement.message.source.MessagesSource;
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
    private final DepartmentService departmentService;
    private final MessagesSource messages;
    private final EmployeeMapper mapper = Mappers.getMapper(EmployeeMapper.class);

    @Override
    public List<Employee> findAllByPageable(Pageable pageable) {
        return employeeRepository.findAll(pageable).getContent();
    }

    @Override
    public Employee findById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        messages.getMessage(EmployeeMessageKey.EMPLOYEE_NOT_FOUND_BY_ID, id)
                ));
    }

    @Override
    @Transactional
    public Employee createEmployeeInDepartment(Employee employee, Long departmentId) {
        Department departmentToCreateEmployeeIn = departmentService.findById(departmentId);
        employee.setDepartment(departmentToCreateEmployeeIn);

        return employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public void updateEmployeeById(Long employeeId, Employee updateEmployee) {
        if (employeeRepository.existsByEmail(updateEmployee.getEmail())) {
            throw new EntityNotUniqueException(
                    messages.getMessage(EmployeeMessageKey.EMPLOYEE_ALREADY_EXISTS_BY_EMAIL,
                            updateEmployee.getEmail())
            );
        }
        Employee employeeToUpdate = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(""));
        mapper.copyAllFields(employeeToUpdate, updateEmployee);
        employeeRepository.save(employeeToUpdate);
    }

    @Override
    @Transactional
    public void updateEmployeePartiallyById(Long employeeId, Employee updateEmployee) {
        if (employeeRepository.existsByEmail(updateEmployee.getEmail())) {
            throw new EntityNotUniqueException(
                    messages.getMessage(EmployeeMessageKey.EMPLOYEE_ALREADY_EXISTS_BY_EMAIL,
                            updateEmployee.getEmail())
            );
        }
        Employee employeeToUpdate = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(""));
        mapper.copyNotNullFields(employeeToUpdate, updateEmployee);
        employeeRepository.save(employeeToUpdate);
    }

    @Override
    @Transactional
    public void deleteEmployeeById(Long employeeId) {
        Employee employeeToDelete = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        messages.getMessage(EmployeeMessageKey.EMPLOYEE_NOT_FOUND_BY_ID,
                                employeeId)
                ));
        employeeRepository.delete(employeeToDelete);
    }
}
