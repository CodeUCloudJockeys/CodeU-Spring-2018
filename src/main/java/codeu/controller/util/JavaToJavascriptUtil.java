package codeu.controller.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JavaToJavascriptUtil {

  // Returns a long string with each word
  // Ex: "hello hello what is up with the"
  public static String WordFreqUtil(Map<String,Integer> wordFrequency) {
    StringBuilder sb = new StringBuilder();
    sb.append("'");

    for (String key : wordFrequency.keySet()) {
      for (int i = 0; i < wordFrequency.get(key); i++) {
        sb.append(key);
        sb.append(' ');
      }
    }
    sb.append("'");
    return sb.toString();
  }

  // Returns string with a javascript array of maps
  // Ex: "[ {y:10, label: "1"}, {y:20, label: "2"} ]"
  public static String HourFreqUtil(Map<Integer,Integer> hourFrequency) {
    StringBuilder sb = new StringBuilder();

    sb.append("[");

    // [{y:10 label: "1"}, ]

    for (int i = 0; i < 24; i++) {
      sb.append('{');

      sb.append("y:");
      sb.append(hourFrequency.getOrDefault(i, 0));

      sb.append(", label:\"");
      sb.append(i);
      sb.append("\"},");

    }

    sb.setLength(sb.length() - 1);

    sb.append(']');

    return sb.toString();
  }

  // Returns a string of the following form:
  // "graph.xAxis = ["bob", "alice", "chad"]\n graph.yAxis = [1, 2, 3]"
  public static String UsernameFreqUtil(Map<String,Integer> userFrequency) {
    StringBuilder sb = new StringBuilder();

    sb.append("[");

    // [{y:10 label: "1"}, ]
    List keys = new ArrayList(userFrequency.keySet());
    List values = new ArrayList(userFrequency.values());
    for (int i = 0; i < keys.size(); i++) {
      sb.append('{');

      sb.append("y:");
      sb.append(values.get(i));

      sb.append(", label:\"");
      sb.append(keys.get(i));
      sb.append("\"},");

    }

    sb.setLength(sb.length() - 1);

    sb.append(']');

    return sb.toString();
  }
}
