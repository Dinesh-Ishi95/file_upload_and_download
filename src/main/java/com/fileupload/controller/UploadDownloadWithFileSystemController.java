package com.fileupload.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fileupload.dto.FileUploadResponse;
import com.fileupload.service.FileStorageService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class UploadDownloadWithFileSystemController {

	@Autowired
	private FileStorageService fileStorageService;

	@PostMapping("single/upload")
	FileUploadResponse singleFileUpload(@RequestParam("file") MultipartFile file) {
		String fileName = this.fileStorageService.storeFile(file);
		// http://localhost:8888/download/filename
		String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/").path(fileName)
				.toUriString();
		String contentType = file.getContentType();
		FileUploadResponse response = new FileUploadResponse(fileName, contentType, url);
		return response;
	}

	@GetMapping("/download/{fileName}")
	ResponseEntity<Resource> downloadSingleFile(@PathVariable String fileName, HttpServletRequest request) {
		Resource resource = this.fileStorageService.downloadFile(fileName);
		// MediaType contentType = MediaType.IMAGE_JPEG;
		String mimeType;
		try {
			mimeType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException e) {
			mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType))
//				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=" + resource.getFilename())
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + resource.getFilename()).body(resource);
	}

	@PostMapping("/multiple/upload")
	List<FileUploadResponse> multipleUpload(@RequestParam("files") MultipartFile[] files) {

		if (files.length > 7) {
			throw new RuntimeException("too many files");
		}

		List<FileUploadResponse> uploadResponseList = new ArrayList<>();

		Arrays.asList(files).stream().forEach(file -> {
			String fileName = this.fileStorageService.storeFile(file);
			// http://localhost:8888/download/filename
			String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/").path(fileName)
					.toUriString();
			String contentType = file.getContentType();
			FileUploadResponse response = new FileUploadResponse(fileName, contentType, url);
			uploadResponseList.add(response);
		});

		return uploadResponseList;
	}

	@GetMapping("zipDownload")
	void zipDownload(@RequestParam("fileName") String[] files, HttpServletResponse response) throws IOException {
		try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
			Arrays.asList(files).stream().forEach(file -> {
				Resource resource = this.fileStorageService.downloadFile(file);
				ZipEntry zipEntry = new ZipEntry(resource.getFilename());
				try {
					zipEntry.setSize(resource.contentLength());
					zos.putNextEntry(zipEntry);
					StreamUtils.copy(resource.getInputStream(), zos);
					zos.closeEntry();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("some exception has occured while zipping the files " + e);
				}
			});
			
			zos.finish();
		}
		response.setStatus(200);
		response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=zipFile");
	}

}
