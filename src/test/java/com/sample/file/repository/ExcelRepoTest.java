package com.sample.file.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sample.file.model.ExcelFileAttachment;
import com.sample.file.model.ExcelRecords;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExcelRepoTest {
	
	@Autowired
	ExcelFileRepository excelRepo;

	@Test
	public void testCreate() {
		List<ExcelRecords> contentExcel=new ArrayList<>();
		ExcelRecords row1= new ExcelRecords();
		row1.setRecord("row1");
		ExcelRecords row2= new ExcelRecords();
		row2.setRecord("row2");
		contentExcel.add(row1);
		contentExcel.add(row2);
		
		ExcelFileAttachment excel = new ExcelFileAttachment();
		excel.setFileName("UnitTest.xlsx");
		excel.setFilePath("C:\\Users\\username\\Desktop\\ExcelDemo\\UnitTest.xlsx");
		excel.setFileType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		excel.setLastAccesssTime(new Date(System.currentTimeMillis()));
		excel.setRecords(contentExcel);
		excel.setReviewedBy("admin");
		
		ExcelFileAttachment savedExcel = excelRepo.save(excel);
		assertNotNull(excelRepo.findById(savedExcel.getId()));
	}

	@Test
	public void testGetFileById() {
		assertNotNull(excelRepo.findById(10L));
	}
	
	@Test
	public void testGetAllFiles() {
		assertThat(excelRepo.findAll());
	}
	
	@Test
	public void testDeleteFile() {
		Optional<ExcelFileAttachment> file = excelRepo.findById(10L);
		excelRepo.delete(file.get());
		assertNotNull(excelRepo.findById(file.get().getId()));
	}
}
