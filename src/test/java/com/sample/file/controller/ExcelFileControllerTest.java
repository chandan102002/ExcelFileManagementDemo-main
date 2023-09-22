package com.sample.file.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.file.model.ExcelFileAttachment;
import com.sample.file.model.ExcelRecords;
import com.sample.file.service.ExcelFileService;

@RunWith(SpringRunner.class)
@WebMvcTest(value=ExcelFileController.class,secure = false)
public class ExcelFileControllerTest {
	
	@Autowired
	private MockMvc  mockMvc;
	
	@MockBean
	private ExcelFileService excelFileService;
	
	/**
	 * Maps an Object into a JSON String. Uses a Jackson ObjectMapper.
	 */
	private String mapToJson(Object object) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(object);
	}
	
	@Test
	public void testUploadExcel() throws Exception {
		
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
		
		String inputInJson = this.mapToJson(excel);
		
		String URI = "/api/upload/excel";
		
		MockMultipartFile excelfile = new MockMultipartFile("file", "Test.xlsx", "multipart/form-data",new FileInputStream("C:\\Users\\username\\Desktop\\ExcelDemo"));
		
		Mockito.when(excelFileService.uploadExcelFileToDB(excelfile,"C:\\Users\\username\\Desktop\\ExcelDemo")).thenReturn(excel);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(URI)
				.accept(MediaType.APPLICATION_JSON).content(inputInJson)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		
		String outputInJson = response.getContentAsString();
		
		assertThat(outputInJson).isEqualTo(inputInJson);
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
	
	@Test
	public void testContentById() throws Exception {
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
		
		Mockito.when(excelFileService.reviewRecordsOfExcel(Mockito.anyLong())).thenReturn(contentExcel);
		
		String URI = "/api/files/1/review";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				URI).accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expectedJson = this.mapToJson(contentExcel);
		String outputInJson = result.getResponse().getContentAsString();
		assertThat(outputInJson).isEqualTo(expectedJson);
		
	}
	
	@Test
	public void testGetAllFiles() throws Exception {
		ExcelFileAttachment excel = new ExcelFileAttachment();
		excel.setId(1L);
		excel.setFileName("UnitTest.xlsx");
		excel.setFilePath("C:\\Users\\username\\Desktop\\ExcelDemo\\UnitTest.xlsx");

		ExcelFileAttachment excel2 = new ExcelFileAttachment();
		excel2.setId(2L);
		excel2.setFileName("UnitTest1.xlsx");
		excel2.setFilePath("C:\\Users\\username\\Desktop\\ExcelDemo\\UnitTest1.xlsx");
		
		List<ExcelFileAttachment> files = new ArrayList<>();
		files.add(excel);
		files.add(excel2);
		
		Mockito.when(excelFileService.getAllUploadedExcelFiles()).thenReturn(files);
		
		String URI = "/api/files";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				URI).accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		String expectedJson = this.mapToJson(files);
		String outputInJson = result.getResponse().getContentAsString();
		assertThat(outputInJson).isEqualTo(expectedJson);
		
	}
}
