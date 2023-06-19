package com.digitalchief.companymanagement.unit.service.impl;

import com.digitalchief.companymanagement.builder.impl.CompanyTestBuilder;
import com.digitalchief.companymanagement.entity.Company;
import com.digitalchief.companymanagement.repository.CompanyRepository;
import com.digitalchief.companymanagement.service.exception.EntityNotFoundException;
import com.digitalchief.companymanagement.service.exception.EntityNotUniqueException;
import com.digitalchief.companymanagement.service.impl.CompanyServiceImpl;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    private static final Long COMPANY_ID = 1L;

    private static final String COMPANY_NAME = "New company";

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyServiceImpl companyService;

    @Nested
    class CompanyServiceImplReadMethodsTest {

        @Test
        void findAll_shouldReturnExpectedCompaniesAndCallRepository() {
            Pageable pageable = PageRequest.of(0, 3);
            List<Company> expectedCompanies = List.of(
                    CompanyTestBuilder.aCompany().build(),
                    CompanyTestBuilder.aCompany().build(),
                    CompanyTestBuilder.aCompany().build()
            );
            doReturn(new PageImpl<>(expectedCompanies)).when(companyRepository).findAll(pageable);

            List<Company> actualCompanies = companyService.findAllByPageable(pageable);

            assertThat(actualCompanies).isEqualTo(expectedCompanies);
            verify(companyRepository).findAll(pageable);
        }

        @Test
        void findById_shouldReturnExpectedCompanyAndCallRepository_whenCompanyIsPresent() {
            Company expectedCompany = CompanyTestBuilder.aCompany().build();
            doReturn(Optional.of(expectedCompany)).when(companyRepository).findById(COMPANY_ID);

            Company actualCompany = companyService.findById(COMPANY_ID);

            assertThat(actualCompany).isEqualTo(expectedCompany);
            verify(companyRepository).findById(COMPANY_ID);
        }

        @Test
        void findById_shouldThrowEntityNotFoundException_whenCompanyIsNotPresent() {
            doReturn(Optional.empty()).when(companyRepository).findById(COMPANY_ID);

            assertThatThrownBy(() -> companyService.findById(COMPANY_ID))
                    .isInstanceOf(EntityNotFoundException.class);
        }

    }

    @Nested
    class CompanyServiceImplCreateMethodsTest {

        @Test
        void createCompany_shouldReturnExpectedCompany_whenCompanyNameIsUnique() {
            Company expectedCompany = CompanyTestBuilder.aCompany().withName(COMPANY_NAME).build();
            doReturn(false).when(companyRepository).existsByName(COMPANY_NAME);
            doReturn(expectedCompany).when(companyRepository).save(expectedCompany);

            Company actualCompany = companyService.createCompany(expectedCompany);

            assertThat(actualCompany).isEqualTo(expectedCompany);
            verify(companyRepository).existsByName(COMPANY_NAME);
            verify(companyRepository).save(expectedCompany);
        }

        @Test
        void createCompany_shouldThrowEntityNotUniqueException_whenCompanyNameIsNotUnique() {
            Company company = CompanyTestBuilder.aCompany().withName(COMPANY_NAME).build();
            doReturn(true).when(companyRepository).existsByName(COMPANY_NAME);

            assertThatThrownBy(() -> companyService.createCompany(company))
                    .isInstanceOf(EntityNotUniqueException.class);
        }

    }

    @Nested
    class CompanyServiceImplUpdateMethodsTest {

        @Test
        void updateCompanyById_shouldCallRepository_whenCompanyNameIsUniqueAndCompanyIsPresent() {
            Company updateCompany = CompanyTestBuilder.aCompany().withName(COMPANY_NAME).build();
            Company companyToUpdate = CompanyTestBuilder.aCompany().build();
            doReturn(false).when(companyRepository).existsByName(COMPANY_NAME);
            doReturn(Optional.of(companyToUpdate)).when(companyRepository).findById(COMPANY_ID);

            companyService.updateCompanyById(COMPANY_ID, updateCompany);

            verify(companyRepository).existsByName(COMPANY_NAME);
            verify(companyRepository).findById(COMPANY_ID);
            verify(companyRepository).save(companyToUpdate);
        }

        @Test
        void updateCompanyById_shouldThrowEntityNotUniqueException_whenCompanyNameIsNotUnique() {
            Company updateCompany = CompanyTestBuilder.aCompany().withName(COMPANY_NAME).build();
            doReturn(true).when(companyRepository).existsByName(COMPANY_NAME);

            assertThatThrownBy(() -> companyService.updateCompanyById(COMPANY_ID, updateCompany))
                    .isInstanceOf(EntityNotUniqueException.class);
        }

        @Test
        void updateCompanyById_shouldThrowEntityNotFoundException_whenCompanyIsNotPresent() {
            Company updateCompany = CompanyTestBuilder.aCompany().withName(COMPANY_NAME).build();
            doReturn(false).when(companyRepository).existsByName(COMPANY_NAME);
            doReturn(Optional.empty()).when(companyRepository).findById(COMPANY_ID);

            assertThatThrownBy(() -> companyService.updateCompanyById(COMPANY_ID, updateCompany))
                    .isInstanceOf(EntityNotFoundException.class);
        }

        @Test
        void updateCompanyPartiallyById_shouldCallRepository_whenCompanyNameIsUniqueAndCompanyIsPresent() {
            Company updateCompany = CompanyTestBuilder.aCompany().withName(COMPANY_NAME).build();
            Company companyToUpdate = CompanyTestBuilder.aCompany().build();
            doReturn(false).when(companyRepository).existsByName(COMPANY_NAME);
            doReturn(Optional.of(companyToUpdate)).when(companyRepository).findById(COMPANY_ID);

            companyService.updateCompanyPartiallyById(COMPANY_ID, updateCompany);

            verify(companyRepository).existsByName(COMPANY_NAME);
            verify(companyRepository).findById(COMPANY_ID);
            verify(companyRepository).save(companyToUpdate);
        }

        @Test
        void updateCompanyPartiallyById_shouldThrowEntityNotUniqueException_whenCompanyNameIsNotUnique() {
            Company updateCompany = CompanyTestBuilder.aCompany().withName(COMPANY_NAME).build();
            doReturn(true).when(companyRepository).existsByName(COMPANY_NAME);

            assertThatThrownBy(() -> companyService.updateCompanyPartiallyById(COMPANY_ID, updateCompany))
                    .isInstanceOf(EntityNotUniqueException.class);
        }

        @Test
        void updateCompanyPartiallyById_shouldThrowEntityNotFoundException_whenCompanyIsNotPresent() {
            Company updateCompany = CompanyTestBuilder.aCompany().withName(COMPANY_NAME).build();
            doReturn(false).when(companyRepository).existsByName(COMPANY_NAME);
            doReturn(Optional.empty()).when(companyRepository).findById(COMPANY_ID);

            assertThatThrownBy(() -> companyService.updateCompanyPartiallyById(COMPANY_ID, updateCompany))
                    .isInstanceOf(EntityNotFoundException.class);
        }

    }

    @Nested
    class CompanyServiceImplDeleteMethodsTest {

        @Test
        void deleteCompanyById_shouldCallRepository_whenCompanyIsPresent() {
            Company companyToDelete = CompanyTestBuilder.aCompany().build();
            doReturn(Optional.of(companyToDelete)).when(companyRepository).findById(COMPANY_ID);

            companyService.deleteCompanyById(COMPANY_ID);

            verify(companyRepository).findById(COMPANY_ID);
            verify(companyRepository).delete(companyToDelete);
        }

        @Test
        void deleteCompanyById_shouldThrowEntityNotFoundException_whenCompanyIsNotPresent() {
            doReturn(Optional.empty()).when(companyRepository).findById(COMPANY_ID);

            assertThatThrownBy(() -> companyService.deleteCompanyById(COMPANY_ID))
                    .isInstanceOf(EntityNotFoundException.class);
        }

    }

}
