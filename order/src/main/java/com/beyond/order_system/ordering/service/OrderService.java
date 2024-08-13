package com.beyond.order_system.ordering.service;

import com.beyond.order_system.common.dto.CommonResDto;
import com.beyond.order_system.common.service.StockInventoryService;
import com.beyond.order_system.ordering.controller.SseController;
import com.beyond.order_system.ordering.domain.OrderDetail;
import com.beyond.order_system.ordering.domain.OrderStatus;
import com.beyond.order_system.ordering.domain.Ordering;
import com.beyond.order_system.ordering.dto.*;
import com.beyond.order_system.ordering.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
//import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final StockInventoryService stockInventoryService;
//    private final StockDecreaseEventHandler stockDecreaseEventHandler;
    private final RestTemplate restTemplate;
    private final SseController sseController;
    private final ProductFeign productFeign;
//    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public OrderService(OrderRepository orderRepository, StockInventoryService stockInventoryService, RestTemplate restTemplate, SseController sseController, ProductFeign productFeign) {

        this.orderRepository = orderRepository;
        this.stockInventoryService = stockInventoryService;
//        this.stockDecreaseEventHandler = stockDecreaseEventHandler;
        this.restTemplate = restTemplate;
        this.sseController = sseController;
        this.productFeign = productFeign;
//        this.kafkaTemplate = kafkaTemplate;
    }

//    syncronized를 설정한다 하더라도, 재고 감소가 db에 반영되는 시점은 트랜잭션이 커밋되고 종료되는 시점
    public Ordering orderRestTemplateCreate(List<OrderSaveRequestDto> dtos) {
//        Member member = memberRepository.findById(dto.getMemberId()).orElseThrow(()->new EntityNotFoundException("Member not found"));
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
//        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();

        Ordering ordering = Ordering.builder()
                .memberEmail(memberEmail)
                .orderDetail(new ArrayList<>()).build();
        for(OrderSaveRequestDto dto: dtos){
//            product API에 요청을 통해 product객체를 조회해야함
            String productGetUrl = "http://product-service/product/"+dto.getProductId();
            HttpHeaders httpHeaders = new HttpHeaders();
            String token = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
            httpHeaders.set("Authorization", token);
            HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
            ResponseEntity<CommonResDto> productEntity = restTemplate.exchange(productGetUrl, HttpMethod.GET, entity, CommonResDto.class);
            ObjectMapper objectMapper = new ObjectMapper();
            ProductDto productDto = objectMapper.convertValue(productEntity.getBody().getResult(), ProductDto.class);
            System.out.println(productDto);
            if(productDto.getName().contains("sale")){
//            redis를 통한 재고관리 및 재고 잔량 확인
                int newQuantity = stockInventoryService.decreaseStock(dto.getProductId(),dto.getProductCount()).intValue();
                if(newQuantity < 0){
                    throw new IllegalArgumentException("재고 부족");
                }
//                rdb에 재고를 업데이트 rabbitmq를 통해 비동기적으로 이벤트 처리
//                stockDecreaseEventHandler.publish(new StockDecreaseEvent(productDto.getId(),dto.getProductCount()));
            }else {
                if(productDto.getStockQuantity()< dto.getProductCount()){
                    throw new IllegalArgumentException("재고 부족");
                }
////                restTemplate를 통한 update 요청
//                product.updateStockQuantity(dto.getProductCount());
                String updateUrl = "http://product-service/product/update-stock";
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<ProductUpdateStockDto> updateEntity = new HttpEntity<>(new ProductUpdateStockDto(dto.getProductId(),dto.getProductCount()), httpHeaders);
                restTemplate.exchange(updateUrl,HttpMethod.PUT, updateEntity,Void.class);
            }
            OrderDetail orderDetail = OrderDetail.builder() // 주문상세 OrderDetail 객체 조립
                    .productId(productDto.getId())
                    .ordering(ordering)
                    .quantity(dto.getProductCount())
                    .build();
            ordering.getOrderDetail().add(orderDetail);
//            ordering.getOrderDetail().add(dto.toEntity(ordering,productDto));
        }
        Ordering savedOrdering = orderRepository.save(ordering);
        sseController.publishMessage(savedOrdering.fromEntity(),"admin@test.com");
        return savedOrdering;
    }

    public Ordering orderFeignClientCreate(List<OrderSaveRequestDto> dtos) {
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Ordering ordering = Ordering.builder()
                .memberEmail(memberEmail)
                .orderDetail(new ArrayList<>()).build();
        for(OrderSaveRequestDto dto: dtos){
//            product API에 요청을 통해 product객체를 조회해야함

//            ResponseEntity가 기본응답 값이므로 바로 CommonResDto로 매핑
            CommonResDto commonResDto = productFeign.getProductById(dto.getProductId());
            ObjectMapper objectMapper = new ObjectMapper();
            ProductDto productDto = objectMapper.convertValue(commonResDto.getResult(), ProductDto.class);

            System.out.println(productDto);
            if(productDto.getName().contains("sale")){
//            redis를 통한 재고관리 및 재고 잔량 확인
                int newQuantity = stockInventoryService.decreaseStock(dto.getProductId(),dto.getProductCount()).intValue();
                if(newQuantity < 0){
                    throw new IllegalArgumentException("재고 부족");
                }
//                rdb에 재고를 업데이트 rabbitmq를 통해 비동기적으로 이벤트 처리
//                stockDecreaseEventHandler.publish(new StockDecreaseEvent(productDto.getId(),dto.getProductCount()));
            }else {
                if(productDto.getStockQuantity()< dto.getProductCount()){
                    throw new IllegalArgumentException("재고 부족");
                }
                productFeign.updateProductStock(new ProductUpdateStockDto(dto.getProductId(),dto.getProductCount()));
            }
            OrderDetail orderDetail = OrderDetail.builder() // 주문상세 OrderDetail 객체 조립
                    .productId(productDto.getId())
                    .ordering(ordering)
                    .quantity(dto.getProductCount())
                    .build();
            ordering.getOrderDetail().add(orderDetail);
//            ordering.getOrderDetail().add(dto.toEntity(ordering,productDto));
        }
        Ordering savedOrdering = orderRepository.save(ordering);
        sseController.publishMessage(savedOrdering.fromEntity(),"admin@test.com");
        return savedOrdering;
    }

