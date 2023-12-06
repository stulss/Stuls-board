package com.example.bod.dto;

import com.example.bod.entity.Comment;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Long id;

    private String writer;

    private String contents;

    private Long boardId;

    public Comment toEntity() {
        return Comment.builder()
                .writer(writer)
                .contents(contents)
                .build();
    }

    public static CommentDTO toCommentDTO(Comment comment, Long boardId) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setWriter(comment.getWriter());
        commentDTO.setContents(comment.getContents());
        commentDTO.setBoardId(boardId);
        return commentDTO;
    }

    public static CommentDTO fromComment(Comment comment) {
        return new CommentDTO(
                comment.getId(),
                comment.getWriter(),
                comment.getContents(),
                comment.getBoard().getId()
        );
    }
}