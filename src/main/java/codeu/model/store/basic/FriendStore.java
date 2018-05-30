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

import codeu.model.data.User;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Store class that uses in-memory data structures to hold values and automatically loads from and
 * saves to PersistentStorageAgent. It's a singleton so all servlet classes can access the same
 * instance.
 */
public class FriendStore {

  /** Singleton instance of FriendStore. */
  private static FriendStore instance;

  /**
   * Returns the singleton instance of FriendStore
   */
  public static FriendStore getInstance() {
    if (instance == null) {
      instance = new FriendStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  /**
   * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
   *
   * @param persistentStorageAgent a mock used for testing
   */
  public static FriendStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new FriendStore(persistentStorageAgent);
  }

  /**
   * The PersistentStorageAgent responsible for loading Users from and saving Users to Datastore.
   */
  private PersistentStorageAgent persistentStorageAgent;

  /** maps user to friends. */
  private List<Friend> friends;
  /** maps friend id to users */
  private Map<String, UUID> friend_id;

  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
  private FriendStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
    friends = new HashMap<>();
    friend_id = new HashMap<>();
  }

  public List<Friend> getAllFriends(){
    return friends.
  }

  public void addFriend(Friend friend){
    friends.add(friend);
    persistentStorageAgent.writeThrough(friend);
  }
  
  public void setFriend(List<Friend> friends){
    this.friends = friends.
  }
}

