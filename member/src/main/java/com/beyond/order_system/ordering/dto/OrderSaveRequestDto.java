package com.beyond.order_system.ordering.dto;

import com.beyond.order_system.member.domain.Member;
import com.beyond.order_system.ordering.domain.OrderDetail;
import com.beyond.order_system.ordering.domain.OrderStatus;
import com.beyond.order_system.ordering.domain.Ordering;
import com.beyond.order_system.product.domain.Product;
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
//    dto-memberId, [productId, productCount]
//    private Long memberId;
//    private List<OrderPair> orderList;

//    static class OrderPair{
//        private Long productId;
//        private int productCount;
//    }

    public OrderDetail toEntity(Ordering ordering, Product product) {
        return OrderDetail.builder()
                .quantity(this.productCount)
                .ordering(ordering)
                .product(product).build();
    }
}
