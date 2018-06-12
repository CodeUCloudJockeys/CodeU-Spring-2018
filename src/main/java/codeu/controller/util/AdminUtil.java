package codeu.controller.util;

import codeu.model.data.User;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

public class AdminUtil {

  public static boolean redirectNonAdmins (User user, HttpServletResponse response)
      throws IOException {

    if (user == null) {
      // Back to login
      response.sendRedirect("/login");
      return true;
    } else if (!user.getIsAdmin()) {
      // Back to site index
      response.sendRedirect("/");
      return true;
    }

    return false;
  }

}
