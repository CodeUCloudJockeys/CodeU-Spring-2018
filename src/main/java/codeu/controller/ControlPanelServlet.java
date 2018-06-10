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

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet class responsible for the admin page. */
public class ControlPanelServlet extends HttpServlet {

  /** Store class that gives access to Users. */
  private UserStore userStore;

  private List<User> userList;

  /** Set up state. */
  @Override
  public void init() throws ServletException {
    // Sets up the servlet
    super.init();
    setUserStore(UserStore.getInstance());
    userList = userStore.getUserList();
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    String username = (String) request.getSession().getAttribute("user");
    User user = userStore.getUser(username);

    if (user == null) {
      // Back to login
      response.sendRedirect("/login");
      return;
    } else if (!user.getIsAdmin()) {
      // Back to site index
      response.sendRedirect("/");
      return;
    }

    // TODO: Add pagination (this will get ridiculous with thousands of users)
    request.setAttribute("userList", userList);

    // Let the user through
    request.getRequestDispatcher("/WEB-INF/view/control_panel.jsp").forward(request, response);
  }
}
