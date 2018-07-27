package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

/** Servlet implementation class GeneratingData */
public class GeneratingData extends HttpServlet {
  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;
  /** Store class that gives access to Messages. */
  private MessageStore messageStore;
  /** Store class that gives access to Users. */
  private UserStore userStore;

  private String[] users = new String[] {"boss", "manager", "ted", "frank", "linda", "exuser"};
  private String[] conversations =
      new String[] {
        "codeu_faq", "bookClub", "favorite_songs", "stackoverflow", "cLoUdJoCkEys", "terminator"
      };
  private String conversation = null;
  private List<String> messages = new ArrayList<String>();
  private boolean terminate = false;

  /** Set up state for handling chat requests. */
  @Override
  public void init() throws ServletException {
    super.init();
    setConversationStore(ConversationStore.getInstance());
    setMessageStore(MessageStore.getInstance());
    setUserStore(UserStore.getInstance());
  }

  void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
  }

  void setMessageStore(MessageStore messageStore) {
    this.messageStore = messageStore;
  }

  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // Max of 5 users
    for (String user : users) {
      if (!userStore.isUserRegistered(user)) {
        newUser(user);
      }
    }

    // Max of 5 new conversations
    for (String convo : conversations) {
      if (!conversationStore.isTitleTaken(convo) && !convo.equals("terminator")) {
        conversation = convo;
        terminate = false;
        newConvo("boss", conversation);
        break;
      }
      terminate = true;
    }

    // Exceeds number of times the generator can run
    if (terminate) {
      response.sendRedirect("/register");
      return;
    }

    ServletContext cntxt = getServletContext();
    InputStream resource = cntxt.getResourceAsStream("/WEB-INF/datagen.txt");
    BufferedReader br = new BufferedReader(new InputStreamReader(resource));
    String line = br.readLine();
    while (line != null) {
      messages.add(br.readLine());
      line = br.readLine();
    }

    Random rand = new Random();
    for (int i = 0; i < 25; i++) {
      newMessage(users[rand.nextInt(6)], conversation, messages.get(rand.nextInt(90)));
    }

    response.sendRedirect("/login");
  }

  public void newUser(String username) {
    String hashed = BCrypt.hashpw("1234", BCrypt.gensalt());
    User newUser = new User(UUID.randomUUID(), username, hashed, Instant.now());
    userStore.addUser(newUser);
  }

  public void newConvo(String username, String title) {
    User user = userStore.getUser(username);
    Conversation newConvo = new Conversation(UUID.randomUUID(), user.getId(), title, Instant.now());
    conversationStore.addConversation(newConvo);
  }

  public void newPrivateConvo(String username, String title) {
    User user = userStore.getUser(username);
    Conversation newConvo =
        new Conversation(UUID.randomUUID(), user.getId(), title, Instant.now(), true);
    for (String name : users) {
      User users = userStore.getUser(name);
      newConvo.addUserToWhitelist(users);
    }
    conversationStore.addConversation(newConvo);
  }

  public void newMessage(String username, String title, String message) {
    User user = userStore.getUser(username);
    Conversation convo = conversationStore.getConversationWithTitle(title);
    // Takes current time then offsets it at the most 90 minutes and at the least 0 minutes
    Clock baseClock = Clock.systemDefaultZone();
    Random rand = new Random();
    Clock clock = Clock.offset(baseClock, Duration.ofMinutes(rand.nextInt(1440)));
    Message newMessage =
        new Message(UUID.randomUUID(), convo.getId(), user.getId(), message, Instant.now(clock));
    messageStore.addMessage(newMessage);
  }
}
