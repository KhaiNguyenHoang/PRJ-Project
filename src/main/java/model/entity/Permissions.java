package model.entity;

public class Permissions {
    private int idPermission;
    private int roleId;
    private String action;

    public Permissions() {
    }

    public Permissions(int idPermission, int roleId, String action) {
        this.idPermission = idPermission;
        this.roleId = roleId;
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getIdPermission() {
        return idPermission;
    }

    public void setIdPermission(int idPermission) {
        this.idPermission = idPermission;
    }
}
