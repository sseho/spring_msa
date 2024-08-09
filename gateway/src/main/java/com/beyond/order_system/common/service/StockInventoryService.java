package com.beyond.order_system.common.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class StockInventoryService {

    @Qualifier("4")
    private final RedisTemplate<String, Object> redisTemplate;

    public StockInventoryService(@Qualifier("4") RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

//    상품등록시 increaseStock 호출
    public Long increaseStock(Long itemId, int quantity){
//        redis가 음수까지 내려갈경우 추후 재고 update상황에서 increase값이 정확하지 않을 수 있으므로,
//        음수이면 0으로 세팅

//        아래 메서드의 리턴 값은 증가하고 감소한 후의 잔량값을 리턴
        return redisTemplate.opsForValue().increment(String.valueOf(itemId),quantity);
    }

//    주문등록시 decreaseStock 호출
    public Long decreaseStock(Long itemId, int quantity){
        Object remains = redisTemplate.opsForValue().get(String.valueOf(itemId));
        int longRemains = Integer.parseInt(remains.toString());
        if(longRemains<quantity){
            return -1L;
        }else {
//            남아있는 잔량을 리턴
            return redisTemplate.opsForValue().decrement(String.valueOf(itemId),quantity);
        }

    }
}
