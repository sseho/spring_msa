package com.beyond.order_system.ordering.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailRes {
    private Long detailId;
    private Long ProductId;
    private String ProductName;
    private Integer productCount;
}
