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
import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User; // unused for now
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet class responsible for the admin page. */
public class AdminServlet extends HttpServlet {

  /** Store class that gives access to Users. */
  private UserStore userStore;

  /** Store class that gives access to Messages. */
  private MessageStore messageStore;

  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;

  /**
   * Set up state for handling admin page requests.
   */
  @Override
  public void init() throws ServletException {
    // Sets up the servlet
    super.init();
    setUserStore(UserStore.getInstance());
    setMessageStore(MessageStore.getInstance());
    setConversationStore(ConversationStore.getInstance());
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * Sets the MessageStore used by this servlet. This function provides a common setup method for
   * use by the test framework or the servlet's init() function.
   */
  void setMessageStore(MessageStore messageStore) {
    this.messageStore = messageStore;
  }

  /**
   * Sets the ConversationStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
  }

  /**
>>>>>>> ea7ba240a1be24824241b46686ed62675421fde2
   * This function fires when a user requests the /admin URL. It forwards the request to login.jsp
   * if the user is in the admin list, else it redirects to login. Currently a very insecure
   * barebones version
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    // TODO(rafer45): Investigate security implications of using Session
    String username = (String) request.getSession().getAttribute("user");
    User user = userStore.getUser(username);

    int userCount = userStore.Count();
    int messageCount = messageStore.Count();
    int conversationCount = conversationStore.Count();

    if (user != null && user.getIsAdmin()) {

      // Set the stats
      request.setAttribute("userCount", userCount);
      request.setAttribute("messageCount", messageCount);
      request.setAttribute("conversationCount", conversationCount);
      // Let the user through
      request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
    } else {
      // Back to login
      response.sendRedirect("/login");
      return;
    }
  }
}
