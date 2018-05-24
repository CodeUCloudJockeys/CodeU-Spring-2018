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

package codeu.model.store.basic;

import codeu.model.data.Message;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Store class that uses in-memory data structures to hold values and automatically loads from and
 * saves to PersistentStorageAgent. It's a singleton so all servlet classes can access the same
 * instance.
 */
public class MessageStore {

  /** Singleton instance of MessageStore. */
  private static MessageStore instance;

  /**
   * Returns the singleton instance of MessageStore that should be shared between all servlet
   * classes. Do not call this function from a test; use getTestInstance() instead.
   */
  public static MessageStore getInstance() {
    if (instance == null) {
      instance = new MessageStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  /**
   * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
   *
   * @param persistentStorageAgent a mock used for testing
   */
  public static MessageStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new MessageStore(persistentStorageAgent);
  }

  /**
   * The PersistentStorageAgent responsible for loading Messages from and saving Messages to
   * Datastore.
   */
  private PersistentStorageAgent persistentStorageAgent;

  /** The in-memory map of Messages. */
  private Map<UUID, Message> messages;

  // These map from some conversation/user ID to a list containing the IDs of their messages
  private Map<UUID, List<UUID>> conversationIdToMessageIdList;
  private Map<UUID, List<UUID>> authorIdToMessageIdList;

  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
  private MessageStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
    messages = new HashMap<>();

    conversationIdToMessageIdList = new HashMap<>();
    authorIdToMessageIdList = new HashMap<>();
  }

  /**
   * Add a new message to the current set of messages known to the application.
   * This writes the message to the persistent storage.
   */
  public void addMessage(Message message) {
    addMessageWithoutPersistentStorage(message);

    persistentStorageAgent.writeThrough(message);
  }

  /**
   * Add a new message to the current set of messages known to the application.
   * This does NOT write the message to the persistent storage.
   */
  public void addMessageWithoutPersistentStorage(Message message) {
    UUID id = message.getId();
    UUID conversationId = message.getConversationId();
    UUID authorId = message.getAuthorId();

    messages.put(id, message);

    addToListInMap(conversationIdToMessageIdList, conversationId, id);

    addToListInMap(authorIdToMessageIdList, authorId, id);
  }

  /** Access the current set of Messages within the given Conversation. */
  public List<Message> getMessagesInConversation(UUID conversationId) {

    List<Message> messagesInConversation;

    if (conversationIdToMessageIdList.containsKey(conversationId)) {
      // Get all messages from conversation ID
      messagesInConversation =
          conversationIdToMessageIdList.get(conversationId)
              .stream()
              .map(id -> messages.get(id))
              .collect(Collectors.toList());
    } else {
      // If the conversation has no messages, return an empty list
      messagesInConversation = new ArrayList<>();
    }

    return messagesInConversation;
  }

  /** Access the set of Messages sent by a given User. */
  public List<Message> getMessagesByUser(UUID authorId) {
    List<Message> messagesByUser;

    if (authorIdToMessageIdList.containsKey(authorId)) {
      // Get all messages from author ID
      messagesByUser =
          authorIdToMessageIdList.get(authorId)
              .stream()
              .map(id -> messages.get(id))
              .collect(Collectors.toList());
    } else {
      // If the user has no messages, return an empty list
      messagesByUser = new ArrayList<>();
    }

    return messagesByUser;
  }

  /** Sets the List of Messages stored by this MessageStore. */
  public void setMessages(List<Message> messages) {
    messages.forEach(message -> addMessageWithoutPersistentStorage(message));
  }
  
  private static void addToListInMap(map, key, value) {
    if (!map.containsKey(key)) {
      map.put(key, new ArrayList<>());
    }

    map.get(key).add(value);
  }
}
