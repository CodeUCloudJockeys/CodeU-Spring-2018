package codeu.model.store.basic;
import codeu.model.data.Profile;
import codeu.model.data.User;
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



  public Profile getProfile(String username) {
    for (Profile profile : profiles) {
	  if (profile.equals(username)) {
	    return profile;
	  }
    }
    return null;
  }
  
  public void updateAbout(String username, String about) {
    for (Profile profile : profiles) {
      if (profile.equals(username)) {
    	persistentStorageAgent.writeThroughAbout(profile, about);
      }
	}
  }

  public void setProfile(List<Profile> profiles) {
    this.profiles = profiles;
  }
}
