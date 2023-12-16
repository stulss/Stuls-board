package com.example.bod.service;

import com.example.bod.dto.CommentDTO;
import com.example.bod.entity.Board;
import com.example.bod.entity.Comment;
import com.example.bod.repository.BoardRepository;
import com.example.bod.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

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
        java.util.List<Comment> commentEntityList = commentRepository.findAllByBoardOrderByIdDesc(boardEntity);
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
        Optional<Comment> commentOptional = commentRepository.findById(commentDTO.getId());

        if(commentOptional.isPresent()){
        Comment comment = commentOptional.get();

        comment.updateFromDTO(commentDTO);

        commentRepository.save(comment);
        }else{
            throw new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."+commentDTO.getId());
        }

    }

    @Transactional
    public Comment delete(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);

        if(optionalComment.isPresent()) {
            commentRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("해당 댓글이 존재하지 않습니다. id=" + id);
        }
        return null;
    }
}