package com.digitalchief.companymanagement.controller;

import com.digitalchief.companymanagement.entity.Company;
import com.digitalchief.companymanagement.mapper.CompanyMapper;
import com.digitalchief.companymanagement.model.CompanyModel;
import com.digitalchief.companymanagement.service.CompanyService;
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
public class CompanyController {
    private final CompanyService companyService;
    private final CompanyMapper mapper = Mappers.getMapper(CompanyMapper.class);

    @GetMapping("/companies")
    public ResponseEntity<List<CompanyModel>> getCompaniesWithPagination(@PageableDefault Pageable pageable) {
        List<CompanyModel> companyModels = mapper.toModel(companyService.findAllByPageable(pageable));

        return ResponseEntity.ok(companyModels);
    }

    @GetMapping("/companies/{companyId}")
    public ResponseEntity<CompanyModel> getCompanyById(@PathVariable Long companyId) {
        Company company = companyService.findById(companyId);

        return ResponseEntity.ok(mapper.toModel(company));
    }

    @PostMapping("/companies")
    public ResponseEntity<CompanyModel> createCompany(@Valid @RequestBody CompanyModel companyModel) {
        Company createdCompany = companyService.createCompany(mapper.toEntity(companyModel));

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toModel(createdCompany));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/companies/{companyId}")
    public void updateCompany(@PathVariable Long companyId, @Valid @RequestBody CompanyModel companyModel) {
        Company updateCompany = mapper.toEntity(companyModel);
        companyService.updateCompanyById(companyId, updateCompany);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/companies/{companyId}")
    public void updateCompanyPartiallyById(@PathVariable Long companyId, @RequestBody CompanyModel companyModel) {
        Company updateCompany = mapper.toEntity(companyModel);
        companyService.updateCompanyPartiallyById(companyId, updateCompany);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/companies/{companyId}")
    public void deleteCompanyById(@PathVariable Long companyId) {
        companyService.deleteCompanyById(companyId);
    }
}
