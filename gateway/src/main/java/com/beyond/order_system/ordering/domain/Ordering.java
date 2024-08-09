package com.beyond.order_system.ordering.domain;

import com.beyond.order_system.member.domain.Member;
import com.beyond.order_system.ordering.dto.OrderDetailRes;
import com.beyond.order_system.ordering.dto.OrderListResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ordering {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.ORDERED;
//    cascade = CascadeType.PERSIST(영속성) 기능을 통해 Ordering을 저장할 때 orderDetail도 같이 저장해 줄 수 있다
    @OneToMany(mappedBy = "ordering", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetail;

//    빌더패턴에서도 ArrayList로 초기화 되도록하는 설정
//    @Builder.Default
//    private List<OrderDetail> orderDetail = new ArrayList<>();

    public OrderListResDto fromEntity() {
        List<OrderDetailRes> orderDetailResList = new ArrayList<>();
        for (OrderDetail orderDetail : this.orderDetail) {
            orderDetailResList.add(orderDetail.fromEntity());
        }
        return OrderListResDto.builder()
                .orderingId(this.id)
                .orderStatus(this.orderStatus)
                .memberEmail(this.member.getEmail())
                .orderDetails(orderDetailResList).build();
    }
    public void updateOrderStatusToCancel() {
        this.orderStatus = OrderStatus.CANCELD;
    }
}
