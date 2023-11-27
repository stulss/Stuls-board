package com.example.bod.dto;

import com.example.bod.entity.File;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class FileDTO {


    private String filePath;
    // 파일 이름
    private String fileName;
    // 파일 포멧
    private String fileType;
    // 파일 크기
    private Long fileSize;
    // 게시물 id
    private Long boardId;

    public File toEntity(){
        return File.builder()
                .filePath(filePath)
                .fileName(fileName)
                .fileType(fileType)
                .fileSize(fileSize)
                .build();
    }
}
