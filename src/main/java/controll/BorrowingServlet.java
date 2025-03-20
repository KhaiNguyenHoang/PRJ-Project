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

    // Centralized session check
    private Members getCurrentMember(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Members currentMember = (Members) request.getSession().getAttribute("user");
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
            List<BorrowingHistory> borrowingHistoryList = new BorrowingHistoryDAO().getBorrowingHistoryByMemberId(currentMember.getIdMember());
            request.setAttribute("borrowingHistoryList", borrowingHistoryList);

            // Handle book selection for borrowing form
            if (bookIdParam != null && !bookIdParam.isEmpty()) {
                int bookId = Integer.parseInt(bookIdParam);
                Books book = new BooksDAO().getBookById(bookId);
                if (book != null) {
                    request.setAttribute("selectedBook", book);
                } else {
                    request.setAttribute("errorMessage", "Book not found.");
                }
            }

            // Handle book search
            if (keyword != null && !keyword.isEmpty()) {
                List<Books> booksList = new BooksDAO().searchBooks(keyword);
                request.setAttribute("booksList", booksList);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid book ID.");
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An error occurred while processing your request.");
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

            if (bookIdStr == null || dueDateStr == null || bookIdStr.isEmpty() || dueDateStr.isEmpty()) {
                request.setAttribute("errorMessage", "Missing required parameters.");
            } else {
                int bookId = Integer.parseInt(bookIdStr);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dueDate = dateFormat.parse(dueDateStr);
                Date borrowDate = new Date();

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
                        request.setAttribute("errorMessage", "Failed to borrow the book. Please try again.");
                    }
                }
            }

            // Refresh borrowing history after borrowing attempt
            List<BorrowingHistory> borrowingHistoryList = new BorrowingHistoryDAO().getBorrowingHistoryByMemberId(currentMember.getIdMember());
            request.setAttribute("borrowingHistoryList", borrowingHistoryList);

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid book ID.");
        } catch (ParseException e) {
            request.setAttribute("errorMessage", "Invalid due date format. Please use yyyy-MM-dd.");
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An error occurred while processing your request.");
            e.printStackTrace();
        }

        request.getRequestDispatcher(JSP_PATH).forward(request, response);
    }
}