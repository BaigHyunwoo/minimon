package com.minimon.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="yanadoo.dbo.tbl_mon_result")
public class TblMonResult {

	@Id	
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native",strategy = "native")
    private int seq;

    @NotNull
    private String type;

    @NotNull
    private String title;
    
    @NotNull
    private int mon_seq;

    @NotNull
    private Date regDate;
    
    @NotNull
    private String result;

    @Lob
    @NotNull
    private String response;
    
    @NotNull
    private double loadTime;

    
    
    
	public double getLoadTime() {
		return loadTime;
	}

	public void setLoadTime(double loadTime) {
		this.loadTime = loadTime;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getMon_seq() {
		return mon_seq;
	}

	public void setMon_seq(int mon_seq) {
		this.mon_seq = mon_seq;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public int getSeq() {
		return seq;
	}

    
}
