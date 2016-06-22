package com.perceptive.epm.perkolcentral.businessobject;

import com.perceptive.epm.perkolcentral.hibernate.pojo.Groups;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 19/8/12
 * Time: 10:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class GroupBO implements Serializable{
    private String groupId;
    private String groupName;
    private Boolean isRallyGroup;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Boolean getRallyGroup() {
        return isRallyGroup;
    }

    public void setRallyGroup(Boolean rallyGroup) {
        isRallyGroup = rallyGroup;
    }

    public GroupBO(Groups groups) {
        groupId = groups.getGroupId().toString();
        groupName = groups.getGroupName();
        isRallyGroup = groups.getIsRallyGroup();
    }
}
