package codeu.model.store.basic;

import codeu.model.data.Profile;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProfileStore {

  private static ProfileStore instance;

  public static ProfileStore getInstance() {
    if (instance == null) {
      instance = new ProfileStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  public static ProfileStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new ProfileStore(persistentStorageAgent);
  }

  private PersistentStorageAgent persistentStorageAgent;

  private List<Profile> profiles;
  private ProfileStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
    profiles = new ArrayList<>();
  }

  public List<Profile> getAllProfiles() {
    return profiles;
  }

  public void addProfile(Profile profile) {
    profiles.add(profile);
    persistentStorageAgent.writeThrough(profile);
  }

  public Profile getProfile(UUID id) {
    for (Profile profile : profiles) {
      if (profile.getId().equals(id)) {
        return profile;
      }
    }
    return null;
  }

  public void updateAbout(UUID id, String about) {
    for (int i = 0; i<profiles.size(); i++) {
      Profile profile = profiles.get(i);
      if (profile.getProfile().equals(id)) {
        profile.setAbout(about);
        profiles.set(i, profile);
        persistentStorageAgent.writeThrough(profile);
      }
    }
  }

  public String getAbout(UUID id) {
    for (Profile profile : profiles) {
      if (profile.getProfile().equals(id)) {
        return profile.getAbout();
      }
    }
    return "Enter an About me!";
  }

  public void setProfile(List<Profile> profiles) {
    this.profiles = profiles;
  }
}
