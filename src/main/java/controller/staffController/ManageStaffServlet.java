package controller.staffController;

import dao.AccountDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet(name = "ManageStaffServlet", value = "/ManageStaff")
public class ManageStaffServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ManageStaffServlet.class.getName());
    private AccountDAO accountDAO;

    @Override
    public void init() throws ServletException {
        accountDAO = new AccountDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Account currentStaff = (Account) request.getSession().getAttribute("account");
        if (currentStaff == null || currentStaff.getRoleId() != 1) {
            response.sendRedirect("HomePage");
            return;
        }

        String action = request.getParameter("action");
        if (action == null || action.equals("list")) {
            // Hiển thị danh sách nhân viên
            try {
                List<Account> staffList = accountDAO.getAllAccounts().stream()
                        .filter(account -> account.getRoleId() == 2)
                        .collect(Collectors.toList());
                request.setAttribute("staffList", staffList);
                request.getRequestDispatcher("HomeHTML/HomeAdminHTML/ManageStaff.jsp").forward(request, response);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error retrieving staff list: " + e.getMessage(), e);
                request.setAttribute("error", "An error occurred while retrieving staff list.");
                request.getRequestDispatcher("HomeHTML/HomeAdminHTML/ManageStaff.jsp").forward(request, response);
            }
        } else if (action.equals("edit")) {
            // Hiển thị form chỉnh sửa
            String idStr = request.getParameter("id");
            try {
                int id = Integer.parseInt(idStr);
                Account staff = accountDAO.getAllAccounts().stream()
                        .filter(account -> account.getIdAccount() == id && account.getRoleId() == 2)
                        .findFirst()
                        .orElse(null);
                if (staff != null) {
                    request.setAttribute("staff", staff);
                    request.getRequestDispatcher("HomeHTML/HomeAdminHTML/EditStaff.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "Staff not found.");
                    request.getRequestDispatcher("HomeHTML/HomeAdminHTML/ManageStaff.jsp").forward(request, response);
                }
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid staff ID: " + idStr, e);
                request.setAttribute("error", "Invalid staff ID.");
                request.getRequestDispatcher("HomeHTML/HomeAdminHTML/ManageStaff.jsp").forward(request, response);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error retrieving staff: " + e.getMessage(), e);
                request.setAttribute("error", "An error occurred while retrieving staff.");
                request.getRequestDispatcher("HomeHTML/HomeAdminHTML/ManageStaff.jsp").forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("update".equals(action)) {
            String idStr = request.getParameter("id");
            String email = request.getParameter("email");
            String fullName = request.getParameter("fullName");
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            if (email == null || email.trim().isEmpty() || fullName == null || fullName.trim().isEmpty() ||
                    username == null || username.trim().isEmpty()) {
                LOGGER.warning("Invalid input data for updating staff with email: " + email);
                request.setAttribute("error", "All fields except password are required.");
                doGet(request, response);
                return;
            }

            try {
                int id = Integer.parseInt(idStr);
                Account existingStaff = accountDAO.getAllAccounts().stream()
                        .filter(acc -> acc.getIdAccount() == id && acc.getRoleId() == 2)
                        .findFirst()
                        .orElse(null);

                if (existingStaff == null) {
                    LOGGER.warning("Staff not found with ID: " + id);
                    request.setAttribute("error", "Staff with ID " + id + " not found.");
                    doGet(request, response);
                    return;
                }

                // Nếu password để trống thì giữ nguyên password cũ
                if (password == null || password.trim().isEmpty()) {
                    password = existingStaff.getPasswordHash();
                }

                boolean success = accountDAO.updateAccountByEmail(email, fullName, username, password);
                if (success) {
                    LOGGER.info("Staff updated successfully with ID: " + id);
                    request.getSession().setAttribute("success", "Staff updated successfully");
                    response.sendRedirect("ManageStaff");
                } else {
                    LOGGER.warning("Failed to update staff with ID: " + id);
                    request.setAttribute("error", "Failed to update staff. Please try again.");
                    request.setAttribute("staff", existingStaff);
                    request.getRequestDispatcher("HomeHTML/HomeAdminHTML/EditStaff.jsp").forward(request, response);
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error updating staff: " + e.getMessage(), e);
                request.setAttribute("error", "An error occurred while updating staff.");
                request.setAttribute("staff", accountDAO.getAllAccounts().stream()
                        .filter(acc -> acc.getIdAccount() == Integer.parseInt(idStr))
                        .findFirst().orElse(null));
                request.getRequestDispatcher("HomeHTML/HomeAdminHTML/EditStaff.jsp").forward(request, response);
            }
        } else {
            LOGGER.warning("Invalid action received: " + action);
            doGet(request, response);
        }
    }
}