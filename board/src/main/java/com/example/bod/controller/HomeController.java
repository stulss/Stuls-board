package com.example.bod.controller;

import com.example.bod.dto.BoardDTO;
import com.example.bod.service.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class HomeController {

    private final BoardService boardService;

    @GetMapping("/")
    public String home(@PageableDefault(page = 1) Pageable pageable, Model model){
        Page<BoardDTO> boards = boardService.paging(pageable);

        model.addAttribute("boardList",boards);


        return "index";
    }
}
