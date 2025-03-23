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

@WebServlet(name = "BanMemberServlet", value = "/BanMember")
public class BanMemberServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(BanMemberServlet.class.getName());
    private static final String JSP_PATH = "HomeHTML/HomeStaffHTML/BanMember.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Kiểm tra quyền truy cập (giả định nhân viên đã đăng nhập với session attribute "staff")
        if (request.getSession().getAttribute("account") == null) {
            LOGGER.log(Level.WARNING, "Unauthorized access attempt to BanMemberServlet");
            response.sendRedirect("/Auth/SignIn-SignUp.jsp");
            return;
        }

        // Lấy danh sách tất cả thành viên để hiển thị
        MembersDAO membersDAO = new MembersDAO();
        List<Members> membersList = membersDAO.getAllMembers();
        request.setAttribute("membersList", membersList);

        request.getRequestDispatcher(JSP_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Kiểm tra quyền truy cập
        if (request.getSession().getAttribute("account") == null) {
            LOGGER.log(Level.WARNING, "Unauthorized access attempt to BanMemberServlet");
            response.sendRedirect("/Auth/SignIn-SignUp.jsp");
            return;
        }

        String memberIdParam = request.getParameter("memberId");
        String email = request.getParameter("email");
        MembersDAO membersDAO = new MembersDAO();

        try {
            // Ưu tiên xử lý theo memberId nếu có, nếu không thì dùng email
            if (memberIdParam != null && !memberIdParam.trim().isEmpty()) {
                int memberId = Integer.parseInt(memberIdParam);
                Members member = membersDAO.getMemberById(memberId);
                if (member == null) {
                    request.setAttribute("errorMessage", "Member with ID " + memberId + " not found.");
                    LOGGER.log(Level.WARNING, "Member not found for ID: {0}", memberId);
                } else if ("suspended".equalsIgnoreCase(member.getStatus())) {
                    request.setAttribute("errorMessage", "Member with ID " + memberId + " is already banned.");
                    LOGGER.log(Level.INFO, "Member with ID {0} is already banned", memberId);
                } else {
                    // Cập nhật trạng thái thành "suspended" bằng cách dùng email
                    membersDAO.banMemberByEmail(member.getEmail());
                    request.setAttribute("message", "Member with ID " + memberId + " has been banned successfully.");
                    LOGGER.log(Level.INFO, "Member with ID {0} banned successfully", memberId);
                }
            } else if (email != null && !email.trim().isEmpty()) {
                int memberId = membersDAO.getMemberIdByEmail(email);
                if (memberId == -1) {
                    request.setAttribute("errorMessage", "Member with email " + email + " not found.");
                    LOGGER.log(Level.WARNING, "Member not found for email: {0}", email);
                } else {
                    Members member = membersDAO.getMemberById(memberId);
                    if ("suspended".equalsIgnoreCase(member.getStatus())) {
                        request.setAttribute("errorMessage", "Member with email " + email + " is already banned.");
                        LOGGER.log(Level.INFO, "Member with email {0} is already banned", email);
                    } else {
                        membersDAO.banMemberByEmail(email);
                        request.setAttribute("message", "Member with email " + email + " has been banned successfully.");
                        LOGGER.log(Level.INFO, "Member with email {0} banned successfully", email);
                    }
                }
            } else {
                request.setAttribute("errorMessage", "Please provide either Member ID or Email.");
                LOGGER.log(Level.WARNING, "No Member ID or Email provided in request");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid Member ID format.");
            LOGGER.log(Level.WARNING, "Invalid Member ID format: {0}", memberIdParam);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An unexpected error occurred while banning the member.");
            LOGGER.log(Level.SEVERE, "Error in BanMemberServlet doPost", e);
        }

        // Làm mới danh sách thành viên sau khi xử lý
        List<Members> membersList = membersDAO.getAllMembers();
        request.setAttribute("membersList", membersList);

        request.getRequestDispatcher(JSP_PATH).forward(request, response);
    }
}