package model.dao;

import model.entity.UserPermissions;
import model.utils.LibraryContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserPermissionDAO extends LibraryContext {
    public UserPermissionDAO() {
        super();
    }

    public List<UserPermissions> getPermissionsByAccountId() {
        List<UserPermissions> permissions = new ArrayList<>();
        try {
            String query = "SELECT IdUserPermission, UserPermissions.AccountID, PermissionID FROM UserPermissions";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idUserPermission = resultSet.getInt(1);
                int accountId = resultSet.getInt(2);
                int permissionId = resultSet.getInt(3);
                UserPermissions userPermission = new UserPermissions(idUserPermission, accountId, permissionId);
                permissions.add(userPermission);
            }
        } catch (SQLException e) {
            System.out.println("Cannot get permissions");
        }
        return permissions;
    }

    public void addPermission(int accountId, int permissionId) {
        try {
            String query = "INSERT INTO UserPermissions (AccountID, PermissionID) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, accountId);
            statement.setInt(2, permissionId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Cannot add permission");
        }
    }

    public void deletePermission(int accountId, int permissionId) {
        try {
            String query = "DELETE FROM UserPermissions WHERE AccountID = ? AND PermissionID = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, accountId);
            statement.setInt(2, permissionId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Cannot delete permission");
        }
    }

    public void updatePermission(int accountId, int permissionId) {
        try {
            String query = "UPDATE UserPermissions SET PermissionID = ? WHERE AccountID = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, permissionId);
            statement.setInt(2, accountId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Cannot update permission");
        }
    }

    public void searchPermission(int accountId, int permissionId) {
        try {
            String query = "SELECT * FROM UserPermissions WHERE AccountID = ? AND PermissionID = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, accountId);
            statement.setInt(2, permissionId);
            statement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Cannot search permission");
        }
    }
}
