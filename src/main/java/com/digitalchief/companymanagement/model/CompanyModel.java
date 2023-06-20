package com.digitalchief.companymanagement.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Company Model")
public class CompanyModel {

    @Schema(
            description = "ID of the company",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotEmpty
    @Schema(description = "Name of the company", example = "Acme Corporation")
    @JsonProperty(value = "name")
    private String name;

    @NotEmpty
    @Schema(description = "Description of the company", example = "A multinational conglomerate")
    @JsonProperty(value = "description")
    private String description;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(description = "Date of creation of the company", example = "2000-01-01")
    @JsonProperty(value = "dateOfCreation")
    private LocalDate dateOfCreation;
}

