package codeu.controller.util;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import java.time.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ConversationDataUtil {

  private UserStore userStore;
  private MessageStore messageStore;

  private Map<String,Integer> wordFrequency;
  private Map<Integer,Integer> hourFrequency;
  private Map<String,Integer> usernameFrequency;

  public ConversationDataUtil(Conversation conversation) {
    userStore = UserStore.getInstance();
    messageStore = MessageStore.getInstance();

    processData(conversation);
  }

  private void processData(Conversation conversation) {

    List<Message> messageList = messageStore.getMessagesInConversation(conversation.getId());
    Stream<Message> messageStream = messageList.stream();

    wordFrequency = messageStreamToWordFrequencyMap(messageStream);
    hourFrequency = messageStreamToHourlyMessageFrequencyMap(messageStream);
    usernameFrequency = messageStreamToUsernameFrequencyMap(messageStream);

  }

  // Returns the map from words in messages to their frequency
  // [ "hello world", "goodbye world", "hello hello" ] ->
  // { hello: 3, world: 2, goodbye: 1 }
  public Map<String,Integer> getWordFrequency () {
    return wordFrequency;
  }

  // Returns the map from hours of messages to the amount of messages in each hour
  // [ Message at 12:30, Message at 18:00, Message at 18:15, Message at 13:00, Message at 13:02 ] ->
  // { 12: 1, 18: 2, 13: 2 }
  public Map<Integer,Integer> getHourFrequency () {
    return hourFrequency;
  }

  // Returns the map from usernames to amount of messages that username sent
  // [ Message by bob, Message by bob, Message by alice, Message by bob, Message by chad ] ->
  // { bob:3, alice:1, chad:1 }
  public Map<String,Integer> getUsernameFrequency () {
    return usernameFrequency;
  }

  // TODO: Handle weird inputs, strip numbers and upper/lowercase
  // Takes a stream of messages and returns a frequency map of the messages
  // For example, something like
  // ["i'm a message", "i'm also a message"] -> { "i'm": 2, "a": 2, "message": 2, "also": 1 }
  // Except the input is messages, not strings.
  private Map<String,Integer> messageStreamToWordFrequencyMap(Stream<Message> messageStream) {

    Map<String, Integer> frequencyMap = new HashMap<>();

    // Counts the frequency of each word on the list of messages.
    messageStream.map(Message::getContent) // Turn into stream of message strings
        .flatMap(str -> Arrays.stream(str.split("\\s+"))) // Turn into stream of words
        .forEach((word) -> countInMap(frequencyMap, word)); // Count each word with the map

    return frequencyMap;
  }

  // Takes a stream of messages and returns a frequency map per hour
  // For example, something like
  // [12:30, 13:30, 12:15, 14:00, 16:12, 16:00, 16:24] -> {12: 2, 13: 1, 14: 1, 16: 3}
  // Except the input is messages, not instants.
  private Map<Integer,Integer> messageStreamToHourlyMessageFrequencyMap(Stream<Message> messageStream) {

    Map<Integer, Integer> frequencyMap = new HashMap<>();

    // Counts the number of messages that are sent in each hour
    messageStream.map(Message::getCreationTime)
        .mapToInt(instant -> LocalDateTime.ofInstant(instant, ZoneOffset.UTC).getHour())
        .forEach((hour)-> countInMap(frequencyMap, hour));

    return frequencyMap;
  }

  private Map<String,Integer> messageStreamToUsernameFrequencyMap(Stream<Message> messageStream) {
    Map<String, Integer> frequencyMap = new HashMap<>();

    messageStream.map(Message::getAuthorId)
        .map(userStore::getUser)
        .map(User::getName)
        .forEach((username) -> countInMap(frequencyMap, username));

    return frequencyMap;
  }

  private <K> void countInMap (Map<K, Integer> frequencyMap, K key) {
    frequencyMap.put(key, frequencyMap.getOrDefault(key, 0) + 1);
  }
}
