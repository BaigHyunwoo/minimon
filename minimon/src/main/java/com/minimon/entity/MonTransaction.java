package com.minimon.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "MON_TRANSACTION")
public class MonTransaction extends CommonEntity {

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
    private String useable;

    @NotNull
    private int loadTimeCheck;

    @NotNull
    private String transactionCode;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    @NotNull
    private int startHour;

    @NotNull
    private int endHour;


    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "mon_transaction_seq")
    private List<MonCodeData> codeDataList = new ArrayList<MonCodeData>();

    /*
     * Origin Data Info
     */
    @NotNull
    private int status;

    @NotNull
    private double loadTime;

}
