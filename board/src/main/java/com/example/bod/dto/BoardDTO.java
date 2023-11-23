package com.example.bod.dto;

import com.example.bod.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {

    private Long id;

    private String username;

    private String title;

    private String content;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


    public Board toEntity(){
        return Board.builder()
                .username(username)
                .title(title)
                .content(content)
                .createTime(createTime)
                .updateTime(LocalDateTime.now())
                .build();
    }

    public static BoardDTO toBoardDTO(Board board){
        return new BoardDTO(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getUsername(),
                board.getCreateTime(),
                board.getUpdateTime() );
    }


    public Board toUpdate(){
        return Board.builder()
                .title(title)
                .content(content)
                .updateTime(updateTime)
                .build();
    }
}
