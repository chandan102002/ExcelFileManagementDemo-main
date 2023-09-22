package com.sample.file.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;
import com.sample.file.views.ExcelFileViews.ExcelFileBasicView;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name="excel_file_attachment")
@Accessors(chain = true)
@Getter
@Setter
public class ExcelFileAttachment implements Serializable{
	
	private static final long serialVersionUID = 1042599793692653631L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(ExcelFileBasicView.class)
	private Long id;
	
	private String fileName;
	
	private String fileType;
	
	@JsonView(ExcelFileBasicView.class)
	private String filePath;
	
	private Date lastAccesssTime;
	
	private String reviewedBy;
	
	@OneToMany(mappedBy="excel",cascade=CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval=true)
	private List<ExcelRecords> records;

}
