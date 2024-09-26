package com.shubham.lightbill.lightbill_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterDto {
    @NotBlank
    private String filter;

    @NotBlank
    private String type;

    @NotBlank
    private String value;

    @Builder.Default
    private Integer page = 0;

    @Builder.Default
    private Integer size = 10;
}
