package com.minimon.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "tbl_mon_result")
public class MonResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
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
    private String response;

    @NotNull
    private double loadTime;

}
