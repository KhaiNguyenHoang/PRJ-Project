package controller.bookController;

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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Logger;

@WebServlet(name = "AddBookServlet", value = "/AddBook")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10, // 10MB (ngưỡng lưu tạm)
        maxFileSize = 1024 * 1024 * 100,  // 100MB cho mỗi file
        maxRequestSize = 1024 * 1024 * 200) // 200MB tổng request
public class AddBookServlet extends HttpServlet {
    private static final String PROJECT_BASE_DIR = "D:\\Study Space\\Code Saving\\New Folder\\PRJ-Project\\src\\main\\webapp";
    private static final String IMAGE_DIR = "Images";
    private static final String PDF_DIR = "PDF";
    private static final long MAX_IMAGE_SIZE = 2 * 1024 * 1024; // 2MB cho ảnh
    private static final long MAX_PDF_SIZE = 100 * 1024 * 1024;  // 100MB cho PDF
    private static final Logger LOGGER = Logger.getLogger(AddBookServlet.class.getName());
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
        response.setContentType("text/html;charset=UTF-8");

        // Debug: Kiểm tra xem request có phải multipart không
        LOGGER.info("Is multipart request: " + request.getContentType().contains("multipart"));

        // Debug: In tất cả tham số gửi từ form
        LOGGER.info("Received parameters:");
        request.getParameterMap().forEach((key, value) ->
                LOGGER.info(key + ": " + String.join(", ", value)));

        try {
            // Lấy dữ liệu trực tiếp từ request
            String title = request.getParameter("title");
            String author = request.getParameter("author");
            String isbn = request.getParameter("isbn");
            String publisher = request.getParameter("publisher");
            String yearPublishedStr = request.getParameter("yearPublished");
            String categoryIdStr = request.getParameter("categoryId");
            String copiesAvailableStr = request.getParameter("copiesAvailable");
            String isDigitalStr = request.getParameter("isDigital");

            // Debug: In giá trị từng tham số
            LOGGER.info("title: " + title);
            LOGGER.info("author: " + author);
            LOGGER.info("isbn: " + isbn);
            LOGGER.info("publisher: " + publisher);
            LOGGER.info("yearPublished: " + yearPublishedStr);
            LOGGER.info("categoryId: " + categoryIdStr);
            LOGGER.info("copiesAvailable: " + copiesAvailableStr);
            LOGGER.info("isDigital: " + isDigitalStr);

            // Xử lý dữ liệu, gán giá trị mặc định nếu null
            Books book = new Books();
            book.setTitle(title != null && !title.trim().isEmpty() ? title.trim() : "Untitled");
            book.setAuthor(author != null && !author.trim().isEmpty() ? author.trim() : "Unknown Author");
            book.setIsbn(isbn != null && !isbn.trim().isEmpty() ? isbn.trim() : "N/A");
            book.setPublisher(publisher != null && !publisher.trim().isEmpty() ? publisher.trim() : "Unknown Publisher");

            int yearPublished;
            try {
                yearPublished = yearPublishedStr != null ? Integer.parseInt(yearPublishedStr) : 2025;
            } catch (NumberFormatException e) {
                yearPublished = 2025; // Giá trị mặc định
            }
            book.setYearPublished(yearPublished);

            int categoryId;
            try {
                categoryId = categoryIdStr != null ? Integer.parseInt(categoryIdStr) : 1;
            } catch (NumberFormatException e) {
                categoryId = 1; // Giá trị mặc định
            }
            book.setCategoryId(categoryId);

            int copiesAvailable;
            try {
                copiesAvailable = copiesAvailableStr != null ? Integer.parseInt(copiesAvailableStr) : 1;
            } catch (NumberFormatException e) {
                copiesAvailable = 1; // Giá trị mặc định
            }
            book.setCopiesAvailable(copiesAvailable);

            boolean isDigital = Boolean.parseBoolean(isDigitalStr);
            book.setDigital(isDigital);
            book.setStatus("Available");

            // Xử lý file upload
            String baseDir = getBaseDir();
            String[] filePaths = handleFileUpload(request, baseDir, isDigital);

            // Gán đường dẫn file
            book.setFilePath(filePaths[0]);
            String pdfPath = filePaths[1];
            String description = request.getParameter("description");

            // Thêm sách
            boolean success = booksDAO.addBook(book, description, pdfPath);
            if (success) {
                LOGGER.info("Book added successfully: " + book.getTitle());
                request.getSession().setAttribute("success", "Book '" + book.getTitle() + "' added successfully!");
                response.sendRedirect(request.getContextPath() + "/HomePage");
            } else {
                throw new Exception("Failed to add book to database.");
            }
        } catch (Exception e) {
            LOGGER.severe("Error adding book: " + e.getMessage());
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("HomeHTML/HomeStaffHTML/AddBook.jsp").forward(request, response);
        }
    }

    private String getBaseDir() throws IOException {
        String baseDir = PROJECT_BASE_DIR;
        File baseDirFile = new File(baseDir);
        if (!baseDirFile.exists() && !baseDirFile.mkdirs()) {
            throw new IOException("Failed to create base directory: " + baseDir);
        }
        LOGGER.info("Base Dir: " + baseDir);
        return baseDir;
    }

    private String[] handleFileUpload(HttpServletRequest request, String baseDir, boolean isDigital) throws IOException, ServletException {
        String imageDir = baseDir + File.separator + IMAGE_DIR;
        String pdfDir = baseDir + File.separator + PDF_DIR;

        ensureDirectoryExists(imageDir, "Images");
        ensureDirectoryExists(pdfDir, "PDF");

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);

        // Xử lý file ảnh
        Part imagePart = request.getPart("imageFile");
        if (imagePart == null || imagePart.getSize() == 0) {
            throw new IOException("No image file uploaded.");
        }
        if (imagePart.getSize() > MAX_IMAGE_SIZE) {
            throw new IOException("Image file size exceeds 2MB.");
        }
        String imageFileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
        if (!imageFileName.toLowerCase().endsWith(".png")) {
            throw new IOException("Only PNG images are allowed.");
        }
        String uniqueImageFileName = "img_" + timestamp + "_" + uniqueId + ".png";
        String imagePath = IMAGE_DIR + "/" + uniqueImageFileName;
        String fullImagePath = imageDir + File.separator + uniqueImageFileName;
        saveFile(imagePart, fullImagePath, "Image");

        // Xử lý file PDF nếu isDigital
        String pdfPath = null;
        if (isDigital) {
            Part pdfPart = request.getPart("pdfFile");
            if (pdfPart == null || pdfPart.getSize() == 0) {
                throw new IOException("No PDF file uploaded for digital book.");
            }
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
            saveFile(pdfPart, fullPdfPath, "PDF");
        }

        return new String[]{imagePath, pdfPath};
    }

    private void ensureDirectoryExists(String dirPath, String dirName) throws IOException {
        File dir = new File(dirPath);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Failed to create " + dirName + " directory: " + dirPath);
        }
        if (!dir.canWrite()) {
            throw new IOException("No write permission for " + dirName + " directory: " + dirPath);
        }
    }

    private void saveFile(Part part, String fullPath, String fileType) throws IOException {
        File file = new File(fullPath);
        if (file.exists()) {
            LOGGER.warning(fileType + " file already exists, overwriting: " + fullPath);
        }
        Files.copy(part.getInputStream(), file.toPath());
        LOGGER.info(fileType + " saved to: " + fullPath);
    }
}