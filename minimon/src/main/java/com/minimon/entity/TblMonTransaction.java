package com.minimon.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="tbl_mon_transaction")
public class TblMonTransaction {

	/*
	 * Setting Info
	 */
	@Id	
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native",strategy = "native")
    private int seq;

    @NotNull
    private String title;
    
    @NotNull
    private int timer;

    @NotNull
    private int timeout;

    @NotNull
    private int errLoadTime;

    @NotNull
    private int useable;
    
    @NotNull
    private Date regDate;
    
    @NotNull
    private Date uptDate;
    
    @NotNull
    private String transactionCode;
    

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "tbl_mon_transaction_seq")
    private List<TblMonCodeData> codeDatas = new ArrayList<TblMonCodeData>();
    
    /*
     * Origin Data Info
     */
    @NotNull
    private int status;

    @NotNull
    private double loadTime;

    
	public List<TblMonCodeData> getCodeDatas() {
		return codeDatas;
	}

	public void setCodeDatas(List<TblMonCodeData> codeDatas) {
		this.codeDatas = codeDatas;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setErrLoadTime(int errLoadTime) {
		this.errLoadTime = errLoadTime;
	}

	public void setUseable(int useable) {
		this.useable = useable;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setLoadTime(double loadTime) {
		this.loadTime = loadTime;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public void setUptDate(Date uptDate) {
		this.uptDate = uptDate;
	}

	public Date getRegDate() {
		return regDate;
	}

	public Date getUptDate() {
		return uptDate;
	}

	public int getUseable() {
		return useable;
	}

	public int getSeq() {
		return seq;
	}

	public String getTitle() {
		return title;
	}

	public int getTimer() {
		return timer;
	}

	public int getTimeout() {
		return timeout;
	}

	public int getErrLoadTime() {
		return errLoadTime;
	}

	public int getStatus() {
		return status;
	}

	public double getLoadTime() {
		return loadTime;
	}
    
}
