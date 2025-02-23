package model.entity;

public class UserPermissions {
    private int idUserPermission;
    private int accountId;
    private int permissionId;

    public UserPermissions() {
    }

    public UserPermissions(int idUserPermission, int accountId, int permissionId) {
        this.idUserPermission = idUserPermission;
        this.accountId = accountId;
        this.permissionId = permissionId;
    }

    public int getIdUserPermission() {
        return idUserPermission;
    }

    public void setIdUserPermission(int idUserPermission) {
        this.idUserPermission = idUserPermission;
    }

    public int getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
