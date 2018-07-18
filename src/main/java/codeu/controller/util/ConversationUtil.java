package codeu.controller.util;

import codeu.model.data.Conversation;
import codeu.model.store.basic.UserStore;
import java.util.Arrays;

public class ConversationUtil {

  public static void AddUsersFromSpaceDelimitedString
      (UserStore userStore, String usernamesToAdd, Conversation conversation) {

    // Do nothing for public conversations.
    if (!conversation.getIsPrivate()) {
      return;
    }

    // Get all users from the spacebar-separated username list
    Arrays.stream(usernamesToAdd.trim().split("\\s+"))
        .distinct()                          // Remove duplicate usernames
        .filter(userStore::isUserRegistered) // Remove invalid usernames
        .map(userStore::getUser)             // Get users from usernames
        .forEach(user -> user.addToConversation(conversation)); // add each user to the conversation
  }
}
