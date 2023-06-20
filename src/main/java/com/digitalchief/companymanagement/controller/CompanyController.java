package com.digitalchief.companymanagement.controller;

import com.digitalchief.companymanagement.entity.Company;
import com.digitalchief.companymanagement.mapper.CompanyMapper;
import com.digitalchief.companymanagement.model.CompanyModel;
import com.digitalchief.companymanagement.model.ErrorResponse;
import com.digitalchief.companymanagement.service.CompanyService;
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

@Tag(
        name = "Companies API",
        description = "Operations for working with companies"
)
@RestController
@RequestMapping("/api/v0")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    private final CompanyMapper mapper = Mappers.getMapper(CompanyMapper.class);

    @Operation(summary = "Retrieve companies with pagination and optional sorting")
    @Parameters(value = {
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
                    example = "createdDate,asc",
                    schema = @Schema(type = "string")
            ),
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Companies were successfully retrieved",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = CompanyModel.class))
                    )
            )
    })
    @GetMapping("/companies")
    public ResponseEntity<List<CompanyModel>> getCompaniesWithPagination(@PageableDefault Pageable pageable) {
        List<CompanyModel> companyModels = mapper.toModel(companyService.findAllByPageable(pageable));

        return ResponseEntity.ok(companyModels);
    }

    @Operation(summary = "Retrieve a specific company by id")
    @Parameters(value = {
            @Parameter(
                    name = "companyId",
                    description = "ID of company to retrieve",
                    example = "1",
                    schema = @Schema(type = "integer")
            )
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Company was successfully retrieved",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CompanyModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Company was not found by id",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"message\":\"Company not found\",\"url\":\"/api/v0/companies/1\"}")
                    )
            ),
    })
    @GetMapping("/companies/{companyId}")
    public ResponseEntity<CompanyModel> getCompanyById(@PathVariable Long companyId) {
        Company company = companyService.findById(companyId);

        return ResponseEntity.ok(mapper.toModel(company));
    }

    @Operation(summary = "Create new company")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Company was successfully created",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CompanyModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Company was not created because of not unique email",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"message\":\"Company with already exists\",\"url\":\"/api/v0/companies\"}")
                    )
            ),

    })
    @PostMapping("/companies")
    public ResponseEntity<CompanyModel> createCompany(@Valid @RequestBody CompanyModel companyModel) {
        Company createdCompany = companyService.createCompany(mapper.toEntity(companyModel));

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toModel(createdCompany));
    }

    @Operation(summary = "Update a specific company by ID")
    @Parameters(value = {
            @Parameter(
                    name = "companyId",
                    description = "ID of the company to update",
                    example = "1",
                    schema = @Schema(type = "integer")
            )
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Company was successfully updated"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Company was not found by ID",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"message\":\"Company not found\",\"url\":\"/api/v0/companies/1\"}")
                    )
            )
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/companies/{companyId}")
    public void updateCompany(@PathVariable Long companyId, @Valid @RequestBody CompanyModel companyModel) {
        Company updateCompany = mapper.toEntity(companyModel);
        companyService.updateCompanyById(companyId, updateCompany);
    }

    @Operation(summary = "Partially update a specific company by ID")
    @Parameters(value = {
            @Parameter(
                    name = "companyId",
                    description = "ID of the company to update partially",
                    example = "1",
                    schema = @Schema(type = "integer")
            )
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Company was successfully updated"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Company was not found by ID",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"message\":\"Company not found\",\"url\":\"/api/v0/companies/1\"}")
                    )
            )
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/companies/{companyId}")
    public void updateCompanyPartiallyById(@PathVariable Long companyId, @RequestBody CompanyModel companyModel) {
        Company updateCompany = mapper.toEntity(companyModel);
        companyService.updateCompanyPartiallyById(companyId, updateCompany);
    }

    @Operation(summary = "Delete a specific company by ID")
    @Parameters(value = {
            @Parameter(
                    name = "companyId",
                    description = "ID of the company to delete",
                    example = "1",
                    schema = @Schema(type = "integer")
            )
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Company was successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Company was not found by ID",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = "{\"message\":\"Company not found\",\"url\":\"/api/v0/companies/1\"}")
                    )
            )
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/companies/{companyId}")
    public void deleteCompanyById(@PathVariable Long companyId) {
        companyService.deleteCompanyById(companyId);
    }
}
