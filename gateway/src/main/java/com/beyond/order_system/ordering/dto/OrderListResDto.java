package com.beyond.order_system.ordering.dto;

import com.beyond.order_system.ordering.domain.OrderDetail;
import com.beyond.order_system.ordering.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderListResDto {
    private Long orderingId;
    private String memberEmail;
    private OrderStatus orderStatus;
//    orderDetailDtos
    private List<OrderDetailRes> orderDetails;
}
