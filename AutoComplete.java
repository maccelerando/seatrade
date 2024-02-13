
public class AutoComplete {


  public String autoCompleteInput(String input, String[] inputSelectionStrings) {
    String completed = "";
    for (int i = 0; i < inputSelectionStrings.length; i++) {
      for (int j = 0; j < inputSelectionStrings[i].length(); j++) {
        if (input.equals(inputSelectionStrings[i].substring(0, j + 1))) {
          completed = inputSelectionStrings[i];
          break;
        }
      }
    }
    if (!completed.isEmpty()) {
      System.out.println("Assume input = " + completed);
    } else {
      System.out.println("Wrong input: " + input);
    }
    return completed;
  }
}
