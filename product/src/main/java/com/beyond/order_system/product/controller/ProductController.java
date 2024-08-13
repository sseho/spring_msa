package com.beyond.order_system.product.controller;

import com.beyond.order_system.common.dto.CommonResDto;
import com.beyond.order_system.product.domain.Product;
import com.beyond.order_system.product.dto.ProductResDto;
import com.beyond.order_system.product.dto.ProductSaveReqDto;
import com.beyond.order_system.product.dto.ProductSearchDto;
import com.beyond.order_system.product.dto.ProductUpdateStockDto;
import com.beyond.order_system.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// 해당어노테이션 사용시 아래 스프링빈은 실시간  config변경사항의 대상이 됨
@RefreshScope
@RestController
public class ProductController {

    @Value("${message.hello}")
    private String helloWorlds;

    private final ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("product/config/test")
    public String configTest(){
        return helloWorlds;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("product/create")
    public ResponseEntity<?> createProduct(ProductSaveReqDto dto) {
        Product product = productService.productAwsCreate(dto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED,"product is successfully created",product.getId());
        return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
    }

    @GetMapping("product/list")
    public ResponseEntity<?> productList(ProductSearchDto searchDto, Pageable pageable) {
        System.out.println(searchDto);
        Page<ProductResDto> productResDtos = productService.productList(searchDto, pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,"정상 조회 완료",productResDtos);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @GetMapping("product/{id}")
    public ResponseEntity<?> productDetail(@PathVariable Long id) {
        ProductResDto productResDto = productService.productDetail(id);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,"정상 조회 완료",productResDto);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @PutMapping("product/update-stock")
    public ResponseEntity<?> productStockUpdate(@RequestBody ProductUpdateStockDto dto) {
        Product product = productService.productUpdateStock(dto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,"update is successful",product.getId());
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

}
