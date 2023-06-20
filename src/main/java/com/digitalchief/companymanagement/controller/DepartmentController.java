package com.digitalchief.companymanagement.controller;

import com.digitalchief.companymanagement.entity.Department;
import com.digitalchief.companymanagement.mapper.DepartmentMapper;
import com.digitalchief.companymanagement.model.DepartmentModel;
import com.digitalchief.companymanagement.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v0")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;
    private final DepartmentMapper mapper = Mappers.getMapper(DepartmentMapper.class);

    @GetMapping("/companies/{companyId}/departments")
    public ResponseEntity<List<DepartmentModel>> getDepartmentsByCompanyIdWithPagination(
            @PathVariable Long companyId, @PageableDefault Pageable pageable) {
        List<Department> departments = departmentService.findAllByCompanyIdWithPagination(companyId, pageable);

        return ResponseEntity.ok(mapper.toModel(departments));
    }

    @GetMapping("/companies/{companyId}/departments/{departmentId}")
    public ResponseEntity<DepartmentModel> getDepartmentByCompanyAndDepartmentId(
            @PathVariable Long companyId, @PathVariable Long departmentId) {
        Department department = departmentService.findByCompanyAndDepartmentId(companyId, departmentId);

        return ResponseEntity.ok(mapper.toModel(department));
    }

    @PostMapping("/companies/{companyId}/departments")
    public ResponseEntity<DepartmentModel> createDepartmentInCompany(
            @PathVariable Long companyId, @Valid @RequestBody DepartmentModel departmentModel) {
        Department createdDepartment = departmentService
                .createDepartmentInCompany(mapper.toEntity(departmentModel), companyId);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toModel(createdDepartment));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/companies/{companyId}/departments/{departmentId}")
    public void updateDepartmentByCompanyAndDepartmentId(@PathVariable Long companyId, @PathVariable Long departmentId,
                                                         @Valid @RequestBody DepartmentModel departmentModel) {
        departmentService.updateDepartmentInCompanyById(companyId, departmentId, mapper.toEntity(departmentModel));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/companies/{companyId}/departments/{departmentId}")
    public void updateDepartmentPartiallyByCompanyAndDepartmentId(
            @PathVariable Long companyId, @PathVariable Long departmentId,
            @RequestBody DepartmentModel departmentModel) {
        departmentService.updateDepartmentInCompanyPartiallyById(companyId, departmentId,
                mapper.toEntity(departmentModel));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/companies/{companyId}/departments/{departmentId}")
    public void deleteDepartmentByCompanyAndDepartmentId(@PathVariable Long companyId,
                                                         @PathVariable Long departmentId) {
        departmentService.deleteDepartmentFromCompanyById(companyId, departmentId);
    }
}
