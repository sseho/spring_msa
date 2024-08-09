package com.beyond.order_system.product.controller;

import com.beyond.order_system.common.dto.CommonResDto;
import com.beyond.order_system.product.domain.Product;
import com.beyond.order_system.product.dto.ProductResDto;
import com.beyond.order_system.product.dto.ProductSaveReqDto;
import com.beyond.order_system.product.dto.ProductSearchDto;
import com.beyond.order_system.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
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

}
