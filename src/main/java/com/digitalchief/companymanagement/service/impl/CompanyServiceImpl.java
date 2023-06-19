package com.digitalchief.companymanagement.service.impl;

import com.digitalchief.companymanagement.entity.Company;
import com.digitalchief.companymanagement.mapper.CompanyMapper;
import com.digitalchief.companymanagement.message.key.CompanyMessageKey;
import com.digitalchief.companymanagement.message.source.MessagesSource;
import com.digitalchief.companymanagement.repository.CompanyRepository;
import com.digitalchief.companymanagement.service.CompanyService;
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
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final MessagesSource messagesSource;
    private final CompanyMapper mapper = Mappers.getMapper(CompanyMapper.class);

    @Override
    public List<Company> findAllByPageable(Pageable pageable) {
        return companyRepository.findAll(pageable).getContent();
    }

    @Override
    public Company findById(Long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException(
                        messagesSource.getMessage(CompanyMessageKey.NOT_FOUND_BY_ID, companyId)
                ));
    }

    @Override
    @Transactional
    public Company createCompany(Company company) {
        if (companyRepository.existsByName(company.getName())) {
            throw new EntityNotUniqueException(
                    messagesSource.getMessage(CompanyMessageKey.ALREADY_EXISTS_BY_EMAIL, company.getName())
            );
        }
        return companyRepository.save(company);
    }

    @Override
    @Transactional
    public void updateCompanyById(Long companyId, Company updateCompany) {
        if (companyRepository.existsByName(updateCompany.getName())) {
            throw new EntityNotUniqueException(
                    messagesSource.getMessage(CompanyMessageKey.ALREADY_EXISTS_BY_EMAIL, updateCompany.getName())
            );
        }
        Company companyToUpdate = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException(""));
        mapper.copyAllFields(companyToUpdate, updateCompany);
        companyRepository.save(companyToUpdate);
    }

    @Override
    @Transactional
    public void updateCompanyPartiallyById(Long companyId, Company updateCompany) {
        if (companyRepository.existsByName(updateCompany.getName())) {
            throw new EntityNotUniqueException(
                    messagesSource.getMessage(CompanyMessageKey.ALREADY_EXISTS_BY_EMAIL, updateCompany.getName())
            );
        }
        Company companyToUpdate = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException(""));
        mapper.copyNotNullFields(companyToUpdate, updateCompany);
        companyRepository.save(companyToUpdate);
    }

    @Override
    @Transactional
    public void deleteCompanyById(Long companyId) {
        Company companyToDelete = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException(
                        messagesSource.getMessage(CompanyMessageKey.NOT_FOUND_BY_ID, companyId)
                ));
        companyRepository.delete(companyToDelete);
    }
}
