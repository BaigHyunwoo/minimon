package com.minimon.entity;

import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "MON_RESULT")
@EntityListeners(AuditingEntityListener.class)
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
    private String result;

    @Lob
    private String response;

    @NotNull
    private double loadTime;

    @CreatedDate
    @Column(updatable = false)
    @ApiModelProperty(value = "등록일", hidden = true)
    private LocalDateTime regDate;

}
