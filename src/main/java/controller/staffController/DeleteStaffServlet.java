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

@WebServlet(name = "DeleteStaffServlet", value = "/DeleteStaff")
public class DeleteStaffServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(DeleteStaffServlet.class.getName());
    private AccountDAO accountDAO;

    @Override
    public void init() throws ServletException {
        accountDAO = new AccountDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Account currentAdmin = (Account) request.getSession().getAttribute("account");
        if (currentAdmin == null || currentAdmin.getRoleId() != 1) {
            response.sendRedirect("HomePage");
            return;
        }

        try {
            // Lấy danh sách nhân viên (roleId = 2)
            List<Account> staffList = accountDAO.getAllAccounts().stream()
                    .filter(account -> account.getRoleId() == 2)
                    .collect(Collectors.toList());
            request.setAttribute("staffList", staffList);
            request.getRequestDispatcher("HomeHTML/HomeAdminHTML/DeleteStaff.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving staff list: " + e.getMessage(), e);
            request.setAttribute("error", "An error occurred while retrieving staff list.");
            request.getRequestDispatcher("HomeHTML/HomeAdminHTML/DeleteStaff.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Account currentAdmin = (Account) request.getSession().getAttribute("account");
        if (currentAdmin == null || currentAdmin.getRoleId() != 1) {
            response.sendRedirect("HomePage");
            return;
        }

        String idStr = request.getParameter("id");
        try {
            int id = Integer.parseInt(idStr);
            Account staffToDelete = accountDAO.getAllAccounts().stream()
                    .filter(account -> account.getIdAccount() == id && account.getRoleId() == 2)
                    .findFirst()
                    .orElse(null);

            if (staffToDelete == null) {
                LOGGER.warning("Staff not found with ID: " + id);
                request.setAttribute("error", "Staff with ID " + id + " not found.");
                doGet(request, response);
                return;
            }

            // Gọi phương thức xóa từ DAO (giả định có phương thức deleteAccountById)
            boolean success = accountDAO.deleteAccountById(id);
            if (success) {
                LOGGER.info("Staff deleted successfully with ID: " + id);
                request.getSession().setAttribute("success", "Staff deleted successfully.");
                response.sendRedirect("DeleteStaff");
            } else {
                LOGGER.warning("Failed to delete staff with ID: " + id);
                request.setAttribute("error", "Failed to delete staff. Please try again.");
                doGet(request, response);
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid staff ID: " + idStr, e);
            request.setAttribute("error", "Invalid staff ID.");
            doGet(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting staff: " + e.getMessage(), e);
            request.setAttribute("error", "An error occurred while deleting staff.");
            doGet(request, response);
        }
    }
}