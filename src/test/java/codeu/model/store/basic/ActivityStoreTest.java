package codeu.model.store.basic;

import codeu.model.data.Activity;
import codeu.model.data.Conversation;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ActivityStoreTest {

    private ActivityStore activityStore;
    private PersistentStorageAgent mockPersistentStorageAgent;

    private final Activity ACTIVITY_ONE =
            new Activity(
                    UUID.randomUUID(), Instant.ofEpochMilli(1000), "activity_one");

    @Before
    public void setup() {
        mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
        activityStore = ActivityStore.getTestInstance(mockPersistentStorageAgent);

        final List<Activity> activityList = new ArrayList<>();
        activityList.add(ACTIVITY_ONE);
        activityStore.setActivities(activityList);
    }

    @Test
    public void testAddConversation() {
        UUID id = UUID.randomUUID();
        Activity inputActivity =
                new Activity(id, Instant.now(), "test_activity");

        activityStore.addActivity(inputActivity);
        Activity resultActivity =
                activityStore.getActivity(id);

        assertEquals(inputActivity, resultActivity);
        Mockito.verify(mockPersistentStorageAgent).writeThrough(inputActivity);
    }

    @Test
    public void testCount() {
        Assert.assertEquals(1, activityStore.Count());
    }

    private void assertEquals(Activity expectedActivity, Activity actualActivity) {
        Assert.assertEquals(expectedActivity.getId(), actualActivity.getId());
        Assert.assertEquals(
                expectedActivity.getCreationTime(), actualActivity.getCreationTime());
        Assert.assertEquals(expectedActivity.getActivityMessage(), actualActivity.getActivityMessage());
    }
}
