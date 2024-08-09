package com.beyond.order_system.ordering.dto;

import com.beyond.order_system.ordering.domain.OrderDetail;
import com.beyond.order_system.ordering.domain.Ordering;
import com.beyond.order_system.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPair {
    private Long productId;
    private int productCount;

    public OrderDetail toEntity(Ordering ordering, Product product) {
        return OrderDetail.builder()
                .quantity(this.productCount)
                .ordering(ordering)
                .product(product).build();
    }
}
