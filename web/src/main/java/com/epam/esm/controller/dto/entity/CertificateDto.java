package com.epam.esm.controller.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CertificateDto extends RepresentationModel<CertificateDto> {

    private long id;

    @NotNull
    @Size(min=3, max = 50)
    private String name;

    @NotNull
    @Size(min=1, max = 250)
    private String description;

    @Min(value = 0)
    @Max(value = 1000000)
    private BigDecimal price;

    @Min(value = 1)
    @Max(value = 12)
    private int duration;

    private Instant createDate;

    private Instant lastUpdateDate;

    private List<TagDto> tagNames;
}

