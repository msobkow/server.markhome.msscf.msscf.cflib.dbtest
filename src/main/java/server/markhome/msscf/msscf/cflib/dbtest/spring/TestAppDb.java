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
package server.markhome.msscf.msscf.cflib.dbtest.spring;

import java.time.LocalDateTime;
import java.util.List;

import server.markhome.msscf.msscf.cflib.dbtest.appdb.AppDbAddress;
import server.markhome.msscf.msscf.cflib.dbtest.appdb.AppDbAddressService;
import server.markhome.msscf.msscf.cflib.dbutil.CFLibDbKeyHash256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

@Service("TestAppDb")
public class TestAppDb {

    @Autowired
    @Qualifier("appEntityManagerFactory")
    private LocalContainerEntityManagerFactoryBean appEntityManagerFactoryBean;

    @Autowired
    private AppDbAddressService appDbAddressService;
    
    @Transactional(value = Transactional.TxType.REQUIRES_NEW, dontRollbackOn = NoResultException.class)
    // @PersistenceContext(unitName = "AppDbPU")
    public String performTests(EntityManager em) {
        StringBuffer responseMessage = new StringBuffer();
        LocalDateTime now = LocalDateTime.now();
        // CFLibDbKeyHash256 adminpid = new CFLibDbKeyHash256("0123456789abcdef");
        CFLibDbKeyHash256 mgrpid = new CFLibDbKeyHash256("fedcba9876543210");
        List<AppDbAddress> addresses = appDbAddressService.findByRefUID(mgrpid);
        if (addresses == null || addresses.isEmpty()) {
            AppDbAddress appAddress = new AppDbAddress(new CFLibDbKeyHash256(0), mgrpid, "Home", "Mark Sobkow", "19", "207 Seventh Avenue North", null, "Yorkton", "SK", "Canada", "S3N 0X3", now, mgrpid, now, mgrpid);
            appAddress = appDbAddressService.create(appAddress);
            responseMessage.append("Sample AppDbAddress for Manager " + mgrpid.toString() + " created in AppDb.");
        } else {
            responseMessage.append("Sample AppDbAddress already exists for Manager " + mgrpid.toString() + ", or at least there isn't an empty list we can assume indicates a clean database");
        }
        return responseMessage.toString();
    }
}
