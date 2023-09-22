package com.sample.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sample.file.model.ExcelFileAttachment;

@Repository
public interface ExcelFileRepository extends JpaRepository<ExcelFileAttachment,Long>{
	
	@Transactional
	@Query(value="select * from excel_file_attachment f where f.file_path = :filePath", nativeQuery = true)
	ExcelFileAttachment findByFileName(@Param("filePath") String filePath);
}
