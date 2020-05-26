package com.minimon.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="tbl_mon_code_data")
public class TblMonCodeData {

	@Id	
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native",strategy = "native")
    private int seq;
    
    @NotNull
    private String action;

    private String selector_type;
    
    private String selector_value;

    private String value;
    
	public String getSelector_type() {
		return selector_type;
	}

	public void setSelector_type(String selector_type) {
		this.selector_type = selector_type;
	}

	public String getSelector_value() {
		return selector_value;
	}

	public void setSelector_value(String selector_value) {
		this.selector_value = selector_value;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getSeq() {
		return seq;
	}
    
    
}
