package com.beyond.order_system.ordering.domain;

import com.beyond.order_system.ordering.dto.OrderDetailRes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordering_id")
    private Ordering ordering;

    private Long productId;

    public OrderDetailRes fromEntity() {
        return OrderDetailRes.builder()
                .detailId(this.id)
                .productCount(this.quantity)
                .ProductId(this.productId).build();
    }
}
