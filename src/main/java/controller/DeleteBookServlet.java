package controller;

import dao.BooksDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Books;

import java.io.IOException;
import java.util.List;

@WebServlet("/DeleteBook")
public class DeleteBookServlet extends HttpServlet {
    private BooksDAO booksDAO;

    @Override
    public void init() throws ServletException {
        booksDAO = new BooksDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Books> booksList = booksDAO.getAllBooks();
        request.setAttribute("booksList", booksList);
        request.getRequestDispatcher("/HomeHTML/HomeStaffHTML/DeleteBook.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idBookStr = request.getParameter("IdBook");
        try {
            int idBook = Integer.parseInt(idBookStr);
            boolean success = booksDAO.deleteBook(idBook);
            if (success) {
                request.setAttribute("message", "Đã xóa sách với ID " + idBook + " thành công.");
            } else {
                request.setAttribute("error", "Không thể xóa sách với ID " + idBook + ".");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID sách không hợp lệ.");
        }
        // Cập nhật lại danh sách sách sau khi xóa
        List<Books> booksList = booksDAO.getAllBooks();
        request.setAttribute("booksList", booksList);
        request.getRequestDispatcher("/HomeHTML/HomeStaffHTML/DeleteBook.jsp").forward(request, response);
    }
}