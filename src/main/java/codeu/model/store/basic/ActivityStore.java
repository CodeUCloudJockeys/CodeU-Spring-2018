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

package codeu.model.store.basic;

import codeu.model.data.Activity;
import codeu.model.store.persistence.PersistentStorageAgent;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Store class that uses in-memory data structures to hold values and automatically loads from and
 * saves to PersistentStorageAgent. It's a singleton so all servlet classes can access the same
 * instance.
 */
public class ActivityStore {

    /** Singleton instance of ActivityStore. */
    private static ActivityStore instance;

    /**
     * Returns the singleton instance of ActivityStore that should be shared between all servlet
     * classes. Do not call this function from a test; use getTestInstance() instead.
     */
    public static ActivityStore getInstance() {
        if (instance == null) {
            instance = new ActivityStore(PersistentStorageAgent.getInstance());
        }
        return instance;
    }

    /**
     * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
     *
     * @param persistentStorageAgent a mock used for testing
     */
    public static ActivityStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
        return new ActivityStore(persistentStorageAgent);
    }

    /**
     * The PersistentStorageAgent responsible for loading Activities from and saving Activities
     * to Datastore.
     */
    private PersistentStorageAgent persistentStorageAgent;

    /** The in-memory map of Activities. */
    private Map<UUID, Activity> activities;

    /**
     * A map from types to IDs, so activity IDs can be fetched from types quickly.
     */
    private Map<Type, UUID> typeToId;

    /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
    private ActivityStore(PersistentStorageAgent persistentStorageAgent) {
        this.persistentStorageAgent = persistentStorageAgent;
        activities = new HashMap<>();
        typeToId = new HashMap<>();
    }

    /** Amount of activities */
    public int Count() {

        return activities.size();
    }

    /** Access the current list of conversations known to the application. */
    public List<Activity> getAllActivities() {

        return new ArrayList<>(activities.values());
    }

    /**
     * Add a new activity to the current map of activities known to the application. This
     * writes the activity to the persistent storage.
     */
    public void addActivity(Activity activity) {
        addActivityWithoutPersistentStorage(activity);

        persistentStorageAgent.writeThrough(activity);
    }

    /**
     * Add a new activity to the current map of activities known to the application. This does
     * NOT write the activity to the persistent storage.
     */
    public void addActivityWithoutPersistentStorage(Activity activity) {
        activities.put(activity.getId(), activity);
    }

    /** Find and return the Activity with the given title. */
//    public Activity getConversationWithType(String title) {
//        UUID id = titleToId.get(title);
//        return (id == null) ? null : conversations.get(id);
//    }

    /** Get a list with all the activities. */
    public List<Activity> getActivityList() {

        return new ArrayList<>(activities.values());
    }

    /** Sets the Map of Activity stored by this ActivityStore. */
    public void setActivities(List<Activity> activityList) {
        // For each activity in the list, add it without writing it to persistent storage.
        activityList.forEach(activity -> addActivityWithoutPersistentStorage(activity));
    }
}
