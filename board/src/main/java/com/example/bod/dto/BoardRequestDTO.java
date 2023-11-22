package com.example.bod.dto;

import com.example.bod.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.tuple.UpdateTimestampGeneration;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardRequestDTO {

    private Long id;

    private String username;

    private String title;

    private String content;

    private LocalDateTime createTime;


    public Board toEntity(){
        return Board.builder()
                .username(username)
                .title(title)
                .content(content)
                .createTime(createTime)
                .updateTime(LocalDateTime.now())
                .build();
    }
    public Board toUpdate(){
        return Board.builder()
                .title(title)
                .content(content)
                .updateTime(LocalDateTime.now())
                .build();
    }
}
