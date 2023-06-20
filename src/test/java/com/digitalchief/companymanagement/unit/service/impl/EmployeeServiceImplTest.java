package com.digitalchief.companymanagement.unit.service.impl;

import com.digitalchief.companymanagement.builder.impl.DepartmentTestBuilder;
import com.digitalchief.companymanagement.builder.impl.EmployeeTestBuilder;
import com.digitalchief.companymanagement.entity.Department;
import com.digitalchief.companymanagement.entity.Employee;
import com.digitalchief.companymanagement.message.source.MessagesSource;
import com.digitalchief.companymanagement.repository.CompanyRepository;
import com.digitalchief.companymanagement.repository.EmployeeRepository;
import com.digitalchief.companymanagement.service.DepartmentService;
import com.digitalchief.companymanagement.service.exception.EntityNotFoundException;
import com.digitalchief.companymanagement.service.exception.EntityNotUniqueException;
import com.digitalchief.companymanagement.service.impl.EmployeeServiceImpl;
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
class EmployeeServiceImplTest {

    private static final Long COMPANY_ID = 1L;
    private static final Long DEPARTMENT_ID = 2L;
    private static final Long EMPLOYEE_ID = 3L;

    private static final String EMPLOYEE_EMAIL = "employee@example.com";

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private DepartmentService departmentService;

    @Mock
    private MessagesSource messagesSource;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Nested
    class EmployeeServiceImplReadMethodsTest {

        @Test
        void findAllByCompanyAndDepartmentIdWithPagination_shouldReturnExpectedEmployeesAndCallRepository() {
            Pageable pageable = PageRequest.of(0, 3);
            List<Employee> expectedEmployees = List.of(
                    EmployeeTestBuilder.anEmployee().build(),
                    EmployeeTestBuilder.anEmployee().build(),
                    EmployeeTestBuilder.anEmployee().build()
            );
            doReturn(expectedEmployees).when(employeeRepository).findAllByDepartmentIdAndId(
                    COMPANY_ID, DEPARTMENT_ID, pageable
            );

            List<Employee> actualEmployees = employeeService.findAllByCompanyAndDepartmentIdWithPagination(
                    COMPANY_ID, DEPARTMENT_ID, pageable
            );

            assertThat(actualEmployees).isEqualTo(expectedEmployees);
            verify(employeeRepository).findAllByDepartmentIdAndId(COMPANY_ID, DEPARTMENT_ID, pageable);
        }

        @Test
        void findByCompanyAndDepartmentAndEmployeeId_shouldReturnExpectedEmployeeAndCallRepository_whenEmployeeIsPresent() {
            Employee expectedEmployee = EmployeeTestBuilder.anEmployee().build();
            doReturn(Optional.of(expectedEmployee)).when(employeeRepository).findByDepartmentIdAndId(
                    DEPARTMENT_ID, EMPLOYEE_ID
            );
            doReturn(true).when(companyRepository).existsById(COMPANY_ID);

            Employee actualEmployee = employeeService.findByCompanyAndDepartmentAndEmployeeId(
                    COMPANY_ID, DEPARTMENT_ID, EMPLOYEE_ID
            );

            assertThat(actualEmployee).isEqualTo(expectedEmployee);
            verify(employeeRepository).findByDepartmentIdAndId(DEPARTMENT_ID, EMPLOYEE_ID);
        }

        @Test
        void findByCompanyAndDepartmentAndEmployeeId_shouldThrowEntityNotFoundException_whenEmployeeIsNotPresent() {
            doReturn(Optional.empty()).when(employeeRepository).findByDepartmentIdAndId(
                    DEPARTMENT_ID, EMPLOYEE_ID
            );
            doReturn(true).when(companyRepository).existsById(COMPANY_ID);

            assertThatThrownBy(() -> employeeService.findByCompanyAndDepartmentAndEmployeeId(
                    COMPANY_ID, DEPARTMENT_ID, EMPLOYEE_ID
            )).isInstanceOf(EntityNotFoundException.class);
        }

