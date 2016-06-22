package com.perceptive.epm.perkolcentral.hibernate.pojo;
// Generated Sep 23, 2012 6:50:23 PM by Hibernate Tools 3.2.4.GA


import java.util.Date;

/**
 * Employeelicensemapping generated by hbm2java
 */
public class Employeelicensemapping  implements java.io.Serializable {


     private Long empLicenseMappingId;
     private Employee employee;
     private Licensemaster licensemaster;
     private Date licenseProvisionDate;

    public Employeelicensemapping() {
    }

	
    public Employeelicensemapping(Employee employee, Licensemaster licensemaster) {
        this.employee = employee;
        this.licensemaster = licensemaster;
    }
    public Employeelicensemapping(Employee employee, Licensemaster licensemaster, Date licenseProvisionDate) {
       this.employee = employee;
       this.licensemaster = licensemaster;
       this.licenseProvisionDate = licenseProvisionDate;
    }
   
    public Long getEmpLicenseMappingId() {
        return this.empLicenseMappingId;
    }
    
    public void setEmpLicenseMappingId(Long empLicenseMappingId) {
        this.empLicenseMappingId = empLicenseMappingId;
    }
    public Employee getEmployee() {
        return this.employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    public Licensemaster getLicensemaster() {
        return this.licensemaster;
    }
    
    public void setLicensemaster(Licensemaster licensemaster) {
        this.licensemaster = licensemaster;
    }
    public Date getLicenseProvisionDate() {
        return this.licenseProvisionDate;
    }
    
    public void setLicenseProvisionDate(Date licenseProvisionDate) {
        this.licenseProvisionDate = licenseProvisionDate;
    }




}

