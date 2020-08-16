package com.minimon.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name="MON_API")
public class MonApi implements Serializable {

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
	private int loadTimeCheck;

	@NotNull
	private int payLoadCheck;

	@NotNull
	private int responseCheck;

	@NotNull
	private Date startDate;

	@NotNull
	private Date endDate;

	@NotNull
	private int startHour;

	@NotNull
	private int endHour;

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
    private List<MonApiParam> apiParams = new ArrayList<MonApiParam>();
    
    @NotNull
    private int status;

    @NotNull
    private double loadTime;

    @NotNull
    private double payLoad;

    @Lob
	private String response;				// 검사-데이터 : 데이터

}
