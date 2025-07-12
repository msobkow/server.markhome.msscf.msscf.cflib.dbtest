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

import jakarta.persistence.*;

import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import server.markhome.msscf.msscf.cflib.dbtest.secdb.SecDbUser;
import server.markhome.msscf.msscf.cflib.dbtest.secdb.SecDbUserService;
import server.markhome.msscf.msscf.cflib.dbutil.CFLibDbKeyHash256;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Table(name = "app_addr", schema = "appdb",
    indexes = {
        @Index(name = "app_addr_pidx", columnList = "pid", unique = true),
        @Index(name = "app_addr_axname", columnList = "refuid,addrname", unique = true),
    }
)
@PersistenceContext(unitName = "AppDbPU")
public class AppDbAddress implements Comparable<Object> {
    public static final int ADDR_NAME = 24;
    public static final int ADDR_CONTACT = 64;
    public static final int ADDR_APARTMENT = 16;
    public static final int ADDR_STREET = 64;
    public static final int ADDR_STREET2 = 64;
    public static final int ADDR_CITY = 64;
    public static final int ADDR_PROVINCE = 32;
    public static final int ADDR_COUNTRY = 32;
    public static final int ADDR_POSTAL_CODE = 16;

    @Id
    @AttributeOverrides({
        @AttributeOverride(name = "bytes", column = @Column(name = "pid", nullable = false, unique = true, length = CFLibDbKeyHash256.HASH_LENGTH))
    })
    private CFLibDbKeyHash256 pid;

    @AttributeOverrides({
        @AttributeOverride(name = "bytes", column = @Column(name = "refuid", nullable = false, unique = false, length = CFLibDbKeyHash256.HASH_LENGTH))
    })
    private CFLibDbKeyHash256 refUID;

    @Column(name = "addrname", nullable = false, unique = true, length = ADDR_NAME)
    private String addressName;

    @Column(name = "addrcontact", nullable = true, unique = false, length = ADDR_CONTACT)
    private String addressContact;

    @Column(name = "addrapt", nullable = true, unique = false, length = ADDR_APARTMENT)
    private String addressApartment;

    @Column(name = "addrstreet", nullable = true, unique = false, length = ADDR_STREET)
    private String addressStreet;

    @Column(name = "addrstreet2", nullable = true, unique = false, length = ADDR_STREET2)
    private String addressStreet2;

    @Column(name = "addrcity", nullable = true, unique = false, length = ADDR_CITY)
    private String addressCity;

    @Column(name = "addrprovince", nullable = true, unique = false, length = ADDR_PROVINCE)
    private String addressProvince;

    @Column(name = "addrcountry", nullable = true, unique = false, length = ADDR_COUNTRY)
    private String addressCountry;

    @Column(name = "addrpostalcode", nullable = true, unique = false, length = ADDR_POSTAL_CODE)
    private String addressPostalCode;

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

    @Transient
    @Autowired
    private transient SecDbUserService secDbUserService;

    public AppDbAddress() {}

    public AppDbAddress(CFLibDbKeyHash256 pid) {
        this.pid = pid;
        this.refUID = null;
        this.addressName = "NameInSetOfAddresses";
        this.addressContact = null;
        this.addressApartment = null;
        this.addressStreet = null;
        this.addressStreet2 = null;
        this.addressCity = null;
        this.addressProvince = null;
        this.addressCountry = null;
        this.addressPostalCode = null;
        this.createdAt = LocalDateTime.now();
        this.createdBy = null;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = null;
    }

    public AppDbAddress(CFLibDbKeyHash256 pid, CFLibDbKeyHash256 refUID, String addressName) {
        this.pid = pid;
        this.refUID = refUID;
        this.addressName = addressName;
        this.addressContact = null;
        this.addressApartment = null;
        this.addressStreet = null;
        this.addressStreet2 = null;
        this.addressCity = null;
        this.addressProvince = null;
        this.addressCountry = null;
        this.addressPostalCode = null;
        this.createdAt = LocalDateTime.now();
        this.createdBy = null;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = null;
    }

