package com.beyond.order_system.ordering.controller;

import com.beyond.order_system.common.dto.CommonResDto;
import com.beyond.order_system.ordering.domain.Ordering;
import com.beyond.order_system.ordering.dto.OrderListResDto;
import com.beyond.order_system.ordering.dto.OrderSaveRequestDto;
import com.beyond.order_system.ordering.service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {
//    1.구성
//-Ordering구성  : id, member(ManyToOne), orderStatus(ORDERED, CANCLED), orderdetail(OneToMany)
//-OrderDetail구성 : id, quantity(주문수량), ordering관계, product(ManyToOne)
//
//            2.API
//    -주문등록(/order/create) : dto-memberId, [productId, productCount]
//    -주문목록조회 : id, memberEmail, orderStatus, [id(주문상세), productId, productCount]]

    private final OrderService orderService;
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order/create")
    public String createOrder(@RequestBody List<OrderSaveRequestDto> dto) {
        Ordering ordering = orderService.orderFeignClientCreate(dto);
        return "ok";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("order/list")
    public ResponseEntity<?> listOrders() {
        List<OrderListResDto> orderListResDto = orderService.orderList();
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,"order list is successfully retrieved",orderListResDto);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

//    내 주문만 볼 수 있는 myOrders : order/myorders
    @GetMapping("order/myorders")
    public ResponseEntity<?> myOrders() {
        List<OrderListResDto> orderListResDto = orderService.myOrderList();
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,"myorder list is successfully retrieved",orderListResDto);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

//    admin사용자가 주문취소 : /order/{id}/cancel -> orderstatus 만 변경
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/order/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
        Ordering ordering = orderService.orderCancel(id);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,"order is successfully canceled",ordering.getId());
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

//    @PatchMapping("/order/{id}/cancel/myorder")
//    public ResponseEntity<?> cancelMyOrder(@PathVariable Long id) {
//        Ordering ordering = orderService.cancelMyOrder(id);
//        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,"myOrder is successfully canceled",ordering.getId());
//        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
//    }
}
