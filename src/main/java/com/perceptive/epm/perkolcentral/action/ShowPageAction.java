package com.perceptive.epm.perkolcentral.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.perceptive.epm.perkolcentral.bl.PagesBL;
import com.perceptive.epm.perkolcentral.common.exception.ExceptionWrapper;
import com.perceptive.epm.perkolcentral.hibernate.pojo.Pages;
import org.springbyexample.httpclient.HttpClientTemplate;
import org.springbyexample.httpclient.ResponseStringCallback;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 22/8/12
 * Time: 10:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShowPageAction extends ActionSupport {

    private String pageId;
    private String pageName;
    private String pageURL;
    private PagesBL pagesBL;
    private String pageContent;
    private HttpClientTemplate httpClientTemplate;

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getPageURL() {
        return pageURL;
    }

    public void setPageURL(String pageURL) {
        this.pageURL = pageURL;
    }

    public PagesBL getPagesBL() {
        return pagesBL;
    }

    public void setPagesBL(PagesBL pagesBL) {
        this.pagesBL = pagesBL;
    }

    public String getPageContent() {
        return pageContent;
    }

    public void setPageContent(String pageContent) {
        this.pageContent = pageContent;
    }

    public HttpClientTemplate getHttpClientTemplate() {
        return httpClientTemplate;
    }

    public void setHttpClientTemplate(HttpClientTemplate httpClientTemplate) {
        this.httpClientTemplate = httpClientTemplate;
    }

    public String executeShowPage() throws ExceptionWrapper {
        try {
            HashMap<String, Pages> pagesHashMap = new HashMap<String, Pages>();
            if (ActionContext.getContext().getSession().containsKey("ALLPAGES"))
                pagesHashMap = (HashMap<String, Pages>) ActionContext.getContext().getSession().get("ALLPAGES");
            else
                pagesHashMap = pagesBL.getAllPages();
            Pages pages = pagesHashMap.get(pageId);
            pageName = pages.getPageName();
            pageURL = pages.getPageUrl();
            httpClientTemplate.setDefaultUri(pageURL);
            httpClientTemplate.executeGetMethod(new ResponseStringCallback() {
                public void doWithResponse(String response) throws IOException {
                    pageContent = response;
                }
            });

        } catch (Exception ex) {
            throw new ExceptionWrapper(ex);
        }
        return SUCCESS;
    }
}
