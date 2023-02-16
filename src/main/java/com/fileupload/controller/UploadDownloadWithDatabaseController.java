package com.fileupload.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fileupload.dto.FileDocument;
import com.fileupload.dto.FileUploadResponse;
import com.fileupload.repository.DocFileDao;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class UploadDownloadWithDatabaseController {

	@Autowired
	private DocFileDao docFileDao;

	@PostMapping("single/uploadDb")
	FileUploadResponse singleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {

		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		FileDocument fileDocument = new FileDocument();
		fileDocument.setFileName(fileName);
		fileDocument.setDocFile(file.getBytes());

		docFileDao.save(fileDocument);

		// http://localhost:8888/download/filename
		String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFromDb/").path(fileName)
				.toUriString();
		String contentType = file.getContentType();
		FileUploadResponse response = new FileUploadResponse(fileName, contentType, url);
		return response;
	}

	@GetMapping("/downloadFromDb/{fileName}")
	ResponseEntity<byte[]> downloadSingleFile(@PathVariable String fileName, HttpServletRequest request) {

		FileDocument document = docFileDao.findByFileName(fileName);

		// MediaType contentType = MediaType.IMAGE_JPEG;
		String mimeType = request.getServletContext().getMimeType(document.getFileName());
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType))
//				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=" + resource.getFilename())
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + document.getFileName())
				.body(document.getDocFile());
	}

}
