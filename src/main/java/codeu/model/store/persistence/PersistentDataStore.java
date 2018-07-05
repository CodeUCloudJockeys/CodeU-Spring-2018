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

package codeu.model.store.persistence;


import codeu.model.data.*;
import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.Profile;
import codeu.model.data.User;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.lang.reflect.Type;

import org.apache.commons.lang3.SerializationUtils;


/**
 * This class handles all interactions with Google App Engine's Datastore service. On startup it
 * sets the state of the applications's data objects from the current contents of its Datastore. It
 * also performs writes of new of modified objects back to the Datastore.
 */
public class PersistentDataStore {

  // Handle to Google AppEngine's Datastore service.
  private DatastoreService datastore;

  /**
   * Constructs a new PersistentDataStore and sets up its state to begin loading objects from the
   * Datastore service.
   */
  public PersistentDataStore() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  /**
   * Loads all User objects from the Datastore service and returns them in a List.
   *
   * @throws PersistentDataStoreException if an error was detected during the load from the
   *     Datastore service
   */
  public List<User> loadUsers() throws PersistentDataStoreException {

    List<User> users = new ArrayList<>();

    // Retrieve all users from the datastore.
    Query query = new Query("chat-users");
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        String userName = (String) entity.getProperty("username");
        String password = (String) entity.getProperty("password_hash");
        Instant creationTime = Instant.ofEpochMilli((long) entity.getProperty("creation_time"));
        boolean isAdmin = (boolean) entity.getProperty("is_admin");

        HashSet<UUID> conversationIdSet = blobToIdSet((Blob) entity.getProperty("conversation_ids"));

        User user = new User(uuid, userName, password, creationTime, isAdmin);
        user.setConversationIdSet(conversationIdSet);
        users.add(user);
      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }

