package com.digitalchief.companymanagement.integration.controller;

import com.digitalchief.companymanagement.builder.impl.EmployeeModelTestBuilder;
import com.digitalchief.companymanagement.integration.BaseIntegrationTest;
import com.digitalchief.companymanagement.model.EmployeeModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

@AutoConfigureMockMvc
public class EmployeeControllerTest extends BaseIntegrationTest {

    private static final Long VALID_COMPANY_ID = 1L;

    private static final Long VALID_DEPARTMENT_ID = 1L;

    private static final Long VALID_EMPLOYEE_ID = 1L;

    private static final Long INVALID_COMPANY_ID = 1000L;

    private static final Long INVALID_DEPARTMENT_ID = 1001L;

    private static final Long INVALID_EMPLOYEE_ID = 1002L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class EmployeeControllerReadMethodsTest {

        @Test
        @SneakyThrows
        void findEmployeesByCompanyIdAndDepartmentIdWithPagination_shouldReturnOkStatusAndCorrectCountOfEmployees() {
            int expectedLength = 1;
            String url = fromPath("/api/v0/companies/{companyId}/departments/{departmentId}/employees")
                    .buildAndExpand(VALID_COMPANY_ID, VALID_DEPARTMENT_ID)
                    .toUriString();


            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.length()").value(expectedLength));
        }

        @Test
        @SneakyThrows
        void findEmployeeById_shouldReturnOkStatusAndExpectedJson_whenEmployeeIsPresent() {
            String url = fromPath("/api/v0/companies/{companyId}/departments/{departmentId}/employees/{id}")
                    .buildAndExpand(VALID_COMPANY_ID, VALID_DEPARTMENT_ID, VALID_EMPLOYEE_ID)
                    .toUriString();
            EmployeeModel employeeModel = EmployeeModelTestBuilder.anEmployeeModel()
                    .withId(1L)
                    .withFirstname("John")
                    .withLastname("Doe")
                    .withPosition("Manager")
                    .withAge(35)
                    .withEmail("john.doe@example.com")
                    .withDateOfEmployment(LocalDate.parse("2019-01-15"))
                    .withSalaryPerMonth(new BigDecimal("5000.00"))
                    .build();
            String expectedJson = objectMapper.writeValueAsString(employeeModel);

            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(expectedJson));
        }

