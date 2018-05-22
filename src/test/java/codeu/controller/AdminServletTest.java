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
import java.time.Instant;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class AdminServletTest {

  private AdminServlet adminServlet;
  private HttpServletRequest mockRequest;
  private HttpSession mockSession;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
  private UserStore mockUserStore;

  @Before
  public void setup() {
    adminServlet = new AdminServlet();

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
    adminServlet.setUserStore(mockUserStore);
  }

  @Test
  public void testDoGet_NotAdminUsername() throws IOException, ServletException {

    User user = new User(
        UUID.randomUUID(),
        "notadmin",
        "$2a$10$.e.4EEfngEXmxAO085XnYOmDntkqod0C384jOR9oagwxMnPNHaGLa",
        Instant.now(),
        false
    );

    Mockito.when(mockSession.getAttribute("user")).thenReturn("notadmin");

    Mockito.when(mockUserStore.getUser("notadmin")).thenReturn(user);

    adminServlet.doGet(mockRequest, mockResponse);

    // Verify user is *NOT* forwarded to admin page
    Mockito.verify(mockRequestDispatcher, Mockito.never()).forward(mockRequest, mockResponse);

    // Verify user is redirected to login page
    Mockito.verify(mockResponse).sendRedirect("/login");
  }

  @Test
  public void testDoGet_AdminUsername() throws IOException, ServletException {

    User user = new User(
        UUID.randomUUID(),
        "admin",
        "$2a$10$.e.4EEfngEXmxAO085XnYOmDntkqod0C384jOR9oagwxMnPNHaGLa",
        Instant.now(),
        true
    );

    Mockito.when(mockSession.getAttribute("user")).thenReturn("admin");

    Mockito.when(mockUserStore.getUser("notadmin")).thenReturn(user);

    adminServlet.doGet(mockRequest, mockResponse);

    // Verify user is forwarded to admin page
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }
}
