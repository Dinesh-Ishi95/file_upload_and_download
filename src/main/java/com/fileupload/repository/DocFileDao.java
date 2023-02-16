package com.fileupload.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fileupload.dto.FileDocument;

@Repository
public interface DocFileDao extends JpaRepository<FileDocument, Integer> {

	FileDocument findByFileName(String fileName);

}
