package controll;

import dao.BookCopiesDAO;
import dao.BooksDAO;
import dao.BorrowingDAO;
import dao.BorrowingHistoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Books;
import model.Borrowing;
import model.BorrowingHistory;
import model.Members;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet(name = "BorrowingServlet", value = "/Borrowing")
public class BorrowingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String JSP_PATH = "HomeHTML/HomeMemberHTML/Borrowing.jsp";

    // Centralized session check
    private Members getCurrentMember(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Members currentMember = (Members) request.getSession().getAttribute("members");
        if (currentMember == null) {
            response.sendRedirect("/Auth/SignIn-SignUp.jsp");
        }
        return currentMember;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Members currentMember = getCurrentMember(request, response);
        if (currentMember == null) return;

        String keyword = request.getParameter("keyword");
        String bookIdParam = request.getParameter("id");

        try {
            // Always fetch borrowing history
            BorrowingHistoryDAO historyDAO = new BorrowingHistoryDAO();
            List<BorrowingHistory> borrowingHistoryList = historyDAO.getBorrowingHistoryByMemberId(currentMember.getIdMember());
            request.setAttribute("borrowingHistoryList", borrowingHistoryList);

            // Handle book selection for borrowing form
            if (bookIdParam != null && !bookIdParam.isEmpty()) {
                int bookId = Integer.parseInt(bookIdParam);
                BooksDAO booksDAO = new BooksDAO();
                Books book = booksDAO.getBookById(bookId);
                if (book != null) {
                    request.setAttribute("selectedBook", book);
                } else {
                    request.setAttribute("errorMessage", "Book not found.");
                }
            }

            // Handle book search
            if (keyword != null && !keyword.trim().isEmpty()) {
                BooksDAO booksDAO = new BooksDAO();
                List<Books> booksList = booksDAO.searchBooks(keyword.trim());
                request.setAttribute("booksList", booksList);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid book ID format.");
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Database error occurred while retrieving borrowing history.");
            e.printStackTrace();
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An unexpected error occurred.");
            e.printStackTrace();
        }

        request.getRequestDispatcher(JSP_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Members currentMember = getCurrentMember(request, response);
        if (currentMember == null) return;

        try {
            // Extract and validate form parameters
            String bookIdStr = request.getParameter("bookId");
            String dueDateStr = request.getParameter("dueDate");

            if (bookIdStr == null || dueDateStr == null || bookIdStr.trim().isEmpty() || dueDateStr.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Missing required parameters.");
            } else {
                int bookId = Integer.parseInt(bookIdStr);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateFormat.setLenient(false);
                Date dueDate = dateFormat.parse(dueDateStr);
                Date borrowDate = new Date();

                // Validate due date
                if (dueDate.before(borrowDate)) {
                    request.setAttribute("errorMessage", "Due date cannot be before today's date.");
                } else {
                    BookCopiesDAO bookCopiesDAO = new BookCopiesDAO();
                    if (!bookCopiesDAO.hasAvailableCopy(bookId)) {
                        request.setAttribute("errorMessage", "No available copies of the selected book.");
                    } else {
                        int bookCopyId = bookCopiesDAO.getFirstAvailableBookCopy(bookId).getIdCopy();
                        Borrowing borrowing = new Borrowing(currentMember.getIdMember(), bookId, bookCopyId, borrowDate, dueDate, "Borrowed");

                        BorrowingDAO borrowingDAO = new BorrowingDAO();
                        boolean isSuccess = borrowingDAO.borrowBook(borrowing);

                        if (isSuccess) {
                            request.setAttribute("message", "Book borrowed successfully!");
                        } else {
                            request.setAttribute("errorMessage", "Failed to borrow the book. Check server logs for details.");
                        }
                    }
                }
            }

            // Refresh borrowing history
            BorrowingHistoryDAO historyDAO = new BorrowingHistoryDAO();
            List<BorrowingHistory> borrowingHistoryList = historyDAO.getBorrowingHistoryByMemberId(currentMember.getIdMember());
            request.setAttribute("borrowingHistoryList", borrowingHistoryList);

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid book ID format.");
        } catch (ParseException e) {
            request.setAttribute("errorMessage", "Invalid due date format. Please use yyyy-MM-dd.");
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Database error occurred. Check server logs for details.");
            e.printStackTrace();
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An unexpected error occurred. Check server logs for details.");
            e.printStackTrace();
        }

        request.getRequestDispatcher(JSP_PATH).forward(request, response);
    }
}