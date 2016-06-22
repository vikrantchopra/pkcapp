package com.perceptive.epm.perkolcentral.bl;

import com.perceptive.epm.perkolcentral.businessobject.EmployeeBO;
import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;
import com.perceptive.epm.perkolcentral.common.utils.Constants;
import com.perceptive.epm.perkolcentral.dataaccessor.ImageNowLicenseDataAccessor;
import com.perceptive.epm.perkolcentral.hibernate.pojo.Imagenowlicenses;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 21/9/12
 * Time: 3:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageNowLicenseBL {
    private ImageNowLicenseDataAccessor imageNowLicenseDataAccessor;
    private HtmlEmail email;
    private String emailHost;
    private String fileUploadPath;
    private EmployeeBL employeeBL;


    public void setImageNowLicenseDataAccessor(ImageNowLicenseDataAccessor imageNowLicenseDataAccessor) {
        this.imageNowLicenseDataAccessor = imageNowLicenseDataAccessor;
    }


    public String getFileUploadPath() {
        return fileUploadPath;
    }

    public void setFileUploadPath(String fileUploadPath) {
        this.fileUploadPath = fileUploadPath;
    }

    public void setEmployeeBL(EmployeeBL employeeBL) {
        this.employeeBL = employeeBL;
    }

    public void setEmailHost(String emailHost) {
        this.emailHost = emailHost;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE,rollbackFor = ExceptionWrapper.class)
    public void addImageNowLicenseRequest(Imagenowlicenses imagenowlicenses, String groupRequestedFor, String employeeUIDWhoRequestedLicense, File sysfpFile, String originalFileName) throws ExceptionWrapper {
        try {
            imageNowLicenseDataAccessor.addImageNowLicense(imagenowlicenses, groupRequestedFor, employeeUIDWhoRequestedLicense);
            if (!new File(fileUploadPath).exists())
                FileUtils.forceMkdir(new File(fileUploadPath));
            File filePathForThisRequest = new File(FilenameUtils.concat(fileUploadPath, imagenowlicenses.getImageNowLicenseRequestId()));
            FileUtils.forceMkdir(filePathForThisRequest);
            File fileNameToCopy = new File(FilenameUtils.concat(filePathForThisRequest.getAbsolutePath(), originalFileName));
            FileUtils.copyFile(sysfpFile, fileNameToCopy);
            //Send the email
            String messageToSend = String.format(Constants.email_license_request,
                    imagenowlicenses.getEmployeeByRequestedByEmployeeId().getEmployeeName(),
                    imagenowlicenses.getGroups().getGroupName(),
                    new SimpleDateFormat("dd-MMM-yyyy").format(imagenowlicenses.getLicenseRequestedOn()),
                    imagenowlicenses.getImageNowVersion(),
                    Long.toString(imagenowlicenses.getNumberOfLicenses()));
            email=new HtmlEmail();
            email.setHostName(emailHost);
            email.setHtmlMsg(messageToSend);
            email.setSubject("ImageNow License Request");
            //email.setFrom("ImageNowLicenseRequest@perceptivesoftware.com", "ImageNow License Request");
            email.setFrom("ImageNowLicenseRequest@lexmark.com", "ImageNow License Request");
            // Create the attachment
            EmailAttachment attachment = new EmailAttachment();
            attachment.setPath(fileNameToCopy.getAbsolutePath());
            attachment.setDisposition(EmailAttachment.ATTACHMENT);
            attachment.setDescription(originalFileName);
            attachment.setName(originalFileName);
            email.attach(attachment);
            //Send mail to scrum masters
            for (EmployeeBO item : employeeBL.getAllEmployeesKeyedByGroupId().get(Integer.valueOf("14"))) {
                email.addTo(item.getEmail(), item.getEmployeeName());
            }
            //email.addTo("saibal.ghosh@perceptivesoftware.com","Saibal Ghosh");
            email.addCc(imagenowlicenses.getEmployeeByRequestedByEmployeeId().getEmail());
            email.send();

        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, readOnly = true,rollbackFor = ExceptionWrapper.class)
    public ArrayList<Imagenowlicenses> getLicensesRequestedByMe(String employeeUIDWhoRequestedLicense) throws ExceptionWrapper {
        try {
            ArrayList<Imagenowlicenses> imagenowlicensesArrayList = new ArrayList<Imagenowlicenses>(imageNowLicenseDataAccessor.getAllImageNowLicensesByRequestor(employeeUIDWhoRequestedLicense).values());
            CollectionUtils.filter(imagenowlicensesArrayList, new Predicate() {
                @Override
                public boolean evaluate(Object o) {
                    return !((Imagenowlicenses) o).isIsProvided();  //To change body of implemented methods use File | Settings | File Templates.
                }
            });
            return imagenowlicensesArrayList;
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, readOnly = true,rollbackFor = ExceptionWrapper.class)
    public ArrayList<Imagenowlicenses> getLicensesToBeProvided() throws ExceptionWrapper {
        try {
            ArrayList<Imagenowlicenses> imagenowlicensesArrayList = new ArrayList<Imagenowlicenses>(imageNowLicenseDataAccessor.getAllImageNowLicenses().values());
            CollectionUtils.filter(imagenowlicensesArrayList, new Predicate() {
                @Override
                public boolean evaluate(Object o) {
                    return !((Imagenowlicenses) o).isIsProvided();  //To change body of implemented methods use File | Settings | File Templates.
                }
            });
            return imagenowlicensesArrayList;
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, readOnly = true,rollbackFor = ExceptionWrapper.class)
    public FileInputStream getSysFpFileInputStream(Imagenowlicenses imagenowlicenses) throws ExceptionWrapper {
        try {
            imagenowlicenses = imageNowLicenseDataAccessor.getAllImageNowLicensesByRequestId(imagenowlicenses.getImageNowLicenseRequestId());
            File filePathForThisRequest = new File(FilenameUtils.concat(fileUploadPath, imagenowlicenses.getImageNowLicenseRequestId()));
            File fileLocation = new File(FilenameUtils.concat(filePathForThisRequest.getAbsolutePath(), imagenowlicenses.getFileName()));
            return FileUtils.openInputStream(fileLocation);

        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE,rollbackFor = ExceptionWrapper.class)
    public void cancelImageNowLicense(Imagenowlicenses imagenowlicenses) throws ExceptionWrapper {
        try {
            imagenowlicenses = imageNowLicenseDataAccessor.getAllImageNowLicensesByRequestId(imagenowlicenses.getImageNowLicenseRequestId());
            File filePathForThisRequest = new File(FilenameUtils.concat(fileUploadPath, imagenowlicenses.getImageNowLicenseRequestId()));
            imageNowLicenseDataAccessor.deleteImageNowLicense(imagenowlicenses);
            FileUtils.deleteQuietly(filePathForThisRequest);
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE,rollbackFor = ExceptionWrapper.class)
    public void provideImageNowLicense(Imagenowlicenses imagenowlicenses, String providerUID) throws ExceptionWrapper {
        try {
            imagenowlicenses = imageNowLicenseDataAccessor.getAllImageNowLicensesByRequestId(imagenowlicenses.getImageNowLicenseRequestId());
            imagenowlicenses.setIsProvided(true);
            imagenowlicenses.setLicenseProvidedOn(Calendar.getInstance().getTime());
            File filePathForThisRequest = new File(FilenameUtils.concat(fileUploadPath, imagenowlicenses.getImageNowLicenseRequestId()));
            imageNowLicenseDataAccessor.updateImageNowLicense(imagenowlicenses, providerUID);
            FileUtils.deleteQuietly(filePathForThisRequest);
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }
}
