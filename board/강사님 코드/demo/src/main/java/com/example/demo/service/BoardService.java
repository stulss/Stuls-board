package com.example.demo.service;


import com.example.demo.dto.BoardDTO;
import com.example.demo.entity.Board;
import com.example.demo.entity.BoardFile;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;

    // ** 학원에서는 /G/
    // ** 집에서는 본인 PC 이름.
    private final String filePath = "C:/Users/G/Desktop/green/Board Files/";


    // ** paging 을 함수
    public Page<BoardDTO> paging(Pageable pageable) {

        // ** 페이지 시작 번호 셋팅
        int page = pageable.getPageNumber() - 1;
        
        // ** 페이지에 포함될 게시물 개수
        int size = 5;

        // ** 전체 게시물을 불러온다.
        Page<Board> boards = boardRepository.findAll(
                PageRequest.of(page, size));

        return boards.map(board -> new BoardDTO(
                board.getId(),
                board.getTitle(),
                board.getContents(),
                board.getCreateTime(),
                board.getUpdateTime() ));
    }

    public BoardDTO findById(Long id) {

        //if(boardRepository.findById(id).isPresent()) ... 예외처리 생략
        Board board = boardRepository.findById(id).get();
        return BoardDTO.toBoardDTO(board);
    }

    @Transactional
    public void save(BoardDTO dto, MultipartFile[] files) throws IOException {

        Path uploadPath = Paths.get(filePath);

        // ** 만약 경로가 없다면... 경로 생성.
        if(!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // ** UUID 생성
        String uuid = UUID.randomUUID().toString();

        // ** 게시글 DB에 저장 후 pk을 받아옴.
        Long id = boardRepository.save(dto.toEntity()).getId();
        Board board = boardRepository.findById(id).get();

        // ** 파일 정보 저장.
        for(MultipartFile file : files) {

            // ** 파일명 추출
            String originalFileName = file.getOriginalFilename();

            // ** 확장자 추출
            String formatType = originalFileName.substring(
                    originalFileName.lastIndexOf("."));

            // ** 경로 지정
            // ** C:/Users/G/Desktop/green/Board Files/{uuid + originalFileName}
            String path = filePath + uuid + originalFileName;

            // ** 경로에 파일을 저장.  DB 아님
            file.transferTo( new File(path) );

            BoardFile boardFile = BoardFile.builder()
                    .filePath(filePath)
                    .fileName(originalFileName)
                    .uuid(uuid)
                    .fileType(formatType)
                    .fileSize(file.getSize())
                    .board(board)
                    .build();

            fileRepository.save(boardFile);
        }
    }

    @Transactional
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    @Transactional
    public void update(BoardDTO boardDTO) {
        Optional<Board> boardOptional = boardRepository.findById(boardDTO.getId());

        //if(boardOptional.isPresent()) ... 예외처리 생략
        Board board = boardOptional.get();

        board.updateFromDTO(boardDTO);

        boardRepository.save(board);
    }
}
