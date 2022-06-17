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
public class OrderDto extends RepresentationModel<OrderDto> {

    private Integer orderId;

    @Min(value = 0)
    private double cost;
    private Instant dateOfBuy;
    private Integer userId;
    private Integer certificateId;
}