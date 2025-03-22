package controller;

import dao.MembersDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Members;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "UnbanMemberServlet", value = "/UnbanMember")
public class UnbanMemberServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(UnbanMemberServlet.class.getName());
    private static final String JSP_PATH = "HomeHTML/HomeStaffHTML/UnbanMember.jsp";
    private static final String LOGIN_PATH = "/Auth/SignIn-SignUp.jsp";

    /**
     * Kiểm tra quyền truy cập của nhân viên
     */
    private boolean isStaffAuthorized(HttpServletRequest request) {
        Object staff = request.getSession().getAttribute("account"); // Sử dụng "account" giống BanMember.jsp
        return staff != null;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Kiểm tra quyền truy cập
        if (!isStaffAuthorized(request)) {
            LOGGER.log(Level.WARNING, "Unauthorized GET access attempt to UnbanMemberServlet");
            response.sendRedirect(LOGIN_PATH);
            return;
        }

        // Lấy danh sách tất cả thành viên để hiển thị
        MembersDAO membersDAO = new MembersDAO();
        try {
            List<Members> membersList = membersDAO.getAllMembers();
            request.setAttribute("membersList", membersList);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving members list in doGet", e);
            request.setAttribute("errorMessage", "Failed to load members list.");
        }

        request.getRequestDispatcher(JSP_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Kiểm tra quyền truy cập
        if (!isStaffAuthorized(request)) {
            LOGGER.log(Level.WARNING, "Unauthorized POST access attempt to UnbanMemberServlet");
            response.sendRedirect(LOGIN_PATH);
            return;
        }

        String memberIdParam = request.getParameter("memberId");
        String email = request.getParameter("email");
        MembersDAO membersDAO = new MembersDAO();

        try {
            // Kiểm tra đầu vào: ít nhất một trong hai trường phải được cung cấp
            if ((memberIdParam == null || memberIdParam.trim().isEmpty()) && (email == null || email.trim().isEmpty())) {
                request.setAttribute("errorMessage", "Please provide either Member ID or Email.");
                LOGGER.log(Level.WARNING, "No Member ID or Email provided in request");
            } else {
                Members member = null;
                int memberId = -1;

                // Xử lý theo memberId nếu có
                if (memberIdParam != null && !memberIdParam.trim().isEmpty()) {
                    memberId = Integer.parseInt(memberIdParam);
                    member = membersDAO.getMemberById(memberId);
                    if (member == null) {
                        request.setAttribute("errorMessage", "Member with ID " + memberId + " not found.");
                        LOGGER.log(Level.WARNING, "Member not found for ID: {0}", memberId);
                    }
                }
                // Nếu không có memberId hoặc member không tìm thấy, thử email
                else if (email != null && !email.trim().isEmpty()) {
                    memberId = membersDAO.getMemberIdByEmail(email);
                    if (memberId != -1) {
                        member = membersDAO.getMemberById(memberId);
                    }
                    if (member == null) {
                        request.setAttribute("errorMessage", "Member with email " + email + " not found.");
                        LOGGER.log(Level.WARNING, "Member not found for email: {0}", email);
                    }
                }

                // Nếu tìm thấy thành viên, xử lý bỏ cấm
                if (member != null) {
                    if ("active".equalsIgnoreCase(member.getStatus())) {
                        request.setAttribute("errorMessage", "Member " + (memberIdParam != null ? "with ID " + memberId : "with email " + email) + " is already active.");
                        LOGGER.log(Level.INFO, "Member {0} is already active", member.getEmail());
                    } else {
                        membersDAO.unbanMemberByEmail(member.getEmail());
                        request.setAttribute("message", "Member " + (memberIdParam != null ? "with ID " + memberId : "with email " + email) + " has been unbanned successfully.");
                        LOGGER.log(Level.INFO, "Member {0} unbanned successfully", member.getEmail());
                    }
                }
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid Member ID format.");
            LOGGER.log(Level.WARNING, "Invalid Member ID format: {0}", memberIdParam);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An unexpected error occurred while unbanning the member.");
            LOGGER.log(Level.SEVERE, "Error in UnbanMemberServlet doPost", e);
        }

        // Làm mới danh sách thành viên sau khi xử lý
        try {
            List<Members> membersList = membersDAO.getAllMembers();
            request.setAttribute("membersList", membersList);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error refreshing members list in doPost", e);
            request.setAttribute("errorMessage", "Failed to refresh members list.");
        }

        request.getRequestDispatcher(JSP_PATH).forward(request, response);
    }
}