package codeu.controller;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import codeu.model.data.Conversation;
import codeu.model.data.Profile;
import codeu.model.data.User;
import codeu.model.store.basic.ProfileStore;
import codeu.model.store.basic.UserStore;


  /** This servlet class is responsible the profile pages*/
public class ProfileServlet extends HttpServlet{

  /** From my perspective this variable is used to keep track of who is looking at
   *  the profile page. If the account and user match then they will be allowed 
   *  to edit their profile page.
   *  */
  private String name;
  private UserStore userStore;
  private ProfileStore profileStore;

  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
    setProfileStore(ProfileStore.getInstance());

  }
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }
  void setProfileStore(ProfileStore profileStore) {
    this.profileStore = profileStore;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
	  throws IOException, ServletException {

		  
    String username = (String) request.getSession().getAttribute("user");
    request.setAttribute("username", username);

    if(username == null)
    {
	    response.sendRedirect("/login");
    }
    else
    {
        List<Profile> profiles = profileStore.getAllProfiles();
        request.setAttribute("profiles", profiles);
	    request.getRequestDispatcher("/WEB-INF/view/profile.jsp").forward(request, response);
    }
  }
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    String username = (String) request.getSession().getAttribute("user");
    if (username == null) {
      // user is not logged in, don't let them in
      response.sendRedirect("/login");
      return;
    }

    User user = userStore.getUser(username);
    if (user == null) {
      response.sendRedirect("/login");
      return;
    }
    
    String profileAbout = request.getParameter("profileAbout");
    if(profileAbout != null)
    {
      profileStore.updateAbout(username, profileAbout);
    }

    Profile profile =
        new Profile(UUID.randomUUID(), user.getId(), profileAbout, Instant.now());
    profileStore.addProfile(profile);
    response.sendRedirect("/profile/" + username);

  }
}
