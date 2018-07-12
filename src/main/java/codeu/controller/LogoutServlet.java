package codeu.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException{
        String username = (String) request.getSession().getAttribute("user");
        if(username == null){
            response.sendRedirect("/login");
            return;
        }
        request.getSession().invalidate();
        response.sendRedirect("/login");
    }
}
