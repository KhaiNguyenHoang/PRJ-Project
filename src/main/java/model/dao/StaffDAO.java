package model.dao;

import model.entity.Staff;
import model.utils.LibraryContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO extends LibraryContext {
    public StaffDAO() {
        super();
    }

    public List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        String query = "SELECT * " + "FROM Staff";
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Staff staff = new Staff(
                        resultSet.getInt("idStaff"),
                        resultSet.getInt("accountId"),
                        resultSet.getString("position"),
                        resultSet.getDouble("salary")
                );
                staffList.add(staff);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }

    public Staff getStaffById(int idStaff) {
        String query = "SELECT * " + "FROM Staff WHERE idStaff = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, idStaff);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Staff(
                            resultSet.getInt("idStaff"),
                            resultSet.getInt("accountId"),
                            resultSet.getString("position"),
                            resultSet.getDouble("salary")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addStaff(Staff staff) {
        String query = "INSERT INTO Staff (accountId, position, salary) VALUES (?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, staff.getAccountId());
            statement.setString(2, staff.getPosition());
            statement.setDouble(3, staff.getSalary());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStaff(Staff staff) {
        String query = "UPDATE Staff SET accountId = ?, position = ?, salary = ? WHERE idStaff = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, staff.getAccountId());
            statement.setString(2, staff.getPosition());
            statement.setDouble(3, staff.getSalary());
            statement.setInt(4, staff.getIdStaff());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStaff(int idStaff) {
        String query = "DELETE FROM Staff WHERE idStaff = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, idStaff);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