        @Test
        void findByCompanyAndDepartmentAndEmployeeId_shouldThrowEntityNotUniqueException_whenCompanyIsNotPresent() {
            doReturn(false).when(companyRepository).existsById(COMPANY_ID);

            assertThatThrownBy(() -> employeeService
                    .findByCompanyAndDepartmentAndEmployeeId(COMPANY_ID, DEPARTMENT_ID, EMPLOYEE_ID))
                    .isInstanceOf(EntityNotUniqueException.class);
        }

    }

    @Nested
    class EmployeeServiceImplCreateMethodsTests {

        @Test
        void createEmployeeInDepartment_shouldReturnExpectedEmployee_whenCompanyAndDepartmentArePresentAndEmailIsNotPresent() {
            Department departmentToCreateEmployeeIn = DepartmentTestBuilder.aDepartment().build();
            Employee expectedEmployee = EmployeeTestBuilder.anEmployee().withEmail(EMPLOYEE_EMAIL).build();
            doReturn(true).when(companyRepository).existsById(COMPANY_ID);
            doReturn(false).when(employeeRepository).existsByEmail(EMPLOYEE_EMAIL);
            doReturn(departmentToCreateEmployeeIn).when(departmentService)
                    .findByCompanyAndDepartmentId(COMPANY_ID, DEPARTMENT_ID);
            doReturn(expectedEmployee).when(employeeRepository).save(expectedEmployee);

            Employee actualEmployee = employeeService.createEmployeeInDepartment(
                    expectedEmployee, COMPANY_ID, DEPARTMENT_ID
            );

            assertThat(actualEmployee).isEqualTo(expectedEmployee);
            verify(companyRepository).existsById(COMPANY_ID);
            verify(employeeRepository).existsByEmail(EMPLOYEE_EMAIL);
            verify(departmentService).findByCompanyAndDepartmentId(COMPANY_ID, DEPARTMENT_ID);
            verify(employeeRepository).save(expectedEmployee);
        }

        @Test
        void createEmployeeInDepartment_shouldThrowEntityNotFoundException_whenCompanyIsNotPresent() {
            Employee employee = EmployeeTestBuilder.anEmployee().withEmail(EMPLOYEE_EMAIL).build();
            doReturn(false).when(companyRepository).existsById(COMPANY_ID);

            assertThatThrownBy(() -> employeeService.createEmployeeInDepartment(employee, COMPANY_ID, DEPARTMENT_ID))
                    .isInstanceOf(EntityNotFoundException.class);
        }

        @Test
        void createEmployeeInDepartment_shouldThrowEntityNotUniqueException_whenEmailIsNotUnique() {
            Employee employee = EmployeeTestBuilder.anEmployee().withEmail(EMPLOYEE_EMAIL).build();
            doReturn(true).when(companyRepository).existsById(COMPANY_ID);
            doReturn(true).when(employeeRepository).existsByEmail(EMPLOYEE_EMAIL);

            assertThatThrownBy(() -> employeeService.createEmployeeInDepartment(employee, COMPANY_ID, DEPARTMENT_ID))
                    .isInstanceOf(EntityNotUniqueException.class);
        }

    }

    @Nested
    class EmployeeServiceImplUpdateMethodsTest {

        @Test
        void updateEmployeeInDepartmentById_shouldCallRepositories_whenCompanyAndDepartmentAndEmployeeArePresent() {
            Employee updateEmployee = EmployeeTestBuilder.anEmployee().withEmail(EMPLOYEE_EMAIL).build();
            Employee employeeToUpdate = EmployeeTestBuilder.anEmployee().build();
            doReturn(true).when(companyRepository).existsById(COMPANY_ID);
            doReturn(false).when(employeeRepository).existsByEmail(EMPLOYEE_EMAIL);
            doReturn(Optional.of(employeeToUpdate)).when(employeeRepository)
                    .findByDepartmentIdAndId(DEPARTMENT_ID, EMPLOYEE_ID);

            employeeService.updateEmployeeInDepartmentById(COMPANY_ID, DEPARTMENT_ID, EMPLOYEE_ID, updateEmployee);

            verify(companyRepository).existsById(COMPANY_ID);
            verify(employeeRepository).existsByEmail(EMPLOYEE_EMAIL);
            verify(employeeRepository).findByDepartmentIdAndId(DEPARTMENT_ID, EMPLOYEE_ID);
        }

