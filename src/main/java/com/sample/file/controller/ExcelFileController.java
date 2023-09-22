package com.sample.file.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;
import com.sample.file.model.ExcelFileAttachment;
import com.sample.file.model.ExcelRecords;
import com.sample.file.service.ExcelFileService;
import com.sample.file.utils.APIJSONDisplay;
import com.sample.file.views.ExcelFileViews;
import com.sample.file.views.ExcelFileViews.ExcelFileBasicView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api")
public class ExcelFileController {
	
	@Autowired
	private ExcelFileService excelFileService;
	
	@ApiOperation(value = "Upload any version of excel to database", notes = APIJSONDisplay.EXCEL_UPLOAD)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "File records saved to database."),
			@ApiResponse(code = 401, message = "You are not authorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 400, message = "Bad Request")})
	@JsonView(ExcelFileBasicView.class)
	@PostMapping("/upload/excel")
	public ExcelFileAttachment uploadExcelAttachment(@RequestParam("file") MultipartFile file,@RequestParam("filePath") String filePath){
		return excelFileService.uploadExcelFileToDB(file,filePath);
	}
	
	@ApiOperation(value = "check the progress of current uploading file")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "File records saved to the database."),
			@ApiResponse(code = 401, message = "You are not authorized"),
			@ApiResponse(code = 403, message = "Forbidden"), @ApiResponse(code = 400, message = "Bad Request") })
	@JsonView(ExcelFileBasicView.class)
	@GetMapping("/checkProgress")
	public int checkUploadProgress(@RequestParam("file") MultipartFile file) {
		return excelFileService.calculateUploadProgress(file);
	}

	@ApiOperation(value = "List all the uploaded excel files.", notes = APIJSONDisplay.EXCEL_FILES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Fetched the list of uploaded files."),
			@ApiResponse(code = 401, message = "You are not authorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 400, message = "Bad Request")})
	@JsonView(ExcelFileBasicView.class)
	@GetMapping("/files")
	public List<ExcelFileAttachment> fetchAllUploadedExcelAttachments(){		
		return excelFileService.getAllUploadedExcelFiles();
	}
	
	@ApiOperation(value = "Get excel file by id.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Fetched the list of uploaded files."),
			@ApiResponse(code = 401, message = "You are not authorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 400, message = "Bad Request")})
	@JsonView(ExcelFileBasicView.class)
	@GetMapping("/file/{id}")
	public ExcelFileAttachment getfileById(@PathVariable("id")Long id) {
		return excelFileService.geExcelFileById(id);
	}
	
	@ApiOperation(value = "Review the content of uploaded excel file.", notes = APIJSONDisplay.EXCEL_REVIEW)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Fetched the content of specific excel file."),
			@ApiResponse(code = 401, message = "You are not authorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 400, message = "Bad Request")})
	@JsonView(ExcelFileViews.ExcelFileContentView.class)
	@GetMapping("/files/{excelId}")
	public List<ExcelRecords> reviewRecordsOfExcel(@PathVariable("excelId")Long excelId){
		return excelFileService.reviewRecordsOfExcel(excelId);
	}
	
	@ApiOperation(value = "Delete specific excel file.", notes = APIJSONDisplay.EXCEL_DELETE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"),
			@ApiResponse(code = 500, message = "Internal Server Error")})
	@DeleteMapping("/excel")
	public void deleteFile(@RequestParam("fileId") Long fileId){
		excelFileService.deleteExcelFile(fileId);
	}
}
