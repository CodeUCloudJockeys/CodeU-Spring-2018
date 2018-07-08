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

package codeu.model.data;

import java.time.Instant;
import java.util.UUID;

/**
 * Class representing a conversation, which can be thought of as a chat room. Conversations are
 * created by a User and contain Messages.
 */
public class Conversation {
  private final UUID id;
  private final UUID owner;
  private final Instant creation;
  private final String title;
  private final boolean isPrivate;
  private final String conversationUserAdded;

  /**
   * Constructs a new public Conversation.
   *
   * @param id the ID of this Conversation
   * @param owner the ID of the User who created this Conversation
   * @param title the title of this Conversation
   * @param creation the creation time of this Conversation
   */
  public Conversation(UUID id, UUID owner, String title, Instant creation, String conversationUserAdded) {
    this.id = id;
    this.owner = owner;
    this.creation = creation;
    this.title = title;
    this.isPrivate = false;
    this.conversationUserAdded = conversationUserAdded;
  }

  /**
   * Constructs a new Conversation, specifying whether it is private or not.
   *
   * @param id the ID of this Conversation
   * @param owner the ID of the User who created this Conversation
   * @param title the title of this Conversation
   * @param creation the creation time of this Conversation
   * @param isPrivate whether the conversation is private or not
   * @param conversationUserAdded the user added to the conversation in the conversation jsp file
   */
  public Conversation(UUID id, UUID owner, String title, Instant creation, boolean isPrivate, String conversationUserAdded) {
    this.id = id;
    this.owner = owner;
    this.creation = creation;
    this.title = title;
    this.isPrivate = isPrivate;
    this.conversationUserAdded = conversationUserAdded;
  }

  /** Returns the ID of this Conversation. */
  public UUID getId() {
    return id;
  }

  /** Returns the ID of the User who created this Conversation. */
  public UUID getOwnerId() {
    return owner;
  }

  /** Returns the title of this Conversation. */
  public String getTitle() {
    return title;
  }

  /** Returns the creation time of this Conversation. */
  public Instant getCreationTime() {
    return creation;
  }

  /** Returns whether the conversation is private or not */
  public boolean getIsPrivate() {
    return isPrivate;
  }
  /**Returns the users in the conversation */
  public String getConversationUserAdded(){
    return conversationUserAdded;
  }

  /** Override equality, so conversations are compared based on their ID */
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Conversation)) {
      return false;
    }

    Conversation other = (Conversation) o;

    return other.getId().equals(id);
  }

  /** Override hashcode, so conversations are hashed based on their ID */
  @Override
  public int hashCode() {
    return this.getId().hashCode();
  }
}
