package controller.fineController;

import dao.FinesDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import model.Fines;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet(name = "StaffManageFineServlet", value = "/StaffManageFine")
public class StaffManageFineServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String JSP_PATH = "HomeHTML/HomeStaffHTML/StaffManageFine.jsp";
    private static final Logger LOGGER = Logger.getLogger(StaffManageFineServlet.class.getName());

    // Kiểm tra phiên đăng nhập của Staff
    private Account getCurrentStaff(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Account currentStaff = (Account) request.getSession().getAttribute("account");
        if (currentStaff == null) {
            LOGGER.log(Level.WARNING, "No staff found in session, redirecting to login");
            response.sendRedirect("/Auth/SignIn-SignUp.jsp");
            return null;
        }
        return currentStaff;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Account currentStaff = getCurrentStaff(request, response);
        if (currentStaff == null) return;

        try {
            String searchKeyword = request.getParameter("search");
            String filterStatus = request.getParameter("status");
            refreshFinesList(request, searchKeyword, filterStatus);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading fines", e);
            request.setAttribute("errorMessage", "Failed to load fines: " + e.getMessage());
        }
        request.getRequestDispatcher(JSP_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Account currentStaff = getCurrentStaff(request, response);
        if (currentStaff == null) return;

        String fineIdStr = request.getParameter("fineId");
        String paymentMethod = request.getParameter("paymentMethod");
        String action = request.getParameter("action");
        String searchKeyword = request.getParameter("search");
        String filterStatus = request.getParameter("status");

        try {
            // Kiểm tra dữ liệu đầu vào
            if (fineIdStr == null || fineIdStr.trim().isEmpty() || action == null || action.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Invalid fine ID or action.");
                refreshFinesList(request, searchKeyword, filterStatus);
                request.getRequestDispatcher(JSP_PATH).forward(request, response);
                return;
            }

            if (!"confirmPayment".equals(action)) {
                request.setAttribute("errorMessage", "Invalid action specified.");
                refreshFinesList(request, searchKeyword, filterStatus);
                request.getRequestDispatcher(JSP_PATH).forward(request, response);
                return;
            }

            int fineId;
            try {
                fineId = Integer.parseInt(fineIdStr);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid fine ID format.");
                LOGGER.log(Level.WARNING, "Invalid fineId format: {0}", fineIdStr);
                refreshFinesList(request, searchKeyword, filterStatus);
                request.getRequestDispatcher(JSP_PATH).forward(request, response);
                return;
            }

            FinesDAO finesDAO = new FinesDAO();
            Fines fine = finesDAO.getFinesById(fineId);

            if (fine == null) {
                request.setAttribute("errorMessage", "Fine not found.");
                LOGGER.log(Level.WARNING, "Fine not found for fineId: {0}", fineId);
            } else if ("Paid".equalsIgnoreCase(fine.getStatus())) {
                request.setAttribute("errorMessage", "This fine has already been paid.");
                LOGGER.log(Level.WARNING, "Fine already paid for fineId: {0}", fineId);
            } else if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Payment method is required.");
                LOGGER.log(Level.WARNING, "Missing payment method for fineId: {0}", fineId);
            } else {
                double amountPaid = fine.getAmount();
                LOGGER.log(Level.INFO, "Attempting to pay fineId: {0} with amount: {1} and method: {2}",
                        new Object[]{fineId, amountPaid, paymentMethod});

                // Gọi trực tiếp payFine từ FinesDAO
                boolean isSuccess = finesDAO.payFine(fineId, amountPaid, paymentMethod);

                if (isSuccess) {
                    request.setAttribute("message", "Fine payment confirmed successfully for Fine ID: " + fineId);
                    LOGGER.log(Level.INFO, "Fine payment confirmed for fineId: {0} by staff: {1}",
                            new Object[]{fineId, currentStaff.getIdAccount()});
                } else {
                    request.setAttribute("errorMessage", "Failed to confirm payment. Please try again.");
                    LOGGER.log(Level.SEVERE, "Failed to confirm payment for fineId: {0}", fineId);
                }
            }

            refreshFinesList(request, searchKeyword, filterStatus);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException while processing payment for fineId: {0}", fineIdStr);
            request.setAttribute("errorMessage", "An error occurred while processing the payment: " + e.getMessage());
            try {
                refreshFinesList(request, searchKeyword, filterStatus);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Error refreshing fines list after SQLException", ex);
                request.setAttribute("errorMessage", "Critical error: Unable to refresh fines list.");
            }
        } finally {
            request.getRequestDispatcher(JSP_PATH).forward(request, response);
        }
    }

    // Làm mới danh sách khoản phạt với tìm kiếm và lọc
    private void refreshFinesList(HttpServletRequest request, String searchKeyword, String filterStatus) throws SQLException {
        FinesDAO finesDAO = new FinesDAO();
        List<Fines> finesList;

        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            finesList = finesDAO.searchFinesByMemberIdOrBorrowId(searchKeyword);
        } else {
            finesList = finesDAO.getAllFines();
        }

        if (filterStatus != null && !filterStatus.trim().isEmpty() && !"All".equals(filterStatus)) {
            finesList = finesList.stream()
                    .filter(fine -> fine.getStatus().equalsIgnoreCase(filterStatus))
                    .collect(Collectors.toList());
        }

        request.setAttribute("finesList", finesList);
        request.setAttribute("searchKeyword", searchKeyword);
        request.setAttribute("filterStatus", filterStatus);
        if (finesList.isEmpty()) {
            request.setAttribute("infoMessage", "There are no fines matching your criteria.");
        }
    }
}