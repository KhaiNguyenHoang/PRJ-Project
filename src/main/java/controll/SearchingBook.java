package controll;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.BooksDAO;

import java.io.IOException;

@WebServlet(name = "SearchingBook", value = "/SearchingBook")
public class SearchingBook extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.getRequestDispatcher("/HomeHTML/HomeMemberHTML/SearchingBook.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String query = request.getParameter("searchQuery");
        BooksDAO bookDAO = new BooksDAO();
        if (query != null && !query.isEmpty()) {
            request.setAttribute("books", bookDAO.searchBooks(query));
            request.setAttribute("searchQuery", query);
            try {
                request.getRequestDispatcher("/HomeHTML/HomeMemberHTML/SearchingBook.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
