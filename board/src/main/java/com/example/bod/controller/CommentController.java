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

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        CommentDTO commentDTO = commentService.findById(id);
        model.addAttribute("comment", commentDTO);
        return "redirect:/list/{boardId}"; // 수정할 댓글을 보여주는 페이지로 이동
    }

    @PostMapping("/update")
    public String update(@ModelAttribute CommentDTO commentDTO) {
        commentService.update(commentDTO);
        return "redirect:/board/detail/" + commentDTO.getBoardId(); // 수정한 댓글을 보여주는 게시물 상세 페이지로 이동
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        commentService.deletComment(id);
        return "redirect:/";
    }

}