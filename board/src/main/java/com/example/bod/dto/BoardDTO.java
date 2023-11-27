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

    private String contents;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


    public Board toEntity(){
        return Board.builder()
                .userName(username)
                .title(title)
                .contents(contents)
                .createTime(createTime)
                .updateTime(LocalDateTime.now())
                .build();
    }

    public static BoardDTO toBoardDTO(Board board){
        return new BoardDTO(
                board.getId(),
                board.getTitle(),
                board.getContents(),
                board.getUserName(),
                board.getCreateTime(),
                board.getUpdateTime() );
    }



}
