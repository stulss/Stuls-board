package com.example.bod.controller;

import com.example.bod.dto.BoardDTO;
import com.example.bod.dto.FileDTO;
import com.example.bod.entity.BoardFile;
import com.example.bod.repository.FileRepository;
import com.example.bod.service.BoardService;
import com.example.bod.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final FileRepository fileRepository;


    @GetMapping("/create")
    public String create(){
        return "create";
    }


    @GetMapping(value = {"/paging", "/"})
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model modal){

        Page<BoardDTO> boards = boardService.paging(pageable);

        int blockLimit = 3;
        int startPage = (int)(Math.ceil((double)pageable.getPageNumber() / blockLimit) - 1) * blockLimit + 1;
        int endPage = ((startPage + blockLimit - 1) < boards.getTotalPages()) ? (startPage + blockLimit - 1) : boards.getTotalPages();

        modal.addAttribute("boardList", boards);
        modal.addAttribute("startPage", startPage);
        modal.addAttribute("endPage",endPage);

        return "paging";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {

        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        List<BoardFile> byBoardID = fileRepository.findByBoardId(id);
        model.addAttribute("files",byBoardID);
        return "update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO, @RequestParam(value = "files", required = false) MultipartFile[] files) throws IOException {
        boardService.update(boardDTO, files);
        return "redirect:/board/";
    }


    @GetMapping("/{id}")
    public String paging(@PathVariable Long id, Model model,
                         @PageableDefault(page = 1) Pageable pageable){

        BoardDTO dto = boardService.findById(id);

        model.addAttribute("board", dto);
        model.addAttribute("page", pageable.getPageNumber());

        List<BoardFile> byBoardID = fileRepository.findByBoardId(id);
        model.addAttribute("files",byBoardID);

        return "Detail";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO,
                       @RequestParam MultipartFile[] files) throws IOException {

        boardDTO.setCreateTime(LocalDateTime.now());
        boardService.save(boardDTO, files);

        return "redirect:/board/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         @RequestParam BoardFile files) {
        System.out.println(id);
        boardService.delete(id,files);
        return "redirect:/board/paging";
    }


}