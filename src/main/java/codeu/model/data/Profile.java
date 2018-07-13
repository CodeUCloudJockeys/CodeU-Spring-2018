package codeu.model.data;

import java.time.Instant;
import java.util.UUID;

public class Profile {
  private final UUID id;
  private final UUID ownerId;
  private final Instant creation;
  private String about;

  public Profile(UUID ownerId, UUID id, String about, Instant creation) {
    this.ownerId = ownerId;
    this.id = id;
    this.creation = creation;
    this.about = about;
  }

  public UUID getId() {
    return ownerId;
  }
  /** Returns the ID of this profile. */
  public UUID getProfile() {
    return id;
  }

  /** Returns the about me section of this profile page. */
  public String getAbout() {
    return about;
  }

  public void setAbout(String newAbout) {
    this.about = newAbout;
  }

  /** Returns the creation time of this profile. */
  public Instant getCreation() {
    return creation;
  }
}
