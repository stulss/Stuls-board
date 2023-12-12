package com.example.bod.controller;

import com.example.bod.dto.CommentDTO;
import com.example.bod.entity.Comment;
import com.example.bod.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


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
    public ResponseEntity<List<CommentDTO>> getCommentList(@PathVariable Long boardId) {
        List<CommentDTO> commentList = commentService.findAll(boardId);
        return ResponseEntity.ok(commentList);
    }

    @GetMapping("/list/{boardId}")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long boardId) {
        List<CommentDTO> comments = commentService.findAll(boardId);
        return new ResponseEntity<>(comments, HttpStatus.OK);

    }
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        Optional<Comment> commentOptional = commentService.findById(id);

        model.addAttribute("commentDTO", commentOptional);
        return "update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute CommentDTO commentDTO) {
        commentService.update(commentDTO);
        return "redirect:/board/paging/";
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        System.out.println(id);
        commentService.delete(id);
        return ResponseEntity.ok("삭제 성공");
    }
}