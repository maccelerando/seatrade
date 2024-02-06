
public class AutoComplete {
	private String[] inputSelectionStrings;
	
	public AutoComplete(String input, String[] inputSelectionStrings) {
		this.inputSelectionStrings = inputSelectionStrings;
	}
	
	public String autoCompleteInput(String input) {
	   String completed = "";
	   for (int i = 0; i < inputSelectionStrings.length; i++) {
		   for (int j = 0; j < inputSelectionStrings[i].length(); j++) {
			   if (input.equals(inputSelectionStrings[i].substring(0, j + 1))) {
				   completed = inputSelectionStrings[i];
				   break;
			   }
		   }
	   }
	   return completed;
   }
}
