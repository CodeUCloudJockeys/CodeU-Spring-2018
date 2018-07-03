package codeu.model.data;

import java.time.Instant;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;

public class ProfileTest {

  @Test
  public void testCreate() {
    UUID id = UUID.randomUUID();
    UUID ownerId = UUID.randomUUID();
    String aboutMe = "This is my page";
    Instant creation = Instant.now();

    Profile profile = new Profile(id, ownerId, aboutMe, creation);

    Assert.assertEquals(id, profile.getId());
    Assert.assertEquals(ownerId, profile.getProfile());
    Assert.assertEquals(aboutMe, profile.getAbout());
    Assert.assertEquals(creation, profile.getCreation());
  }
}
