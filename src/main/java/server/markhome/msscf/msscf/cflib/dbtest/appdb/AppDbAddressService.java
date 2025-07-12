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
package server.markhome.msscf.msscf.cflib.dbtest.appdb;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import server.markhome.msscf.msscf.cflib.dbtest.secdb.SecDbUser;
import server.markhome.msscf.msscf.cflib.dbtest.secdb.SecDbUserService;
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

@Service("AppDbAddressService")
public class AppDbAddressService {

    @Autowired
    @Qualifier("appEntityManagerFactory")
    private LocalContainerEntityManagerFactoryBean appEntityManagerFactory;
    
    @Autowired
    private AppDbAddressRepository appDbAddressRepository;

    @Autowired
    private SecDbUserService secDbUserService;

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "appTransactionManager")
    public AppDbAddress find(CFLibDbKeyHash256 pid) {
        return appDbAddressRepository.findById(pid).orElse(null);
    }

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "appTransactionManager")
    public List<AppDbAddress> findByRefUID(CFLibDbKeyHash256 refUID) {
        if (refUID == null || refUID.isNull()) {
            return new ArrayList<>();
        }
        return appDbAddressRepository.findByRefUID(refUID);
    }

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "appTransactionManager")
    public List<AppDbAddress> findByUser(SecDbUser user) {
        if (user == null || user.getPid() == null || user.getPid().isNull()) {
            return new ArrayList<>();
        }
        return appDbAddressRepository.findByRefUID(user.getPid());
    }

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "appTransactionManager")
    public AppDbAddress findByRefUIDName(CFLibDbKeyHash256 refUID, String addressName) {
        if (refUID == null || refUID.isNull() || addressName == null || addressName.isEmpty()) {
            return null;
        }
        AppDbAddress probe = new AppDbAddress();
        probe.setRefUID(refUID);
        probe.setAddressName(addressName);

        ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreNullValues()
            .withMatcher("refuid, addrname", ExampleMatcher.GenericPropertyMatchers.exact());

        Example<AppDbAddress> example = Example.of(probe, matcher);

        return appDbAddressRepository.findOne(example).orElse(null);
    }

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NoResultException.class, transactionManager = "appTransactionManager")
    public AppDbAddress findByUserName(SecDbUser user, String addressName) {
        if (user == null || user.getPid() == null || user.getPid().isNull() || addressName == null || addressName.isEmpty()) {
            return null;
        }
        AppDbAddress probe = new AppDbAddress();
        probe.setRefUID(user.getPid());
        probe.setAddressName(addressName);

        ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreNullValues()
            .withMatcher("refuid, addrname", ExampleMatcher.GenericPropertyMatchers.exact());

        Example<AppDbAddress> example = Example.of(probe, matcher);

        return appDbAddressRepository.findOne(example).orElse(null);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = NoResultException.class, transactionManager = "appTransactionManager")
    public AppDbAddress create(AppDbAddress data) {
        if (data == null) {
            return null;
        }
        if (data.getRefUID() == null || data.getRefUID().isNull()) {
            throw new IllegalArgumentException("AppDbAddressService.create() Cannot create data without a valid RefUID");
        }
        SecDbUser user = secDbUserService.find(data.getRefUID());
        if (user == null) {
            throw new IllegalArgumentException("AppDbAddressService.create() RefUID " + data.getRefUID().asString() + " does not reference an existing SecDbUser");
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
            if (data.getPid() != null && appDbAddressRepository.existsById(data.getPid())) {
                return appDbAddressRepository.findById(data.getPid()).orElse(null);
            }

            return appDbAddressRepository.save(data);
        } catch (Exception e) {
            // Remove auto-generated pid if there was an error
            if (generatedPid) {
                data.setPid(null);
            }
            System.err.println("ERROR: AppDbAddressService.create(data) Caught and rethrew " + e.getClass().getCanonicalName() +
                " while creating AppDbAddress instance with pid: " +
                (data.getPid() != null ? data.getPid().asString() : "null") + " - " + e.getMessage());
            throw e;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = NoResultException.class, transactionManager = "appTransactionManager")
    public AppDbAddress update(AppDbAddress data) {
        if (data == null) {
            return null;
        }
        if (data.getPid() == null || data.getPid().isNull()) {
            throw new IllegalArgumentException("Cannot update AppDbAddress with null primary identifier (pid)");
        }
        if (data.getRefUID() == null || data.getRefUID().isNull()) {
            throw new IllegalArgumentException("AppDbAddressService.update() Cannot update data without a valid RefUID");
        }
        SecDbUser user = secDbUserService.find(data.getRefUID());
        if (user == null) {
            throw new IllegalArgumentException("AppDbAddressService.update() RefUID " + data.getRefUID().asString() + " does not reference an existing SecDbUser");
        }

        // Check if the entity exists
        AppDbAddress existing = appDbAddressRepository.findById(data.getPid())
            .orElseThrow(() -> new NoResultException("AppDbAddress with pid " + data.getPid() + " does not exist"));

        // Update fields (except pid, createdAt)
        existing.setRefUID(data.getRefUID());
        existing.setAddressName(data.getAddressName());
        existing.setAddressApartment(data.getAddressApartment());
        existing.setAddressCity(data.getAddressCity());
        existing.setAddressContact(data.getAddressContact());
        existing.setAddressCountry(data.getAddressCountry());
        existing.setAddressPostalCode(data.getAddressPostalCode());
        existing.setAddressProvince(data.getAddressProvince());
        existing.setAddressStreet(data.getAddressStreet());
        existing.setAddressStreet2(data.getAddressStreet2());

        // ... update other fields as needed ...
        existing.setUpdatedAt(LocalDateTime.now());

        return appDbAddressRepository.save(existing);
    }
}
