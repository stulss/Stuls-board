package com.example.bod.repository;

import com.example.bod.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<BoardFile, Long> {
    List<BoardFile> findByBoardId(Long boardId);

    void deleteByBoardId(Long boardId);
}
