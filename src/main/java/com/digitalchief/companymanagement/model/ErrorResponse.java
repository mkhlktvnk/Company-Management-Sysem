package com.digitalchief.companymanagement.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Error Response")
public class ErrorResponse {

    @Schema(description = "Error message")
    private String message;

    @Schema(description = "URL where the error occurred")
    private String url;
}

