package com.example.bod.dto;

import com.example.bod.entity.BoardFile;
import lombok.*;

@ToString
@Data
@NoArgsConstructor
public class FileDTO {

    // ** 파일 경로
    private String filePath;

    // ** 파일 이름
    private String fileName;

    // ** uuid (랜덤 키)
    private String uuid;

    // ** 파일 포멧
    private String fileType;

    // ** 파일 크기
    private Long fileSize;

    // ** 게시물 id
    private Long boardId;

    public BoardFile toEntity() {
        return BoardFile.builder()
                .filePath(filePath)
                .fileName(fileName)
                .uuid(uuid)
                .fileType(fileType)
                .fileSize(fileSize)
                .build();
    }
}










