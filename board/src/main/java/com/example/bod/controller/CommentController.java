package com.example.bod.controller;

import com.example.bod.dto.CommentDTO;
import com.example.bod.entity.Comment;
import com.example.bod.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@AllArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@ModelAttribute CommentDTO commentDTO) {

        System.out.println(commentDTO);
        Comment comment = commentService.save(commentDTO);

        List<CommentDTO> all = commentService.findAll(commentDTO.getBoardId());

        if(comment != null) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("게시글이 없음.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/list/{boardId}")
    public ResponseEntity<List<CommentDTO>> list(@PathVariable Long boardId) {
        List<CommentDTO> comments = commentService.findAll(boardId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping("/update")
    public String update(@ModelAttribute CommentDTO commentDTO) {
        commentService.update(commentDTO);
        return "redirect:/board/paging/";
    }

    @PostMapping("/delete/{Id}")
    public String deleteComment(@PathVariable Long Id) {
        Comment deletedComment = commentService.delete(Id);
        return "redirect:/board/paging/";
    }
}