package com.minimon.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="yanadoo_membership.dbo.tblMember")
public class TblModuleInfo {

    @Id
    @Column(name="idnum")
    private int idnum;
}
