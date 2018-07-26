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

import java.time.Instant;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;

public class ActivityTest {

    @Test
    public void testCreate() {
        UUID activityId = UUID.randomUUID();
        Instant activityCreation = Instant.now();
        String activityMessage = "Test_activity_message";

        Activity activity = new Activity(activityId, activityCreation, activityMessage);

        Assert.assertEquals(activityId, activity.getId());
        Assert.assertEquals(activityCreation, activity.getCreationTime());
        Assert.assertEquals(activityMessage, activity.getActivityMessage());
    }
}
