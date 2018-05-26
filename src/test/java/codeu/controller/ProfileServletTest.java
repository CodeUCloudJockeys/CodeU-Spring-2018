package codeu.controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

  public class ProfileServletTest {

  private ProfileServlet profileServlet;
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private HttpSession mockSession;
  private RequestDispatcher mockRequestDispatcher;
  
  @Before
  public void setup() throws IOException {
    profileServlet = new ProfileServlet();
    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockSession = Mockito.mock(HttpSession.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/chat.jsp"))
     .thenReturn(mockRequestDispatcher);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);
  }

  @Test
  public void testDoGet_noUser() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn(null);
    profileServlet.doGet(mockRequest, mockResponse);

    //Checks if the user null, the redirected into login page
    Mockito.verify(mockResponse).sendRedirect("/login");
  }

}
