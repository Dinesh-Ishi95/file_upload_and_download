package com.fileupload.dto;

public class FileUploadResponse {

	private String fileName;
	private String contentType;
	private String url;

	@Override
	public String toString() {
		return "FileUploadResponse [fileName=" + fileName + ", contentType=" + contentType + ", url=" + url + "]";
	}

	public FileUploadResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FileUploadResponse(String fileName, String contentType, String url) {
		super();
		this.fileName = fileName;
		this.contentType = contentType;
		this.url = url;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
