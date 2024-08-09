package com.beyond.order_system.ordering.service;

import com.beyond.order_system.common.configs.RabbitMqConfig;
import com.beyond.order_system.ordering.dto.StockDecreaseEvent;
import com.beyond.order_system.product.domain.Product;
import com.beyond.order_system.product.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Component
public class StockDecreaseEventHandler {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ProductRepository productRepository;

    public void publish(StockDecreaseEvent event){
        rabbitTemplate.convertAndSend(RabbitMqConfig.STOCK_DECREASE_QUEUE,event);
    }

//    트랜잭션이 완료된 이후에 그다음 메시지 수신하므로, 동시성 이슈 발생x
    @Transactional // Component 가 붙어있으면 Transactional 처리 가능
    @RabbitListener(queues = RabbitMqConfig.STOCK_DECREASE_QUEUE)
    public void listen(Message message) {
        String messageBody = new String(message.getBody());
//        json메시지를 ObjectMapper로 직접 parsing
        ObjectMapper objectMapper = new ObjectMapper();
        StockDecreaseEvent stockDecreaseEvent = null;
        try {
            stockDecreaseEvent = objectMapper.readValue(messageBody, StockDecreaseEvent.class);
            //        재고 update
            Product product = productRepository.findById(stockDecreaseEvent.getProductId()).orElseThrow(()-> new EntityNotFoundException("Product not found"));
            if(product.getStockQuantity()>=stockDecreaseEvent.getProductCount()){
                product.updateStockQuantity(stockDecreaseEvent.getProductCount());
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
