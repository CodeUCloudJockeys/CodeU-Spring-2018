package codeu.controller.util;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.store.basic.MessageStore;
import java.time.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ConversationDataUtil {

  static void processData(Conversation conversation) {
    MessageStore messageStore = MessageStore.getInstance();

    List<Message> messageList = messageStore.getMessagesInConversation(conversation.getId());

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
        .forEach(
            (word) -> {
              frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
            }
        ); // Count each word with the map

    return frequencyMap;
  }

  // Takes a stream of messages and returns a frequency map per hour
  // For example, something like
  // [12:30, 13:30, 12:15, 14:00, 16:12, 16:00, 16:24] -> {12: 2, 13: 1, 14: 1, 16: 3}
  // Except the input is messages, not instants.
  private Map<Integer,Integer> messageStreamToHourlyMessageFrequencyMap(Stream<Message> messageStream) {

    Map<Integer, Integer> frequencyMap = new HashMap<>();

    // Counts the number of messages that are
    messageStream.map(Message::getCreationTime)
        .mapToInt(instant -> LocalDateTime.ofInstant(instant, ZoneOffset.UTC).getHour())
        .forEach(
            (hour) -> {
              frequencyMap.put(hour, frequencyMap.getOrDefault(hour, 0) + 1);
            }
        );

    return frequencyMap;
  }
}
