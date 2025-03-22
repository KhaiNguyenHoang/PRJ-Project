package controll;

import dao.BooksDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.Books;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@WebServlet(name = "AddBookServlet", value = "/AddBook")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50) // 50MB
public class AddBookServlet extends HttpServlet {
    private static final String IMAGE_DIR = "Images";
    private static final String PDF_DIR = "PDF";
    private BooksDAO booksDAO;

    @Override
    public void init() throws ServletException {
        booksDAO = new BooksDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("HomeHTML/HomeStaffHTML/AddBook.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy dữ liệu từ form
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String isbn = request.getParameter("isbn");
        String publisher = request.getParameter("publisher");
        int yearPublished = Integer.parseInt(request.getParameter("yearPublished"));
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        int copiesAvailable = Integer.parseInt(request.getParameter("copiesAvailable"));
        boolean isDigital = Boolean.parseBoolean(request.getParameter("isDigital"));
        String description = request.getParameter("description");

        // Đường dẫn lưu file trong webapp
        String appPath = request.getServletContext().getRealPath("");
        String imageDir = appPath + File.separator + IMAGE_DIR;
        String pdfDir = appPath + File.separator + PDF_DIR;

        // Tạo thư mục nếu chưa tồn tại
        File imageDirFile = new File(imageDir);
        if (!imageDirFile.exists()) {
            imageDirFile.mkdirs();
        }
        File pdfDirFile = new File(pdfDir);
        if (!pdfDirFile.exists()) {
            pdfDirFile.mkdirs();
        }

        // Xử lý file ảnh minh họa
        Part imagePart = request.getPart("imageFile");
        String imageFileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
        String imagePath = IMAGE_DIR + File.separator + imageFileName;
        imagePart.write(imageDir + File.separator + imageFileName);

        // Xử lý file PDF nếu isDigital = true
        String pdfPath = null;
        if (isDigital) {
            Part pdfPart = request.getPart("pdfFile");
            String pdfFileName = Paths.get(pdfPart.getSubmittedFileName()).getFileName().toString();
            pdfPath = PDF_DIR + File.separator + pdfFileName;
            pdfPart.write(pdfDir + File.separator + pdfFileName);
        }

        // Tạo đối tượng Books
        Books book = new Books();
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setPublisher(publisher);
        book.setYearPublished(yearPublished);
        book.setCategoryId(categoryId);
        book.setCopiesAvailable(copiesAvailable);
        book.setDigital(isDigital);
        book.setFilePath(imagePath); // FilePath lưu ảnh minh họa
        book.setStatus("Available"); // Trạng thái mặc định

        // Thêm sách và chi tiết sách
        try {
            boolean success = booksDAO.addBook(book, description, pdfPath);
            if (success) {
                response.sendRedirect("HomePage");
            } else {
                request.setAttribute("error", "Failed to add book.");
                request.getRequestDispatcher("HomeHTML/HomeStaffHTML/AddBook.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while adding the book.");
            request.getRequestDispatcher("HomeHTML/HomeStaffHTML/AddBook.jsp").forward(request, response);
        }
    }
}