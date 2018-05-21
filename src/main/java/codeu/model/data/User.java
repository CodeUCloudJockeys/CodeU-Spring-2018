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

/** Class representing a registered user. */
public class User {
  private final UUID id;
  private final String name;
  private final String passwordHash;
  private final Instant creation;
  private boolean isAdmin;

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
  public boolean getIsAdmin() { return isAdmin; }

  /**
   * Turns a user into an admin. This is not setAdmin because promoting and demoting a user are
   * very different operations. That is, user.adminify() seems to me to be more explicit than
   * user.setIsAdmin(true), and explicitness is something we want for such a security-critical
   * method
   */
  public void adminify() { this.isAdmin = true; } // maybe rename to promote()

  public void unadminify() { this.isAdmin = false; } // maybe rename to demote()
}