    public AppDbAddress(CFLibDbKeyHash256 pid, CFLibDbKeyHash256 refUID, String addressName, String addressContact, String addressApartment,
        String addressStreet, String addressStreet2, String addressCity, String addressProvince, String addressCountry, String addressPostalCode) {
        this.pid = pid;
        this.refUID = refUID;
        this.addressName = addressName;
        this.addressContact = addressContact;
        this.addressApartment = addressApartment;
        this.addressStreet = addressStreet;
        this.addressStreet2 = addressStreet2;
        this.addressCity = addressCity;
        this.addressProvince = addressProvince;
        this.addressCountry = addressCountry;
        this.addressPostalCode = addressPostalCode;
        this.createdAt = LocalDateTime.now();
        this.createdBy = null;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = null;
    }

    public AppDbAddress(CFLibDbKeyHash256 pid, CFLibDbKeyHash256 refUID, String addressName, String addressContact, String addressApartment,
        String addressStreet, String addressStreet2, String addressCity, String addressProvince, String addressCountry, String addressPostalCode,
        java.time.LocalDateTime createdAt, CFLibDbKeyHash256 createdBy, java.time.LocalDateTime updatedAt, CFLibDbKeyHash256 updatedBy) {

        this.pid = pid;
        this.refUID = refUID;
        this.addressName = addressName;
        this.addressContact = addressContact;
        this.addressApartment = addressApartment;
        this.addressStreet = addressStreet;
        this.addressStreet2 = addressStreet2;
        this.addressCity = addressCity;
        this.addressProvince = addressProvince;
        this.addressCountry = addressCountry;
        this.addressPostalCode = addressPostalCode;
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

    public CFLibDbKeyHash256 getRefUID() {
        return refUID;
    }

    public void setRefUID(CFLibDbKeyHash256 refUID) {
        if (refUID == null || refUID.isNull()) {
            throw new IllegalArgumentException("refUID cannot be null");
        }
        this.refUID = refUID;
    }

    public SecDbUser getUser() {
        if (refUID == null || refUID.isNull()) {
            return null;
        }
        else {
            SecDbUser user = secDbUserService.find(refUID);
            if (user == null) {
                throw new IllegalStateException("AppDbAddress.getUser() could not resolve refUID " + refUID.asString() + " to an existing SecDbUser");
            }
            else {
                return user;
            }
        }
    }

    public void setUser(SecDbUser user) {
        if (user == null || user.getPid() == null || user.getPid().isNull()) {
            throw new IllegalArgumentException("AppDbAddress.setUser() user cannot be null and must be persisted");
        }
        refUID = user.getPid();
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        if (addressName == null || addressName.isEmpty()) {
            throw new IllegalArgumentException("addressName cannot be null or empty");
        }
        if (addressName.length() > ADDR_NAME) {
            throw new IllegalArgumentException("addressName exceeds maximum length of " + ADDR_NAME);
        }
        this.addressName = addressName;
    }

    public String getAddressContact() {
        return addressContact;
    }

    public void setAddressContact(String addressContact) {
        if (addressContact != null && addressContact.isEmpty()) {
            throw new IllegalArgumentException("addressContact cannot be empty if provided");
        }
        if (addressContact != null && addressContact.length() > ADDR_CONTACT) {
            throw new IllegalArgumentException("addressContact exceeds maximum length of " + ADDR_CONTACT);
        }
        this.addressContact = addressContact;
    }

    public String getAddressApartment() {
        return addressApartment;
    }

    public void setAddressApartment(String addressApartment) {
        if (addressApartment != null && addressApartment.isEmpty()) {
            throw new IllegalArgumentException("addressApartment cannot be empty if provided");
        }
        if (addressApartment != null && addressApartment.length() > ADDR_APARTMENT) {
            throw new IllegalArgumentException("addressApartment exceeds maximum length of " + ADDR_APARTMENT);
        }
        this.addressApartment = addressApartment;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        if (addressStreet != null && addressStreet.isEmpty()) {
            throw new IllegalArgumentException("addressStreet cannot be empty if provided");
        }
        if (addressStreet != null && addressStreet.length() > ADDR_STREET) {
            throw new IllegalArgumentException("addressStreet exceeds maximum length of " + ADDR_STREET);
        }
        this.addressStreet = addressStreet;
    }

    public String getAddressStreet2() {
        return addressStreet2;
    }

    public void setAddressStreet2(String addressStreet2) {
        if (addressStreet2 != null && addressStreet2.isEmpty()) {
            throw new IllegalArgumentException("addressStreet2 cannot be empty if provided");
        }
        if (addressStreet2 != null && addressStreet2.length() > ADDR_STREET2) {
            throw new IllegalArgumentException("addressStreet2 exceeds maximum length of " + ADDR_STREET2);
        }
        this.addressStreet2 = addressStreet2;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        if (addressCity != null && addressCity.isEmpty()) {
            throw new IllegalArgumentException("addressCity cannot be empty if provided");
        }
        if (addressCity != null && addressCity.length() > ADDR_CITY) {
            throw new IllegalArgumentException("addressCity exceeds maximum length of " + ADDR_CITY);
        }
        this.addressCity = addressCity;
    }

    public String getAddressProvince() {
        return addressProvince;
    }

    public void setAddressProvince(String addressProvince) {
        if (addressProvince != null && addressProvince.isEmpty()) {
            throw new IllegalArgumentException("addressProvince cannot be empty if provided");
        }
        if (addressProvince != null && addressProvince.length() > ADDR_PROVINCE) {
            throw new IllegalArgumentException("addressProvince exceeds maximum length of " + ADDR_PROVINCE);
        }
        this.addressProvince = addressProvince;
    }

    public String getAddressCountry() {
        return addressCountry;
    }

    public void setAddressCountry(String addressCountry) {
        if (addressCountry != null && addressCountry.isEmpty()) {
            throw new IllegalArgumentException("addressCountry cannot be empty if provided");
        }
        if (addressCountry != null && addressCountry.length() > ADDR_COUNTRY) {
            throw new IllegalArgumentException("addressCountry exceeds maximum length of " + ADDR_COUNTRY);
        }
        this.addressCountry = addressCountry;
    }

    public String getAddressPostalCode() {
        return addressPostalCode;
    }

    public void setAddressPostalCode(String addressPostalCode) {
        if (addressPostalCode != null && addressPostalCode.isEmpty()) {
            throw new IllegalArgumentException("addressPostalCode cannot be empty if provided");
        }
        if (addressPostalCode != null && addressPostalCode.length() > ADDR_POSTAL_CODE) {
            throw new IllegalArgumentException("addressPostalCode exceeds maximum length of " + ADDR_POSTAL_CODE);
        }
        this.addressPostalCode = addressPostalCode;
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

    @Override
    public int compareTo(Object o) {
        if (this == o) return 0;
        if (o == null || getClass() != o.getClass()) return 1;
        AppDbAddress that = (AppDbAddress) o;
        int cmp = this.pid.compareTo(that.pid);
        if (cmp != 0) return cmp;
        cmp = this.refUID == null ? (that.refUID == null ? 0 : -1) : (that.refUID == null ? 1 : this.refUID.compareTo(that.refUID));
        if (cmp != 0) return cmp;
        cmp = this.addressName.compareTo(that.addressName);
        if (cmp != 0) return cmp;
        cmp = this.addressContact == null ? (that.addressContact == null ? 0 : -1) : (that.addressContact == null ? 1 : this.addressContact.compareTo(that.addressContact));
        if (cmp != 0) return cmp;
        cmp = this.addressApartment == null ? (that.addressApartment == null ? 0 : -1) : (that.addressApartment == null ? 1 : this.addressApartment.compareTo(that.addressApartment));
        if (cmp != 0) return cmp;
        cmp = this.addressStreet == null ? (that.addressStreet == null ? 0 : -1) : (that.addressStreet == null ? 1 : this.addressStreet.compareTo(that.addressStreet));
        if (cmp != 0) return cmp;
        cmp = this.addressStreet2 == null ? (that.addressStreet2 == null ? 0 : -1) : (that.addressStreet2 == null ? 1 : this.addressStreet2.compareTo(that.addressStreet2));
        if (cmp != 0) return cmp;
        cmp = this.addressCity == null ? (that.addressCity == null ? 0 : -1) : (that.addressCity == null ? 1 : this.addressCity.compareTo(that.addressCity));
        if (cmp != 0) return cmp;
        cmp = this.addressProvince == null ? (that.addressProvince == null ? 0 : -1) : (that.addressProvince == null ? 1 : this.addressProvince.compareTo(that.addressProvince));
        if (cmp != 0) return cmp;
        cmp = this.addressCountry == null ? (that.addressCountry == null ? 0 : -1) : (that.addressCountry == null ? 1 : this.addressCountry.compareTo(that.addressCountry));
        if (cmp != 0) return cmp;
        cmp = this.addressPostalCode == null ? (that.addressPostalCode == null ? 0 : -1) : (that.addressPostalCode == null ? 1 : this.addressPostalCode.compareTo(that.addressPostalCode));
        return cmp;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppDbAddress that = (AppDbAddress) o;
        return 0 == this.pid.compareTo(that.pid) &&
               (this.refUID == null ? that.refUID == null : this.refUID.equals(that.refUID)) &&
               0 == this.addressName.compareTo(that.addressName) &&
               (this.addressContact == null ? that.addressContact == null : this.addressContact.equals(that.addressContact)) &&
               (this.addressApartment == null ? that.addressApartment == null : this.addressApartment.equals(that.addressApartment)) &&
               (this.addressStreet == null ? that.addressStreet == null : this.addressStreet.equals(that.addressStreet)) &&
               (this.addressStreet2 == null ? that.addressStreet2 == null : this.addressStreet2.equals(that.addressStreet2)) &&
               (this.addressCity == null ? that.addressCity == null : this.addressCity.equals(that.addressCity)) &&
               (this.addressProvince == null ? that.addressProvince == null : this.addressProvince.equals(that.addressProvince)) &&
               (this.addressCountry == null ? that.addressCountry == null : this.addressCountry.equals(that.addressCountry)) &&
               (this.addressPostalCode == null ? that.addressPostalCode == null : this.addressPostalCode.equals(that.addressPostalCode));
    }

    @Override
    public final int hashCode() {
        int hc = pid == null ? 0 : pid.hashCode();
        hc = 31 * hc + (refUID == null ? 0 : refUID.hashCode());
        hc = 31 * hc + (addressName == null ? 0 : addressName.hashCode());
        hc = 31 * hc + (addressContact == null ? 0 : addressContact.hashCode());
        hc = 31 * hc + (addressApartment == null ? 0 : addressApartment.hashCode());
        hc = 31 * hc + (addressStreet == null ? 0 : addressStreet.hashCode());
        hc = 31 * hc + (addressStreet2 == null ? 0 : addressStreet2.hashCode());
        hc = 31 * hc + (addressCity == null ? 0 : addressCity.hashCode());
        hc = 31 * hc + (addressProvince == null ? 0 : addressProvince.hashCode());
        hc = 31 * hc + (addressCountry == null ? 0 : addressCountry.hashCode());
        hc = 31 * hc + (addressPostalCode == null ? 0 : addressPostalCode.hashCode());
        return hc;
    }
}
