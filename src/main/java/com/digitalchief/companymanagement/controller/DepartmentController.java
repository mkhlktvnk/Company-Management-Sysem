package com.digitalchief.companymanagement.controller;

import com.digitalchief.companymanagement.entity.Department;
import com.digitalchief.companymanagement.mapper.DepartmentMapper;
import com.digitalchief.companymanagement.model.DepartmentModel;
import com.digitalchief.companymanagement.model.ErrorResponse;
import com.digitalchief.companymanagement.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

@Tag(name = "Department API",
        description = "Operations for working with departments")
@RestController
@RequestMapping("/api/v0")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;
    private final DepartmentMapper mapper = Mappers.getMapper(DepartmentMapper.class);

    @Operation(summary = "Retrieve departments by company ID with pagination and optional sorting")
    @Parameters(value = {
            @Parameter(
                    name = "companyId",
                    description = "ID of the company",
                    example = "1",
                    schema = @Schema(type = "integer")
            ),
            @Parameter(
                    name = "page",
                    description = "The page number of the results to retrieve. Default is 0.",
                    example = "0",
                    schema = @Schema(type = "integer")
            ),
            @Parameter(
                    name = "size",
                    description = "The number of results per page. Default is 10.",
                    example = "10",
                    schema = @Schema(type = "integer")
            ),
            @Parameter(
                    name = "sort",
                    description = "The sorting criteria for the results in the format `property, direction`. " +
                            "Multiple sorting criteria can be separated by commas.",
                    example = "name,asc",
                    schema = @Schema(type = "string")
            )
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Departments were successfully retrieved",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = DepartmentModel.class))
                    )
            )
    })
    @GetMapping("/companies/{companyId}/departments")
    public ResponseEntity<List<DepartmentModel>> getDepartmentsByCompanyIdWithPagination(
            @PathVariable Long companyId, @PageableDefault Pageable pageable) {
        List<Department> departments = departmentService.findAllByCompanyIdWithPagination(companyId, pageable);

        return ResponseEntity.ok(mapper.toModel(departments));
    }

    @Operation(summary = "Retrieve a specific department by company ID and department ID")
    @Parameters(value = {
            @Parameter(
                    name = "companyId",
                    description = "ID of the company",
                    example = "1",
                    schema = @Schema(type = "integer")
            ),
            @Parameter(
                    name = "departmentId",
                    description = "ID of the department",
                    example = "1",
                    schema = @Schema(type = "integer")
            )
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Department was successfully retrieved",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DepartmentModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Department was not found by company ID and department ID",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/companies/{companyId}/departments/{departmentId}")
    public ResponseEntity<DepartmentModel> getDepartmentByCompanyAndDepartmentId(
            @PathVariable Long companyId, @PathVariable Long departmentId) {
        Department department = departmentService.findByCompanyAndDepartmentId(companyId, departmentId);

        return ResponseEntity.ok(mapper.toModel(department));
    }

    @Operation(summary = "Create a department in a specific company")
    @Parameters(value = {
            @Parameter(
                    name = "companyId",
                    description = "ID of the company",
                    example = "1",
                    schema = @Schema(type = "integer")
            )
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Department was successfully created",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DepartmentModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Department was not created due to validation errors or constraints",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Company to create department in was not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/companies/{companyId}/departments")
    public ResponseEntity<DepartmentModel> createDepartmentInCompany(
            @PathVariable Long companyId, @Valid @RequestBody DepartmentModel departmentModel) {
        Department createdDepartment = departmentService
                .createDepartmentInCompany(mapper.toEntity(departmentModel), companyId);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toModel(createdDepartment));
    }


    @Operation(summary = "Update a specific department in a company by department ID")
    @Parameters(value = {
            @Parameter(
                    name = "companyId",
                    description = "ID of the company",
                    example = "1",
                    schema = @Schema(type = "integer")
            ),
            @Parameter(
                    name = "departmentId",
                    description = "ID of the department to update",
                    example = "1",
                    schema = @Schema(type = "integer")
            )
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Department was successfully updated"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Department was not found by company ID or department ID",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/companies/{companyId}/departments/{departmentId}")
    public void updateDepartmentByCompanyAndDepartmentId(@PathVariable Long companyId, @PathVariable Long departmentId,
                                                         @Valid @RequestBody DepartmentModel departmentModel) {
        departmentService.updateDepartmentInCompanyById(companyId, departmentId, mapper.toEntity(departmentModel));
    }

    @Operation(summary = "Partially update a specific department in a company by department ID")
    @Parameters(value = {
            @Parameter(
                    name = "companyId",
                    description = "ID of the company",
                    example = "1",
                    schema = @Schema(type = "integer")
            ),
            @Parameter(
                    name = "departmentId",
                    description = "ID of the department to update partially",
                    example = "1",
                    schema = @Schema(type = "integer")
            )
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Department was successfully updated"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Department was not found by company ID or department ID",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/companies/{companyId}/departments/{departmentId}")
    public void updateDepartmentPartiallyByCompanyAndDepartmentId(
            @PathVariable Long companyId, @PathVariable Long departmentId,
            @RequestBody DepartmentModel departmentModel) {
        departmentService.updateDepartmentInCompanyPartiallyById(companyId, departmentId,
                mapper.toEntity(departmentModel));
    }

    @Operation(summary = "Delete a specific department in a company by department ID")
    @Parameters(value = {
            @Parameter(
                    name = "companyId",
                    description = "ID of the company",
                    example = "1",
                    schema = @Schema(type = "integer")
            ),
            @Parameter(
                    name = "departmentId",
                    description = "ID of the department to delete",
                    example = "1",
                    schema = @Schema(type = "integer")
            )
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Department was successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Department was not found by company ID or department ID",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/companies/{companyId}/departments/{departmentId}")
    public void deleteDepartmentByCompanyAndDepartmentId(@PathVariable Long companyId,
                                                         @PathVariable Long departmentId) {
        departmentService.deleteDepartmentFromCompanyById(companyId, departmentId);
    }
}
