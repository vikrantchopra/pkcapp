package com.perceptive.epm.perkolcentral.bl;

import com.perceptive.epm.perkolcentral.businessobject.LicenseBO;
import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;
import com.perceptive.epm.perkolcentral.dataaccessor.EmployeeLicenseDataAccessor;
import com.perceptive.epm.perkolcentral.dataaccessor.LicenseMasterDataAccessor;
import com.perceptive.epm.perkolcentral.dataaccessor.LicensePurchaseDataAccessor;
import com.perceptive.epm.perkolcentral.hibernate.pojo.Licensepurchase;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 26/8/12
 * Time: 12:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class LicensesBL {
    private LicenseMasterDataAccessor licenseMasterDataAccessor;
    private LicensePurchaseDataAccessor licensePurchaseDataAccessor;
    EmployeeLicenseDataAccessor employeeLicenseDataAccessor;


    public LicenseMasterDataAccessor getLicenseMasterDataAccessor() {
        return licenseMasterDataAccessor;
    }

    public void setLicenseMasterDataAccessor(LicenseMasterDataAccessor licenseMasterDataAccessor) {
        this.licenseMasterDataAccessor = licenseMasterDataAccessor;
    }

    public LicensePurchaseDataAccessor getLicensePurchaseDataAccessor() {
        return licensePurchaseDataAccessor;
    }

    public void setLicensePurchaseDataAccessor(LicensePurchaseDataAccessor licensePurchaseDataAccessor) {
        this.licensePurchaseDataAccessor = licensePurchaseDataAccessor;
    }

    public EmployeeLicenseDataAccessor getEmployeeLicenseDataAccessor() {
        return employeeLicenseDataAccessor;
    }

    public void setEmployeeLicenseDataAccessor(EmployeeLicenseDataAccessor employeeLicenseDataAccessor) {
        this.employeeLicenseDataAccessor = employeeLicenseDataAccessor;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, isolation = Isolation.SERIALIZABLE, rollbackFor = ExceptionWrapper.class)
    public ArrayList<LicenseBO> getAllLicenseType() throws ExceptionWrapper {
        ArrayList<LicenseBO> licenseBOArrayList = new ArrayList<LicenseBO>();
        try {
            for (Object obj : licenseMasterDataAccessor.getAllLicenseType().values()) {
                final LicenseBO licenseBO = (LicenseBO) obj;
                licenseBOArrayList.add(licenseBO);
            }
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return licenseBOArrayList;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, isolation = Isolation.SERIALIZABLE, rollbackFor = ExceptionWrapper.class)
    public HashMap<String, ArrayList<String>> getLicenseRelatedInfo() throws ExceptionWrapper {
        HashMap<String, ArrayList<String>> licenseInfoKeyedByLicenseName = new HashMap<String, ArrayList<String>>();
        try {

            for (Object obj : licenseMasterDataAccessor.getAllLicenseType().values()) {
                final LicenseBO licenseBO = (LicenseBO) obj;

                //get the total license purchased for this type of item
                ArrayList<Licensepurchase> licensepurchaseArrayList = licensePurchaseDataAccessor.getAllLicensePurchaseInformation();
                CollectionUtils.filter(licensepurchaseArrayList, new Predicate() {
                    @Override
                    public boolean evaluate(Object o) {
                        Licensepurchase item = (Licensepurchase) o;
                        return (item.getLicensemaster().getLicenseTypeId().equals(Long.valueOf(licenseBO.getLicenseTypeId())));
                    }
                });
                //Add up all the licenses purchased
                int totLicenses = 0;
                for (Object item : licensepurchaseArrayList) {
                    totLicenses = totLicenses + ((Licensepurchase) item).getNumberOfLicenses();
                }

                if (!licenseInfoKeyedByLicenseName.containsKey(licenseBO.getLicenseTypeName()))
                    licenseInfoKeyedByLicenseName.put(licenseBO.getLicenseTypeName(), new ArrayList<String>());
                licenseInfoKeyedByLicenseName.get(licenseBO.getLicenseTypeName()).add(Integer.toString(totLicenses));

                //Get the total number of licenses used up for this type of licenses
                licenseInfoKeyedByLicenseName.get(licenseBO.getLicenseTypeName()).add(Integer.toString(employeeLicenseDataAccessor.getEmployeeIdsByLicenseId(licenseBO.getLicenseTypeId()).size()));
            }

        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return licenseInfoKeyedByLicenseName;
    }

    public String getLicenseSummaryChartData() throws ExceptionWrapper {
        String chartData = "";
        try {
            HashMap<String, ArrayList<String>> licenseInfoKeyedByLicenseName = getLicenseRelatedInfo();
            String categories = "";
            for (String licenseTypeName : licenseInfoKeyedByLicenseName.keySet()) {
                categories = categories + "<category name='" + StringUtils.abbreviate(licenseTypeName, 15) + "' hoverText='" + licenseTypeName + "'/>";
            }
            String licensePurchased = "";
            String licenseUsedUp = "";
            /*for (Object obj : licenseInfoKeyedByLicenseName.values()) {
                ArrayList<String> values = (ArrayList<String>) obj;
                licensePurchased = licensePurchased + "<set value='" + values.get(0) + "'/>";
                licenseUsedUp = licenseUsedUp + "<set value='" + values.get(1) + "' link='JavaScript: isJavaScriptCall=true;$('#id_selectedLicenseTypeId').val('');'/>";
            } */
            for (Map.Entry<String, ArrayList<String>> obj : licenseInfoKeyedByLicenseName.entrySet()) {
                ArrayList<String> values = obj.getValue();
                licensePurchased = licensePurchased + "<set value='" + values.get(0) + "'/>";
                licenseUsedUp = licenseUsedUp + "<set value='" + values.get(1) + "' link='JavaScript:populateTheDetails(%26apos;" + obj.getKey() + "%26apos;)'/>";
            }
            chartData = "<graph xaxisname='License Type' yaxisname='Number Of Licenses' hovercapbg='DEDEBE' hovercapborder='889E6D' rotateNames='0' yAxisMaxValue='100' numdivlines='9' divLineColor='CCCCCC' divLineAlpha='80' decimalPrecision='0' showAlternateHGridColor='1' AlternateHGridAlpha='30' AlternateHGridColor='CCCCCC' caption='Perceptive Software Kolkata' subcaption='License purchase/distribution summary'>" +
                    "<categories font='Arial' fontSize='11' fontColor='000000'>" +
                    categories +
                    "</categories>" +
                    "<dataset seriesname='Total License Purchased' color='FDC12E'>" +
                    licensePurchased +
                    "</dataset>" +
                    "<dataset seriesname='Total License Used Up' color='56B9F9'>" +
                    licenseUsedUp +
                    "</dataset>" +
                    "</graph>";

        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return chartData;
    }
}
