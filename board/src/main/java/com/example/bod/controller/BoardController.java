package com.example.bod.controller;

import com.example.bod.dto.BoardDTO;
import com.example.bod.entity.Board;
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
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    public final BoardService boardService;


    @GetMapping("/create")
    public String createBoard(){
        return "create";
    }

    @GetMapping(value = {"/paging","/"})
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model){

        Page<BoardDTO> boards = boardService.paging(pageable);

        int blockLimit = 3;
        int startPage =(int) (Math.ceil((double) pageable.getPageNumber()/blockLimit)-1)*blockLimit+1;
        int endPage = ((startPage+blockLimit -1) < boards.getTotalPages()) ? (startPage+blockLimit -1) : boards.getTotalPages();

        model.addAttribute("boardList",boards);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);

        return "paging";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, @PageableDefault(page = 1) Pageable pageable){

        BoardDTO boardDTO = boardService.findById(id);

        model.addAttribute("board",boardDTO);
        model.addAttribute("page", pageable.getPageNumber());


        /*System.out.println(board.get().getId());
        System.out.println(board.get().getTitle());
        System.out.println(board.get().getUsername());
        System.out.println(board.get().getContent());
        System.out.println(board.get().getCreateTime());
        System.out.println(board.get().getUpdateTime());*/
        return "Detail";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO){
        System.out.println(boardDTO.getUsername());
        System.out.println(boardDTO.getTitle()+" : "+boardDTO.getContent());

        boardDTO.setCreateTime(LocalDateTime.now());
        boardService.save(boardDTO);
        return "redirect:/board/";
    }


    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model){

        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);

        return "update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO){

        boardService.update(boardDTO);

        return "redirect:/board/";
    }

    @GetMapping("/delete/id:{id}")
    public String deleteBoard(@PathVariable Long id){
        boardService.deleteBoard(id);
        return "redirect:/board/";
    }

}