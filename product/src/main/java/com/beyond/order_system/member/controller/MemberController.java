package com.beyond.order_system.member.controller;

import com.beyond.order_system.common.auth.JwtTokenProvider;
import com.beyond.order_system.common.dto.CommonErrorDto;
import com.beyond.order_system.common.dto.CommonResDto;
import com.beyond.order_system.member.domain.Member;
import com.beyond.order_system.member.dto.*;
import com.beyond.order_system.member.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class MemberController {
    @Value("${jwt.secretKeyRt}")
    private String secretKeyRt;

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    @Qualifier("2")
    private final RedisTemplate<String, Object> redisTemplate;
    @Autowired
    public MemberController(MemberService memberService, JwtTokenProvider jwtTokenProvider, @Qualifier("2") RedisTemplate<String, Object> redisTemplate) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/member/create")
    public ResponseEntity<?> memberCreate(@Valid @RequestBody MemberSaveReqDto dto) {
//        name,email,password,address,rele(user),password
        Member member = memberService.memberCreate(dto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED,"member is successfully created",member.getId());
        return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
    }

//    admin만 회원목록 전체조회 가능
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/member/list")
    public ResponseEntity<?> memberList(Pageable pageable) {
//        id,name,email,address
        Page<MemberListRes> memberListRes = memberService.memberList(pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,"member list is successfully retrieved",memberListRes);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }
//    본인은 본인회원정보만 조회가능
//    /member/myinfo. MemberResDto
    @GetMapping("/member/myinfo")
    public ResponseEntity<?> memberMyinfo() {
        MemberListRes dto = memberService.myinfo();
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,"member is found",dto);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }


    @PostMapping("/doLogin")
    public ResponseEntity<?> doLogin(@RequestBody MemberLoginDto dto){
//        eamil, password가 일치하는지 검증
        Member member = memberService.login(dto);

//        일치할 경우 accessToken 생성
        String jwtToken = jwtTokenProvider.createToken(member.getEmail(),member.getRole().toString());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail(),member.getRole().toString());

//        redis에 email과 rt를 key:value로 하여 저장
        redisTemplate.opsForValue().set(member.getEmail(),refreshToken,240, TimeUnit.HOURS); // 240시간
//        생성된 토큰을 CommonResDto에 담아 사용자에게 return
        Map<String,Object> loginInfo = new HashMap<>();
        loginInfo.put("id",member.getId());
        loginInfo.put("token",jwtToken);
        loginInfo.put("refreshToken",refreshToken);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,"member login is successful",loginInfo);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }

    @PostMapping("/member/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPWReqDto dto){
        Member member = memberService.resetPW(dto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,"pw is successful change",member.getId());
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> gererateNewAccessToken(@RequestBody MemberRefreshDto dto){
        String rt = dto.getRefreshToken();
        Claims claims=null;
        try{
//            코드를 통해 rt검증
            claims = Jwts.parser().setSigningKey(secretKeyRt).parseClaimsJws(rt).getBody();
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new CommonErrorDto(HttpStatus.BAD_REQUEST.value(),"invalid refresh token"),HttpStatus.BAD_REQUEST);
        }
        String email = claims.getSubject();
        String role = claims.get("role").toString();

//        redis를 조회하여 rt 추가 검증
        Object obj = redisTemplate.opsForValue().get(email);
        if(obj == null || !obj.toString().equals(rt)){
            return new ResponseEntity<>(new CommonErrorDto(HttpStatus.BAD_REQUEST.value(),"invalid refresh token"),HttpStatus.BAD_REQUEST);
        }
//        String storedRedisRt = redisTemplate.opsForValue().get(email).toString();
//        if(storedRedisRt==null || !storedRedisRt.equals(rt)){
//            return new ResponseEntity<>(new CommonErrorDto(HttpStatus.BAD_REQUEST.value(),"invalid refresh token"),HttpStatus.BAD_REQUEST);
//        }
//        System.out.println(storedRedisRt.equals(rt));

        String newAT = jwtTokenProvider.createToken(email,role);

        Map<String,Object> info = new HashMap<>();
        info.put("token",newAT);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,"at is renewed",info);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }
}
