package com.sample.file.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sample.file.model.ExcelFileAttachment;
import com.sample.file.model.ExcelRecords;

public interface ExcelFileService {

	ExcelFileAttachment uploadExcelFileToDB(MultipartFile file, String filepath);
	
	int calculateUploadProgress(MultipartFile file);

	List<ExcelFileAttachment> getAllUploadedExcelFiles();

	List<ExcelRecords> reviewRecordsOfExcel(Long fileId);

	ExcelFileAttachment geExcelFileById(Long excelId);

	void deleteExcelFile(Long fileId);

}
