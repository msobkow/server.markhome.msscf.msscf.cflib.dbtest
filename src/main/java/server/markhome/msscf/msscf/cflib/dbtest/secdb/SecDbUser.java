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

import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import org.hibernate.annotations.UpdateTimestamp;

import org.hibernate.annotations.CreationTimestamp;

import server.markhome.msscf.msscf.cflib.dbutil.CFLibDbKeyHash256;

@Entity
@Table(
    name = "sec_user", schema = "secdb",
    indexes = {
        @Index(name = "sec_user_pidx", columnList = "pid", unique = true),
        @Index(name = "sec_user_axname", columnList = "username", unique = true),
        @Index(name = "sec_user_dxemail", columnList = "email", unique = false),
        @Index(name = "sec_user_dxmbrdptcd", columnList = "member_deptcode", unique = false),
    }
)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorValue("0")
@Transactional(Transactional.TxType.SUPPORTS)
@PersistenceContext(unitName = "SecDbPU")
public class SecDbUser implements Comparable<Object> {
    public static final int USERNAME_SIZE = 64;
    public static final int EMAIL_SIZE = 1023;

    @Id
    @AttributeOverrides({
        @AttributeOverride(name = "bytes", column = @Column(name = "pid", nullable = false, unique = true, length = CFLibDbKeyHash256.HASH_LENGTH))
    })
    private CFLibDbKeyHash256 pid;

    @Column(name = "username", nullable = false, unique = true, length = USERNAME_SIZE)
    private String username;

    @Column(name = "email", nullable = false, unique = false, length = EMAIL_SIZE)
    private String email;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private java.time.LocalDateTime createdAt;

    @AttributeOverrides({
        @AttributeOverride(name = "bytes", column = @Column(name = "created_by", nullable = false, unique = false, length = CFLibDbKeyHash256.HASH_LENGTH))
    })
    private CFLibDbKeyHash256 createdBy;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private java.time.LocalDateTime updatedAt;

    @AttributeOverrides({
        @AttributeOverride(name = "bytes", column = @Column(name = "updated_by", nullable = false, unique = false, length = CFLibDbKeyHash256.HASH_LENGTH))
    })
    private CFLibDbKeyHash256 updatedBy;

    @Column(name = "member_deptcode", length = 32, nullable = true)
    private String memberDeptCode;

    public SecDbUser() {}

    public SecDbUser(CFLibDbKeyHash256 pid) {
        this.pid = pid;
        this.username = "";
        this.email = "";
    }

    public SecDbUser(CFLibDbKeyHash256 pid, SecDbUser user) {
        this.pid = pid;
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.memberDeptCode = user.getMemberDeptCode();
    }
    
    public SecDbUser(CFLibDbKeyHash256 pid, String username, String email) {
        this.pid = pid;
        this.username = username;
        this.email = email;
    }

    public SecDbUser(CFLibDbKeyHash256 pid, String username, String email, String memberDeptCode) {
        this.pid = pid;
        this.username = username;
        this.email = email;
        this.memberDeptCode = memberDeptCode;
    }

    public SecDbUser(CFLibDbKeyHash256 pid, String username, String email, String memberDeptCode,
                     java.time.LocalDateTime createdAt, CFLibDbKeyHash256 createdBy,
                     java.time.LocalDateTime updatedAt, CFLibDbKeyHash256 updatedBy) {
        this.pid = pid;
        this.username = username;
        this.email = email;
        this.memberDeptCode = memberDeptCode;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

    public CFLibDbKeyHash256 getPid() {
        return pid;
    }

    public void setPid(CFLibDbKeyHash256 pid) {
        this.pid = pid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.time.LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public CFLibDbKeyHash256 getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CFLibDbKeyHash256 createdBy) {
        this.createdBy = createdBy;
    }

    public java.time.LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(java.time.LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public CFLibDbKeyHash256 getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(CFLibDbKeyHash256 updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getMemberDeptCode() {
        return memberDeptCode;
    }

    public void setMemberDeptCode(String memberDeptCode) {
        this.memberDeptCode = memberDeptCode;
    }

    @Override
    public int compareTo(Object o) {
        if (this == o) return 0;
        if (o == null || getClass() != o.getClass()) return 1;
        SecDbUser that = (SecDbUser) o;
        int cmp = this.pid.compareTo(that.pid);
        if (cmp != 0) return cmp;
        cmp = this.username.compareTo(that.username);
        if (cmp != 0) return cmp;
        cmp = this.email.compareTo(that.email);
        if (cmp != 0) return cmp;

        // Compare memberDeptCode
        cmp = ((this.memberDeptCode == null && that.memberDeptCode == null) ? 0 :
               (this.memberDeptCode != null && that.memberDeptCode != null && this.memberDeptCode.equals(that.memberDeptCode)) ? 0 :
               (this.memberDeptCode == null ? -1 : (that.memberDeptCode == null ? 1 : this.memberDeptCode.compareTo(that.memberDeptCode))));
        return cmp;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SecDbUser that = (SecDbUser) o;
        return 0 == this.pid.compareTo(that.pid) &&
                0 == this.username.compareTo(that.username) &&
                0 == this.email.compareTo(that.email) &&
                ((this.memberDeptCode == null && that.memberDeptCode == null) ||
                 (this.memberDeptCode != null && that.memberDeptCode != null && this.memberDeptCode.equals(that.memberDeptCode)));
    }

    @Override
    public final int hashCode() {
        int hc = pid == null ? 0 : pid.hashCode();
        hc = 31 * hc + (username == null ? 0 : username.hashCode());
        hc = 31 * hc + (email == null ? 0 : email.hashCode());
        hc = 31 * hc + (memberDeptCode == null ? 0 : memberDeptCode.hashCode());
        return hc;
    }
}
