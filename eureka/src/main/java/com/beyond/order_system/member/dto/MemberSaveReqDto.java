package com.beyond.order_system.member.dto;

import com.beyond.order_system.common.domain.Address;
import com.beyond.order_system.member.domain.Member;
import com.beyond.order_system.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberSaveReqDto {
    private String name;
    @NotEmpty(message = "email is essential")
    private String email;
    @NotEmpty(message = "password is essential")
    @Size(min = 8, message = "password minimun length is 8")
    private String password;
    private Address address;
    private Role role = Role.USER;

//    private String city;
//    private String street;
//    private String zipcode;

    public Member toEntity(String password) {
        Member member = Member.builder()
                .name(this.name)
                .email(this.email)
                .password(password)
                .address(this.address)
                .role(this.role).build();
        return member;
    }

//    public Member toEntity() {
//        Member member = Member.builder()
//                .name(this.name)
//                .email(this.email)
//                .password(this.password)
//                .address(Address.builder()
//                        .city(this.city)
//                        .street(this.street)
//                        .zipcode(this.zipcode).build())
//                .role(Role.USER).build();
//        return member;
//    }
}
