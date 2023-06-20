package com.digitalchief.companymanagement.unit.service.impl;

import com.digitalchief.companymanagement.builder.impl.CompanyTestBuilder;
import com.digitalchief.companymanagement.builder.impl.DepartmentTestBuilder;
import com.digitalchief.companymanagement.entity.Company;
import com.digitalchief.companymanagement.entity.Department;
import com.digitalchief.companymanagement.message.source.MessagesSource;
import com.digitalchief.companymanagement.repository.DepartmentRepository;
import com.digitalchief.companymanagement.service.CompanyService;
import com.digitalchief.companymanagement.service.exception.EntityNotFoundException;
import com.digitalchief.companymanagement.service.impl.DepartmentServiceImpl;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    private static final Long COMPANY_ID = 1L;
    private static final Long DEPARTMENT_ID = 1L;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private CompanyService companyService;

    @Mock
    private MessagesSource messagesSource;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @Nested
    class DepartmentServiceImplReadMethodsTest {

        @Test
        void findAllByCompanyIdWithPagination_shouldReturnExpectedDepartmentsAndCallRepository() {
            Pageable pageable = PageRequest.of(0, 3);
            List<Department> expectedDepartments = List.of(
                    DepartmentTestBuilder.aDepartment().build(),
                    DepartmentTestBuilder.aDepartment().build(),
                    DepartmentTestBuilder.aDepartment().build()
            );
            doReturn(expectedDepartments).when(departmentRepository).findAllByCompanyId(COMPANY_ID, pageable);

            List<Department> actualDepartments = departmentService.findAllByCompanyIdWithPagination(COMPANY_ID, pageable);

            assertThat(actualDepartments).isEqualTo(expectedDepartments);
            verify(departmentRepository).findAllByCompanyId(COMPANY_ID, pageable);
        }

        @Test
        void findByCompanyAndDepartmentId_shouldReturnExpectedDepartmentAndCallRepository_whenDepartmentIsPresent() {
            Department expectedDepartment = DepartmentTestBuilder.aDepartment().build();
            doReturn(Optional.of(expectedDepartment))
                    .when(departmentRepository).findByCompanyIdAndId(COMPANY_ID, DEPARTMENT_ID);

            Department actualDepartment = departmentService.findByCompanyAndDepartmentId(COMPANY_ID, DEPARTMENT_ID);

            assertThat(actualDepartment).isEqualTo(expectedDepartment);
            verify(departmentRepository).findByCompanyIdAndId(COMPANY_ID, DEPARTMENT_ID);
        }

        @Test
        void findByCompanyAndDepartmentId_shouldThrowEntityNotFoundException_whenDepartmentIsNotPresent() {
            doReturn(Optional.empty()).when(departmentRepository).findByCompanyIdAndId(COMPANY_ID, DEPARTMENT_ID);

            assertThatThrownBy(() -> departmentService.findByCompanyAndDepartmentId(COMPANY_ID, DEPARTMENT_ID))
                    .isInstanceOf(EntityNotFoundException.class);
        }

        @Test
        void findById_shouldReturnExpectedDepartmentAndCallRepository_whenDepartmentIsPresent() {
            Department expectedDepartment = DepartmentTestBuilder.aDepartment().build();
            doReturn(Optional.of(expectedDepartment)).when(departmentRepository).findById(DEPARTMENT_ID);

            Department actualDepartment = departmentService.findById(DEPARTMENT_ID);

            assertThat(actualDepartment).isEqualTo(expectedDepartment);
            verify(departmentRepository).findById(DEPARTMENT_ID);
        }

        @Test
        void findById_shouldThrowEntityNotFoundException_whenDepartmentIsNotPresent() {
            doReturn(Optional.empty()).when(departmentRepository).findById(DEPARTMENT_ID);

            assertThatThrownBy(() -> departmentService.findById(DEPARTMENT_ID))
                    .isInstanceOf(EntityNotFoundException.class);
        }

    }

    @Nested
    class DepartmentServiceImplCreateMethodsTest {

        @Test
        void createDepartmentInCompany_shouldReturnExpectedDepartment_whenCompanyExists() {
            Company company = CompanyTestBuilder.aCompany().build();
            Department expectedDepartment = DepartmentTestBuilder.aDepartment().build();

            doReturn(company).when(companyService).findById(COMPANY_ID);
            doReturn(expectedDepartment).when(departmentRepository).save(expectedDepartment);

            Department actualDepartment = departmentService.createDepartmentInCompany(expectedDepartment, COMPANY_ID);

            assertThat(actualDepartment).isEqualTo(expectedDepartment);
            assertThat(actualDepartment.getCompany()).isEqualTo(company);
            verify(companyService).findById(COMPANY_ID);
            verify(departmentRepository).save(expectedDepartment);
        }

    }

    @Nested
    class DepartmentServiceImplUpdateMethodsTest {

        @Test
        void updateDepartmentInCompanyById_shouldCallRepository_whenDepartmentExistsAndCompanyExists() {
            Department updateDepartment = DepartmentTestBuilder.aDepartment().build();
            Department departmentToUpdate = DepartmentTestBuilder.aDepartment().build();

            doReturn(Optional.of(departmentToUpdate))
                    .when(departmentRepository).findByCompanyIdAndId(COMPANY_ID, DEPARTMENT_ID);

            departmentService.updateDepartmentInCompanyById(COMPANY_ID, DEPARTMENT_ID, updateDepartment);

            verify(departmentRepository).findByCompanyIdAndId(COMPANY_ID, DEPARTMENT_ID);
            verify(departmentRepository).save(departmentToUpdate);
        }

        @Test
        void updateDepartmentInCompanyById_shouldThrowEntityNotFoundException_whenDepartmentIsNotPresent() {
            Department updateDepartment = DepartmentTestBuilder.aDepartment().build();

            doReturn(Optional.empty())
                    .when(departmentRepository).findByCompanyIdAndId(COMPANY_ID, DEPARTMENT_ID);

            assertThatThrownBy(() -> departmentService.updateDepartmentInCompanyById(
                    COMPANY_ID, DEPARTMENT_ID, updateDepartment))
                    .isInstanceOf(EntityNotFoundException.class);
        }

        @Test
        void updateDepartmentInCompanyPartiallyById_shouldCallRepository_whenDepartmentExistsAndCompanyExists() {
            Department updateDepartment = DepartmentTestBuilder.aDepartment().build();
            Department departmentToUpdate = DepartmentTestBuilder.aDepartment().build();

            doReturn(Optional.of(departmentToUpdate))
                    .when(departmentRepository).findByCompanyIdAndId(COMPANY_ID, DEPARTMENT_ID);

            departmentService.updateDepartmentInCompanyPartiallyById(COMPANY_ID, DEPARTMENT_ID, updateDepartment);

            verify(departmentRepository).findByCompanyIdAndId(COMPANY_ID, DEPARTMENT_ID);
            verify(departmentRepository).save(departmentToUpdate);
        }

        @Test
        void updateDepartmentInCompanyPartiallyById_shouldThrowEntityNotFoundException_whenDepartmentIsNotPresent() {
            Department updateDepartment = DepartmentTestBuilder.aDepartment().build();

            doReturn(Optional.empty())
                    .when(departmentRepository).findByCompanyIdAndId(COMPANY_ID, DEPARTMENT_ID);

            assertThatThrownBy(() -> departmentService.updateDepartmentInCompanyById(
                    COMPANY_ID, DEPARTMENT_ID, updateDepartment))
                    .isInstanceOf(EntityNotFoundException.class);
        }
    }

    @Nested
    class DepartmentServiceImplDeleteMethodsTest {

        @Test
        void deleteDepartmentFromCompanyById_shouldCallRepository_whenDepartmentExistsAndCompanyExists() {
            Department departmentToDelete = DepartmentTestBuilder.aDepartment().build();

            doReturn(Optional.of(departmentToDelete))
                    .when(departmentRepository).findByCompanyIdAndId(COMPANY_ID, DEPARTMENT_ID);

            departmentService.deleteDepartmentFromCompanyById(COMPANY_ID, DEPARTMENT_ID);

            verify(departmentRepository).findByCompanyIdAndId(COMPANY_ID, DEPARTMENT_ID);
            verify(departmentRepository).delete(departmentToDelete);
        }

        @Test
        void deleteDepartmentFromCompanyById_shouldThrowEntityNotFoundException_whenDepartmentIsNotPresent() {
            doReturn(Optional.empty())
                    .when(departmentRepository).findByCompanyIdAndId(COMPANY_ID, DEPARTMENT_ID);

            assertThatThrownBy(() -> departmentService.deleteDepartmentFromCompanyById(COMPANY_ID, DEPARTMENT_ID))
                    .isInstanceOf(EntityNotFoundException.class);
        }

    }
}