        @Test
        @SneakyThrows
        void findEmployeeById_shouldReturnNotFoundStatus_whenEmployeeIsNotPresent() {
            String url = fromPath("/api/v0/companies/{companyId}/departments/{departmentId}/employees/{id}")
                    .buildAndExpand(VALID_COMPANY_ID, VALID_DEPARTMENT_ID, INVALID_EMPLOYEE_ID)
                    .toUriString();

            mockMvc.perform(get(url))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.url").value(url));
        }
    }

    @Nested
    class EmployeeControllerCreateMethodsTest {

        @Test
        @SneakyThrows
        void createEmployeeInDepartment_shouldReturnOkStatusAndEmployeeWithNotNullId() {
            String url = fromPath("/api/v0/companies/{companyId}/departments/{departmentId}/employees")
                    .buildAndExpand(VALID_COMPANY_ID, VALID_DEPARTMENT_ID)
                    .toUriString();
            EmployeeModel employeeModel = EmployeeModelTestBuilder.anEmployeeModel()
                    .withFirstname("Mike")
                    .withLastname("Jackson")
                    .withPosition("Software Engineer")
                    .withAge(21)
                    .withEmail("mike.jackson@example.com")
                    .withDateOfEmployment(LocalDate.parse("2023-07-01"))
                    .withSalaryPerMonth(new BigDecimal("500.00"))
                    .build();
            String json = objectMapper.writeValueAsString(employeeModel);

            mockMvc.perform(post(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").isNotEmpty());
        }

        @Test
        @SneakyThrows
        void createEmployeeInDepartment_shouldReturnNotFoundStatusAndErrorResponse_whenCompanyIsNotPresent() {
            String url = fromPath("/api/v0/companies/{companyId}/departments/{departmentId}/employees")
                    .buildAndExpand(INVALID_COMPANY_ID, VALID_DEPARTMENT_ID)
                    .toUriString();
            EmployeeModel employeeModel = EmployeeModelTestBuilder.anEmployeeModel()
                    .withFirstname("Mike")
                    .withLastname("Jackson")
                    .withPosition("Software Engineer")
                    .withAge(21)
                    .withEmail("mike.jackson@example.com")
                    .withDateOfEmployment(LocalDate.parse("2023-07-01"))
                    .withSalaryPerMonth(new BigDecimal("500.00"))
                    .build();
            String json = objectMapper.writeValueAsString(employeeModel);

            mockMvc.perform(post(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.url").value(url));
        }

        @Test
        @SneakyThrows
        void createEmployeeInDepartment_shouldReturnBadRequestStatus_whenEmailIsNotUnique() {
            String url = fromPath("/api/v0/companies/{companyId}/departments/{departmentId}/employees")
                    .buildAndExpand(VALID_COMPANY_ID, VALID_DEPARTMENT_ID)
                    .toUriString();
            EmployeeModel employeeModel = EmployeeModelTestBuilder.anEmployeeModel()
                    .withFirstname("Alexander")
                    .withLastname("Doe")
                    .withPosition("UI/UX")
                    .withAge(30)
                    .withEmail("john.doe@example.com")
                    .withDateOfEmployment(LocalDate.parse("2019-01-15"))
                    .withSalaryPerMonth(new BigDecimal("5000.00"))
                    .build();
            String json = objectMapper.writeValueAsString(employeeModel);

            mockMvc.perform(post(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.url").value(url));
        }
    }

    @Nested
    class EmployeeControllerUpdateMethodsTest {

        @Test
        @SneakyThrows
        void updateEmployeeInDepartmentById_shouldReturnNoContentStatus() {
            String url = fromPath("/api/v0/companies/{companyId}/departments/{departmentId}/employees/{id}")
                    .buildAndExpand(VALID_COMPANY_ID, VALID_DEPARTMENT_ID, VALID_EMPLOYEE_ID)
                    .toUriString();
            EmployeeModel employeeModel = EmployeeModelTestBuilder.anEmployeeModel()
                    .withFirstname("Mike")
                    .withLastname("Jackson")
                    .withPosition("Software Engineer")
                    .withAge(21)
                    .withEmail("mike.jackson@example.com")
                    .withDateOfEmployment(LocalDate.parse("2023-07-01"))
                    .withSalaryPerMonth(new BigDecimal("500.00"))
                    .build();
            String json = objectMapper.writeValueAsString(employeeModel);

            mockMvc.perform(put(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isNoContent());
        }

        @Test
        @SneakyThrows
        void updateEmployeeInDepartmentById_shouldReturnNotFoundStatusAndErrorResponse_whenCompanyIsNotPresent() {
            String url = fromPath("/api/v0/companies/{companyId}/departments/{departmentId}/employees/{id}")
                    .buildAndExpand(INVALID_COMPANY_ID, VALID_DEPARTMENT_ID, VALID_EMPLOYEE_ID)
                    .toUriString();
            EmployeeModel employeeModel = EmployeeModelTestBuilder.anEmployeeModel()
                    .withFirstname("Mike")
                    .withLastname("Jackson")
                    .withPosition("Software Engineer")
                    .withAge(21)
                    .withEmail("mike.jackson@example.com")
                    .withDateOfEmployment(LocalDate.parse("2023-07-01"))
                    .withSalaryPerMonth(new BigDecimal("500.00"))
                    .build();
            String json = objectMapper.writeValueAsString(employeeModel);

            mockMvc.perform(put(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.url").value(url));;
        }

        @Test
        @SneakyThrows
        void updateEmployeeInDepartmentById_shouldReturnBadRequestStatus_whenEmailIsNotUnique() {
            String url = fromPath("/api/v0/companies/{companyId}/departments/{departmentId}/employees/{id}")
                    .buildAndExpand(VALID_COMPANY_ID, VALID_DEPARTMENT_ID, VALID_EMPLOYEE_ID)
                    .toUriString();
            EmployeeModel employeeModel = EmployeeModelTestBuilder.anEmployeeModel()
                    .withFirstname("Alexander")
                    .withLastname("Doe")
                    .withPosition("UI/UX")
                    .withAge(30)
                    .withEmail("john.doe@example.com")
                    .withDateOfEmployment(LocalDate.parse("2019-01-15"))
                    .withSalaryPerMonth(new BigDecimal("5000.00"))
                    .build();
            String json = objectMapper.writeValueAsString(employeeModel);

            mockMvc.perform(put(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.url").value(url));;
        }

        @Test
        @SneakyThrows
        void updateEmployeeInDepartmentPartiallyById_shouldReturnNoContentStatus() {
            String url = fromPath("/api/v0/companies/{companyId}/departments/{departmentId}/employees/{id}")
                    .buildAndExpand(VALID_COMPANY_ID, VALID_DEPARTMENT_ID, VALID_EMPLOYEE_ID)
                    .toUriString();
            EmployeeModel employeeModel = EmployeeModelTestBuilder.anEmployeeModel()
                    .withFirstname("Mike")
                    .build();
            String json = objectMapper.writeValueAsString(employeeModel);

            mockMvc.perform(patch(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isNoContent());
        }

        @Test
        @SneakyThrows
        void updateEmployeeInDepartmentPartiallyById_shouldReturnNotFoundStatusAndErrorResponse_whenCompanyIsNotPresent() {
            String url = fromPath("/api/v0/companies/{companyId}/departments/{departmentId}/employees/{id}")
                    .buildAndExpand(INVALID_COMPANY_ID, VALID_DEPARTMENT_ID, VALID_EMPLOYEE_ID)
                    .toUriString();
            EmployeeModel employeeModel = EmployeeModelTestBuilder.anEmployeeModel()
                    .withFirstname("Mike")
                    .build();
            String json = objectMapper.writeValueAsString(employeeModel);

            mockMvc.perform(patch(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.url").value(url));
        }

        @Test
        @SneakyThrows
        void updateEmployeeInDepartmentPartiallyById_shouldReturnBadRequestStatus_whenEmailIsNotUnique() {
            String url = fromPath("/api/v0/companies/{companyId}/departments/{departmentId}/employees/{id}")
                    .buildAndExpand(VALID_COMPANY_ID, VALID_DEPARTMENT_ID, VALID_EMPLOYEE_ID)
                    .toUriString();
            EmployeeModel employeeModel = EmployeeModelTestBuilder.anEmployeeModel()
                    .withFirstname("Alexander")
                    .withEmail("john.doe@example.com")
                    .build();
            String json = objectMapper.writeValueAsString(employeeModel);

            mockMvc.perform(patch(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.url").value(url));
        }

    }

    @Nested
    class EmployeeControllerDeleteMethodsTest {

        @Test
        @SneakyThrows
        void deleteEmployeeById_shouldReturnNoContentStatus() {
            String url = fromPath("/api/v0/companies/{companyId}/departments/{departmentId}/employees/{id}")
                    .buildAndExpand(VALID_COMPANY_ID, VALID_DEPARTMENT_ID, VALID_EMPLOYEE_ID)
                    .toUriString();

            mockMvc.perform(delete(url))
                    .andExpect(status().isNoContent());
        }

        @Test
        @SneakyThrows
        void deleteEmployee_shouldReturnNotFoundStatus_whenEmployeeIsNotPresent() {
            String url = fromPath("/api/v0/companies/{companyId}/departments/{departmentId}/employees/{id}")
                    .buildAndExpand(VALID_COMPANY_ID, VALID_DEPARTMENT_ID, INVALID_EMPLOYEE_ID)
                    .toUriString();

            mockMvc.perform(delete(url))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.url").value(url));
        }

    }
}