//    public Ordering orderFeignKafkaCreate(List<OrderSaveRequestDto> dtos) {
//        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        Ordering ordering = Ordering.builder()
//                .memberEmail(memberEmail)
//                .orderDetail(new ArrayList<>()).build();
//        for(OrderSaveRequestDto dto: dtos){
////            product API에 요청을 통해 product객체를 조회해야함
//
////            ResponseEntity가 기본응답 값이므로 바로 CommonResDto로 매핑
//            CommonResDto commonResDto = productFeign.getProductById(dto.getProductId());
//            ObjectMapper objectMapper = new ObjectMapper();
//            ProductDto productDto = objectMapper.convertValue(commonResDto.getResult(), ProductDto.class);
//
//            System.out.println(productDto);
//            if(productDto.getName().contains("sale")){
////            redis를 통한 재고관리 및 재고 잔량 확인
//                int newQuantity = stockInventoryService.decreaseStock(dto.getProductId(),dto.getProductCount()).intValue();
//                if(newQuantity < 0){
//                    throw new IllegalArgumentException("재고 부족");
//                }
////                rdb에 재고를 업데이트 rabbitmq를 통해 비동기적으로 이벤트 처리
//                stockDecreaseEventHandler.publish(new StockDecreaseEvent(productDto.getId(),dto.getProductCount()));
//            }else {
//                if(productDto.getStockQuantity()< dto.getProductCount()){
//                    throw new IllegalArgumentException("재고 부족");
//                }
//                ProductUpdateStockDto productUpdateStockDto = new ProductUpdateStockDto(dto.getProductId(),dto.getProductCount());
//                kafkaTemplate.send("product-update-topic", productUpdateStockDto);
//            }
//            OrderDetail orderDetail = OrderDetail.builder() // 주문상세 OrderDetail 객체 조립
//                    .productId(productDto.getId())
//                    .ordering(ordering)
//                    .quantity(dto.getProductCount())
//                    .build();
//            ordering.getOrderDetail().add(orderDetail);
////            ordering.getOrderDetail().add(dto.toEntity(ordering,productDto));
//        }
//        Ordering savedOrdering = orderRepository.save(ordering);
//        sseController.publishMessage(savedOrdering.fromEntity(),"admin@test.com");
//        return savedOrdering;
//    }

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
        List<Ordering> orderingList = orderRepository.findByMemberEmail(email);
        List<OrderListResDto> orderListResDtos = new ArrayList<>();
        for(Ordering ordering: orderingList){
            orderListResDtos.add(ordering.fromEntity());
        }
        return orderListResDtos;
    }

//    public Ordering cancelMyOrder(Long id) {
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        Ordering ordering = orderRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Ordering not found"));
//        if(ordering.getMember().getEmail().equals(email)){
//            ordering.updateOrderStatusToCancel();
//        }else{
//            throw new IllegalArgumentException("해당 주문이 없습니다");
//        }
//
//        return ordering;
//    }

    public Ordering orderCancel(Long id) {
        Ordering ordering = orderRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Ordering not found"));
        ordering.updateOrderStatusToCancel();
        return ordering;
    }
}
