package controller.bookController;

import dao.BookDetailDAO;
import dao.BooksDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.BookDetail;
import model.Books;

import java.io.IOException;

@WebServlet(name = "BookDetails", value = "/BookDetails")
public class BookDetailsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Lấy ID của sách từ tham số URL
            String bookIdParam = request.getParameter("id");
            if (bookIdParam != null) {
                int bookId = Integer.parseInt(bookIdParam);

                // Tạo đối tượng DAO để lấy thông tin sách và chi tiết sách
                BooksDAO booksDAO = new BooksDAO();
                BookDetailDAO bookDetailDAO = new BookDetailDAO();

                // Lấy thông tin sách
                Books book = booksDAO.getBookById(bookId);
                // Lấy thông tin chi tiết sách
                BookDetail bookDetail = bookDetailDAO.getBookDetailByBookID(bookId);

                // Nếu sách và chi tiết sách tồn tại
                if (book != null && bookDetail != null) {
                    // Đưa dữ liệu vào request
                    request.setAttribute("book", book);
                    request.setAttribute("bookDetail", bookDetail);

                    // Chuyển đến trang BookDetail.jsp
                    request.getRequestDispatcher("/HomeHTML/HomeMemberHTML/BookDetail.jsp").forward(request, response);
                } else {
                    // Nếu không tìm thấy sách, chuyển đến trang lỗi hoặc thông báo
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Book not found");
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Book ID is required");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Book ID format");
        }
    }
}
