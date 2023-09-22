package com.sample.file.service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;

import com.sample.file.helper.ExcelFileHelper;
import com.sample.file.model.ExcelFileAttachment;
import com.sample.file.model.ExcelRecords;
import com.sample.file.repository.ExcelFileRepository;
import com.sample.file.repository.ExcelRecordsRepo;

@Service
public class ExcelFileServiceImpl implements ExcelFileService {
	
	@Autowired
	private ExcelFileRepository fileAttachmentRepo; 
	
	@Autowired
	ExcelRecordsRepo excelRecordsRepo;
	
	@Autowired
	ExcelFileHelper helper;
	
	@Override
	public ExcelFileAttachment uploadExcelFileToDB(MultipartFile file,String filepath) {
		List<ExcelRecords> content = helper.convertDatetoList(file);
		ExcelFileAttachment excelFileAttachment = new ExcelFileAttachment();
		if (helper.checkExcelFormat(file)) {
			//To check if the same file is being uploaded.If file already uploaded, it will get updated against the same id.
			ExcelFileAttachment uploadedexcel= fileAttachmentRepo.findByFileName(filepath+"\\"+file.getOriginalFilename());
			if(uploadedexcel !=null) {
				excelFileAttachment.setId(uploadedexcel.getId());
			}
			excelFileAttachment.setFileName(StringUtils.cleanPath(file.getOriginalFilename()));
			excelFileAttachment.setFileType(file.getContentType());
			excelFileAttachment.setFilePath(filepath+"\\"+file.getOriginalFilename());
			excelFileAttachment.setRecords(content);
			excelFileAttachment.setLastAccesssTime(new Date(System.currentTimeMillis()));
			excelFileAttachment.setReviewedBy("admin");
			for(ExcelRecords record:content) {
				record.setExcel(excelFileAttachment);
			}
		}
		return fileAttachmentRepo.save(excelFileAttachment);
	}
	
	@Override
	public int calculateUploadProgress(MultipartFile file) {
		long totalSize = 200 * 1024 * 1024;
		long bytesRead = file.getSize();
		int progress = (int) ((bytesRead * 100) / totalSize);
		return Math.min(100, Math.max(0, progress));
	}
	
	@Override
	public List<ExcelFileAttachment> getAllUploadedExcelFiles(){
		return fileAttachmentRepo.findAll();
	}
	
	@Override
	public ExcelFileAttachment geExcelFileById(Long excelId){
		Optional<ExcelFileAttachment> excelFileAttachment= fileAttachmentRepo.findById(excelId);
		if(!excelFileAttachment.isPresent()) {
			throw new RestClientException("Excel File not found");
		}
		return excelFileAttachment.get();
	}
	
	@Override
	public List<ExcelRecords> reviewRecordsOfExcel(Long fileId) {
		Optional<ExcelFileAttachment> excelFileAttachment= fileAttachmentRepo.findById(fileId);
		if(!excelFileAttachment.isPresent()) {
			throw new RestClientException("Excel File not found");
		}
		excelFileAttachment.get().setLastAccesssTime(new Date(System.currentTimeMillis()));
		fileAttachmentRepo.save(excelFileAttachment.get());
		return excelFileAttachment.get().getRecords();	
	}
	
	@Override
	public void deleteExcelFile(Long fileId) {
		ExcelFileAttachment excelToDelete = geExcelFileById(fileId);
		try {	
			if(excelToDelete!=null) {
				List<ExcelRecords> records = excelToDelete.getRecords();
				excelRecordsRepo.deleteAll(records);
				fileAttachmentRepo.deleteById(excelToDelete.getId());
			}
			Files.delete(Paths.get(excelToDelete.getFilePath()));
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
}
