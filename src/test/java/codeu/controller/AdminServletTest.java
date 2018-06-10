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
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class AdminServletTest {

  private AdminServlet adminServlet;
  private HttpServletRequest mockRequest;
  private HttpSession mockSession;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
  private UserStore mockUserStore;
  private MessageStore mockMessageStore;
  private ConversationStore mockConversationStore;

  private User notAdmin;
  private User admin;
  private User wordy;

  private List<User> userList;

  private List<Message> notAdminMessages;
  private List<Message> adminMessages;
  private List<Message> wordyMessages;

  @Before
  public void setup() {

    adminServlet = new AdminServlet();
    adminServlet.initLabeledStats();

    notAdmin =
        new User(
            UUID.randomUUID(),
            "notadmin",
            "$2a$10$.e.4EEfngEXmxAO085XnYOmDntkqod0C384jOR9oagwxMnPNHaGLa",
            Instant.MAX,
            false);
    notAdminMessages = new LinkedList<>();

    admin =
        new User(
            UUID.randomUUID(),
            "admin",
            "$2a$10$.e.4EEfngEXmxAO085XnYOmDntkqod0C384jOR9oagwxMnPNHaGLa",
            Instant.now(),
            true);
    adminMessages = new LinkedList<>();
    adminMessages.add(
        new Message(UUID.randomUUID(), UUID.randomUUID(), admin.getId(), "hello", Instant.now()));
    adminMessages.add(
        new Message(UUID.randomUUID(), UUID.randomUUID(), admin.getId(), "world", Instant.now()));

    wordy =
        new User(
            UUID.randomUUID(),
            "wordy",
            "$2a$10$.e.4EEfngEXmxAO085XnYOmDntkqod0C384jOR9oagwxMnPNHaGLa",
            Instant.now(),
            false);
    wordyMessages = new LinkedList<>();
    wordyMessages.add(
        new Message(
            UUID.randomUUID(), UUID.randomUUID(), wordy.getId(), "yo what up", Instant.now()));

    userList = new ArrayList<>();
    userList.add(notAdmin);
    userList.add(admin);
    userList.add(wordy);

    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockResponse = Mockito.mock(HttpServletResponse.class);

    mockSession = Mockito.mock(HttpSession.class);
    // When AdminServlet tries to get the session, just return the mock session.
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    /* When AdminServlet tries to forward to the dispatcher of "/WEB-INF/view/admin.jsp", just
     * return the mock dispatcher.
     */
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/admin.jsp"))
        .thenReturn(mockRequestDispatcher);

    mockUserStore = Mockito.mock(UserStore.class);
    Mockito.when(mockUserStore.getUser("notadmin")).thenReturn(notAdmin);
    Mockito.when(mockUserStore.getUser("admin")).thenReturn(admin);
    Mockito.when(mockUserStore.getUser("wordy")).thenReturn(wordy);
    Mockito.when(mockUserStore.getUser("nobody")).thenReturn(null);
    Mockito.when(mockUserStore.getUserList()).thenReturn(userList);
    Mockito.when(mockUserStore.Count()).thenReturn(3);

    adminServlet.setUserStore(mockUserStore);

    mockMessageStore = Mockito.mock(MessageStore.class);
    Mockito.when(mockMessageStore.getMessagesByUser(notAdmin.getId())).thenReturn(notAdminMessages);
    Mockito.when(mockMessageStore.getMessagesByUser(admin.getId())).thenReturn(adminMessages);
    Mockito.when(mockMessageStore.getMessagesByUser(wordy.getId())).thenReturn(wordyMessages);

    Mockito.when(mockMessageStore.Count()).thenReturn(1337);

    adminServlet.setMessageStore(mockMessageStore);

    mockConversationStore = Mockito.mock(ConversationStore.class);
    Mockito.when(mockConversationStore.Count()).thenReturn(5040);

    adminServlet.setConversationStore(mockConversationStore);
  }

  @Test
  public void testDoGet_NoUser() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn("nobody");

    adminServlet.doGet(mockRequest, mockResponse);

    // Verify stats are NOT added to request
    Mockito.verify(mockRequest, Mockito.never())
        .setAttribute(Mockito.eq("labeledStats"), Mockito.any());

    // Verify user is *NOT* forwarded to admin page
    Mockito.verify(mockRequestDispatcher, Mockito.never()).forward(mockRequest, mockResponse);

    // Verify user is redirected to login page
    Mockito.verify(mockResponse).sendRedirect("/login");
  }

  @Test
  public void testDoGet_NotAdminUsername() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn("notadmin");

    adminServlet.doGet(mockRequest, mockResponse);

    // Verify stats are NOT added to request
    Mockito.verify(mockRequest, Mockito.never())
        .setAttribute(Mockito.eq("labeledStats"), Mockito.any());

    // Verify user is *NOT* forwarded to admin page
    Mockito.verify(mockRequestDispatcher, Mockito.never()).forward(mockRequest, mockResponse);

    // Verify user is redirected to index page
    Mockito.verify(mockResponse).sendRedirect("/");
  }

  @Test
  public void testDoGet_AdminUsername() throws IOException, ServletException {

    User user =
        new User(
            UUID.randomUUID(),
            "admin",
            "$2a$10$.e.4EEfngEXmxAO085XnYOmDntkqod0C384jOR9oagwxMnPNHaGLa",
            Instant.now(),
            true);

    Mockito.when(mockSession.getAttribute("user")).thenReturn("admin");

    Mockito.when(mockUserStore.getUser("admin")).thenReturn(user);

    adminServlet.doGet(mockRequest, mockResponse);

    // Capture the input to setAttribute
    ArgumentCaptor<Map> argumentCaptor = ArgumentCaptor.forClass(Map.class);
    Mockito.verify(mockRequest).setAttribute(Mockito.eq("labeledStats"), argumentCaptor.capture());

    Map<String, String> expectedMap = new LinkedHashMap<>();
    expectedMap.put("Number of users:", "3");
    expectedMap.put("Number of messages:", "1337");
    expectedMap.put("Number of conversations:", "5040");
    expectedMap.put("Newest user:", "notadmin");
    expectedMap.put("Most active user:", "admin");
    expectedMap.put("Wordiest user:", "wordy");

    // Compare with what is expected
    Assert.assertEquals(expectedMap, argumentCaptor.getValue());

    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }
}
