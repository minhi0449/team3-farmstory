package com.farmstory.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    private int parent;
    private String content;
    //private String writer;
    private String regip;

    @CreationTimestamp
    private LocalDateTime rdate;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "writer")
    private User user;

    public void addUser(User user){
        this.user = user;
    }

}

