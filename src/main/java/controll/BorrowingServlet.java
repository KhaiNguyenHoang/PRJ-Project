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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet(name = "BorrowingServlet", value = "/Borrowing")
public class BorrowingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String JSP_PATH = "HomeHTML/HomeMemberHTML/Borrowing.jsp";

    // Handle GET request: search for books or display borrowing history
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        String bookIdParam = request.getParameter("id");

        Members currentMember = (Members) request.getSession().getAttribute("user");

        if (currentMember == null) {
            response.sendRedirect("/Auth/SignIn-SignUp.jsp");
            return;
        }

        try {
            if (bookIdParam != null && !bookIdParam.isEmpty()) {
                int bookId = Integer.parseInt(bookIdParam);
                Books book = new BooksDAO().getBookById(bookId);
                if (book != null) {
                    request.setAttribute("selectedBook", book);
                } else {
                    request.setAttribute("errorMessage", "Book not found.");
                }
            } else {
                // Display borrowing history
                List<BorrowingHistory> borrowingHistoryList = new BorrowingHistoryDAO().getBorrowingHistoryByMemberId(currentMember.getIdMember());
                request.setAttribute("borrowingHistoryList", borrowingHistoryList);

                // If search keyword is provided, search for books
                if (keyword != null && !keyword.isEmpty()) {
                    List<Books> booksList = new BooksDAO().searchBooks(keyword);
                    request.setAttribute("booksList", booksList);
                }
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid book ID.");
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An error occurred while processing your request.");
            e.printStackTrace(); // Log the exception for debugging
        }

        // Forward to Borrowing.jsp
        request.getRequestDispatcher(JSP_PATH).forward(request, response);
    }

    // Handle POST request: borrow a book after selecting from the form
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Members currentMember = (Members) request.getSession().getAttribute("user");

        if (currentMember == null) {
            response.sendRedirect("/Auth/SignIn-SignUp.jsp");
            return;
        }

        try {
            // Extract and validate form parameters
            String bookIdStr = request.getParameter("bookId");
            String dueDateStr = request.getParameter("dueDate");

            if (bookIdStr == null || dueDateStr == null || bookIdStr.isEmpty() || dueDateStr.isEmpty()) {
                request.setAttribute("errorMessage", "Missing required parameters.");
                request.getRequestDispatcher(JSP_PATH).forward(request, response);
                return;
            }

            int bookId = Integer.parseInt(bookIdStr);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dueDate = dateFormat.parse(dueDateStr);
            Date borrowDate = new Date(); // Current date as borrow date

            BookCopiesDAO bookCopiesDAO = new BookCopiesDAO();

            // Check book availability
            if (!bookCopiesDAO.hasAvailableCopy(bookId)) {
                request.setAttribute("errorMessage", "No available copies of the selected book.");
                request.getRequestDispatcher(JSP_PATH).forward(request, response);
                return;
            }

            // Get the first available copy of the book
            int bookCopyId = bookCopiesDAO.getFirstAvailableBookCopy(bookId).getIdCopy();

            // Create the Borrowing object
            Borrowing borrowing = new Borrowing(currentMember.getIdMember(), bookId, bookCopyId, borrowDate, dueDate, "Borrowed");

            // Process the borrowing request
            BorrowingDAO borrowingDAO = new BorrowingDAO();
            boolean isSuccess = borrowingDAO.borrowBook(borrowing);

            if (isSuccess) {
                request.setAttribute("message", "Book borrowed successfully!");
            } else {
                request.setAttribute("errorMessage", "Failed to borrow the book. Please try again.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid book ID.");
        } catch (ParseException e) {
            request.setAttribute("errorMessage", "Invalid due date format. Please use yyyy-MM-dd.");
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An error occurred while processing your request.");
            e.printStackTrace(); // Log the exception for debugging
        }

        // Forward back to Borrowing.jsp to display the result
        request.getRequestDispatcher(JSP_PATH).forward(request, response);
    }
}