    return users;
  }

  /**
   * Loads all Conversation objects from the Datastore service and returns them in a List, sorted in
   * ascending order by creation time.
   *
   * @throws PersistentDataStoreException if an error was detected during the load from the
   *     Datastore service
   */
  public List<Conversation> loadConversations() throws PersistentDataStoreException {

    List<Conversation> conversations = new ArrayList<>();

    // Retrieve all conversations from the datastore.
    Query query = new Query("chat-conversations").addSort("creation_time", SortDirection.ASCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        UUID ownerUuid = UUID.fromString((String) entity.getProperty("owner_uuid"));
        String title = (String) entity.getProperty("title");
        Instant creationTime = Instant.ofEpochMilli((long) entity.getProperty("creation_time"));
        Conversation conversation = new Conversation(uuid, ownerUuid, title, creationTime);
        conversations.add(conversation);
      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }

    return conversations;
  }

  /**
   * Loads all Message objects from the Datastore service and returns them in a List, sorted in
   * ascending order by creation time.
   *
   * @throws PersistentDataStoreException if an error was detected during the load from the
   *     Datastore service
   */
  public List<Message> loadMessages() throws PersistentDataStoreException {

    List<Message> messages = new ArrayList<>();

    // Retrieve all messages from the datastore.
    Query query = new Query("chat-messages").addSort("creation_time", SortDirection.ASCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        UUID conversationUuid = UUID.fromString((String) entity.getProperty("conv_uuid"));
        UUID authorUuid = UUID.fromString((String) entity.getProperty("author_uuid"));
        Instant creationTime = Instant.ofEpochMilli((long) entity.getProperty("creation_time"));
        String content = (String) entity.getProperty("content");
        Message message = new Message(uuid, conversationUuid, authorUuid, content, creationTime);
        messages.add(message);
      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }

    return messages;
  }

  public List<Profile> loadProfile() throws PersistentDataStoreException {

    List<Profile> profiles = new ArrayList<>();

    // Retrieve all profiles from the datastore.
    Query query = new Query("chat-profile").addSort("creation_time", SortDirection.ASCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID uuid = UUID.fromString((String) entity.getProperty("owner_id"));
        UUID profileUuid = UUID.fromString((String) entity.getProperty("id"));
        Instant creationTime = Instant.ofEpochMilli((long) entity.getProperty("creation_time"));
        String about = (String) entity.getProperty("about");
        Profile profile = new Profile(uuid, profileUuid, about, creationTime);
        profiles.add(profile);
      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }

    return profiles;
  }

  public List<Activity> loadActivities() throws PersistentDataStoreException {

    List<Activity> activities = new ArrayList<>();

    //Retrieve all activities from the datastore
    Query query = new Query("chat-activity").addSort("creation_time", SortDirection.ASCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID uuid = UUID.fromString((String) entity.getProperty("activityId"));
        Instant creationTime = Instant.parse((String) entity.getProperty("activityCreation"));
        Type type = Type.fromString((String) entity.getProperty("activityType"));

        Activity activity = new Activity(uuid, creationTime, type);
        activities.add(activity);
      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }
    return activities;
  }

  /** Write a User object to the Datastore service. */
  public void writeThrough(User user) {
    Entity userEntity = new Entity("chat-users", user.getId().toString());
    userEntity.setProperty("uuid", user.getId().toString());
    userEntity.setProperty("username", user.getName());
    userEntity.setProperty("password_hash", user.getPasswordHash());
    userEntity.setProperty("creation_time", user.getCreationTime().toEpochMilli());
    userEntity.setProperty("is_admin", user.getIsAdmin());

    userEntity.setProperty("conversation_ids", idSetToBlob(user.getConversationIdSet()));
    datastore.put(userEntity);
  }

  /** Write a Message object to the Datastore service. */
  public void writeThrough(Message message) {
    Entity messageEntity = new Entity("chat-messages", message.getId().toString());
    messageEntity.setProperty("uuid", message.getId().toString());
    messageEntity.setProperty("conv_uuid", message.getConversationId().toString());
    messageEntity.setProperty("author_uuid", message.getAuthorId().toString());
    messageEntity.setProperty("content", message.getContent());
    messageEntity.setProperty("creation_time", message.getCreationTime().toEpochMilli());
    datastore.put(messageEntity);
  }

  /** Write a Conversation object to the Datastore service. */
  public void writeThrough(Conversation conversation) {
    Entity conversationEntity = new Entity("chat-conversations", conversation.getId().toString());
    conversationEntity.setProperty("uuid", conversation.getId().toString());
    conversationEntity.setProperty("owner_uuid", conversation.getOwnerId().toString());
    conversationEntity.setProperty("title", conversation.getTitle());
    conversationEntity.setProperty("creation_time", conversation.getCreationTime().toEpochMilli());
    datastore.put(conversationEntity);
  }

  public void writeThrough(Profile profile) {
    Entity profileEntity = new Entity("chat-profile", profile.getId().toString());
    profileEntity.setProperty("uuid", profile.getId().toString());
    profileEntity.setProperty("profile_uuid", profile.getProfile().toString());
    profileEntity.setProperty("about", profile.getAbout());
    profileEntity.setProperty("creation_time", profile.getCreation().toEpochMilli());
    datastore.put(profileEntity);
  }

  public void writeThroughAbout(Profile profile, String about) {
    Entity profileEntity = new Entity("chat-profile", profile.getId().toString());
    profileEntity.setProperty("uuid", profile.getId().toString());
    profileEntity.setProperty("profile_uuid", profile.getProfile().toString());
    profileEntity.setProperty("about", about);
    profileEntity.setProperty("creation_time", profile.getCreation().toEpochMilli());
    datastore.put(profileEntity);
  }


  public void writeThrough (Activity activity) {
    Entity activityEntity = new Entity("chat-activity", activity.getId().toString());
    activityEntity.setProperty("activityId", activity.getId().toString());
    activityEntity.setProperty("activityType", activity.getType().toString());
    activityEntity.setProperty("activityCreation", activity.getCreationTime().toString());
    datastore.put(activityEntity);
  }
  // TODO: Test these
  private Blob idSetToBlob(HashSet<UUID> idSet) {
    return new Blob(SerializationUtils.serialize(idSet));
  }

  private HashSet<UUID> blobToIdSet(Blob blob) {
    return SerializationUtils.deserialize(blob.getBytes());
  }
}
