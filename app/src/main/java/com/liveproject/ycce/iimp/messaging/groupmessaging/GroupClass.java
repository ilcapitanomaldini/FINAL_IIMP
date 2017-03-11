package com.liveproject.ycce.iimp.messaging.groupmessaging;

/**
 * Created by Laptop on 26-10-2016.
 */
public class GroupClass {
    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getGName() {
        return GName;
    }

    public void setGName(String GName) {
        this.GName = GName;
    }

    public String getGType() {
        return GType;
    }

    public void setGType(String GType) {
        this.GType = GType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String gid, GName, GType, status,GRole;

   public GroupClass()
    {
        //default
        gid=GName=GType=status=null;
    }
    public GroupClass(String gid, String GName){
        this.GType=this.status=null;
        this.gid=gid;
        this.GName=GName;
    }
    public GroupClass(String gid, String GName, String GRoles){
        this.GType=this.status=null;
        this.gid=gid;
        this.GName=GName;
        this.GRole=GRoles;
    }

    public String getGRole() {
        return GRole;
    }

    public void setGRole(String GRole) {
        this.GRole = GRole;
    }


    //TODO : Create an actual constructor for all arguments.

}
