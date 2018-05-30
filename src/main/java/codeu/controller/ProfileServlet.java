package codeu.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** This servlet class is responsible the profile pages */
public class ProfileServlet extends HttpServlet {

  /**
   * From my perspective this variable is used to keep track of who is looking at the profile page.
   * If the account and user match then they will be allowed to edit their profile page.
   */
  private String name;

  @Override
  public void init() throws ServletException {
    // Sets ProfileServlet
    super.init();
    // username of the profile page hardcoded
    name = "exusername";
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    /**
     * The hardcoded 'name' is the username of the profile page If this matches with 'username' it
     * will allow an editing feature of the profile. I will implement this later.
     */
    String username = (String) request.getSession().getAttribute("user");
    /**
     * For now, if a user does not exist, they will not be able to view the profile page at all.
     * Otherwise, any user will be able to view the prototype.
     */
    if (username == null) {
      response.sendRedirect("/login");
    } else {
      request.getRequestDispatcher("/WEB-INF/view/profile.jsp").forward(request, response);
    }
  }
}
