package controller.bookController;

import dao.BooksDAO;
import dao.CategoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.Account;
import model.BookDetail;
import model.Books;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Logger;

@WebServlet(name = "ManageBookServlet", value = "/ManageBook")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10, // 10MB
        maxFileSize = 1024 * 1024 * 100,  // 100MB cho mỗi file
        maxRequestSize = 1024 * 1024 * 200) // 200MB tổng request
public class ManageBookServlet extends HttpServlet {
    private static final String PROJECT_BASE_DIR = "D:\\Study Space\\Code Saving\\New Folder\\PRJ-Project\\src\\main\\webapp";
    private static final String IMAGE_DIR = "Images";
    private static final String PDF_DIR = "PDF";
    private static final long MAX_IMAGE_SIZE = 2 * 1024 * 1024; // 2MB cho ảnh
    private static final long MAX_PDF_SIZE = 100 * 1024 * 1024;  // 100MB cho PDF
    private static final Logger LOGGER = Logger.getLogger(ManageBookServlet.class.getName());
    private BooksDAO booksDAO;
    private CategoryDAO categoryDAO;

    @Override
    public void init() throws ServletException {
        booksDAO = new BooksDAO();
        categoryDAO = new CategoryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Account account = (Account) req.getSession().getAttribute("account");
        if (account == null) {
            resp.sendRedirect("HomePage");
            return;
        }
        String action = req.getParameter("action");
        if (action == null || action.equals("list")) {
            // Hiển thị danh sách sách
            req.setAttribute("books", booksDAO.getAllBooks());
            req.getRequestDispatcher("HomeHTML/HomeStaffHTML/ManageBooks.jsp").forward(req, resp);
        } else if (action.equals("edit")) {
            // Hiển thị form chỉnh sửa
            int id = Integer.parseInt(req.getParameter("id"));
            Books book = booksDAO.getBookById(id);
            if (book == null) {
                req.setAttribute("error", "Book not found.");
                req.getRequestDispatcher("HomeHTML/HomeStaffHTML/ManageBooks.jsp").forward(req, resp);
                return;
            }
            req.setAttribute("book", book);
            req.setAttribute("categories", categoryDAO.getAllCategories());
            req.getRequestDispatcher("HomeHTML/HomeStaffHTML/ManageBooks.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Books book = booksDAO.getBookById(id);
        if (book == null) {
            req.setAttribute("error", "Book not found.");
            req.getRequestDispatcher("HomeHTML/HomeStaffHTML/ManageBooks.jsp").forward(req, resp);
            return;
        }

        // Cập nhật thông tin sách (không sửa IdBook, ISBN, CopiesAvailable)
        book.setTitle(req.getParameter("title"));
        book.setAuthor(req.getParameter("author"));
        book.setPublisher(req.getParameter("publisher"));
        book.setYearPublished(Integer.parseInt(req.getParameter("yearPublished")));
        book.setCategoryId(Integer.parseInt(req.getParameter("categoryId")));
        book.setDigital(Boolean.parseBoolean(req.getParameter("isDigital")));
        book.setStatus(req.getParameter("status"));

        // Cập nhật thông tin BookDetail
        String description = req.getParameter("description");
        if (book.getBookDetail() == null) {
            book.setBookDetail(new BookDetail(book.getIdBook(), description, null));
        } else {
            book.getBookDetail().setDescription(description);
        }

        // Xử lý file upload
        String baseDir = PROJECT_BASE_DIR;
        String[] filePaths = handleFileUpload(req, baseDir, book.isDigital(), book.getFilePath(),
                book.getBookDetail() != null ? book.getBookDetail().getPdfPath() : null);
        book.setFilePath(filePaths[0]);
        book.getBookDetail().setPdfPath(filePaths[1]);

        // Cập nhật sách
        boolean success = booksDAO.updateBook(book, book.getBookDetail().getDescription(), book.getBookDetail().getPdfPath());
        if (success) {
            LOGGER.info("Book updated successfully: " + book.getTitle());
            req.getSession().setAttribute("success", "Book '" + book.getTitle() + "' updated successfully!");
            resp.sendRedirect(req.getContextPath() + "/ManageBook");
        } else {
            req.setAttribute("error", "Failed to update book.");
            req.setAttribute("book", book);
            req.setAttribute("categories", categoryDAO.getAllCategories());
            req.getRequestDispatcher("HomeHTML/HomeStaffHTML/ManageBooks.jsp").forward(req, resp);
        }
    }

    private String[] handleFileUpload(HttpServletRequest request, String baseDir, boolean isDigital, String oldImagePath, String oldPdfPath) throws IOException, ServletException {
        String imageDir = baseDir + File.separator + IMAGE_DIR;
        String pdfDir = baseDir + File.separator + PDF_DIR;

        File imageDirFile = new File(imageDir);
        if (!imageDirFile.exists()) imageDirFile.mkdirs();
        File pdfDirFile = new File(pdfDir);
        if (!pdfDirFile.exists()) pdfDirFile.mkdirs();

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);

        // Xử lý file ảnh
        String imagePath = oldImagePath;
        Part imagePart = request.getPart("imageFile");
        if (imagePart != null && imagePart.getSize() > 0) {
            if (imagePart.getSize() > MAX_IMAGE_SIZE) {
                throw new IOException("Image file size exceeds 2MB.");
            }
            String imageFileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
            if (!imageFileName.toLowerCase().endsWith(".png")) {
                throw new IOException("Only PNG images are allowed.");
            }
            String uniqueImageFileName = "img_" + timestamp + "_" + uniqueId + ".png";
            imagePath = IMAGE_DIR + "/" + uniqueImageFileName;
            String fullImagePath = imageDir + File.separator + uniqueImageFileName;
            Files.copy(imagePart.getInputStream(), Paths.get(fullImagePath));
            if (oldImagePath != null && !oldImagePath.equals(imagePath)) {
                new File(baseDir + File.separator + oldImagePath).delete();
            }
        }

        // Xử lý file PDF
        String pdfPath = oldPdfPath;
        if (isDigital) {
            Part pdfPart = request.getPart("pdfFile");
            if (pdfPart != null && pdfPart.getSize() > 0) {
                if (pdfPart.getSize() > MAX_PDF_SIZE) {
                    throw new IOException("PDF file size exceeds 100MB.");
                }
                String pdfFileName = Paths.get(pdfPart.getSubmittedFileName()).getFileName().toString();
                if (!pdfFileName.toLowerCase().endsWith(".pdf")) {
                    throw new IOException("Only PDF files are allowed.");
                }
                String uniquePdfFileName = "pdf_" + timestamp + "_" + uniqueId + ".pdf";
                pdfPath = PDF_DIR + "/" + uniquePdfFileName;
                String fullPdfPath = pdfDir + File.separator + uniquePdfFileName;
                Files.copy(pdfPart.getInputStream(), Paths.get(fullPdfPath));
                if (oldPdfPath != null && !oldPdfPath.equals(pdfPath)) {
                    new File(baseDir + File.separator + oldPdfPath).delete();
                }
            }
        } else if (oldPdfPath != null) {
            new File(baseDir + File.separator + oldPdfPath).delete();
            pdfPath = null;
        }

        return new String[]{imagePath, pdfPath};
    }
}