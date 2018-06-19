package codeu.model.store.basic;

import static org.junit.Assert.*;

import codeu.model.data.Profile;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ProfileStoreTest {
  private ProfileStore profileStore;
  private PersistentStorageAgent mockPersistentStorageAgent;

  private final Profile PROFILE =
      new Profile(UUID.randomUUID(), UUID.randomUUID(), "About Me", Instant.ofEpochMilli(1000));

  @Before
  public void setup() {
    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    profileStore = ProfileStore.getTestInstance(mockPersistentStorageAgent);

    final List<Profile> profileList = new ArrayList<>();
    profileList.add(PROFILE);
    profileStore.setProfile(profileList);
  }

  @Test
  public void testGetProfiles_UUID() {
    Profile resultProfile = profileStore.getProfile(PROFILE.getId());
    assertEquals(PROFILE, resultProfile);
  }

  @Test
  public void testGetProfiles_UUID_FAIL() {
    Profile resultProfile = profileStore.getProfile(UUID.randomUUID());
    Assert.assertNull(resultProfile);
  }

  @Test
  public void testAddProfile() {
    Profile startProfile =
        new Profile(UUID.randomUUID(), UUID.randomUUID(), "My_About_Me", Instant.now());

    profileStore.addProfile(startProfile);
    Profile resultProfile = profileStore.getProfile(startProfile.getId());

    assertEquals(startProfile, resultProfile);
    Mockito.verify(mockPersistentStorageAgent).writeThrough(startProfile);
  }

  @Test
  public void testUpdateAbout() {
    Profile startAbout =
        new Profile(UUID.randomUUID(), UUID.randomUUID(), "My_About_Me", Instant.now());
    profileStore.updateAbout(startAbout.getId(), "NewAbout");

    Profile resultAbout = profileStore.getProfile(startAbout.getId());

    Assert.assertFalse(startAbout.equals(resultAbout));
  }

  private void assertEquals(Profile expectedProfile, Profile actualProfile) {
    Assert.assertEquals(expectedProfile.getId(), actualProfile.getId());
    Assert.assertEquals(expectedProfile.getProfile(), actualProfile.getProfile());
    Assert.assertEquals(expectedProfile.getAbout(), actualProfile.getAbout());
    Assert.assertEquals(expectedProfile.getCreation(), actualProfile.getCreation());
  }
}
