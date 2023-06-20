package com.digitalchief.companymanagement.controller;

import com.digitalchief.companymanagement.entity.Employee;
import com.digitalchief.companymanagement.mapper.EmployeeMapper;
import com.digitalchief.companymanagement.model.EmployeeModel;
import com.digitalchief.companymanagement.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v0")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeMapper mapper = Mappers.getMapper(EmployeeMapper.class);

    @GetMapping("/companies/{companyId}/departments/{departmentId}/employees")
    public ResponseEntity<List<EmployeeModel>> findEmployeesByCompanyIdAndDepartmentIdWithPagination(
            @PathVariable Long companyId, @PathVariable Long departmentId, @PageableDefault Pageable pageable) {
        List<Employee> employees = employeeService
                .findAllByCompanyAndDepartmentIdWithPagination(companyId, departmentId, pageable);

        return ResponseEntity.ok(mapper.toModel(employees));
    }

    @GetMapping("/companies/{companyId}/departments/{departmentId}/employees/{id}")
    public ResponseEntity<EmployeeModel> findEmployeeById(@PathVariable Long companyId,
                                                          @PathVariable Long departmentId,
                                                          @PathVariable Long id) {
        Employee employee = employeeService.findByCompanyAndDepartmentAndEmployeeId(companyId, departmentId, id);

        return ResponseEntity.ok(mapper.toModel(employee));
    }

    @PostMapping("/companies/{companyId}/departments/{departmentId}/employees")
    public ResponseEntity<EmployeeModel> createEmployeeInDepartment(@PathVariable Long companyId,
                                                                    @PathVariable Long departmentId,
                                                                    @Valid @RequestBody EmployeeModel employeeModel) {
        Employee createdEmployee = employeeService
                .createEmployeeInDepartment(mapper.toEntity(employeeModel), companyId, departmentId);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toModel(createdEmployee));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/companies/{companyId}/departments/{departmentId}/employees/{employeeId}")
    public void updateEmployeeById(@PathVariable Long companyId, @PathVariable Long departmentId,
                                   @PathVariable Long employeeId, @Valid @RequestBody EmployeeModel employeeModel) {
        Employee employee = mapper.toEntity(employeeModel);
        employeeService.updateEmployeeInDepartmentById(companyId, departmentId, employeeId, employee);
    }

    @PatchMapping("/companies/{companyId}/departments/{departmentId}/employees/{employeeId}")
    public void updateEmployeePartiallyById(@PathVariable Long companyId, @PathVariable Long departmentId,
                                            @PathVariable Long employeeId,
                                            @Valid @RequestBody EmployeeModel employeeModel) {
        Employee employee = mapper.toEntity(employeeModel);
        employeeService.updateEmployeeInDepartmentPartiallyById(companyId, departmentId, employeeId, employee);
    }

    @DeleteMapping("/companies/{companyId}/departments/{departmentId}/employees/{employeeId}")
    public void deleteEmployeeById(@PathVariable Long companyId, @PathVariable Long departmentId,
                                   @PathVariable Long employeeId) {
        employeeService.deleteEmployeeFromDepartmentById(companyId, departmentId, employeeId);
    }
}
