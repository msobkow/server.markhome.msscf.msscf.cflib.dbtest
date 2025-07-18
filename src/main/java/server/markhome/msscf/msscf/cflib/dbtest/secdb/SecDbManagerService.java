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
import java.util.ArrayList;
import java.util.List;

import server.markhome.msscf.msscf.cflib.dbutil.CFLibDbKeyHash256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import jakarta.persistence.NoResultException;

@Service("SecDbManagerService")
public class SecDbManagerService {

    @Autowired
    @Qualifier("secEntityManagerFactory")
    private LocalContainerEntityManagerFactoryBean secEntityManagerFactoryBean;
    
    @Autowired
    private SecDbManagerRepository secDbManagerRepository;

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "secTransactionManager")
    public SecDbUser find(CFLibDbKeyHash256 pid) {
        return secDbManagerRepository.findById(pid).orElse(null);
    }

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "secTransactionManager")
    public SecDbUser findByName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        SecDbManager probe = new SecDbManager();
        probe.setUsername(name);

        ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreNullValues()
            .withMatcher("username", ExampleMatcher.GenericPropertyMatchers.exact());

        Example<SecDbManager> example = Example.of(probe, matcher);

        return secDbManagerRepository.findOne(example).orElse(null);
    }

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "secTransactionManager")
    public List<SecDbUser> findByEmail(String email) {
        if (email == null || email.isEmpty()) {
            return new ArrayList<>();
        }
        List<SecDbManager> l = secDbManagerRepository.findByEmail(email);
        List<SecDbUser> t = new ArrayList<>(l);
        return t;
    }

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "secTransactionManager")
    public List<SecDbUser> findByMemberDeptCode(String memberDeptCode) {
        if (memberDeptCode == null || memberDeptCode.isEmpty()) {
            return new ArrayList<>();
        }
        List<SecDbManager> l = secDbManagerRepository.findByMemberDeptCode(memberDeptCode);
        List<SecDbUser> t = new ArrayList<>(l);
        return t;
    }

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "secTransactionManager")
    public List<SecDbManager> findByDeptCode(String deptCode) {
        if (deptCode == null || deptCode.isEmpty()) {
            return null;
        }
        SecDbManager probe = new SecDbManager();
        probe.setDepartmentCode(deptCode);

        ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreNullValues()
            .withMatcher("deptcode", ExampleMatcher.GenericPropertyMatchers.exact());

        Example<SecDbManager> example = Example.of(probe, matcher);

        return secDbManagerRepository.findAll(example);
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = NoResultException.class, transactionManager = "secTransactionManager")
    public SecDbManager create(SecDbManager data) {
        if (data == null) {
            return null;
        }
        CFLibDbKeyHash256 originalPid = data.getPid();
        boolean generatedPid = false;
        try {
            if (data.getPid() == null) {
                data.setPid(new CFLibDbKeyHash256(0));
                generatedPid = true;
            }
            LocalDateTime now = LocalDateTime.now();
            data.setCreatedAt(now);
            data.setUpdatedAt(now);

            // Check if already exists
            if (data.getPid() != null && secDbManagerRepository.existsById(data.getPid())) {
                return secDbManagerRepository.findById(data.getPid()).orElse(null);
            }

            return secDbManagerRepository.save(data);
        } catch (Exception e) {
            // Remove auto-generated pid if there was an error
            if (generatedPid) {
                data.setPid(originalPid);
            }
            System.err.println("ERROR: SecDbManagerService.create(data) Caught and rethrew " + e.getClass().getCanonicalName() +
                " while creating SecDbManager instance with pid: " +
                (data.getPid() != null ? data.getPid().toString() : "null") + " - " + e.getMessage());
            throw e;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = NoResultException.class, transactionManager = "secTransactionManager")
    public SecDbManager update(SecDbManager data) {
        if (data == null) {
            return null;
        }
        if (data.getPid() == null || data.getPid().isNull()) {
            throw new IllegalArgumentException("Cannot update SecDbManager with null primary identifier (pid)");
        }

        // Check if the entity exists
        SecDbManager existing = secDbManagerRepository.findById(data.getPid())
            .orElseThrow(() -> new NoResultException("SecDbManager with pid " + data.getPid() + " does not exist"));

        // Update fields (except pid, createdAt)
        existing.setUsername(data.getUsername());
        existing.setEmail(data.getEmail());
        existing.setMemberDeptCode(data.getMemberDeptCode());
        existing.setSubDepartmentOf(data.getSubDepartmentOf());
        existing.setTitle(data.getTitle());
        // ... update other fields as needed ...
        existing.setUpdatedAt(LocalDateTime.now());

        return secDbManagerRepository.save(existing);
    }
}
