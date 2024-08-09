package com.beyond.order_system.ordering.dto;

import lombok.*;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderSaveRequestDto {
    private Long productId;
    private int productCount;
}
