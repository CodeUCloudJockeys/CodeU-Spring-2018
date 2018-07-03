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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import codeu.model.data.User;
import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet class responsible for Activity feed page */
public class ActivityFeedServlet extends HttpServlet {
  /**
   * variable used to keep track of activity, I'm thinking to create more variables as we go to
   * register different types of activities
   */

  /** Store class that gives access to Users. */
  private UserStore userStore;

  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;

  /** Store class that gives access to Messages. */
  private MessageStore messageStore;

  /** List of all activities */
  private List<String> activities;




  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(userStore.getInstance());
    setConversationStore(conversationStore.getInstance());
    setMessageStore(messageStore.getInstance());
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore){
    this.userStore = userStore;
  }

  /**
   * Sets the MessageStore used by this servlet. This function provides a common setup method for
   * use by the test framework or the servlet's init() function.
   */
  void setMessageStore(MessageStore messageStore){
    this.messageStore = messageStore;
  }

  /**
   * Sets the ConversationStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setConversationStore(ConversationStore conversationStore){
    this.conversationStore = conversationStore;
  }
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String username = (String) request.getSession().getAttribute("user");

    if (username == null) {
      response.sendRedirect("/login");
      return;
    } else {
      request.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp").forward(request, response);
    }
    List<Conversation> conversations = conversationStore.getConversationList();
    List<User> users = userStore.getUserList();

    for(Conversation conversation : conversations){
      activities.add(conversation.getTitle());
    }

    for(User user : users){
      activities.add(user.getName());
    }

    request.setAttribute("activities", activities);
    request.getRequestDispatcher("WEB-INF/view/activityfeed.jsp");

  }
}
