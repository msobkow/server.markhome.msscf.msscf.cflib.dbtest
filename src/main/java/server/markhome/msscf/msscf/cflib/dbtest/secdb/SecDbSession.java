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

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import server.markhome.msscf.msscf.cflib.dbutil.CFLibDbKeyHash256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;

@Entity
@Table(name = "sec_sess", schema = "secdb")
@Transactional(Transactional.TxType.SUPPORTS)
@PersistenceContext(unitName = "SecDbPU")
public class SecDbSession {
    public final static int SESS_CREATE_INFO_LEN = 1024;
    public final static int SESS_TERMINATION_INFO_LEN = 1024;

    @Id
    @AttributeOverrides({
        @AttributeOverride(name = "bytes", column = @Column(name = "pid", nullable = false, unique = true, length = CFLibDbKeyHash256.HASH_LENGTH))
    })
    private CFLibDbKeyHash256 pid;

    @ManyToOne(fetch = FetchType.LAZY)
    @AttributeOverrides({
        @AttributeOverride(name = "bytes", column = @Column(name = "secuser_pid", nullable = false, unique = false, length = CFLibDbKeyHash256.HASH_LENGTH))
    })
    private SecDbUser secUser;

    @Column(name = "sess_cr_info", nullable = false, updatable = false, length = SESS_CREATE_INFO_LEN)
    private String sessCreateInfo;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "sess_term_info", nullable = true, updatable = true, length = SESS_TERMINATION_INFO_LEN)
    private String sessTerminationInfo;

    @Column(name = "terminated_at", nullable = true, updatable = true)
    private LocalDateTime terminatedAt;

    @Autowired
    @Qualifier("secEntityManagerFactory")
    private static EntityManagerFactory secEntityManagerFactory;
  
    public SecDbSession() {}

    public SecDbSession(CFLibDbKeyHash256 pid, SecDbUser secUser) {
        this.pid = pid;
        this.secUser = secUser;
    }

    public SecDbSession(CFLibDbKeyHash256 pid, SecDbUser secUser, String sessCreateInfo) {
        this.pid = pid;
        this.secUser = secUser;
        this.sessCreateInfo = sessCreateInfo;
        this.createdAt = LocalDateTime.now();
        this.sessTerminationInfo = null;
        this.terminatedAt = null;
    }

    public SecDbSession(CFLibDbKeyHash256 pid, SecDbUser secUser, String sessCreateInfo, LocalDateTime createdAt) {
        this.pid = pid;
        this.secUser = secUser;
        this.sessCreateInfo = sessCreateInfo;
        this.createdAt = createdAt;
        this.sessTerminationInfo = null;
        this.terminatedAt = null;
    }

    public SecDbSession(CFLibDbKeyHash256 pid, SecDbUser secUser, String sessCreateInfo, LocalDateTime createdAt, String sessTerminationInfo, LocalDateTime terminatedAt) {
        this.pid = pid;
        this.secUser = secUser;
        this.sessCreateInfo = sessCreateInfo;
        this.createdAt = createdAt;
        this.sessTerminationInfo = sessTerminationInfo;
        this.terminatedAt = terminatedAt;
    }

    public CFLibDbKeyHash256 getPid() {
        return pid;
    }

    public void setPid(CFLibDbKeyHash256 pid) {
        this.pid = pid;
    }

    public SecDbUser getSecUser() {
        return secUser;
    }

    public void setSecUser(SecDbUser secUser) {
        this.secUser = secUser;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getSessTerminationInfo() {
        return sessTerminationInfo;
    }

    public void setSessTerminationInfo(String sessTerminationInfo) {
        this.sessTerminationInfo = sessTerminationInfo;
    }

    public LocalDateTime getTerminatedAt() {
        return terminatedAt;
    }

    public void setTerminatedAt(LocalDateTime terminatedAt) {
        this.terminatedAt = terminatedAt;
    }
}
