package com.farmstory.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity                 // 엔티티 객체 정의
@Builder
@ToString
@Table(name = "Users")
public class User {
    @Id
    private String uid;

    private String pass;
    private String nick;
    private String name;

    private String role;

    private String email;
    private String hp;
    private String grade;
    private String zip;
    private String addr1;
    private String addr2;
    private String regip;

    @CreationTimestamp
    private String createAt;
    private String deletedAt;

    // 추가 필드
    @Transient
    private String encodedPassword;

//    public UserDTO toDTO() {
//        return UserDTO.builder()
//                .uid(uid)
//                .name(name)
//                .role(role)
//                .pass(pass)
//                .nick(nick)
//                .email(email)
//                .hp(hp)
//                .grade(grade)
//                .zip(zip)
//                .addr1(addr1)
//                .addr2(addr2)
//                .regip(regip)
//                .createdAt(createAt)
//                .deletedAt(deletedAt)
//                .build();
//    }

    // 탈퇴 회원을 위한 생성자
    public void UserDTO(String uid, String createAt, String deletedAt, String role) {
        this.uid = uid;
        this.createAt = createAt;
        this.deletedAt = deletedAt;
        this.role = role;
        this.name = null;
        this.email = null;
        this.pass = null;
        this.nick = null;
        this.hp = null;
        this.zip = null;
        this.addr1 = null;
        this.addr2 = null;
        this.regip = null;
    }

    public void setPass(String encodedPassword) {
    }

    public void setRole(String newGrade) {
    }
}
