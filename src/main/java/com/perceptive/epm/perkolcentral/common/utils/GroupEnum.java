package com.perceptive.epm.perkolcentral.common.utils;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 17/9/12
 * Time: 1:51 PM
 * To change this template use File | Settings | File Templates.
 */
public enum GroupEnum {
    SCRUM_MASTERS(14),DEV_MANAGER(15),VERTICAL_QA(16),VERTICAL_DEV(17),VERTICAL_TW(18),VERTICAL_UX(19);
    private int code;
    private GroupEnum(int c){
        code=c;
    }
    public int getCode(){
        return code;
    }
}
