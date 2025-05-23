package dao;

import model.Members;
import utils.LibraryContext;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MembersDAO extends LibraryContext {

    public MembersDAO() {
        super();
    }

    // Helper method to map ResultSet to Members object
    private Members mapResultSetToMember(ResultSet rs) throws SQLException {
        return new Members(
                rs.getInt("idMember"),
                rs.getString("fullName"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("address"),
                rs.getString("passwordHash"),
                rs.getDate("membershipDate"),
                rs.getString("status"),
                rs.getDate("createdAt"),
                rs.getDate("updatedAt"),
                rs.getDate("deletedAt")
        );
    }

    public List<Members> getAllMembers() {
        String sql = "SELECT * " + "FROM members";
        List<Members> membersList = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                membersList.add(mapResultSetToMember(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return membersList;
    }

    public Members getMemberById(int idMember) {
        String sql = "SELECT * " + "FROM members WHERE idMember = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMember);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMember(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Log the exception (consider better logging in production)
        }
        return null;
    }

    public Members login(String email, String password) {
        String sql = "SELECT * " + "FROM members WHERE email = ? AND passwordHash = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, hashPassword(password));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMember(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Log the exception (consider better logging in production)
        }
        return null;
    }

    public void registerMember(String fullName, String email, String phone, String address, String passwordHash) {
        if (checkEmailExists(email)) {
            return;
        }
        String sql = "INSERT INTO members (fullName, email, phone, address, passwordHash, membershipDate, status, createdAt) " +
                "VALUES (?, ?, ?, ?, ?, GETDATE(), 'active', GETDATE())";
        String hashedPassword = hashPassword(passwordHash);
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, address);
            ps.setString(5, hashedPassword);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();  // Log the exception (consider better logging in production)
        }
    }

    public boolean checkEmailExists(String email) {
        String sql = "SELECT email FROM members WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Log the exception (consider better logging in production)
        }
        return false;
    }

    public boolean updateMember(Members member) {
        String sql = "UPDATE members SET fullName = ?, email = ?, phone = ?, address = ?, passwordHash = ?, status = ?, updatedAt = GETDATE() WHERE idMember = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, member.getFullName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPhone());
            ps.setString(4, member.getAddress());
            ps.setString(5, member.getPasswordHash());
            ps.setString(6, member.getStatus());
            ps.setInt(7, member.getIdMember());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // True if update succeeded
        } catch (SQLException e) {
            e.printStackTrace(); // Replaced with proper logging in production
            return false; // False if update failed
        }
    }

    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(Integer.toHexString(0xFF & b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getMemberIdByEmail(String email) {
        String sql = "SELECT idMember FROM members WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idMember");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Log the exception (consider better logging in production)
        }
        return -1;
    }

    public void banMemberByEmail(String email) {
        String sql = "UPDATE members SET status = 'suspended' WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();  // Log the exception (consider better logging in production)
        }
    }

    public boolean forgetPassword(String email, String newPassword) {
        String sql = "UPDATE members SET passwordHash = ? WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hashPassword(newPassword));
            ps.setString(2, email);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void unbanMemberByEmail(String email) {
        String sql = "UPDATE members SET status = 'active' WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();  // Log the exception (consider better logging in production)
        }
    }

    public boolean changePassword(String currentPassword, String newPassword) {
        String sql = "UPDATE members SET passwordHash = ? WHERE passwordHash = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            // Set the hashed new password for the update
            ps.setString(1, hashPassword(newPassword));

            // Set the hashed current password to find the matching row
            ps.setString(2, hashPassword(currentPassword));

            // Execute the update and check if it was successful
            int rowsAffected = ps.executeUpdate();

            // Return true if at least one row was updated, indicating success
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Return false if there was an error
        }
    }

    public void deactiveAccount(int idMember) {
        String sql = "UPDATE members SET status = 'expired' WHERE idMember = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMember);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();  // Log the exception (consider better logging in production)
        }
    }

    public int getTotalMembers() throws SQLException {
        String query = "SELECT COUNT(*) AS total FROM Members";
        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }
}