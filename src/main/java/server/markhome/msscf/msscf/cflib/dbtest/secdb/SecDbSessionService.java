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

@Service("SecDbSessionService")
public class SecDbSessionService {

    @Autowired
    @Qualifier("secEntityManagerFactory")
    private LocalContainerEntityManagerFactoryBean secEntityManagerFactoryBean;
    
    @Autowired
    private SecDbSessionRepository secDbSessionRepository;

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "secTransactionManager")
    public SecDbSession find(CFLibDbKeyHash256 pid) {
        return secDbSessionRepository.findById(pid).orElse(null);
    }

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "secTransactionManager")
    public List<SecDbSession> findByUser(SecDbUser user) {
        if (user == null || user.getPid() == null || user.getPid().isNull()) {
            return null;
        }
        SecDbSession probe = new SecDbSession();
        probe.setSecUser(user);

        ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreNullValues()
            .withMatcher("secuser_pid", ExampleMatcher.GenericPropertyMatchers.exact());

        Example<SecDbSession> example = Example.of(probe, matcher);

        return secDbSessionRepository.findAll(example);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = NoResultException.class, transactionManager = "secTransactionManager")
    public SecDbSession create(SecDbSession data) {
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

            // Check if already exists
            if (data.getPid() != null && secDbSessionRepository.existsById(data.getPid())) {
                return secDbSessionRepository.findById(data.getPid()).orElse(null);
            }

            return secDbSessionRepository.save(data);
        } catch (Exception e) {
            // Remove auto-generated pid if there was an error
            if (generatedPid) {
                data.setPid(originalPid);
            }
            System.err.println("ERROR: SecDbSessionService.create(data) Caught and rethrew " + e.getClass().getCanonicalName() +
                " while creating SecDbSession instance with pid: " +
                (data.getPid() != null ? data.getPid().toString() : "null") + " - " + e.getMessage());
            throw e;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = NoResultException.class, transactionManager = "secTransactionManager")
    public SecDbSession update(SecDbSession data) {
        if (data == null) {
            return null;
        }
        if (data.getPid() == null || data.getPid().isNull()) {
            throw new IllegalArgumentException("Cannot update SecDbSession with null primary identifier (pid)");
        }

        // Check if the entity exists
        SecDbSession existing = secDbSessionRepository.findById(data.getPid())
            .orElseThrow(() -> new NoResultException("SecDbSession with pid " + data.getPid() + " does not exist"));

        // Update fields (except pid, createdAt)
        existing.setSessTerminationInfo(data.getSessTerminationInfo());
        existing.setTerminatedAt(data.getTerminatedAt());

        return secDbSessionRepository.save(existing);
    }
}
