package com.digitalchief.companymanagement.integration.controller;

import com.digitalchief.companymanagement.builder.impl.DepartmentModelTestBuilder;
import com.digitalchief.companymanagement.integration.BaseIntegrationTest;
import com.digitalchief.companymanagement.model.DepartmentModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

@AutoConfigureMockMvc
class DepartmentControllerTest extends BaseIntegrationTest {

    private static final Long VALID_COMPANY_ID = 1L;
    private static final Long VALID_DEPARTMENT_ID = 1L;
    private static final Long INVALID_COMPANY_ID = 1000L;
    private static final Long INVALID_DEPARTMENT_ID = 2000L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class DepartmentControllerReadMethodsTest {

        @Test
        @SneakyThrows
        void getDepartmentsByCompanyIdWithPagination_shouldReturnOkStatus() {
            int page = 0;
            int size = 10;
            int expectedLength = 2;
            String url = fromPath("/api/v0/companies/{companyId}/departments")
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .buildAndExpand(VALID_COMPANY_ID)
                    .toUriString();

            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.length()").value(expectedLength));
        }

        @Test
        @SneakyThrows
        void getDepartmentByCompanyAndDepartmentId_shouldReturnOkStatusAndExpectedDepartment() {
            DepartmentModel departmentModel = DepartmentModelTestBuilder.aDepartmentModel()
                    .withId(VALID_DEPARTMENT_ID)
                    .withName("Department A")
                    .withDescription("Sample description for Department A")
                    .withAnnualBudget(BigDecimal.valueOf(100000.00))
                    .build();
            String expectedJson = objectMapper.writeValueAsString(departmentModel);

            mockMvc.perform(get("/api/v0/companies/{companyId}/departments/{departmentId}",
                            VALID_COMPANY_ID, VALID_DEPARTMENT_ID))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(expectedJson));
        }

        @Test
        @SneakyThrows
        void getDepartmentByCompanyAndDepartmentId_shouldReturnNotFoundStatusAndErrorResponse() {
            String url = fromPath("/api/v0/companies/{companyId}/departments/{departmentId}")
                    .buildAndExpand(INVALID_COMPANY_ID, INVALID_DEPARTMENT_ID)
                    .toUriString();

            mockMvc.perform(get(url))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.url").value(url));
        }
    }

    @Nested
    class DepartmentControllerCreateMethodsTest {

        @Test
        @SneakyThrows
        void createDepartmentInCompany_shouldReturnCreatedStatusAndDepartmentWithNotNullId() {
            DepartmentModel departmentModel = DepartmentModelTestBuilder.aDepartmentModel()
                    .withName("New department name")
                    .withDescription("New department description")
                    .withAnnualBudget(BigDecimal.valueOf(30000000.00))
                    .build();
            String json = objectMapper.writeValueAsString(departmentModel);

            mockMvc.perform(post("/api/v0/companies/{companyId}/departments", VALID_COMPANY_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").isNotEmpty());
        }
    }

    @Nested
    class DepartmentControllerUpdateMethodsTest {

        @Test
        @SneakyThrows
        void updateDepartmentByCompanyAndDepartmentId_shouldReturnNoContentStatus() {
            DepartmentModel departmentModel = DepartmentModelTestBuilder.aDepartmentModel()
                    .withName("New department name")
                    .withDescription("New department description")
                    .withAnnualBudget(BigDecimal.valueOf(30000000.00))
                    .build();
            String json = objectMapper.writeValueAsString(departmentModel);

            mockMvc.perform(put("/api/v0/companies/{companyId}/departments/{departmentId}",
                            VALID_COMPANY_ID, VALID_DEPARTMENT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isNoContent());
        }

        @Test
        @SneakyThrows
        void updateDepartmentByCompanyAndDepartmentId_shouldReturnNotFoundStatusAndErrorResponse() {
            DepartmentModel departmentModel = DepartmentModelTestBuilder.aDepartmentModel()
                    .withName("New department name")
                    .withDescription("New department description")
                    .withAnnualBudget(BigDecimal.valueOf(30000000.00))
                    .build();
            String json = objectMapper.writeValueAsString(departmentModel);
            String url = fromPath("/api/v0/companies/{companyId}/departments/{departmentId}")
                    .buildAndExpand(INVALID_COMPANY_ID, INVALID_DEPARTMENT_ID)
                    .toUriString();

            mockMvc.perform(put(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.url").value(url));
        }

        @Test
        @SneakyThrows
        void updateDepartmentPartiallyByCompanyAndDepartmentId_shouldReturnNoContentStatus() {
            DepartmentModel departmentModel = DepartmentModelTestBuilder.aDepartmentModel()
                    .withName("New department name")
                    .withDescription("New department description")
                    .withAnnualBudget(BigDecimal.valueOf(30000000.00))
                    .build();
            String json = objectMapper.writeValueAsString(departmentModel);

            mockMvc.perform(patch("/api/v0/companies/{companyId}/departments/{departmentId}",
                            VALID_COMPANY_ID, VALID_DEPARTMENT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isNoContent());
        }

        @Test
        @SneakyThrows
        void updateDepartmentPartiallyByCompanyAndDepartmentId_shouldReturnNotFoundStatusAndErrorResponse() {
            DepartmentModel departmentModel = DepartmentModelTestBuilder.aDepartmentModel()
                    .withName("New department name")
                    .withDescription("New department description")
                    .withAnnualBudget(BigDecimal.valueOf(30000000.00))
                    .build();
            String json = objectMapper.writeValueAsString(departmentModel);
            String url = fromPath("/api/v0/companies/{companyId}/departments/{departmentId}")
                    .buildAndExpand(INVALID_COMPANY_ID, INVALID_DEPARTMENT_ID)
                    .toUriString();

            mockMvc.perform(patch(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.url").value(url));
        }

    }

    @Nested
    class DepartmentControllerDeleteMethodsTest {

        @Test
        @SneakyThrows
        void deleteDepartmentByCompanyAndDepartmentId_shouldReturnNoContentStatus() {
            mockMvc.perform(delete("/api/v0/companies/{companyId}/departments/{departmentId}",
                            VALID_COMPANY_ID, VALID_DEPARTMENT_ID))
                    .andExpect(status().isNoContent());
        }

        @Test
        @SneakyThrows
        void deleteDepartmentByCompanyAndDepartmentId_shouldReturnNotFoundStatusAndErrorResponse() {
            String url = fromPath("/api/v0/companies/{companyId}/departments/{departmentId}")
                    .buildAndExpand(INVALID_COMPANY_ID, INVALID_DEPARTMENT_ID)
                    .toUriString();

            mockMvc.perform(delete(url, INVALID_COMPANY_ID, INVALID_DEPARTMENT_ID))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.url").value(url));
        }
    }
}


