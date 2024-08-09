package com.beyond.order_system.member.service;

import com.beyond.order_system.member.domain.Member;
import com.beyond.order_system.member.dto.MemberLoginDto;
import com.beyond.order_system.member.dto.MemberSaveReqDto;
import com.beyond.order_system.member.dto.MemberListRes;
import com.beyond.order_system.member.dto.ResetPWReqDto;
import com.beyond.order_system.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    @Autowired
    public MemberService(PasswordEncoder passwordEncoder, MemberRepository memberRepository) {
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
    }

    public Member memberCreate(MemberSaveReqDto dto) {
        if(memberRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다");
        }
        Member member = dto.toEntity(passwordEncoder.encode(dto.getPassword()));
        memberRepository.save(member);
        return member;
    }

    public Page<MemberListRes> memberList(Pageable pageable) {
        Page<Member> members = memberRepository.findAll(pageable);
        Page<MemberListRes> memberListRes = members.map(a->a.fromEntity());
        return memberListRes;


//        List<MemberListRes> memberListRes = new ArrayList<>();
//        for (Member member : members) {
//            memberListRes.add(MemberListRes.listFromEntity(member));
//        }
//        return memberListRes;
    }
    public MemberListRes myinfo() {
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new EntityNotFoundException("member is not found"));
        return member.fromEntity();
    }
    public Member login(MemberLoginDto dto){
//        email의 존재여부
        Member member = memberRepository.findByEmail(dto.getEmail()).orElseThrow(()->new EntityNotFoundException("존재하지 않는 email입니다"));
//        password 일치여부
//        if(!member.getPassword().equals(passwordEncoder.encode(dto.getPassword()))){
        if(!passwordEncoder.matches(dto.getPassword(), member.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return member;
    }

    public Member resetPW(ResetPWReqDto dto) {
        System.out.println(dto);
        Member member = memberRepository.findByEmail(dto.getEmail()).orElseThrow(()->new EntityNotFoundException("member is not found"));
        if(!passwordEncoder.matches(dto.getPw(),member.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다2");
        }
        member.updatePW(passwordEncoder.encode(dto.getNewPw()));
        return member;
    }
}
