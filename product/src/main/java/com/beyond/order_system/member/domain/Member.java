package com.beyond.order_system.member.domain;

import com.beyond.order_system.common.domain.Address;
import com.beyond.order_system.common.domain.BaseTimeEntity;
import com.beyond.order_system.member.dto.MemberListRes;
import com.beyond.order_system.ordering.domain.Ordering;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Ordering> orderList;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER;

    public MemberListRes fromEntity(){
        return MemberListRes.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .address(this.address)
                .orderCount(this.orderList.size())
                .build();
    }

    public void updatePW(String encode) {
        this.password = encode;
    }
}
