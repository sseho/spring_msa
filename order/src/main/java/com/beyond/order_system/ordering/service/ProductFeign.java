package com.beyond.order_system.ordering.service;

import com.beyond.order_system.common.configs.FeignConfig;
import com.beyond.order_system.common.dto.CommonResDto;
import com.beyond.order_system.ordering.dto.ProductUpdateStockDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service", configuration = FeignConfig.class)
public interface ProductFeign {
    @GetMapping(value = "/product/{id}")
    CommonResDto getProductById(@PathVariable("id") Long id);

    @PutMapping(value = "/product/update-stock")
    void updateProductStock(@RequestBody ProductUpdateStockDto dto);

}
