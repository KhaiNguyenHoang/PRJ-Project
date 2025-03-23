package controller.borrowController;

import dao.BooksDAO;
import dao.BorrowingHistoryDAO;
import dao.MembersDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.BorrowingHistory;
import model.Members;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "BorrowingHistoryServlet", value = "/BorrowingHistory")
public class BorrowingHistoryServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(BorrowingHistoryServlet.class.getName());
    private static final String JSP_PATH = "HomeHTML/HomeStaffHTML/BorrowingHistory.jsp";
    private static final String LOGIN_PATH = "Auth/SignIn-SignUp.jsp";

    /**
     * Kiểm tra quyền truy cập của nhân viên
     */
    private boolean isStaffAuthorized(HttpServletRequest request) {
        Object account = request.getSession().getAttribute("account");
        return account != null;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Kiểm tra quyền truy cập
        if (!isStaffAuthorized(request)) {
            LOGGER.log(Level.WARNING, "Unauthorized GET access attempt to BorrowingHistoryServlet");
            response.sendRedirect(LOGIN_PATH);
            return;
        }

        // Lấy danh sách Borrowing History
        BorrowingHistoryDAO borrowingHistoryDAO = new BorrowingHistoryDAO();
        MembersDAO membersDAO = new MembersDAO();
        BooksDAO booksDAO = new BooksDAO();

        try {
            List<BorrowingHistory> borrowingList = borrowingHistoryDAO.getAllBorrowingHistory();
            // Lấy tên thành viên và tiêu đề sách
            for (BorrowingHistory borrowing : borrowingList) {
                // Lấy FullName từ MembersDAO
                Members member = membersDAO.getMemberById(borrowing.getMemberId());
                String memberName = (member != null) ? member.getFullName() : "Unknown Member";

                // Gán bookTitle vào BorrowingHistory (transient)
                borrowing.setBookTitle(booksDAO.getBookTitleByBorrowingHistoryID(borrowing.getIdHistory()));

                // Vì model không có memberName, truyền qua request attribute
                request.setAttribute("memberName_" + borrowing.getIdHistory(), memberName);
            }

            // Đặt danh sách để truyền sang JSP
            request.setAttribute("borrowingList", borrowingList);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving borrowing history", e);
            request.setAttribute("error", "Failed to load borrowing history.");
        }

        request.getRequestDispatcher(JSP_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Hiện tại chưa xử lý POST, gọi lại doGet
        doGet(request, response);
    }

    @Override
    public void destroy() {
        // Xử lý khi servlet bị hủy (nếu cần)
    }
}