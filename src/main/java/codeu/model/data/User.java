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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import javafx.util.Pair;

/** Class representing a registered user. */
public class User {
  private final UUID id;
  private final String name;
  private final String passwordHash;
  private final Instant creation;
  private boolean isAdmin;

  /**
   * Used to automate storage of properties.
   *
   * When persistently storing a User, all of the Users's properties will be stored using this list,
   * so when adding properties to User, only the User class needs to be edited!
   */
  private List<Pair<String, Function<User, String>>> fieldNamesToStringValues;

  /**
   * Constructs a new non-admin User.
   *
   * @param id the ID of this User
   * @param name the username of this User
   * @param passwordHash the password of this User
   * @param creation the creation time of this User
   */
  public User(UUID id, String name, String passwordHash, Instant creation) {
    this.id = id;
    this.name = name;
    this.passwordHash = passwordHash;
    this.creation = creation;
    this.isAdmin = false;

    initFieldNamesToStringValues();
  }

  /**
   * Constructs a new User, explicitly ascribing its admin status.
   *
   * @param id the ID of this User
   * @param name the username of this User
   * @param passwordHash the password of this User
   * @param creation the creation time of this User
   * @param isAdmin whether the user is an admin
   */
  public User(UUID id, String name, String passwordHash, Instant creation, boolean isAdmin) {
    this.id = id;
    this.name = name;
    this.passwordHash = passwordHash;
    this.creation = creation;
    this.isAdmin = isAdmin;

    initFieldNamesToStringValues();
  }

  /**
   * Initializes the map from property names to property values*/
  private void initFieldNamesToStringValues() {
    fieldNamesToStringValues = new ArrayList<>();

    // These are all functions and not just values because some of the values can change
    // across the lifetime of the User object.
    fieldNamesToStringValues
        .add(new Pair<>("uuid", (user) -> user.getId().toString()));
    fieldNamesToStringValues
        .add(new Pair<>("username", (user) -> user.getName()));
    fieldNamesToStringValues
        .add(new Pair<>("password_hash", (user) -> user.getPasswordHash()));
    fieldNamesToStringValues
        .add(new Pair<>("creation_time", (user) -> user.getCreationTime().toString()));
    fieldNamesToStringValues
        .add(new Pair<>("is_admin", (user) -> Boolean.toString(user.getIsAdmin())));
  }

  /** Returns the ID of this User. */
  public UUID getId() {
    return id;
  }

  /** Returns the username of this User. */
  public String getName() {
    return name;
  }

  /** Returns the password hash of this User. */
  public String getPasswordHash() {
    return passwordHash;
  }

  /** Returns the creation time of this User. */
  public Instant getCreationTime() {
    return creation;
  }

  /** Returns whether the user is an admin. */
  public boolean getIsAdmin() {
    return isAdmin;
  }

  /**
   * Returns a list of labels and associated string data
   */
  public List<Pair<String,String>> getFieldNamesAndStringValues() {
    List<Pair<String, String>> output = new ArrayList<>();

    // For each <string, function> pair in fieldNamesToStringValues,
    // Call the function with the current user as the argument,
    // store the result in a new list of pairs

    // Put succintly, gets every property name and its current string value and stores them in the
    // output pair
    fieldNamesToStringValues
        .forEach(
            (pair) -> output.add(new Pair<>(pair.getKey(), pair.getValue().apply(this)))
        );

    return output;
  }

  /**
   * Turns a user into an admin. This is not setAdmin because promoting and demoting a user are very
   * different operations. That is, user.adminify() seems to me to be more explicit than
   * user.setIsAdmin(true), and explicitness is something we want for such a security-critical
   * method
   */
  public void adminify() {
    this.isAdmin = true;
  } // maybe rename to promote()

  public void unadminify() {
    this.isAdmin = false;
  } // maybe rename to demote()

  /** Override equality, so users are compared based on their ID */
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof User)) {
      return false;
    }

    User other = (User) o;

    return other.getId().equals(id);
  }

  /** Override hashcode, so users are hashed based on their ID */
  @Override
  public int hashCode() {
    return this.getId().hashCode();
  }
}
