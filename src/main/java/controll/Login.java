package controll;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "controll.Login", value = "/controll.Login ")
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

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Gửi thông điệp tới client.
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Gửi thông điệp tới client.
    }

    @Override
    public void destroy() {
    }
}
