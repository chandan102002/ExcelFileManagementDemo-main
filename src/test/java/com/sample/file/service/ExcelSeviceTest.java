package com.sample.file.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import com.sample.file.model.ExcelFileAttachment;
import com.sample.file.model.ExcelRecords;
import com.sample.file.repository.ExcelFileRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExcelSeviceTest {
	
	@Autowired
	ExcelFileRepository excelRepo;
	
	@MockBean
	private ExcelFileService excelFileService;
	
	@Test
	public void testCreate() throws FileNotFoundException, IOException {
		ExcelFileAttachment excel = new ExcelFileAttachment();
		excel.setFileName("UnitTest.xlsx");
		excel.setFilePath("C:\\Users\\username\\Desktop\\ExcelDemo\\UnitTest.xlsx");
		excel.setFileType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		excel.setLastAccesssTime(new Date(System.currentTimeMillis()));
		List<ExcelRecords> contentExcel=new ArrayList<>();
		ExcelRecords row1= new ExcelRecords();
		row1.setRecord("row1");
		row1.setId(1L);
		row1.setExcel(null);
		ExcelRecords row2= new ExcelRecords();
		row2.setRecord("row2");
		row2.setId(2L);
		row2.setExcel(null);
		contentExcel.add(row1);
		contentExcel.add(row2);
		excel.setRecords(contentExcel);
		excel.setReviewedBy("admin");
		excel.getReviewedBy();
		
		MockMultipartFile excelfile = new MockMultipartFile("file", "UnitTest.xlsx", "multipart/form-data",new FileInputStream("C:\\Users\\username\\Desktop\\ExcelDemo"));
		
		Mockito.when(excelRepo.save(excel)).thenReturn(excel);
		    
		assertThat(excelFileService.uploadExcelFileToDB(excelfile,"C:\\Users\\username\\Desktop\\ExcelDemo\\UnitTest.xlsx")).isEqualTo(excel);
		
	}

	@Test
	public void testGetExcelById() {
		assertNotNull(excelFileService.reviewRecordsOfExcel(11L));
	}
	
	@Test
	public void testGetAllFile() {
		assertThat(excelFileService.getAllUploadedExcelFiles());
	}
	
	@Test
	public void testDeleteExcel() {
		Optional<ExcelFileAttachment> file = excelRepo.findById(13L);
		excelRepo.delete(file.get());
		assertNotNull(excelRepo.findById(file.get().getId()));
	}

}
