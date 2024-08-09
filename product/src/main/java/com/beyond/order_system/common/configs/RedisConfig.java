package com.beyond.order_system.common.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig  {

    //    application.yml의 spring.redis.host의 정보를 소스코드의 변수로 가져오는것
    @Value("${spring.redis.host}")
    public String host;

    @Value("${spring.redis.port}")
    public int port;


    //    refreshtoken service
    @Bean
    @Qualifier("2")
//    RedisConnectionFactory는 Redis서버와의 연결을 설정하는 역할
//    LettuceConnectionFactory는 RedisConnectionFactory의 구현체로서 실질적인 역할 수행
    public RedisConnectionFactory redisConnectionFactory() {
//        return new LettuceConnectionFactory(host, port);
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
//        1번 db 사용
        configuration.setDatabase(1);
//        configuration.setPassword("1234");
        return new LettuceConnectionFactory(configuration);
    }

    //  redisTamplate은 redis와 상호작용할 때 redis key,value의 형식을 정의
    @Bean
    @Qualifier("2")
    public RedisTemplate<String, Object> redisTemplate(@Qualifier("2") RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }
//    redisTemplate.opsForValue().set(key,value)
//    redisTemplate.opsForValue().get(key)
//    redisTemplate.opsForValue().increment or decrement


    @Bean
    @Qualifier("4")
    public RedisConnectionFactory stockFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
//        2번 db 사용
        configuration.setDatabase(2);
        return new LettuceConnectionFactory(configuration);
    }

    //  redisTamplate은 redis와 상호작용할 때 redis key,value의 형식을 정의
    @Bean
    @Qualifier("4")
    public RedisTemplate<String, Object> stockRedisTemplate(@Qualifier("4") RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(factory); // 매개변수로 받은 RedisConnectionFactory 객체 값을 넣어줘야 경로가 지정한대로 설정된다
        return redisTemplate;
    }


    //    =============
    @Bean
    @Qualifier("5")
    public RedisConnectionFactory sseFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
//        2번 db 사용
        configuration.setDatabase(3);
        return new LettuceConnectionFactory(configuration);
    }

    //  redisTamplate은 redis와 상호작용할 때 redis key,value의 형식을 정의
    @Bean
    @Qualifier("5")
    public RedisTemplate<String, Object> sseRedisTemplate(@Qualifier("5") RedisConnectionFactory sseFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        객체안의 객체 직렬화 이슈로인해 아래와 같이 serializer 커스텀
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        serializer.setObjectMapper(objectMapper);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setConnectionFactory(sseFactory);
        return redisTemplate;
    }
//    리스너 객체 생성
    @Bean
    @Qualifier("5")
    public RedisMessageListenerContainer redisMessageListenerContainer(@Qualifier("5") RedisConnectionFactory sseFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(sseFactory);
        return container;
    }

//    redis에 메시지 발행되면 listen하게 되고 , 아래 코드를 통해 특정 메서드를 실행하도록 설정
//    @Bean
//    public MessageListenerAdapter listenerAdapter(SseController sseController){
//        System.out.println("MessageListenerAdapter");
//        return new MessageListenerAdapter(sseController, "onMessage");
//    }


}
