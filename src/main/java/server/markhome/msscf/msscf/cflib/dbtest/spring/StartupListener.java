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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupListener {

    @Autowired
    // @Qualifier("TestSecDb")
    private TestSecDb testSecDb;

    @Autowired
    // @Qualifier("TestAppDb")
    private TestAppDb testAppDb;

    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {

        System.err.println("Executing testSecDb.performTests()");
        try {
            String response = testSecDb.performTests(null);
            if (response != null) {
                System.err.println("TestSecDb.performTests() responded: " + response);
            }
            else {
                System.err.println("TestSecDb.performTests() did not return a response");
            }
        }
        catch (Throwable th) {
            System.err.println("testSecDb.performTests() threw " + th.getClass().getCanonicalName() + " - " + th.getMessage());
            th.printStackTrace(System.err);
        }

        System.err.println("Executing testAppDb.performTests()");
        try {
            String response = testAppDb.performTests(null);
            if (response != null) {
                System.err.println("TestAppDb.performTests() responded: " + response);
            }
            else {
                System.err.println("TestAppDb.performTests() did not return a response");
            }
        }
        catch (Throwable th) {
            System.err.println("testAppDb.performTests() threw " + th.getClass().getCanonicalName() + " - " + th.getMessage());
            th.printStackTrace(System.err);
        }

        System.err.println("DbTest StartupListener tests complete.");
    }
}
