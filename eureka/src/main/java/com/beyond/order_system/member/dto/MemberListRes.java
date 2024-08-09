package com.beyond.order_system.member.dto;

import com.beyond.order_system.common.domain.Address;
import com.beyond.order_system.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberListRes {
    private Long id;
    private String name;
    private String email;
    private Address address;
    private int orderCount;

    public static MemberListRes listFromEntity(Member member) {
        return MemberListRes.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .address(member.getAddress()).build();
    }
}
