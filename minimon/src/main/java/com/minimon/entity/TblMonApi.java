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
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="tbl_mon_api")
public class TblMonApi {

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
    private int errLoadTime;

    @NotNull
    private int payLoadPer;
    
    @NotNull
    private int useable;
    
    @NotNull
    private Date regDate;
    
    @NotNull
    private Date uptDate;

    @NotNull
	private String method;					// http method
	
    @NotNull
	private String data_type;				// 요청 데이터 타입

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "tbl_mon_api_seq")
    private List<TblMonApiParam> apiParams = new ArrayList<TblMonApiParam>();
    
    /*
     * Origin Data Info
     */
    @NotNull
    private int status;

    @NotNull
    private double loadTime;

    @NotNull
    private double payLoad;

    @Lob
	private String response;				// 검사-데이터 : 데이터
	
	public void addApiParam(TblMonApiParam tblMonApiParam) {
		this.apiParams.add(tblMonApiParam);
	}
	
	public List<TblMonApiParam> getApiParams() {
		return apiParams;
	}

	public void setApiParams(List<TblMonApiParam> apiParams) {
		this.apiParams = apiParams;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getData_type() {
		return data_type;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

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

	public void setErrLoadTime(int errLoadTime) {
		this.errLoadTime = errLoadTime;
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

	public int getErrLoadTime() {
		return errLoadTime;
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
