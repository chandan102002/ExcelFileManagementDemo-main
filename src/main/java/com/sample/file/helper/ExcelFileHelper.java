package com.sample.file.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import com.sample.file.model.ExcelRecords;

@Component
public class ExcelFileHelper {

	public boolean checkExcelFormat(MultipartFile file) {
		if (file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
				|| file.getContentType().equals("pplication/vnd.ms-excel.sheet.binary.macroEnabled.12")
				|| file.getContentType().equals("application/vnd.ms-excel")
				|| file.getContentType().equals("application/vnd.ms-excel.sheet.macroEnabled.12")) {
			return true;
		} else {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"Please upload only excel files.");
		}
	}
	
	public List<ExcelRecords> convertDatetoList(MultipartFile file){
		List<ExcelRecords> records= new ArrayList<>();
		try (XSSFWorkbook workBook = new XSSFWorkbook(file.getInputStream())) {
			XSSFSheet sheet = workBook.getSheetAt(0);
			
			for (int r = 0; r <= sheet.getLastRowNum(); r++) {	
				XSSFRow row = sheet.getRow(r);
				if(row !=null) {
					List<String> fileRecord = new ArrayList<>();
					ExcelRecords record = new ExcelRecords();
					for (int c = 0; c < row.getLastCellNum(); c++) {
						XSSFCell cell = row.getCell(c);
						switch (cell.getCellType()) {
						case STRING:
							if(cell.getStringCellValue() != null) {
								fileRecord.add(cell.getStringCellValue());
							}	
							break;
						case NUMERIC:
							Long longValue = (long)cell.getNumericCellValue();
							if(longValue != null) {
								fileRecord.add(longValue.toString());
							}
							break;
						case BOOLEAN:
							Boolean booleanValue = cell.getBooleanCellValue();
							fileRecord.add(booleanValue.toString());
							break;
						default:
							break;	
							
						}
					}
					record.setRecord(fileRecord.toString());
					records.add(record);
				}	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return records;
	}
}
