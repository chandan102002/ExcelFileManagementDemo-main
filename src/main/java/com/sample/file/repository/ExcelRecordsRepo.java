package com.sample.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sample.file.model.ExcelRecords;

@Repository
public interface ExcelRecordsRepo extends JpaRepository<ExcelRecords,Long>{

}
