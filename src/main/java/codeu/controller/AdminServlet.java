// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.controller;

import codeu.model.data.User; // unused for now
import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet class responsible for the login page. */
public class AdminServlet extends HttpServlet {

  /** Store list with admin usernames. */
  // TODO(rafer45): Replace adminUsernames with better system
  private List<String> adminUsernames;

  /**
   * Set up state for handling admin page requests.
   */
  @Override
  public void init() throws ServletException {
    // Sets up the servlet
    super.init();

    // Hardcoded for now
    adminUsernames = Arrays.asList("drew", "elona", "jocelyn", "ricardo");
  }

  /**
   * Set up test state for handling admin page requests.
   * This is a very temporary, hardcoded solution for testing. It will be removed once
   * adminUsernames is moved to a more reasonable part of the code, like possibly a singleton.
   */
  public void test_init() {
    // TODO(rafer45): Remove test_init, replace with better system
    adminUsernames = Arrays.asList("drew", "elona", "jocelyn", "ricardo");
  }


  /**
   * This function fires when a user requests the /admin URL. It forwards the request to login.jsp
   * if the user is in the admin list, else it redirects to login. Currently a very insecure
   * barebones version
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    // TODO(rafer45): Investigate security implications of using Session
    String username = (String) request.getSession().getAttribute("user");

    if (adminUsernames.contains(username)) {
      // Let the user through
      request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
    } else {
      // Back to login
      response.sendRedirect("/login");
      return;
    }
  }
}
