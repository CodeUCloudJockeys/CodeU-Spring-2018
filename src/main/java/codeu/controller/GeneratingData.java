package codeu.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;
import org.omg.CORBA.portable.InputStream;

import codeu.model.data.Activity;
import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ActivityStore;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;

/**
 * Servlet implementation class GeneratingData
 */
public class GeneratingData extends HttpServlet {
    /** Store class that gives access to Conversations. */
    private ConversationStore conversationStore;
    /** Store class that gives access to Messages. */
    private MessageStore messageStore;
    /** Store class that gives access to Users. */
    private UserStore userStore;

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
    
    void setUserStore(UserStore userStore){
    	this.userStore = userStore;
    }
    
      
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");
		ServletContext cntxt = getServletContext();
		FileInputStream fstream = 
                new FileInputStream(getServletContext().getRealPath("/datafile"));	
		
		String username = "nowork";
	    String password = "1234";
	    String message = "noworking";
	    String title = "BadConvo";

		BufferedReader br = new BufferedReader((new InputStreamReader(fstream)));
		String line = br.readLine();
		while(line !=null){
		  username = br.readLine();
		  password = br.readLine();
		  message = br.readLine();
		  title = br.readLine();
		  String number = br.readLine();
		  if(number.equals("0") || number.equals("1")){
			newUser(username,password);
		  }
		  if(number.equals("0") || number.equals("2")){
			newConvo(username,title);
		  }
		  if(number.equals("4") || number.equals("5")){
			newPrivateConvo(username,title);
		  }
		  if(number.equals("0") || number.equals("3") || number.equals("5")){
			newMessage(username,title,message);
		  }

		  line = br.readLine();
		}

	  
	}
	
	public void newUser(String username, String password){
		String hashed =BCrypt.hashpw(password, BCrypt.gensalt());
		User newUser = new User(UUID.randomUUID(), username , hashed, Instant.now());
	    userStore.addUser(newUser);
	}
	public void newConvo(String username, String title){
		User user = userStore.getUser(username);
		Conversation newConvo =
		            new Conversation(UUID.randomUUID(), user.getId(), title, Instant.now());
		conversationStore.addConversation(newConvo);
	}
	public void newPrivateConvo(String username, String title){
		User user = userStore.getUser(username);
		Conversation newConvo =
		            new Conversation(UUID.randomUUID(), user.getId(), title, Instant.now(), true);
		newConvo.addUserToWhitelist(user);
		conversationStore.addConversation(newConvo);
	}
	public void newMessage(String username, String title, String message){
		User user = userStore.getUser(username);
		Conversation convo = conversationStore.getConversationWithTitle(title);
		Message newMessage = new Message(UUID.randomUUID(), convo.getId(), user.getId(), message, Instant.now());
		    messageStore.addMessage(newMessage);
	}
	
	

}
