package com.farmstory.dto;

import com.farmstory.entity.User;
import lombok.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDTO {

    private String uid;
    private String pass;
    private String nick;
    private String name;

    @Builder.Default
    private String role = "USER";

    private String email;
    private String hp;
    private String grade; // 사용자 등급 (권한 | 사용자,관리자)
    private String zip; // 우편번호
    private String addr1; // 주소 1 : 도로명 주소
    private String addr2; // 주소 2: 추가적인 주소 정보
    private String regip; // 등록 IP (사용자가 계정을 등록할 때의 IP 주소를 저장)

    private String createdAt; // 계정 생성 일시
    private String deletedAt; // 계정 삭제 일시

    public void main(String[] args) {
        // 현재 일시를 가져오고 포맷 지정
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 소수점(.) 기준으로 밀리초 부분을 제거
        String trimmedDateTime = createdAt.substring(0, createdAt.indexOf("."));

        // 결과 출력s
        System.out.println(trimmedDateTime);  // 출력 예: 2024-10-01 16:52:38
    }


    public User toEntity() {
        return User.builder()
                .uid(uid)
                .pass(pass)
                .name(name)
                .role(role)
                .nick(nick)
                .email(email)
                .hp(hp)
                .grade(grade)
                .zip(zip)
                .addr1(addr1)
                .addr2(addr2)
                .regip(regip)
                .createAt(createdAt)
                .deletedAt(deletedAt)
                .build();
    }
}







