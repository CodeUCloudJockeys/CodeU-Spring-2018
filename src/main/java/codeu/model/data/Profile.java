package codeu.model.data;

import java.time.Instant;
import java.util.UUID;

public class Profile {
  public final UUID id;
  public final UUID ownerId;
  public final Instant creation;
  public final String about;

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

  /** Returns the creation time of this profile. */
  public Instant getCreation() {
    return creation;
  }
}
