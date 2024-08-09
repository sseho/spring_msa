package com.beyond.order_system.ordering.service;

import com.beyond.order_system.common.service.StockInventoryService;
import com.beyond.order_system.member.domain.Member;
import com.beyond.order_system.member.repository.MemberRepository;
import com.beyond.order_system.ordering.controller.SseController;
import com.beyond.order_system.ordering.domain.OrderDetail;
import com.beyond.order_system.ordering.domain.OrderStatus;
import com.beyond.order_system.ordering.domain.Ordering;
import com.beyond.order_system.ordering.dto.OrderListResDto;
import com.beyond.order_system.ordering.dto.OrderPair;
import com.beyond.order_system.ordering.dto.OrderSaveRequestDto;
import com.beyond.order_system.ordering.dto.StockDecreaseEvent;
import com.beyond.order_system.ordering.repository.OrderRepository;
import com.beyond.order_system.product.domain.Product;
import com.beyond.order_system.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final StockInventoryService stockInventoryService;
    private final StockDecreaseEventHandler stockDecreaseEventHandler;

    private final SseController sseController;
    @Autowired
    public OrderService(ProductRepository productRepository, MemberRepository memberRepository, OrderRepository orderRepository, StockInventoryService stockInventoryService, StockDecreaseEventHandler stockDecreaseEventHandler, SseController sseController) {
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
        this.orderRepository = orderRepository;
        this.stockInventoryService = stockInventoryService;
        this.stockDecreaseEventHandler = stockDecreaseEventHandler;
        this.sseController = sseController;
    }

//    syncronized를 설정한다 하더라도, 재고 감소가 db에 반영되는 시점은 트랜잭션이 커밋되고 종료되는 시점
    public Ordering orderCreate(List<OrderSaveRequestDto> dtos) {
//        Member member = memberRepository.findById(dto.getMemberId()).orElseThrow(()->new EntityNotFoundException("Member not found"));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        Member member = memberRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("Member not found")) ;
        Ordering ordering = Ordering.builder()
                .member(member)
                .orderDetail(new ArrayList<>()).build();
        for(OrderSaveRequestDto dto: dtos){
            Product product = productRepository.findById(dto.getProductId()).get();
//            redis를 통한 재고관리 및 재고 잔량 확인
            if(product.getName().contains("sale")){
                int newQuantity = stockInventoryService.decreaseStock(dto.getProductId(),dto.getProductCount()).intValue();
                if(newQuantity < 0){
                    throw new IllegalArgumentException("재고 부족");
                }
//                rdb에 재고를 업데이트 rabbitmq를 통해 비동기적으로 이벤트 처리
                stockDecreaseEventHandler.publish(new StockDecreaseEvent(product.getId(),dto.getProductCount()));
            }else {
                if(product.getStockQuantity()< dto.getProductCount()){
                    throw new IllegalArgumentException("재고 부족");
                }
                product.updateStockQuantity(dto.getProductCount()); // 변경감지(dirty checking)로 인해 별도의 save불필쵸
            }
            ordering.getOrderDetail().add(dto.toEntity(ordering,product));
        }
        Ordering savedOrdering = orderRepository.save(ordering);
        sseController.publishMessage(savedOrdering.fromEntity(),"admin@test.com");
        return savedOrdering;
    }

    public List<OrderListResDto> orderList() {
        List<Ordering> orderingList = orderRepository.findAll();
        List<OrderListResDto> orderListResDtos = new ArrayList<>();
        for(Ordering ordering: orderingList){
            orderListResDtos.add(ordering.fromEntity());
        }
        return orderListResDtos;
    }

    public List<OrderListResDto> myOrderList() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("Member not found"));
        List<Ordering> orderingList = orderRepository.findByMember(member);
        List<OrderListResDto> orderListResDtos = new ArrayList<>();
        for(Ordering ordering: orderingList){
            orderListResDtos.add(ordering.fromEntity());
        }
        return orderListResDtos;
    }

    public Ordering cancelMyOrder(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Ordering ordering = orderRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Ordering not found"));
        if(ordering.getMember().getEmail().equals(email)){
            ordering.updateOrderStatusToCancel();
        }else{
            throw new IllegalArgumentException("해당 주문이 없습니다");
        }

        return ordering;
    }

    public Ordering orderCancel(Long id) {
        Ordering ordering = orderRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Ordering not found"));
        ordering.updateOrderStatusToCancel();
        return ordering;
    }
}
