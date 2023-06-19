package com.digitalchief.companymanagement.unit.service.impl;

import com.digitalchief.companymanagement.builder.impl.DepartmentTestBuilder;
import com.digitalchief.companymanagement.builder.impl.EmployeeTestBuilder;
import com.digitalchief.companymanagement.entity.Department;
import com.digitalchief.companymanagement.entity.Employee;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    private static final Long EMPLOYEE_ID = 1L;
    private static final Long DEPARTMENT_ID = 1L;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Nested
    class EmployeeServiceImplReadMethodsTest {

        @Test
        void findAllByPageable_shouldReturnExpectedEmployeesAndCallRepository() {
            Pageable pageable = PageRequest.of(0, 3);
            List<Employee> expectedEmployees = List.of(
                    EmployeeTestBuilder.anEmployee().build(),
                    EmployeeTestBuilder.anEmployee().build(),
                    EmployeeTestBuilder.anEmployee().build()
            );
            doReturn(new PageImpl<>(expectedEmployees)).when(employeeRepository).findAll(pageable);

            List<Employee> actualEmployees = employeeService.findAllByPageable(pageable);

            assertThat(actualEmployees).isEqualTo(expectedEmployees);
            verify(employeeRepository).findAll(pageable);
        }

        @Test
        void findById_shouldReturnExpectedEmployeeAndCallRepository_whenEmployeeIsPresent() {
            Employee expectedEmployee = EmployeeTestBuilder.anEmployee().build();
            doReturn(Optional.of(expectedEmployee)).when(employeeRepository).findById(EMPLOYEE_ID);

            Employee actualEmployee = employeeService.findById(EMPLOYEE_ID);

            assertThat(actualEmployee).isEqualTo(expectedEmployee);
            verify(employeeRepository).findById(EMPLOYEE_ID);
        }

        @Test
        void findById_shouldThrowEntityNotFoundException_whenEmployeeIsNotPresent() {
            doReturn(Optional.empty()).when(employeeRepository).findById(EMPLOYEE_ID);

            assertThatThrownBy(() -> employeeService.findById(EMPLOYEE_ID))
                    .isInstanceOf(EntityNotFoundException.class);
        }

    }

    @Nested
    class EmployeeServiceImplCreateMethodsTest {

        @Test
        void createEmployeeInDepartment_shouldReturnExpectedEmployeeAndCallRepository_whenDepartmentIsPresent() {
            Employee expectedEmployee = EmployeeTestBuilder.anEmployee().build();
            Department department = DepartmentTestBuilder.aDepartment().build();
            doReturn(department).when(departmentService).findById(DEPARTMENT_ID);
            doReturn(expectedEmployee).when(employeeRepository).save(expectedEmployee);

            Employee actualEmployee = employeeService.createEmployeeInDepartment(expectedEmployee, DEPARTMENT_ID);

            assertThat(actualEmployee).isEqualTo(expectedEmployee);
            assertThat(actualEmployee.getDepartment()).isEqualTo(department);
            verify(departmentService).findById(DEPARTMENT_ID);
            verify(employeeRepository).save(expectedEmployee);
        }

        @Test
        void createEmployeeInDepartment_shouldThrowEntityNotFoundException_whenDepartmentIsNotPresent() {
            Employee employee = EmployeeTestBuilder.anEmployee().build();
            doThrow(EntityNotFoundException.class).when(departmentService).findById(DEPARTMENT_ID);

            assertThatThrownBy(() -> employeeService.createEmployeeInDepartment(employee, DEPARTMENT_ID))
                    .isInstanceOf(EntityNotFoundException.class);
        }

    }

    @Nested
    class EmployeeServiceImplUpdateMethodsTest {

        @Test
        void updateEmployeeById_shouldCallRepository_whenEmployeeIsPresentAndFieldsAreValid() {
            Employee updateEmployee = EmployeeTestBuilder.anEmployee().build();
            Employee employeeToUpdate = EmployeeTestBuilder.anEmployee().build();
            doReturn(false).when(employeeRepository).existsByEmail(updateEmployee.getEmail());
            doReturn(Optional.of(employeeToUpdate)).when(employeeRepository).findById(EMPLOYEE_ID);

            employeeService.updateEmployeeById(EMPLOYEE_ID, updateEmployee);

            verify(employeeRepository).existsByEmail(updateEmployee.getEmail());
            verify(employeeRepository).findById(EMPLOYEE_ID);
            verify(employeeRepository).save(employeeToUpdate);
        }

        @Test
        void updateEmployeeById_shouldThrowEntityNotUniqueException_whenEmployeeEmailIsNotUnique() {
            Employee updateEmployee = EmployeeTestBuilder.anEmployee().build();
            doReturn(true).when(employeeRepository).existsByEmail(updateEmployee.getEmail());

            assertThatThrownBy(() -> employeeService.updateEmployeeById(EMPLOYEE_ID, updateEmployee))
                    .isInstanceOf(EntityNotUniqueException.class);
        }

        @Test
        void updateEmployeeById_shouldThrowEntityNotFoundException_whenEmployeeIsNotPresent() {
            Employee updateEmployee = EmployeeTestBuilder.anEmployee().build();
            doReturn(false).when(employeeRepository).existsByEmail(updateEmployee.getEmail());
            doReturn(Optional.empty()).when(employeeRepository).findById(EMPLOYEE_ID);

            assertThatThrownBy(() -> employeeService.updateEmployeeById(EMPLOYEE_ID, updateEmployee))
                    .isInstanceOf(EntityNotFoundException.class);
        }

        @Test
        void updateEmployeePartiallyById_shouldCallRepository_whenEmployeeIsPresentAndFieldsAreValid() {
            Employee updateEmployee = EmployeeTestBuilder.anEmployee().build();
            Employee employeeToUpdate = EmployeeTestBuilder.anEmployee().build();
            doReturn(false).when(employeeRepository).existsByEmail(updateEmployee.getEmail());
            doReturn(Optional.of(employeeToUpdate)).when(employeeRepository).findById(EMPLOYEE_ID);

            employeeService.updateEmployeePartiallyById(EMPLOYEE_ID, updateEmployee);

            verify(employeeRepository).existsByEmail(updateEmployee.getEmail());
            verify(employeeRepository).findById(EMPLOYEE_ID);
            verify(employeeRepository).save(employeeToUpdate);
        }

        @Test
        void updateEmployeePartiallyById_shouldThrowEntityNotUniqueException_whenEmployeeEmailIsNotUnique() {
            Employee updateEmployee = EmployeeTestBuilder.anEmployee().build();
            doReturn(true).when(employeeRepository).existsByEmail(updateEmployee.getEmail());

            assertThatThrownBy(() -> employeeService.updateEmployeePartiallyById(EMPLOYEE_ID, updateEmployee))
                    .isInstanceOf(EntityNotUniqueException.class);
        }

        @Test
        void updateEmployeePartiallyById_shouldThrowEntityNotFoundException_whenEmployeeIsNotPresent() {
            Employee updateEmployee = EmployeeTestBuilder.anEmployee().build();
            doReturn(false).when(employeeRepository).existsByEmail(updateEmployee.getEmail());
            doReturn(Optional.empty()).when(employeeRepository).findById(EMPLOYEE_ID);

            assertThatThrownBy(() -> employeeService.updateEmployeePartiallyById(EMPLOYEE_ID, updateEmployee))
                    .isInstanceOf(EntityNotFoundException.class);
        }

    }

    @Nested
    class EmployeeServiceImplDeleteMethodsTest {

        @Test
        void deleteEmployeeById_shouldCallRepository_whenEmployeeIsPresent() {
            Employee employeeToDelete = EmployeeTestBuilder.anEmployee().build();
            doReturn(Optional.of(employeeToDelete)).when(employeeRepository).findById(EMPLOYEE_ID);

            employeeService.deleteEmployeeById(EMPLOYEE_ID);

            verify(employeeRepository).findById(EMPLOYEE_ID);
            verify(employeeRepository).delete(employeeToDelete);
        }

        @Test
        void deleteEmployeeById_shouldThrowEntityNotFoundException_whenEmployeeIsNotPresent() {
            doReturn(Optional.empty()).when(employeeRepository).findById(EMPLOYEE_ID);

            assertThatThrownBy(() -> employeeService.deleteEmployeeById(EMPLOYEE_ID))
                    .isInstanceOf(EntityNotFoundException.class);
        }

    }

}


