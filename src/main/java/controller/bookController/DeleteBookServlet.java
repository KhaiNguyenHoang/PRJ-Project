package controller.bookController;

import dao.BooksDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Books;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/DeleteBook")
public class DeleteBookServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(DeleteBookServlet.class.getName());
    private BooksDAO booksDAO;

    @Override
    public void init() throws ServletException {
        try {
            booksDAO = new BooksDAO();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize BooksDAO", e);
            throw new ServletException("Unable to initialize BooksDAO", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Books> booksList = booksDAO.getAllBooks();
            request.setAttribute("booksList", booksList);
            request.getRequestDispatcher("/HomeHTML/HomeStaffHTML/DeleteBook.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving books list", e);
            request.setAttribute("error", "Không thể tải danh sách sách. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/HomeHTML/HomeStaffHTML/DeleteBook.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idBookStr = request.getParameter("IdBook");

        // Kiểm tra dữ liệu đầu vào
        if (idBookStr == null || idBookStr.trim().isEmpty()) {
            request.setAttribute("error", "ID sách không được để trống.");
            forwardToJsp(request, response);
            return;
        }

        try {
            int idBook = Integer.parseInt(idBookStr);
            if (idBook <= 0) {
                request.setAttribute("error", "ID sách phải là số nguyên dương.");
                forwardToJsp(request, response);
                return;
            }

            boolean success = booksDAO.deleteBook(idBook);
            if (success) {
                request.setAttribute("message", "Đã xóa sách với ID " + idBook + " thành công.");
                LOGGER.log(Level.INFO, "Book with ID {0} deleted successfully", idBook);
            } else {
                request.setAttribute("error", "Không thể xóa sách với ID " + idBook + ". Sách không tồn tại hoặc có lỗi xảy ra.");
                LOGGER.log(Level.WARNING, "Failed to delete book with ID {0}", idBook);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID sách không hợp lệ. Vui lòng nhập số nguyên.");
            LOGGER.log(Level.WARNING, "Invalid book ID format: {0}", idBookStr);
        } catch (Exception e) {
            request.setAttribute("error", "Có lỗi xảy ra khi xóa sách. Vui lòng thử lại sau.");
            LOGGER.log(Level.SEVERE, "Unexpected error while deleting book", e);
        }

        // Cập nhật danh sách sách sau khi xử lý
        forwardToJsp(request, response);
    }

    /**
     * Chuyển tiếp yêu cầu đến JSP và cập nhật danh sách sách
     */
    private void forwardToJsp(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Books> booksList = booksDAO.getAllBooks();
            request.setAttribute("booksList", booksList);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving books list after operation", e);
            request.setAttribute("error", "Không thể tải danh sách sách sau khi thực hiện thao tác.");
        }
        request.getRequestDispatcher("/HomeHTML/HomeStaffHTML/DeleteBook.jsp").forward(request, response);
    }
}