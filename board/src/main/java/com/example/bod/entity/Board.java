package com.example.bod.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    // 작성자 이름
    @Column(nullable = false)
    private String username;

    // 게시물제목
    @Column(nullable = false)
    private String title;

    // 게시물내용
    @Column(nullable = false)
    private String content;

    //  최초 작성 시간

    private LocalDateTime createTime;
    // 최종 작성 시간

    private LocalDateTime updateTime;

    @Builder
    public Board(Long id, String username, String title, String content, LocalDateTime createTime, LocalDateTime updateTime) {
        Id = id;
        this.username = username;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }


}