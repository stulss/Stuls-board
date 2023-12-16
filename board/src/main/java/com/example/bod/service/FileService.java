package com.example.bod.service;

import com.example.bod.dto.BoardDTO;
import com.example.bod.entity.Board;
import com.example.bod.entity.BoardFile;
import com.example.bod.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;


    public Optional<BoardFile> findById(Long fileId) {
        return fileRepository.findById(fileId);
    }

    public List<BoardFile> findByBoardId(Long id) {
        return fileRepository.findByBoardId(id);
    }
}
