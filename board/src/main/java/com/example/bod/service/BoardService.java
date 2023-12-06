package com.example.bod.service;

import com.example.bod.dto.BoardDTO;
import com.example.bod.dto.FileDTO;
import com.example.bod.entity.Board;
import com.example.bod.entity.BoardFile;
import com.example.bod.repository.BoardRepository;
import com.example.bod.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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
    private final String filePath = "C:/Users/stuls/Desktop/developer stuls/green/boardfile/";


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
                board.getUpdateTime()));
    }

    public BoardDTO findById(Long id) {

        //if(boardRepository.findById(id).isPresent()) ... 예외처리 생략
        Board board = boardRepository.findById(id).get();
        return BoardDTO.toBoardDTO(board);
    }

    @Transactional
    public void save(BoardDTO dto, MultipartFile[] files) throws IOException {
        // 게시글 저장
        Board board = boardRepository.save(dto.toEntity());

        // 파일 정보 저장
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                // 파일 경로 생성
                Path uploadPath = Paths.get(filePath);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // 파일명 추출
                String originalFileName = file.getOriginalFilename();

                // 파일 고유 식별자 생성
                String uuid = UUID.randomUUID().toString();

                // 파일 저장 경로 지정
                String path = filePath + uuid + originalFileName;

                // 파일 저장
                file.transferTo(new java.io.File(path));

                // 파일 엔티티 생성 및 저장
                BoardFile boardFile = BoardFile.builder()
                        .filePath(filePath)
                        .fileName(originalFileName)
                        .uuid(uuid)
                        .fileType(file.getContentType())
                        .fileSize(file.getSize())
                        .board(board)
                        .build();
                fileRepository.save(boardFile);
            }
        }
    }

    @Transactional
    public void delete(Long id,BoardFile boardFile) {
        boardRepository.deleteById(id);
        Optional<Board> boardOptional = boardRepository.findById(id);
        Board board = boardOptional.get();
        List<BoardFile> existingFiles = fileRepository.findByBoardId(board.getId());
        for (BoardFile existingFile : existingFiles) {
            deleteFile(existingFile);
        }
    }
    @Transactional
    public void update(BoardDTO boardDTO, MultipartFile[] files) throws IOException {
        Optional<Board> boardOptional = boardRepository.findById(boardDTO.getId());

        //if(boardOptional.isPresent()) ... 예외처리 생략
        Board board = boardOptional.get();

        // 해당 게시물과 관련된 기존 파일 가져오기
        List<BoardFile> existingFiles = fileRepository.findByBoardId(board.getId());

        // 새 파일이 제공되는 경우
        if (files != null && files.length > 0) {
            // 기존 파일 삭제
            for (BoardFile existingFile : existingFiles) {
                deleteFile(existingFile);
            }

            // 새 파일 저장
            for (MultipartFile file : files) {
                uploadFile(file, board);
            }
        } else {
            // 새 파일이 없는 경우 기존 파일 유지
            for (BoardFile existingFile : existingFiles) {
                // 기존 파일 정보를 DTO를 사용하여 업데이트
                FileDTO fileDTO = new FileDTO(); // BoardFile을 다루는 DTO 생성
                fileDTO.setBoardId(board.getId());
                fileDTO.setFileName(existingFile.getFileName());
                // 필요한 다른 정보들을 DTO에 설정

                // DTO를 이용하여 파일 정보 업데이트
                // fileDTO를 이용하여 fileRepository에서 엔티티를 가져와 업데이트
                // fileRepository.save(fileDTO.toEntity()); 와 같은 방식으로 엔티티를 업데이트할 수 있습니다.
            }
        }

        // 다른 게시물 정보 업데이트
        board.updateFromDTO(boardDTO);
        boardRepository.save(board);
    }

    @Transactional
    public void deleteFile(BoardFile file) {
        String path = filePath + file.getUuid() + file.getFileName();
        java.io.File deleteFile = new java.io.File(path);
        if (deleteFile.exists()) {
            deleteFile.delete();
        }
        fileRepository.delete(file);
    }

    private void uploadFile(MultipartFile file, Board board) throws IOException {
        if (!file.isEmpty()) {
            Path uploadPath = Paths.get(filePath);

            // ** 만약 경로가 없다면... 경로 생성.
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // ** 파일명 추출
            String originalFileName = file.getOriginalFilename();

            // ** 확장자 추출
            String formatType = originalFileName.substring(
                    originalFileName.lastIndexOf("."));

            // ** UUID 생성
            String uuid = UUID.randomUUID().toString();

            // 경로 지정
            String path = filePath + uuid + originalFileName;

            // 경로에 파일을 저장.
            file.transferTo(new java.io.File(path));

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
}
