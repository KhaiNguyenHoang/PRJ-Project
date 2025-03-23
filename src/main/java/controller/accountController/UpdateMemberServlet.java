package controller.accountController;

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

@WebServlet(name = "UpdateMemberServlet", value = "/UpdateMember")
public class UpdateMemberServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(UpdateMemberServlet.class.getName());
    private static final String JSP_PATH = "HomeHTML/HomeStaffHTML/UpdateMember.jsp";
    private static final String LOGIN_PATH = "HomePage";

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
            LOGGER.log(Level.WARNING, "Unauthorized GET access attempt to UpdateMemberServlet");
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

        // Nếu có memberId trong query string, lấy thông tin thành viên để hiển thị form
        String memberIdParam = request.getParameter("memberId");
        if (memberIdParam != null && !memberIdParam.trim().isEmpty()) {
            try {
                int memberId = Integer.parseInt(memberIdParam);
                Members member = membersDAO.getMemberById(memberId);
                if (member != null) {
                    request.setAttribute("memberToUpdate", member);
                } else {
                    request.setAttribute("errorMessage", "Member with ID " + memberId + " not found.");
                    LOGGER.log(Level.WARNING, "Member not found for ID: {0}", memberId);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid Member ID format.");
                LOGGER.log(Level.WARNING, "Invalid Member ID format: {0}", memberIdParam);
            }
        }

        request.getRequestDispatcher(JSP_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Kiểm tra quyền truy cập
        if (!isStaffAuthorized(request)) {
            LOGGER.log(Level.WARNING, "Unauthorized POST access attempt to UpdateMemberServlet");
            response.sendRedirect(LOGIN_PATH);
            return;
        }

        String memberIdParam = request.getParameter("memberId");
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String password = request.getParameter("password");
        MembersDAO membersDAO = new MembersDAO();

        try {
            // Kiểm tra memberId là bắt buộc trong POST
            if (memberIdParam == null || memberIdParam.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Member ID is required.");
                LOGGER.log(Level.WARNING, "No Member ID provided in POST request");
            } else {
                int memberId = Integer.parseInt(memberIdParam);
                Members member = membersDAO.getMemberById(memberId);

                if (member == null) {
                    request.setAttribute("errorMessage", "Member with ID " + memberId + " not found.");
                    LOGGER.log(Level.WARNING, "Member not found for ID: {0}", memberId);
                } else {
                    // Cập nhật thông tin (Member ID và Email không thay đổi)
                    member.setFullName(fullName != null && !fullName.trim().isEmpty() ? fullName : member.getFullName());
                    member.setPhone(phone != null && !phone.trim().isEmpty() ? phone : member.getPhone());
                    member.setAddress(address != null && !address.trim().isEmpty() ? address : member.getAddress());

                    // Xử lý password: nếu không nhập thì giữ nguyên, nếu có thì cập nhật
                    if (password != null && !password.trim().isEmpty()) {
                        member.setPasswordHash(membersDAO.hashPassword(password)); // Giả định password được lưu dưới dạng hash
                        LOGGER.log(Level.INFO, "Password updated for member ID: {0}", memberId);
                    }

                    // Gọi DAO để cập nhật
                    membersDAO.updateMember(member);
                    request.setAttribute("message", "Member with ID " + memberId + " has been updated successfully.");
                    LOGGER.log(Level.INFO, "Member {0} updated successfully", member.getEmail());
                }
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid Member ID format.");
            LOGGER.log(Level.WARNING, "Invalid Member ID format: {0}", memberIdParam);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An unexpected error occurred while updating the member.");
            LOGGER.log(Level.SEVERE, "Error in UpdateMemberServlet doPost", e);
        }

        // Làm mới danh sách thành viên sau khi xử lý
        try {
            List<Members> membersList = membersDAO.getAllMembers();
            request.setAttribute("membersList", membersList);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error refreshing members list in doPost", e);
            request.setAttribute("errorMessage", "Failed to refresh members list.");
        }

        // Sau khi cập nhật, không hiển thị lại form
        request.getRequestDispatcher(JSP_PATH).forward(request, response);
    }
}