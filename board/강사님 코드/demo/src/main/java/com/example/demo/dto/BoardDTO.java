package com.example.demo.dto;


import com.example.demo.entity.Board;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {

    private Long id;

    // ** 제목
    private String title;

    // ** 내용
    private String contents;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .contents(contents)
                .createTime(createTime)
                .updateTime(updateTime)
                .build();
    }

    public static BoardDTO toBoardDTO(Board board){
        return new BoardDTO(
                board.getId(),
                board.getTitle(),
                board.getContents(),
                board.getCreateTime(),
                board.getUpdateTime() );
    }
}
