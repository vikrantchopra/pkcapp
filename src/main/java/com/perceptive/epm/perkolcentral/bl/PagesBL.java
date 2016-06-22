package com.perceptive.epm.perkolcentral.bl;

import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;
import com.perceptive.epm.perkolcentral.dataaccessor.PageDataAccessor;
import com.perceptive.epm.perkolcentral.hibernate.pojo.Pages;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 22/8/12
 * Time: 9:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class PagesBL {
    private PageDataAccessor pageDataAccessor;

    public PageDataAccessor getPageDataAccessor() {
        return pageDataAccessor;
    }

    public void setPageDataAccessor(PageDataAccessor pageDataAccessor) {
        this.pageDataAccessor = pageDataAccessor;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, isolation = Isolation.SERIALIZABLE,rollbackFor = ExceptionWrapper.class)
    public HashMap<String, Pages> getAllPages() throws ExceptionWrapper {
        try {
            return pageDataAccessor.getAllPages();
        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
    }
}
