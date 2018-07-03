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

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.UUID;

/**
 * Class creating activity objects that keeps track of the type of Activity, Creation time and id
 */
public class Activity {
    private final UUID activityId;
    private final Instant activityCreation;
    private final Type activityType;

    /**
     * Constructs a new Activity
     *
     * @param activityId the ID of this Activity
     * @param activityCreation the creation time of this activity
     * @param activityType the type of activity (i.e. message, user, etc)
     * */
    public Activity(UUID activityId, Instant activityCreation, Type activityType) {
        this.activityId = activityId;
        this.activityCreation = activityCreation;
        this.activityType = activityType;
    }

    /** Returns the ID of this Activity */
    public UUID getId() {

        return activityId;
    }

    /** Returns the creation time of the Activity */
    public Instant getCreationTime() {

        return activityCreation;
    }

    /** Gets the type of the Activity */
    public Type getType(){
        return activityType;
    }

    /** Override equality, so activities are compared based on their ID */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Activity)) {
            return false;
        }

        Activity other = (Activity) o;

        return other.getId().equals(activityId);
    }

    /** Override hashcode, so activities are hashed based on their ID */
    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }
}
