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
}
