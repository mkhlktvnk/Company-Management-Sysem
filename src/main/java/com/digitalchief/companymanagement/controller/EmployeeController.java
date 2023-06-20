package com.digitalchief.companymanagement.controller;

import com.digitalchief.companymanagement.entity.Employee;
import com.digitalchief.companymanagement.mapper.EmployeeMapper;
import com.digitalchief.companymanagement.model.EmployeeModel;
import com.digitalchief.companymanagement.model.ErrorResponse;
import com.digitalchief.companymanagement.service.EmployeeService;
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

@Tag(name = "Employee API", description = "Operations for working with employees")
@RestController
@RequestMapping("/api/v0")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeMapper mapper = Mappers.getMapper(EmployeeMapper.class);

    @Operation(summary = "Retrieve employees by company ID and department ID with pagination")
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
            )
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Employees were successfully retrieved",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = EmployeeModel.class))
                    )
            )
    })
    @GetMapping("/companies/{companyId}/departments/{departmentId}/employees")
    public ResponseEntity<List<EmployeeModel>> findEmployeesByCompanyIdAndDepartmentIdWithPagination(
            @PathVariable Long companyId, @PathVariable Long departmentId, @PageableDefault Pageable pageable) {
        List<Employee> employees = employeeService
                .findAllByCompanyAndDepartmentIdWithPagination(companyId, departmentId, pageable);

        return ResponseEntity.ok(mapper.toModel(employees));
    }

    @Operation(summary = "Retrieve a specific employee by company ID, department ID, and employee ID")
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
            ),
            @Parameter(
                    name = "id",
                    description = "ID of the employee",
                    example = "1",
                    schema = @Schema(type = "integer")
            )
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee was successfully retrieved",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EmployeeModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Employee was not found by ID",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/companies/{companyId}/departments/{departmentId}/employees/{id}")
    public ResponseEntity<EmployeeModel> findEmployeeById(@PathVariable Long companyId,
                                                          @PathVariable Long departmentId,
                                                          @PathVariable Long id) {
        Employee employee = employeeService.findByCompanyAndDepartmentAndEmployeeId(companyId, departmentId, id);

        return ResponseEntity.ok(mapper.toModel(employee));
    }

    @Operation(summary = "Create a new employee in a department")
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
                    responseCode = "201",
                    description = "Employee was successfully created",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EmployeeModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Employee was not created due to validation errors",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Department or company to create employee in was not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/companies/{companyId}/departments/{departmentId}/employees")
    public ResponseEntity<EmployeeModel> createEmployeeInDepartment(@PathVariable Long companyId,
                                                                    @PathVariable Long departmentId,
                                                                    @Valid @RequestBody EmployeeModel employeeModel) {
        Employee createdEmployee = employeeService
                .createEmployeeInDepartment(mapper.toEntity(employeeModel), companyId, departmentId);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toModel(createdEmployee));
    }

    @Operation(summary = "Update a specific employee by company ID, department ID, and employee ID")
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
            ),
            @Parameter(
                    name = "employeeId",
                    description = "ID of the employee",
                    example = "1",
                    schema = @Schema(type = "integer")
            )
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Employee was successfully updated"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Employee was not updated due to validation errors",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Employee was not found by ID",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/companies/{companyId}/departments/{departmentId}/employees/{employeeId}")
    public void updateEmployeeById(@PathVariable Long companyId, @PathVariable Long departmentId,
                                   @PathVariable Long employeeId, @Valid @RequestBody EmployeeModel employeeModel) {
        Employee employee = mapper.toEntity(employeeModel);
        employeeService.updateEmployeeInDepartmentById(companyId, departmentId, employeeId, employee);
    }

    @Operation(summary = "Partially update a specific employee by company ID, department ID, and employee ID")
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
            ),
            @Parameter(
                    name = "employeeId",
                    description = "ID of the employee",
                    example = "1",
                    schema = @Schema(type = "integer")
            )
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Employee was successfully updated"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Employee was not updated due to validation errors",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Employee was not found by ID",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/companies/{companyId}/departments/{departmentId}/employees/{employeeId}")
    public void updateEmployeePartiallyById(@PathVariable Long companyId, @PathVariable Long departmentId,
                                            @PathVariable Long employeeId,
                                            @RequestBody EmployeeModel employeeModel) {
        Employee employee = mapper.toEntity(employeeModel);
        employeeService.updateEmployeeInDepartmentPartiallyById(companyId, departmentId, employeeId, employee);
    }


    @Operation(summary = "Delete a specific employee by company ID, department ID, and employee ID")
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
            ),
            @Parameter(
                    name = "employeeId",
                    description = "ID of the employee",
                    example = "1",
                    schema = @Schema(type = "integer")
            )
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Employee was successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Employee was not found by ID",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/companies/{companyId}/departments/{departmentId}/employees/{employeeId}")
    public void deleteEmployeeById(@PathVariable Long companyId, @PathVariable Long departmentId,
                                   @PathVariable Long employeeId) {
        employeeService.deleteEmployeeFromDepartmentById(companyId, departmentId, employeeId);
    }
}