        @Test
        void updateEmployeeInDepartmentById_shouldThrowEntityNotFoundException_whenCompanyIsNotPresent() {
            Employee updateEmployee = EmployeeTestBuilder.anEmployee().withEmail(EMPLOYEE_EMAIL).build();
            doReturn(false).when(companyRepository).existsById(COMPANY_ID);

            assertThatThrownBy(() -> employeeService
                    .updateEmployeeInDepartmentById(COMPANY_ID, DEPARTMENT_ID, EMPLOYEE_ID, updateEmployee))
                    .isInstanceOf(EntityNotFoundException.class);
        }

        @Test
        void updateEmployeeInDepartmentById_shouldThrowEntityNotUniqueException_whenEmailIsNotUnique() {
            Employee updateEmployee = EmployeeTestBuilder.anEmployee().withEmail(EMPLOYEE_EMAIL).build();
            doReturn(true).when(companyRepository).existsById(COMPANY_ID);
            doReturn(true).when(employeeRepository).existsByEmail(EMPLOYEE_EMAIL);

            assertThatThrownBy(() -> employeeService
                    .updateEmployeeInDepartmentById(COMPANY_ID, DEPARTMENT_ID, EMPLOYEE_ID, updateEmployee))
                    .isInstanceOf(EntityNotUniqueException.class);
        }

        @Test
        void updateEmployeeInDepartmentById_shouldThrowEntityNotFoundException_whenDepartmentIsNotPresent() {
            Employee updateEmployee = EmployeeTestBuilder.anEmployee().withEmail(EMPLOYEE_EMAIL).build();
            doReturn(true).when(companyRepository).existsById(COMPANY_ID);
            doReturn(false).when(employeeRepository).existsByEmail(EMPLOYEE_EMAIL);
            doReturn(Optional.empty()).when(employeeRepository)
                    .findByDepartmentIdAndId(DEPARTMENT_ID, EMPLOYEE_ID);

            assertThatThrownBy(() -> employeeService
                    .updateEmployeeInDepartmentById(COMPANY_ID, DEPARTMENT_ID, EMPLOYEE_ID, updateEmployee))
                    .isInstanceOf(EntityNotFoundException.class);
        }

        @Test
        void updateEmployeeInDepartmentPartiallyById_shouldCallRepositories_whenCompanyAndDepartmentArePresent() {
            Employee updateEmployee = EmployeeTestBuilder.anEmployee().withEmail(EMPLOYEE_EMAIL).build();
            Employee employeeToUpdate = EmployeeTestBuilder.anEmployee().build();
            doReturn(true).when(companyRepository).existsById(COMPANY_ID);
            doReturn(false).when(employeeRepository).existsByEmail(EMPLOYEE_EMAIL);
            doReturn(Optional.of(updateEmployee)).when(employeeRepository)
                    .findByDepartmentIdAndId(DEPARTMENT_ID, EMPLOYEE_ID);

            employeeService.updateEmployeeInDepartmentPartiallyById(
                    COMPANY_ID, DEPARTMENT_ID, EMPLOYEE_ID, updateEmployee
            );

            verify(companyRepository).existsById(COMPANY_ID);
            verify(employeeRepository).existsByEmail(EMPLOYEE_EMAIL);
            verify(employeeRepository).findByDepartmentIdAndId(DEPARTMENT_ID, EMPLOYEE_ID);
        }

        @Test
        void updateEmployeeInDepartmentPartiallyById_shouldThrowEntityNotFoundException_whenCompanyIsNotPresent() {
            Employee updateEmployee = EmployeeTestBuilder.anEmployee().withEmail(EMPLOYEE_EMAIL).build();
            doReturn(false).when(companyRepository).existsById(COMPANY_ID);

            assertThatThrownBy(() -> employeeService
                    .updateEmployeeInDepartmentPartiallyById(COMPANY_ID, DEPARTMENT_ID, EMPLOYEE_ID, updateEmployee))
                    .isInstanceOf(EntityNotFoundException.class);
        }

