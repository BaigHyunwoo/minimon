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
@Table(name="yanadoo.dbo.tbl_mon_url")
public class TblMonUrl {

	/*
	 * Setting Info
	 */
	@Id	
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native",strategy = "native")
    private int seq;

    @NotNull
    private String url;

    @NotNull
    private String title;
    
    @NotNull
    private int timer;

    @NotNull
    private int timeout;

    @NotNull
    private int loadTimePer;

    @NotNull
    private int payLoadPer;
    
    @NotNull
    private int useable;
    
    @NotNull
    private Date regDate;
    
    @NotNull
    private Date uptDate;
    
    
    /*
     * Origin Data Info
     */
    @NotNull
    private int status;

    @NotNull
    private double loadTime;

    @NotNull
    private double payLoad;
    
    
	public void setUrl(String url) {
		this.url = url;
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

	public void setLoadTimePer(int loadTimePer) {
		this.loadTimePer = loadTimePer;
	}

	public void setPayLoadPer(int payLoadPer) {
		this.payLoadPer = payLoadPer;
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

	public void setPayLoad(double payLoad) {
		this.payLoad = payLoad;
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

	public String getUrl() {
		return url;
	}

	public int getTimer() {
		return timer;
	}

	public int getTimeout() {
		return timeout;
	}

	public int getLoadTimePer() {
		return loadTimePer;
	}

	public int getPayLoadPer() {
		return payLoadPer;
	}

	public int getStatus() {
		return status;
	}

	public double getLoadTime() {
		return loadTime;
	}

	public double getPayLoad() {
		return payLoad;
	}
    
    
}
