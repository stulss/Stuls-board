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
            entity.tosave(board);
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
        Optional<Comment> commentOptional = commentRepository.findById(commentDTO.getId());

        Comment comment = commentOptional.get();

        comment.updateFromDTO(commentDTO);

        commentRepository.save(comment);
    }

    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    @Transactional
    public void delete(Long id){
        commentRepository.deleteById(id);
    }
}