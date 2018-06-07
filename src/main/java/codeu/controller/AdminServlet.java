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

import codeu.model.data.Message;
import codeu.model.data.User; // unused for now
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
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

  /** Data items with their labels */
  private Map<String, String> labeledStats;

  /** Set up state for handling admin page requests. */
  @Override
  public void init() throws ServletException {
    // Sets up the servlet
    super.init();
    setUserStore(UserStore.getInstance());
    setMessageStore(MessageStore.getInstance());
    setConversationStore(ConversationStore.getInstance());

    initLabeledStats();
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

  void initLabeledStats() {
    labeledStats = new LinkedHashMap<>();
  }

  /**
   * This function fires when a user requests the /admin URL. It forwards the request to login.jsp
   * if the user is in the admin list, else it redirects to login. Currently a very insecure
   * barebones version
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    String username = (String) request.getSession().getAttribute("user");
    User user = userStore.getUser(username);

    // If user does not exist or user isn't admin
    if (user == null || !user.getIsAdmin()) {
      // Back to login
      response.sendRedirect("/login");
      return;
    }

    int userCount = userStore.Count();
    int messageCount = messageStore.Count();
    int conversationCount = conversationStore.Count();

    // TODO: Optimize this later
    // This currently will calculate the wordiest/most active/newest users
    // by looking at all users and comparing them. This calculation will be really slow
    // with many users, and it will be done every single time the admin page is opened.

    // Later I'll just add a check in when a user posts a message comparing the user's word count
    // with the word count of the wordiest user. Most active user will work similarly, and newest
    // user will be set upon valid user creation.
    // UserStore will be in charge of keeping track of the state involved with superlative users.
    List<User> users = userStore.getUserList();

    String newestUserName =
        getNewestUser(users)
            .map(User::getName) // Get the name if the user exists
            .orElse("No users."); // return "No users" if it does not exist.

    String mostActiveUserName = getMostActiveUser(users).map(User::getName).orElse("No users.");

    String wordiestUserName = getWordiestUser(users).map(User::getName).orElse("No users.");

    // Set the stats
    labeledStats.put("Number of users:", Integer.toString(userCount));
    labeledStats.put("Number of messages:", Integer.toString(messageCount));
    labeledStats.put("Number of conversations:", Integer.toString(conversationCount));

    labeledStats.put("Newest user:", newestUserName);
    labeledStats.put("Most active user:", mostActiveUserName);
    labeledStats.put("Wordiest user:", wordiestUserName);

    request.setAttribute("labeledStats", labeledStats);

    // Let the user through
    request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
  }

  /**
   * The code below uses the new Java 8 features of Optional and Stream.reduce() Documentation for
   * both can be found at the following links:
   * https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html
   * https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html#reduce-java.util.function.BinaryOperator-
   */

  /**
   * Gets the maximum user from a list by a given metric, or an empty Optional if the user list is
   * empty.
   *
   * <p>The odd method signature is to point out it takes a type parameter (namely, the return type
   * of the metric). It guarantees the return type of the metric is comparable. A bit of this is
   * explained here:
   * https://stackoverflow.com/questions/21854077/java-generics-method-signature-explanation
   */
  private <U extends Comparable<? super U>> Optional<User> getMaxUserBy(
      List<User> users, Function<? super User, U> metric) {

    // Compares the result of applying the metric to the user
    // for example, comparator.comparing(user -> user.getId()) would sort by id.
    Comparator<User> userComparator = Comparator.comparing(metric);

    // Return the "maximum" user under the specified metric.
    return users.stream().max(userComparator);
  }

  /** Gets the newest user from a list, namely the user with the largest creation second. */
  private Optional<User> getNewestUser(List<User> users) {
    return getMaxUserBy(users, this::getUserCreationSecond);
  }

  /** Gets a user's creation second */
  private long getUserCreationSecond(User user) {
    return user.getCreationTime().getEpochSecond();
  }

  /** Gets the most active user from a list, namely the user with the largest message count. */
  private Optional<User> getMostActiveUser(List<User> users) {
    return getMaxUserBy(users, this::getUserMessageCount);
  }

  /** Gets amount of messages sent by a user */
  private int getUserMessageCount(User user) {
    return messageStore.getMessagesByUser(user.getId()).size();
  }

  /** Gets the wordiest user from a list, namely the user with the largest word count. */
  private Optional<User> getWordiestUser(List<User> users) {
    return getMaxUserBy(users, this::getUserWordCount);
  }

  /** Gets a user's word count */
  private int getUserWordCount(User user) {
    return messageStore
        .getMessagesByUser(user.getId())
        .stream()
        .map(Message::getContent)
        .mapToInt(this::countWords)
        .sum();
  }

  /** Counts the words in a string */
  private int countWords(String words) {
    int count = words.trim().split("\\s+").length;
    return count;
  }
}
