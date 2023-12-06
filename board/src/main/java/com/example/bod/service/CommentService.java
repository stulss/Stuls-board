package com.example.bod.service;

import com.example.bod.dto.CommentDTO;
import com.example.bod.entity.Board;
import com.example.bod.entity.Comment;
import com.example.bod.repository.BoardRepository;
import com.example.bod.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;


    @Transactional
    public Comment save(CommentDTO commentDTO) {
        Optional<Board> optionalBoard =
                boardRepository.findById(commentDTO.getBoardId());

        if(optionalBoard.isPresent()) {
            Board board = optionalBoard.get();

            Comment entity = commentDTO.toEntity();
            entity.toUpdate(board);
            return commentRepository.save(entity);
        } else {
            return null;
        }
    }

    public List<CommentDTO> findAll(Long boardId) {
        Board boardEntity = boardRepository.findById(boardId).get();
        List<Comment> commentEntityList = commentRepository.findAllByBoardOrderByIdDesc(boardEntity);
        /* EntityList -> DTOList */
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (Comment commentEntity: commentEntityList) {
            CommentDTO commentDTO = CommentDTO.toCommentDTO(commentEntity, boardId);
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }

    @Transactional
    public void update(CommentDTO commentDTO) {
        Optional<Comment> comment = commentRepository.findById(commentDTO.getId());
        if (comment.isPresent()) {
            Comment comment1 = comment.get();
            comment1.toUpdate(commentDTO.toEntity().getBoard());
            commentRepository.save(comment1);
        }
    }

    public CommentDTO findById(Long id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment != null) {
            return CommentDTO.fromComment(comment);
        }
        return null;
    }

    public void deletComment(Long id) {
        commentRepository.deleteById(id);
    }
}