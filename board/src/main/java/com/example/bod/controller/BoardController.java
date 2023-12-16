package com.example.bod.controller;

import com.example.bod.dto.BoardDTO;
import com.example.bod.dto.FileDTO;
import com.example.bod.entity.BoardFile;
import com.example.bod.repository.FileRepository;
import com.example.bod.service.BoardService;
import com.example.bod.service.CommentService;
import com.example.bod.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final FileService fileService;


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
        List<BoardFile> byBoardID = fileService.findByBoardId(id);
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

        List<BoardFile> byBoardID = fileService.findByBoardId(id);
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
    public String delete(@PathVariable Long id) {
        System.out.println(id);
        boardService.delete(id);
        return "redirect:/board/paging";
    }

    @PostMapping("/deleteFile/{fileId}")
    public void deleteFile(@PathVariable Long fileId) {

        // BoardFile 객체를 얻어옵니다.
        Optional<BoardFile> fileOptional = fileService.findById(fileId);

        if (fileOptional.isPresent()) {
            BoardFile file = fileOptional.get();
            boardService.deleteFile(file);
        }
    }

}