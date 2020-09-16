package com.minimon.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class CommonEntity {

    @CreatedDate
    @Column(updatable = false)
    @ApiModelProperty(value = "등록일", hidden = true)
    private LocalDateTime regDate;

    @LastModifiedDate
    @Column
    @ApiModelProperty(value = "수정일", hidden = true)
    private LocalDateTime uptDate;
}
