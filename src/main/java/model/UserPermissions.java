package model;

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

    // Getters and Setters
}
