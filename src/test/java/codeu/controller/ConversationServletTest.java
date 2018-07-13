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

import codeu.model.data.Activity;
import codeu.model.data.Conversation;
import codeu.model.data.User;
import codeu.model.store.basic.ActivityStore;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

public class ConversationServletTest {

  private ConversationServlet conversationServlet;
  private HttpServletRequest mockRequest;
  private HttpSession mockSession;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
  private ConversationStore mockConversationStore;
  private UserStore mockUserStore;

  private Conversation publicConversation;
  private Conversation privateConversation;

  private User testConversationCreator;

  @Before
  public void setup() {
    conversationServlet = new ConversationServlet();

    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/conversations.jsp"))
        .thenReturn(mockRequestDispatcher);

    mockUserStore = Mockito.mock(UserStore.class);

    testConversationCreator = new User(UUID.randomUUID(), "test_conversation_creator",
        "password", Instant.now(), true);
    Mockito.when(mockUserStore.getUser("test_conversation_creator"))
        .thenReturn(testConversationCreator);

    mockConversationStore = Mockito.mock(ConversationStore.class);
    conversationServlet.setConversationStore(mockConversationStore);

    publicConversation = new Conversation(UUID.randomUUID(), testConversationCreator.getId(),
        "test_public_conversation", Instant.now(), false);
    Mockito.when(mockConversationStore.getConversationWithTitle("test_public_conversation"))
        .thenReturn(publicConversation);

    privateConversation = new Conversation(
        UUID.randomUUID(), testConversationCreator.getId(),
        "test_private_conversation", Instant.now(), true);
    Mockito.when(mockConversationStore.getConversationWithTitle("test_private_conversation"))
        .thenReturn(privateConversation);

    conversationServlet.setUserStore(mockUserStore);
  }

  @Test
  public void testDoGet_UserNotLoggedIn() throws IOException, ServletException {
    List<Conversation> fakeConversationList = new ArrayList<>();
    fakeConversationList.add(publicConversation);
    fakeConversationList.add(privateConversation);

    List<Conversation> publicConversationList = new ArrayList<>();
    publicConversationList.add(publicConversation);

    Mockito.when(mockConversationStore.getAllConversations()).thenReturn(fakeConversationList);

    conversationServlet.doGet(mockRequest, mockResponse);

    // For storing whatever is passed as the conversation list
    ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);

    Mockito.verify(mockRequest)
        .setAttribute(Mockito.eq("conversations"), argumentCaptor.capture());

    // Compare with what is expected
    Assert.assertEquals(publicConversationList, argumentCaptor.getValue());

    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  // TODO: Add more tests

  @Test
  public void testDoPost_UserNotLoggedIn() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn(null);

    conversationServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockConversationStore, Mockito.never())
        .addConversation(Mockito.any(Conversation.class));
    Mockito.verify(mockResponse).sendRedirect("/conversations");
  }

  @Test
  public void testDoPost_InvalidUser() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(null);
    Mockito.when(mockRequest.getParameter("add_users")).thenReturn("bob alice");

    conversationServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockConversationStore, Mockito.never())
        .addConversation(Mockito.any(Conversation.class));
    Mockito.verify(mockResponse).sendRedirect("/conversations");
  }

  @Test
  public void testDoPost_BadConversationName() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("conversationTitle")).thenReturn("bad !@#$% name");
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");

    User fakeUser = new User(UUID.randomUUID(), "test_username", "test_username", Instant.now());
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(fakeUser);

    conversationServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockConversationStore, Mockito.never())
        .addConversation(Mockito.any(Conversation.class));
    Mockito.verify(mockRequest).setAttribute("error", "Please enter only letters and numbers.");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoPost_ConversationNameTaken() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("conversationTitle")).thenReturn("test_conversation");
    Mockito.when(mockRequest.getParameter("add_users")).thenReturn("bob alice");
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");
    Mockito.when(mockRequest.getParameter("conversationUserAdded")).thenReturn("test_user_added");

    User fakeUser = new User(UUID.randomUUID(), "test_username", "test_username", Instant.now());
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(fakeUser);

    Mockito.when(mockConversationStore.isTitleTaken("test_conversation")).thenReturn(true);

    conversationServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockConversationStore, Mockito.never())
        .addConversation(Mockito.any(Conversation.class));
    Mockito.verify(mockResponse).sendRedirect("/chat/test_conversation");
  }

  @Test
  public void testDoPost_NewPublicConversation() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("conversationTitle"))
        .thenReturn("test_conversation");

    Mockito.when(mockRequest.getParameter("add_users")).thenReturn("bob alice");

    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");

    User fakeUser = new User(UUID.randomUUID(), "test_username", "password", Instant.now());
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(fakeUser);
    Mockito.when(mockConversationStore.isTitleTaken("test_conversation")).thenReturn(false);

    ActivityStore mockActivityStore = Mockito.mock(ActivityStore.class);
    conversationServlet.setActivityStore(mockActivityStore);

    conversationServlet.doPost(mockRequest, mockResponse);

    ArgumentCaptor<Conversation> conversationArgumentCaptor =
        ArgumentCaptor.forClass(Conversation.class);
    Mockito.verify(mockConversationStore).addConversation(conversationArgumentCaptor.capture());
    Assert.assertEquals(conversationArgumentCaptor.getValue().getTitle(), "test_conversation");

    ArgumentCaptor<Activity> activityArgumentCaptor = ArgumentCaptor.forClass(Activity.class);

    Mockito.verify(mockActivityStore).addActivity(activityArgumentCaptor.capture());
    Assert.assertEquals("New conversation: test_conversation", activityArgumentCaptor.getValue().getActivityMessage());

    Mockito.verify(mockResponse).sendRedirect("/chat/test_conversation");
  }

  @Test
  public void testDoPost_NewPrivateConversation() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_conversation_creator");

    Mockito.when(mockRequest.getParameter("conversationTitle")).thenReturn("test_new_private_conversation");
    Mockito.when(mockRequest.getParameter("add_users")).thenReturn("bob alice");
    Mockito.when(mockRequest.getParameter("privatize")).thenReturn("notnull");

    User bobUser = new User(UUID.randomUUID(), "bob", "passbob", Instant.now());
    User aliceUser = new User(UUID.randomUUID(), "alice", "palice", Instant.now());
    Mockito.when(mockUserStore.getUser("bob")).thenReturn(bobUser);
    Mockito.when(mockUserStore.getUser("alice")).thenReturn(aliceUser);
    Mockito.when(mockUserStore.isUserRegistered("bob")).thenReturn(true);
    Mockito.when(mockUserStore.isUserRegistered("alice")).thenReturn(true);

    Set<String> expectedUsernameSet = new HashSet<>();
    expectedUsernameSet.add("test_conversation_creator");
    expectedUsernameSet.add(bobUser.getName());
    expectedUsernameSet.add(aliceUser.getName());

    Mockito.when(mockConversationStore.isTitleTaken("test_new_private_conversation")).thenReturn(false);

    ActivityStore mockActivityStore = Mockito.mock(ActivityStore.class);
    conversationServlet.setActivityStore(mockActivityStore);

    conversationServlet.doPost(mockRequest, mockResponse);

    ArgumentCaptor<Conversation> conversationArgumentCaptor =
        ArgumentCaptor.forClass(Conversation.class);
    Mockito.verify(mockConversationStore).addConversation(conversationArgumentCaptor.capture());
    Assert.assertEquals("test_new_private_conversation",
        conversationArgumentCaptor.getValue().getTitle());
    Assert.assertEquals(expectedUsernameSet,
        conversationArgumentCaptor.getValue().getConversationWhitelistedUsernames());

    ArgumentCaptor<Activity> activityArgumentCaptor = ArgumentCaptor.forClass(Activity.class);

    Mockito.verify(mockActivityStore).addActivity(activityArgumentCaptor.capture());
    Assert.assertEquals("New conversation: test_new_private_conversation", activityArgumentCaptor.getValue().getActivityMessage());

    Mockito.verify(mockResponse).sendRedirect("/chat/test_new_private_conversation");
  }
}
