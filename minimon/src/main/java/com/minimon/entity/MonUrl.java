package com.minimon.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "MON_URL")
public class MonUrl implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
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
    private int textCheck;

    private String textCheckValue;

    @NotNull
    private int status;

    @NotNull
    private double loadTime;

    @NotNull
    private double payLoad;

}
