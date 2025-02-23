package model.dao;

import model.entity.Permissions;
import model.utils.LibraryContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PermissionsDAO extends LibraryContext {
    public PermissionsDAO() {
        super();
    }

    public Permissions getPermissionById(int i) {
        Permissions permission = null;
        try {
            String query = "SELECT IdPermission, RoleID, Action FROM Permissions WHERE IdPermission = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, i);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int idPermission = resultSet.getInt(1);
                int roleId = resultSet.getInt(2);
                String action = resultSet.getString(3);
                permission = new Permissions(idPermission, roleId, action);
            }
        } catch (SQLException e) {
            System.out.println("Cannot get permission");
        }
        return permission;
    }

    public List<Permissions> getAllPermissions() {
        List<Permissions> permissions = new ArrayList<>();
        try {
            String query = "SELECT IdPermission, RoleID, Action FROM Permissions";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idPermission = resultSet.getInt("IdPermission");
                int roleId = resultSet.getInt("RoleId");
                String action = resultSet.getString("Action");
                Permissions permission = new Permissions(idPermission, roleId, action);
                permissions.add(permission);
            }
        } catch (SQLException e) {
            System.out.println("Cannot get permissions");
        }
        return permissions;
    }

    public void getPermissionByRoleId(int i) {
        try {
            String query = "SELECT IdPermission, RoleID, Action FROM Permissions WHERE RoleID = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, i);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idPermission = resultSet.getInt("IdPermission");
                int roleId = resultSet.getInt("RoleId");
                String action = resultSet.getString("Action");
                System.out.println("ID: " + idPermission + ", RoleID: " + roleId + ", Action: " + action);
            }
        } catch (SQLException e) {
            System.out.println("Cannot get permission");
        }
    }

    public void insertPermission(Permissions permission) {
        try {
            String query = "INSERT INTO Permissions(RoleID, Action) VALUES(?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, permission.getRoleId());
            statement.setString(2, permission.getAction());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Cannot insert permission");
        }
    }

    public void updatePermission(Permissions permission) {
        try {
            String query = "UPDATE Permissions SET RoleID = ?, Action = ? WHERE IdPermission = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, permission.getRoleId());
            statement.setString(2, permission.getAction());
            statement.setInt(3, permission.getIdPermission());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Cannot update permission");
        }
    }

    public void deletePermission(int i) {
        try {
            String query = "DELETE FROM Permissions WHERE IdPermission = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, i);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Cannot delete permission");
        }
    }

    public boolean checkPermissionbyRoleAndAction(int roleId, String action) {
        try {
            String query = "SELECT IdPermission, RoleID, Action FROM Permissions WHERE RoleID = ? AND Action = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, roleId);
            statement.setString(2, action);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Permission exists");
                return true;
            } else {
                System.out.println("Permission does not exist");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Cannot check permission");
            return false;
        }
    }
}
