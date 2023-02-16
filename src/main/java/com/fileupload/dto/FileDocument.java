package com.fileupload.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class FileDocument {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "filename")
	private String fileName;

	@Column(name = "docfile")
	@Lob
	private byte[] docFile;

	public FileDocument() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FileDocument(int id, String fileName, byte[] docFile) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.docFile = docFile;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getDocFile() {
		return docFile;
	}

	public void setDocFile(byte[] docFile) {
		this.docFile = docFile;
	}

}
