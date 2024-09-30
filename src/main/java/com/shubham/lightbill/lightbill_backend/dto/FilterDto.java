package com.shubham.lightbill.lightbill_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterDto {
    @NotBlank(message = "blank filter field")
    private String filter;

    @NotBlank(message = "blank type field")
    private String type;

    @NotBlank(message = "blank value filed")
    private String value;

    @Builder.Default
    private Integer page = 0;

    @Builder.Default
    private Integer size = 10;
}
