package com.perceptive.epm.perkolcentral.businessobject;

import com.perceptive.epm.perkolcentral.hibernate.pojo.Licensemaster;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 25/8/12
 * Time: 9:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class LicenseBO implements Serializable {
    private String licenseTypeId;
    private String licenseTypeName;
    private String licenseTypeDescription;

    public String getLicenseTypeId() {
        return licenseTypeId;
    }

    public void setLicenseTypeId(String licenseTypeId) {
        this.licenseTypeId = licenseTypeId;
    }

    public String getLicenseTypeName() {
        return licenseTypeName;
    }

    public void setLicenseTypeName(String licenseTypeName) {
        this.licenseTypeName = licenseTypeName;
    }

    public String getLicenseTypeDescription() {
        return licenseTypeDescription;
    }

    public void setLicenseTypeDescription(String licenseTypeDescription) {
        this.licenseTypeDescription = licenseTypeDescription;
    }

    public LicenseBO(Licensemaster licensemaster){
        licenseTypeId=licensemaster.getLicenseTypeId().toString();
        licenseTypeName=licensemaster.getLicenseTypeName();
        licenseTypeDescription=licensemaster.getLicenseTypeDescription();
    }
}
