package controll;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "controll.Login", value = "/login ")
public class Login extends HttpServlet {
    private String message;

    public void init() {
        // Lấy tham số URL và tên từ init-param trong web.xml
        String initUrl = getServletConfig().getInitParameter("url");
        String initName = getServletConfig().getInitParameter("name");

        if (initUrl != null && initName != null) {
            message = "Received URL: " + initUrl + " and Name: " + initName;
        } else {
            message = "Hello World!";
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // In thông điệp lên trình duyệt
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    public void destroy() {
    }
}
