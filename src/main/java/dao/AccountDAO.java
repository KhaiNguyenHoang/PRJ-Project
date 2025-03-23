package dao;

import model.Account;
import utils.LibraryContext;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO extends LibraryContext {

    static final String ID_ACCOUNT = "IdAccount";
    static final String FULL_NAME = "FullName";
    static final String EMAILS = "Emails";
    static final String USERNAME = "Username";
    static final String PASSWORD_HASH = "PasswordHash";
    static final String ROLE_ID = "Role_ID";
    static final String CREATED_AT = "CreatedAt";
    static final String UPDATED_AT = "UpdatedAt";
    static final String DELETED_AT = "DeletedAt";

    public AccountDAO() {
        super();
    }

    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT * " + "FROM Account";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Account account = new Account(
                        resultSet.getInt(ID_ACCOUNT),
                        resultSet.getString(FULL_NAME),
                        resultSet.getString(EMAILS),
                        resultSet.getString(USERNAME),
                        resultSet.getString(PASSWORD_HASH),
                        resultSet.getInt(ROLE_ID),
                        resultSet.getDate(CREATED_AT),
                        resultSet.getDate(UPDATED_AT),
                        resultSet.getDate(DELETED_AT)
                );
                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    // Hash Password using SHA-512
    private String hashPassword(String password) {
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

    // Register a new account
    public boolean registerAccount(String fullName, String emails, String username, String password, int roleId) {
        if (checkUsernameExists(username)) {
            System.out.println("Username already exists.");
            return false;
        }
        if (checkEmailExists(emails)) {
            System.out.println("Email already exists.");
            return false;
        }
        String passwordHash = hashPassword(password);
        String query = "INSERT INTO Account (FullName, Emails, Username, PasswordHash, Role_ID, CreatedAt, UpdatedAt) " +
                "VALUES (?, ?, ?, ?, ?, GETDATE(), GETDATE())";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, fullName);
            statement.setString(2, emails);
            statement.setString(3, username);
            statement.setString(4, passwordHash);
            statement.setInt(5, roleId);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Login method
    public Account login(String emails, String password) {
        if (conn == null) {
            System.out.println("Database connection is null.");
            return null;
        }
        String passwordHash = hashPassword(password);
        String query = "SELECT * " + "FROM Account WHERE Emails = ? AND PasswordHash = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            // Set email and password parameters for the query
            statement.setString(1, emails);
            statement.setString(2, passwordHash);
            ResultSet resultSet = statement.executeQuery();
            // If a record is found, return the account
            if (resultSet.next()) {
                return new Account(
                        resultSet.getInt(ID_ACCOUNT),
                        resultSet.getString(FULL_NAME),
                        resultSet.getString(EMAILS),
                        resultSet.getString(USERNAME),
                        resultSet.getString(PASSWORD_HASH),
                        resultSet.getInt(ROLE_ID),
                        resultSet.getDate(CREATED_AT),
                        resultSet.getDate(UPDATED_AT),
                        resultSet.getDate(DELETED_AT)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Check if username exists
    public boolean checkUsernameExists(String username) {
        String query = "SELECT COUNT(*) FROM Account WHERE Username = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // True if username exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Check if email exists
    public boolean checkEmailExists(String email) {
        String query = "SELECT COUNT(*) FROM Account WHERE Emails = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // True if email exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete account
    public void deleteAccount(int accountId) {
        String query = "DELETE FROM Account WHERE IdAccount = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, accountId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update account information
    public boolean updateAccountByEmail(String email, String fullName, String username, String password) {
        String passwordHash = hashPassword(password);
        String query = "UPDATE Account SET FullName = ?, Username = ?, PasswordHash = ? WHERE Emails = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, fullName);
            statement.setString(2, username);
            statement.setString(3, passwordHash);
            statement.setString(4, email);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all accounts
    public List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT IdAccount, FullName, Emails, Username, PasswordHash, Role_ID, CreatedAt, UpdatedAt FROM Account";
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                accounts.add(new Account(
                        resultSet.getInt(ID_ACCOUNT),
                        resultSet.getString(FULL_NAME),
                        resultSet.getString(EMAILS),
                        resultSet.getString(USERNAME),
                        resultSet.getString(PASSWORD_HASH),
                        resultSet.getInt(ROLE_ID),
                        resultSet.getDate(CREATED_AT),
                        resultSet.getDate(UPDATED_AT),
                        resultSet.getDate(DELETED_AT)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    // Get account by ID
    public Account getAccountById(int accountId) {
        String query = "SELECT * " + "FROM Account WHERE IdAccount = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, accountId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Account(
                        resultSet.getInt(ID_ACCOUNT),
                        resultSet.getString(FULL_NAME),
                        resultSet.getString(EMAILS),
                        resultSet.getString(USERNAME),
                        resultSet.getString(PASSWORD_HASH),
                        resultSet.getInt(ROLE_ID),
                        resultSet.getDate(CREATED_AT),
                        resultSet.getDate(UPDATED_AT),
                        resultSet.getDate(DELETED_AT)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get account by Username
    public Account getAccountByUsername(String username) {
        String query = "SELECT * " + "FROM Account WHERE Username = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Account(
                        resultSet.getInt(ID_ACCOUNT),
                        resultSet.getString(FULL_NAME),
                        resultSet.getString(EMAILS),
                        resultSet.getString(USERNAME),
                        resultSet.getString(PASSWORD_HASH),
                        resultSet.getInt(ROLE_ID),
                        resultSet.getDate(CREATED_AT),
                        resultSet.getDate(UPDATED_AT),
                        resultSet.getDate(DELETED_AT)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}