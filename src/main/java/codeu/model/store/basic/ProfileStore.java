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
    for (Profile profile : profiles) {
      if (profile.getId().equals(id)) {
        persistentStorageAgent.writeThroughAbout(profile, about);
      }
    }
  }
  
  public String getAbout(UUID id){
    for(Profile profile: profiles) {
      if(profile.getId().equals(id)){
        return profile.getAbout();
      }
    }
    return null;
  }

  public void setProfile(List<Profile> profiles) {
    this.profiles = profiles;
  }
}
