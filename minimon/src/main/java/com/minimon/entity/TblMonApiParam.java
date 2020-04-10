package com.minimon.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="yanadoo.dbo.tbl_mon_api_param")
public class TblMonApiParam {

	@Id	
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native",strategy = "native")
    private int seq;
    
    @NotNull
    private String param_key;

    private String param_value;
    
    @NotNull
    private Date regDate;
    
    @NotNull
    private Date uptDate;

	public String getParam_key() {
		return param_key;
	}

	public void setParam_key(String param_key) {
		this.param_key = param_key;
	}

	public String getParam_value() {
		return param_value;
	}

	public void setParam_value(String param_value) {
		this.param_value = param_value;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public Date getUptDate() {
		return uptDate;
	}

	public void setUptDate(Date uptDate) {
		this.uptDate = uptDate;
	}
    
}
