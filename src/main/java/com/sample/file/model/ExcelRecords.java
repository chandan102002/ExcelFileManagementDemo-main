package com.sample.file.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;
import com.sample.file.views.ExcelFileViews.ExcelFileContentView;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name="excel_records")
@Accessors(chain = true)
@Getter
@Setter
public class ExcelRecords implements Serializable{
	
	private static final long serialVersionUID = 3561940546883370788L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JsonView(ExcelFileContentView.class)
	private String record; 
	
	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.DETACH)
	@JoinColumn(name="excel_id", foreignKey=@ForeignKey(name="fk_t_file_records_excel_id_t_file_records_id", value=ConstraintMode.CONSTRAINT))
	private ExcelFileAttachment excel;

}
