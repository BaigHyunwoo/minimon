package com.minimon.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tbl_mon_transaction")
public class MonTransaction {

    /*
     * Setting Info
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
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
    private int loadTimeCheck;

    @NotNull
    private Date regDate;

    @NotNull
    private Date uptDate;

    @NotNull
    private String transactionCode;

    @NotNull
    private Date startDate;

    @NotNull
    private Date endDate;

    @NotNull
    private int startHour;

    @NotNull
    private int endHour;


    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "tbl_mon_transaction_seq")
    private List<MonCodeData> codeDatas = new ArrayList<MonCodeData>();

    /*
     * Origin Data Info
     */
    @NotNull
    private int status;

    @NotNull
    private double loadTime;

}
