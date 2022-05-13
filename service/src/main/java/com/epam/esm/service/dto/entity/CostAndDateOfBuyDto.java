package com.epam.esm.service.dto.entity;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Min;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CostAndDateOfBuyDto extends RepresentationModel<CostAndDateOfBuyDto> {

    @Min(value = 0)
    private Double cost;

    private Instant dateOfBuy;

}
