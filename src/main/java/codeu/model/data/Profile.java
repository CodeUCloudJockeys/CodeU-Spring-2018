package codeu.model.data;

import java.time.Instant;
import java.util.UUID;

public class Profile {
  public final UUID profile;
  public final UUID id;
  public final Instant creation;
  public final String about;
  
  public Profile(UUID id, UUID profile, String about, Instant creation) {
	    this.id = id;
	  	this.profile = profile;
	    this.creation = creation;
	    this.about = about;
	  }

  public UUID getId() {
	    return id;
	  }
  /** Returns the ID of this Conversation. */
  public UUID getProfile() {
    return profile;
  }

  /** Returns the title of this Conversation. */
  public String getAbout() {
    return about;
  }

  /** Returns the creation time of this Conversation. */
  public Instant getCreation() {
    return creation;
  }

}

