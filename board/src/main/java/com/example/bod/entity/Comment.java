package com.example.bod.entity;


import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String writer;

    // ** 내용
    @Column(length = 50)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public Comment(Long id, String writer, String contents, Board board) {
        this.id = id;
        this.writer = writer;
        this.contents = contents;
        this.board = board;
    }

    public Comment toUpdate(Board board) {
        Comment comment = new Comment();
        this.board = board;
        return comment;
    }
}