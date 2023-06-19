package com.digitalchief.companymanagement.unit.service.impl;

import com.digitalchief.companymanagement.builder.impl.CompanyTestBuilder;
import com.digitalchief.companymanagement.builder.impl.DepartmentTestBuilder;
import com.digitalchief.companymanagement.entity.Company;
import com.digitalchief.companymanagement.entity.Department;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    private static final Long DEPARTMENT_ID = 1L;
    private static final Long COMPANY_ID = 1L;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @Nested
    class DepartmentServiceImplReadMethodsTest {

        @Test
        void findAllByPageable_shouldReturnExpectedDepartmentsAndCallRepository() {
            Pageable pageable = PageRequest.of(0, 3);
            List<Department> expectedDepartments = List.of(
                    DepartmentTestBuilder.aDepartment().build(),
                    DepartmentTestBuilder.aDepartment().build(),
                    DepartmentTestBuilder.aDepartment().build()
            );
            doReturn(new PageImpl<>(expectedDepartments)).when(departmentRepository).findAll(pageable);

            List<Department> actualDepartments = departmentService.findAllByPageable(pageable);

            assertThat(actualDepartments).isEqualTo(expectedDepartments);
            verify(departmentRepository).findAll(pageable);
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
        void createDepartmentInCompany_shouldReturnExpectedDepartmentAndCallRepository_whenCompanyIsPresent() {
            Department departmentToCreate = DepartmentTestBuilder.aDepartment().build();
            Company company = CompanyTestBuilder.aCompany().build();
            doReturn(company).when(companyService).findById(COMPANY_ID);
            doReturn(departmentToCreate).when(departmentRepository).save(departmentToCreate);

            Department createdDepartment = departmentService.createDepartmentInCompany(departmentToCreate, COMPANY_ID);

            assertThat(createdDepartment).isEqualTo(departmentToCreate);
            assertThat(createdDepartment.getCompany()).isEqualTo(company);
            verify(companyService).findById(COMPANY_ID);
            verify(departmentRepository).save(departmentToCreate);
        }

    }

    @Nested
    class DepartmentServiceImplUpdateMethodsTest {

        @Test
        void updateDepartmentById_shouldCallRepository_whenDepartmentIsPresent() {
            Department updateDepartment = DepartmentTestBuilder.aDepartment().build();
            Department departmentToUpdate = DepartmentTestBuilder.aDepartment().build();
            doReturn(Optional.of(departmentToUpdate)).when(departmentRepository).findById(DEPARTMENT_ID);

            departmentService.updateDepartmentById(DEPARTMENT_ID, updateDepartment);

            verify(departmentRepository).findById(DEPARTMENT_ID);
            verify(departmentRepository).save(departmentToUpdate);
        }

        @Test
        void updateDepartmentById_shouldThrowEntityNotFoundException_whenDepartmentIsNotPresent() {
            Department updateDepartment = DepartmentTestBuilder.aDepartment().build();
            doReturn(Optional.empty()).when(departmentRepository).findById(DEPARTMENT_ID);

            assertThatThrownBy(() -> departmentService.updateDepartmentById(DEPARTMENT_ID, updateDepartment))
                    .isInstanceOf(EntityNotFoundException.class);
            verify(departmentRepository).findById(DEPARTMENT_ID);
            verify(departmentRepository, never()).save(any());
        }

        @Test
        void updateDepartmentPartiallyById_shouldCallRepository_whenDepartmentIsPresent() {
            Department updateDepartment = DepartmentTestBuilder.aDepartment().build();
            Department departmentToUpdate = DepartmentTestBuilder.aDepartment().build();
            doReturn(Optional.of(departmentToUpdate)).when(departmentRepository).findById(DEPARTMENT_ID);

            departmentService.updateDepartmentPartiallyById(DEPARTMENT_ID, updateDepartment);

            verify(departmentRepository).findById(DEPARTMENT_ID);
            verify(departmentRepository).save(departmentToUpdate);
        }

        @Test
        void updateDepartmentPartiallyById_shouldThrowEntityNotFoundException_whenDepartmentIsNotPresent() {
            Department updateDepartment = DepartmentTestBuilder.aDepartment().build();
            doReturn(Optional.empty()).when(departmentRepository).findById(DEPARTMENT_ID);

            assertThatThrownBy(() -> departmentService.updateDepartmentPartiallyById(DEPARTMENT_ID, updateDepartment))
                    .isInstanceOf(EntityNotFoundException.class);
        }

    }

    @Nested
    class DepartmentServiceImplDeleteMethodsTest {

        @Test
        void deleteDepartmentById_shouldCallRepository_whenDepartmentIsPresent() {
            Department departmentToDelete = DepartmentTestBuilder.aDepartment().build();
            doReturn(Optional.of(departmentToDelete)).when(departmentRepository).findById(DEPARTMENT_ID);

            departmentService.deleteDepartmentById(DEPARTMENT_ID);

            verify(departmentRepository).findById(DEPARTMENT_ID);
            verify(departmentRepository).delete(departmentToDelete);
        }

        @Test
        void deleteDepartmentById_shouldThrowEntityNotFoundException_whenDepartmentIsNotPresent() {
            doReturn(Optional.empty()).when(departmentRepository).findById(DEPARTMENT_ID);

            assertThatThrownBy(() -> departmentService.deleteDepartmentById(DEPARTMENT_ID))
                    .isInstanceOf(EntityNotFoundException.class);
        }

    }

}

