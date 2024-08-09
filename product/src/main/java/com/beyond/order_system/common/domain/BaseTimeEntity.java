package com.beyond.order_system.common.domain;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
// 기본적으로 Entity는 상속관계가 불가능하여, 해당 어노테이션을 붙여야 상속관계가 성립가능
@MappedSuperclass
public abstract class BaseTimeEntity {
    @CreationTimestamp // DB에는 current_timestamp가 생성되지 않음
    private LocalDateTime createdTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;
}
