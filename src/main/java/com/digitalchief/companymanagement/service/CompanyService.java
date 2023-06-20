package com.digitalchief.companymanagement.service;

import com.digitalchief.companymanagement.entity.Company;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompanyService {

    List<Company> findAllByPageable(Pageable pageable);

    Company findById(Long companyId);

    Company createCompany(Company company);

    void updateCompanyById(Long companyId, Company updateCompany);

    void updateCompanyPartiallyById(Long companyId, Company updateCompany);

    void deleteCompanyById(Long companyId);

}
