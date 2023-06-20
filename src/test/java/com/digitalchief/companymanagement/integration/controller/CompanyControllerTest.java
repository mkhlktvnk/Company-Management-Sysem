package com.digitalchief.companymanagement.integration.controller;

import com.digitalchief.companymanagement.builder.impl.CompanyModelTestBuilder;
import com.digitalchief.companymanagement.integration.BaseIntegrationTest;
import com.digitalchief.companymanagement.model.CompanyModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

@AutoConfigureMockMvc
class CompanyControllerTest extends BaseIntegrationTest {

    private static final Long VALID_COMPANY_ID = 1L;

    private static final Long INVALID_COMPANY_ID = 1000L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class CompanyControllerReadMethodsTest {

        @Test
        @SneakyThrows
        void getCompaniesWithPagination_shouldReturnOkStatus() {
            int page = 0;
            int size = 10;
            String url = fromPath("/api/v0/companies")
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .toUriString();

            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.length()").value(size));
        }

        @Test
        @SneakyThrows
        void getCompanyById_shouldReturnOkStatusAndExpectedCompany() {
            CompanyModel companyModel = CompanyModelTestBuilder.aCompanyModel()
                    .withId(1L)
                    .withName("Company A")
                    .withDescription("Sample description for Company A")
                    .withDateOfCreation(LocalDate.parse("2020-01-01"))
                    .build();
            String expectedJson = objectMapper.writeValueAsString(companyModel);

            mockMvc.perform(get("/api/v0/companies/" + VALID_COMPANY_ID))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(expectedJson));

        }

        @Test
        @SneakyThrows
        void getCompanyById_shouldReturnBadRequestStatusAndErrorResponse() {
            String url = "/api/v0/companies/" + INVALID_COMPANY_ID;

            mockMvc.perform(get(url))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.url").value(url));
        }

    }

    @Nested
    class CompanyControllerCreateMethodsTest {

        @Test
        @SneakyThrows
        void createCompany_shouldReturnCreatedStatusAndCompanyWithNotNullId() {
            CompanyModel companyModel = CompanyModelTestBuilder.aCompanyModel()
                    .withName("New company name")
                    .withDescription("New company description")
                    .withDateOfCreation(LocalDate.parse("2020-01-01"))
                    .build();
            String json = objectMapper.writeValueAsString(companyModel);

            mockMvc.perform(post("/api/v0/companies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").isNotEmpty());
        }

        @Test
        @SneakyThrows
        void createCompany_shouldReturnBadRequestStatusAndErrorResponse_whenNameIsNotUnique() {
            String url = "/api/v0/companies";
            CompanyModel companyModel = CompanyModelTestBuilder.aCompanyModel()
                    .withName("Company J")
                    .withDescription("New company description")
                    .withDateOfCreation(LocalDate.parse("2020-01-01"))
                    .build();
            String json = objectMapper.writeValueAsString(companyModel);

            mockMvc.perform(post(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.url").value(url));
        }

    }

    @Nested
    class CompanyControllerUpdateMethodsTest {

        @Test
        @SneakyThrows
        void updateCompany_shouldReturnNoContentStatus() {
            CompanyModel companyModel = CompanyModelTestBuilder.aCompanyModel()
                    .withName("New company name")
                    .withDescription("New company description")
                    .withDateOfCreation(LocalDate.parse("2020-01-01"))
                    .build();
            String json = objectMapper.writeValueAsString(companyModel);

            mockMvc.perform(put("/api/v0/companies/" + VALID_COMPANY_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isNoContent());
        }

        @Test
        @SneakyThrows
        void updateCompany_shouldReturnBadRequestStatusAndErrorResponse_whenNameIsNotUnique() {
            String url = "/api/v0/companies/" + VALID_COMPANY_ID;
            CompanyModel companyModel = CompanyModelTestBuilder.aCompanyModel()
                    .withName("Company J")
                    .withDescription("New company description")
                    .withDateOfCreation(LocalDate.parse("2020-01-01"))
                    .build();
            String json = objectMapper.writeValueAsString(companyModel);

            mockMvc.perform(put(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.url").value(url));
        }

        @Test
        @SneakyThrows
        void updateCompany_shouldReturnNotFoundStatusAndErrorResponse_whenCompanyIsNotPresent() {
            String url = "/api/v0/companies/" + INVALID_COMPANY_ID;
            CompanyModel companyModel = CompanyModelTestBuilder.aCompanyModel()
                    .withName("New company")
                    .withDescription("New company description")
                    .withDateOfCreation(LocalDate.parse("2020-01-01"))
                    .build();
            String json = objectMapper.writeValueAsString(companyModel);

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
        void updateCompanyPartiallyById_shouldReturnNoContentStatus() {
            CompanyModel companyModel = CompanyModelTestBuilder.aCompanyModel()
                    .withName("New company name")
                    .build();
            String json = objectMapper.writeValueAsString(companyModel);

            mockMvc.perform(patch("/api/v0/companies/" + VALID_COMPANY_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isNoContent());
        }

        @Test
        @SneakyThrows
        void updateCompanyPartiallyById_shouldReturnBadRequestStatusAndErrorResponse_whenNameIsNotUnique() {
            String url = "/api/v0/companies/" + VALID_COMPANY_ID;
            CompanyModel companyModel = CompanyModelTestBuilder.aCompanyModel()
                    .withName("Company J")
                    .build();
            String json = objectMapper.writeValueAsString(companyModel);

            mockMvc.perform(patch(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.url").value(url));
        }

        @Test
        @SneakyThrows
        void updateCompanyPartiallyById_shouldReturnNotFoundStatusAndErrorResponse_whenCompanyIsNotPresent() {
            String url = "/api/v0/companies/" + INVALID_COMPANY_ID;
            CompanyModel companyModel = CompanyModelTestBuilder.aCompanyModel()
                    .withName("New company")
                    .build();
            String json = objectMapper.writeValueAsString(companyModel);

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
    class CompanyControllerDeleteMethodsTest {

        @Test
        @SneakyThrows
        void deleteCompanyById_shouldReturnNoContentStatus() {
            mockMvc.perform(delete("/api/v0/companies/" + VALID_COMPANY_ID))
                    .andExpect(status().isNoContent());
        }

        @Test
        @SneakyThrows
        void deleteCompanyById_shouldReturnNotFoundStatusAndErrorResponse_whenCompanyIsNotPresent() {
            String url = "/api/v0/companies/" +  INVALID_COMPANY_ID;

            mockMvc.perform(delete(url))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.url").value(url));
        }
    }

}