package com.example.bod.service;

import com.example.bod.dto.BoardRequestDTO;
import com.example.bod.entity.Board;
import com.example.bod.repository.BoardRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);


    /* paging을 위한 함수
       Pageable = 수량을 사용하기 위한 정보
       int page = pageable.getPageNumber() - 1; : 페이지를 1부터 시작하는 설정
    */
    public Page<BoardRequestDTO> paging(Pageable pageable){
        // 페이지 시작 번호 셋팅
        int page = pageable.getPageNumber() - 1;
        // 페이지에 포함될 게시물 개수
        int size = 5;
        // 전체 게시물을 불러옴
        Page<Board> boards = boardRepository.findAll(
                //자동으로 정렬을 시켜 준다.
                PageRequest.of(page,size));

        // db에 있는 전체 데이터를 불러오고
        return boards.map(board -> new BoardRequestDTO(
                board.getId(),
                board.getUsername(),
                board.getTitle(),
                board.getContent(),
                board.getCreateTime()));
    }


    @Transactional
    public void save(BoardRequestDTO boardDTO) {
        try {
             Board board = boardDTO.toEntity();

             // 저장
             boardRepository.save(board);

             // 로깅
             logger.info("게시물이 저장되었습니다. ID: {}", board.getId());
        } catch (Exception e) {
            logger.error("게시물 저장 중 오류가 발생했습니다.", e);
            // 예외 처리
        }
    }

    @Transactional
    public void update(BoardRequestDTO boardDTO) {
        try {
            Board board = boardRepository.findById(boardDTO.getId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다. ID: " + boardDTO.getId()));

            boardRepository.save(boardDTO.toUpdate());

            logger.info("게시물이 수정되었습니다. ID: {}", board.getId());
        } catch (Exception e) {
            logger.error("게시물 수정 중 오류가 발생했습니다.", e);
            // 예외 처리
        }
    }
}