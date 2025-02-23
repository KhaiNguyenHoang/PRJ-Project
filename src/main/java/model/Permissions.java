package model;

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

    // Getters and Setters
}
