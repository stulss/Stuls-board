package com.example.bod.controller;

import com.example.bod.dto.BoardRequestDTO;
import com.example.bod.repository.BoardRepository;
import com.example.bod.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class BoardController {

    public final BoardService boardService;

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/createBoard")
    public String createBoard(){
        return "createBoard";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardRequestDTO boardDTO){
        System.out.println(boardDTO.getUsername());
        System.out.println(boardDTO.getTitle()+" : "+boardDTO.getContent());

        boardDTO.setCreateTime(LocalDateTime.now());
        boardService.save(boardDTO);
        return "redirect:";
    }

    @GetMapping("/paging")
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model modal){

        Page<BoardRequestDTO> boards = boardService.paging(pageable);

        int blockLimit = 3;
        int startPage =(int) (Math.ceil((double) pageable.getPageNumber()/blockLimit)-1)*blockLimit+1;
        int endPage = ((startPage+blockLimit -1) < boards.getTotalPages()) ? (startPage+blockLimit -1) : boards.getTotalPages();

        modal.addAttribute("boardList",boards);
        modal.addAttribute("startPage",startPage);
        modal.addAttribute("endPage",endPage);

        return "paging";
    }


    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute BoardRequestDTO boardDTO){
        boardDTO.setId(id);
        boardService.update(boardDTO);
        return "redirect:/paging";
    }
}
