package model.dao;

import model.entity.Role;
import model.utils.LibraryContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoleDAO extends LibraryContext {
    public RoleDAO() {
        super();
    }

    public static void main(String[] args) {
        RoleDAO roleDAO = new RoleDAO();
        List<Role> roles = roleDAO.getAllRoles();
        for (Role role : roles) {
            System.out.println("ID: " + role.getIdRole() + ", Name: " + role.getNameRole());
        }
        Role role = roleDAO.getRoleById(1);
        System.out.println("ID: " + role.getIdRole() + ", Name: " + role.getNameRole());
    }

    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        try {
            String query = "SELECT IdRole, NameRole FROM Role";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idRole = resultSet.getInt("IdRole");
                String nameRole = resultSet.getString("NameRole");
                Role role = new Role(idRole, nameRole);
                roles.add(role);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RoleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return roles;
    }

    public Role getRoleById(int idRole) {
        Role role = null;
        try {
            String query = "SELECT NameRole FROM Role WHERE IdRole = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, idRole);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String nameRole = resultSet.getString("NameRole");
                role = new Role(idRole, nameRole);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RoleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return role;
    }

    public Role getRoleByName(String nameRole) {
        Role role = null;
        try {
            String query = "SELECT IdRole FROM Role WHERE NameRole = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, nameRole);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int idRole = resultSet.getInt("IdRole");
                role = new Role(idRole, nameRole);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RoleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return role;
    }
}