        @Test
        void updateEmployeeInDepartmentPartiallyById_shouldThrowEntityNotUniqueException_whenEmailIsNotUnique() {
            Employee updateEmployee = EmployeeTestBuilder.anEmployee().withEmail(EMPLOYEE_EMAIL).build();
            doReturn(true).when(companyRepository).existsById(COMPANY_ID);
            doReturn(true).when(employeeRepository).existsByEmail(EMPLOYEE_EMAIL);

            assertThatThrownBy(() -> employeeService
                    .updateEmployeeInDepartmentPartiallyById(COMPANY_ID, DEPARTMENT_ID, EMPLOYEE_ID, updateEmployee))
                    .isInstanceOf(EntityNotUniqueException.class);
        }

        @Test
        void updateEmployeeInDepartmentPartiallyById_shouldThrowEntityNotFoundException_whenDepartmentIsNotPresent() {
            Employee updateEmployee = EmployeeTestBuilder.anEmployee().withEmail(EMPLOYEE_EMAIL).build();
            doReturn(true).when(companyRepository).existsById(COMPANY_ID);
            doReturn(false).when(employeeRepository).existsByEmail(EMPLOYEE_EMAIL);
            doReturn(Optional.empty()).when(employeeRepository)
                    .findByDepartmentIdAndId(DEPARTMENT_ID, EMPLOYEE_ID);

            assertThatThrownBy(() -> employeeService
                    .updateEmployeeInDepartmentPartiallyById(COMPANY_ID, DEPARTMENT_ID, EMPLOYEE_ID, updateEmployee))
                    .isInstanceOf(EntityNotFoundException.class);
        }
    }

    @Nested
    class EmployeeServiceImplDeleteMethodsTest {

        @Test
        void deleteEmployeeFromDepartmentById_shouldCallRepositories_whenCompanyAndDepartmentAndEmployeeArePresent() {
            Employee employeeToDelete = EmployeeTestBuilder.anEmployee().withEmail(EMPLOYEE_EMAIL).build();
            doReturn(true).when(companyRepository).existsById(COMPANY_ID);
            doReturn(Optional.of(employeeToDelete)).when(employeeRepository)
                    .findByDepartmentIdAndId(DEPARTMENT_ID, EMPLOYEE_ID);

            employeeService.deleteEmployeeFromDepartmentById(COMPANY_ID, DEPARTMENT_ID, EMPLOYEE_ID);

            verify(companyRepository).existsById(COMPANY_ID);
            verify(employeeRepository).findByDepartmentIdAndId(DEPARTMENT_ID, EMPLOYEE_ID);
            verify(employeeRepository).delete(employeeToDelete);
        }

        @Test
        void deleteEmployeeFromDepartmentById_shouldThrowEntityNotFoundException_whenCompanyIsNotPresent() {
            doReturn(false).when(companyRepository).existsById(COMPANY_ID);

            assertThatThrownBy(() -> employeeService
                    .deleteEmployeeFromDepartmentById(COMPANY_ID, DEPARTMENT_ID, EMPLOYEE_ID))
                    .isInstanceOf(EntityNotFoundException.class);
        }

        @Test
        void deleteEmployeeFromDepartmentById_shouldThrowEntityNotFoundException_whenEmployeeIsNotPresent() {
            doReturn(true).when(companyRepository).existsById(COMPANY_ID);
            doReturn(Optional.empty()).when(employeeRepository)
                    .findByDepartmentIdAndId(DEPARTMENT_ID, EMPLOYEE_ID);

            assertThatThrownBy(() -> employeeService
                    .deleteEmployeeFromDepartmentById(COMPANY_ID, DEPARTMENT_ID, EMPLOYEE_ID))
                    .isInstanceOf(EntityNotFoundException.class);
        }
    }
}




