/*
 *	MSS Code Factory CFLib DbTest
 *
 *	Copyright (c) 2025 Mark Stephen Sobkow
 *
 *	This file is part of MSS Code Factory 3.0.
 *
 *	MSS Code Factory 3.0 is free software: you can redistribute it and/or modify
 *	it under the terms of the Apache v2.0 License as published by the Apache Foundation.
 *
 *	MSS Code Factory 3.0 is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 *	You should have received a copy of the Apache v2.0 License along with
 *	MSS Code Factory.  If not, see https://www.apache.org/licenses/LICENSE-2.0
 *
 *	Contact Mark Stephen Sobkow at mark.sobkow@gmail.com for commercial licensing or
 *  customization.
 */
package server.markhome.msscf.msscf.cflib.dbtest.secdb;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;

import java.util.Set;
import java.util.HashSet;

import server.markhome.msscf.msscf.cflib.dbutil.CFLibDbKeyHash256;

@Entity
@DiscriminatorValue("1")
@Table(
    name = "sec_mgr", schema = "secdb",
    indexes = {
        @Index(name = "sec_mgr_deptcode_ax", columnList = "deptcode", unique = true)
    }
)
@Transactional(Transactional.TxType.SUPPORTS)
@PersistenceContext(unitName = "SecDbPU")
public class SecDbManager extends SecDbUser {
    
    public static final int TITLE_SIZE = 64;
    public static final int DEPARTMENT_CODE_SIZE = 32;

    @Column(name = "title", length = TITLE_SIZE, nullable = false)
    private String title = "";

    @Column(name = "deptcode", length = DEPARTMENT_CODE_SIZE, nullable = false, unique = true)
    private String departmentCode = "";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subdeptof")
    @AttributeOverrides({
        @AttributeOverride(name = "bytes", column = @Column(name = "subdeptof", nullable = false, unique = false, length = CFLibDbKeyHash256.HASH_LENGTH))
    })
    private SecDbManager subDepartmentOf;

    @OneToMany(mappedBy = "subDepartmentOf", fetch = FetchType.LAZY)
    private Set<SecDbManager> departments = new HashSet<>();

    public SecDbManager() {
        super();
    }

    public SecDbManager(CFLibDbKeyHash256 pid) {
        super(pid);
        this.title = "";
        this.departmentCode = "";
    }

    public SecDbManager(CFLibDbKeyHash256 pid, SecDbUser user) {
        super(pid, user.getUsername(), user.getEmail());
        this.title = "";
        this.departmentCode = "";
    }

    public SecDbManager(CFLibDbKeyHash256 pid, String username, String email) {
        super(pid, username, email);
        this.title = "";
        this.departmentCode = "";
    }

    public SecDbManager(CFLibDbKeyHash256 pid, String username, String email, String title, String departmentCode) {
        super(pid, username, email);
        this.title = title;
        this.departmentCode = departmentCode;
    }

    public SecDbManager(CFLibDbKeyHash256 pid, String username, String email, String title, String departmentCode, SecDbManager subDepartmentOf) {
        super(pid, username, email);
        this.title = title;
        this.departmentCode = departmentCode;
        this.subDepartmentOf = subDepartmentOf;
    }

    public SecDbManager(CFLibDbKeyHash256 pid, String username, String email, String title, String departmentCode, SecDbManager subDepartmentOf, Set<SecDbManager> departments) {
        super(pid, username, email);
        this.title = title;
        this.departmentCode = departmentCode;
        this.subDepartmentOf = subDepartmentOf;
        this.departments = departments != null ? departments : new HashSet<>();
    }

    public SecDbManager(CFLibDbKeyHash256 pid, String username, String email, String memberDeptCode, String title, String departmentCode,
                          SecDbManager subDepartmentOf, Set<SecDbManager> departments,
                          java.time.LocalDateTime createdAt, CFLibDbKeyHash256 createdBy,
                          java.time.LocalDateTime updatedAt, CFLibDbKeyHash256 updatedBy) {
        super(pid, username, email, memberDeptCode, createdAt, createdBy, updatedAt, updatedBy);
        this.title = title;
        this.departmentCode = departmentCode;
        this.subDepartmentOf = subDepartmentOf;
        this.departments = departments != null ? departments : new HashSet<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public SecDbManager getSubDepartmentOf() {
        return subDepartmentOf;
    }

    public void setSubDepartmentOf(SecDbManager subDepartmentOf) {
        this.subDepartmentOf = subDepartmentOf;
    }

    public Set<SecDbManager> getDepartments() {
        return departments;
    }

    public void setDepartments(Set<SecDbManager> departments) {
        this.departments = departments;
    }

    public void addDepartment(SecDbManager department) {
        if (department != null) {
            departments.add(department);
            department.setSubDepartmentOf(this);
        }
    }

    public void removeDepartment(SecDbManager department) {
        if (department != null) {
            departments.remove(department);
            department.setSubDepartmentOf(null);
        }
    }

    @Override
    public int compareTo(Object o) {
        if (this == o) return 0;
        if (!(o instanceof SecDbManager)) return -1;
        SecDbManager other = (SecDbManager) o;
        int cmp = super.compareTo(other);
        if (cmp != 0) return cmp;
        cmp = (this.title == null && other.title == null) ? 0 : ((this.title != null && this.title.equals(other.title)) ? 0 : (this.title == null ? -1 : (other.title == null ? 1 : this.title.compareTo(other.title))));
        if (cmp != 0) return cmp;
        cmp = (this.departmentCode == null && other.departmentCode == null) ? 0 : ((this.departmentCode != null && this.departmentCode.equals(other.departmentCode)) ? 0 : (this.departmentCode == null ? -1 : (other.departmentCode == null ? 1 : this.departmentCode.compareTo(other.departmentCode))));
        if (cmp != 0) return cmp;
        cmp = (this.subDepartmentOf == null && other.subDepartmentOf == null) ? 0 : ((this.subDepartmentOf != null && other.subDepartmentOf != null && this.subDepartmentOf.getPid().equals((other.subDepartmentOf.getPid()))) ? 0 : (this.subDepartmentOf == null ? -1 : (other.subDepartmentOf == null ? 1 : this.subDepartmentOf.getPid().compareTo(other.subDepartmentOf.getPid()))));
        return cmp;
    }

}
