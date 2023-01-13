package mycalculator;
/* CALCULATOR CONTROLLER 
 * Author: Frank Chen
 * Date: 4/4/2022
 * Purpose: To make a working calculator in JavaFX, this is where the methods and varibles lie
 */

//Imports what we need
import javafx.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class CalculatorController {
	@FXML
	private TextField displayField;
	@FXML
	private TextField memoryField;
	//Creates our two text fields, one to show the user input and calculations and one to show the memory
	
	//initializes our memory number and the boolean to check whether or not + - has been sucessfully run
	float memoryNumber = 0;
	boolean hasFlipped = false;

	public void buttonClickHandler(ActionEvent evt) {

		Button clickedButton = (Button) evt.getTarget();
		String buttonLabel = clickedButton.getText();
		
		// ArrayList<Integer> inputNumbers = new ArrayList<Integer>(Arrays.asList(2));

		// Tell Apart Digits From Operations
		switch (buttonLabel) {
		case "0":
		case "1":
		case "2":
		case "3":
		case "4":
		case "5":
		case "6":
		case "7":
		case "8":
		case "9":
		case "10":
		case ".":
		case "+":
		case "/":
		case "-":
		case "X": //if it's an operand or number, puts the button pressed into the method which adds it to the text field
			process(buttonLabel);
			break;
		case "C": //if 'clear' is clicked, makes the display text empty - clears the calculator
			displayField.setText("");
			break;
		case "=": //if equals is pressed, the method that calculates the numbers is involked
			try {
				processOperation();
			} catch(NumberFormatException e) {
				displayField.setText("Syntax ERROR");
			}
			break;
		case "M+": //if M+ is pressed, adds the current displayed number to the memory and shows it in the memory textfield
			try {
				memoryNumber += Float.parseFloat(displayField.getText());
				memoryField.setText(Float.toString(memoryNumber)); //refreshes the textfield for memory
			} catch(NumberFormatException e) {
				displayField.setText("Syntax ERROR");
			}
			break;
		case "MR": //if MR is pressed and the memorynumber isin't zero, it adds the stored memoryNumber to the displayed text 
			if (memoryNumber != 0) {
				displayField.setText(displayField.getText() + check(Float.toString(memoryNumber)));
			}
			break;
		case "M-": //if M- is pressed, subtracts the current displayed number to the memory and shows it in the memory textfield
			try {
				memoryNumber -= Float.parseFloat(displayField.getText());
				memoryField.setText(Float.toString(memoryNumber)); //refreshes the textfield for memory
			} catch(NumberFormatException e) {
				displayField.setText("Syntax ERROR");
			}
			break; //refreshes the memory textfield
		case "MC":
			memoryNumber = 0; //sets memorynumber to 0 if MC is pressed and refreshes the memoryfield to display accordingly
			memoryField.setText(Float.toString(memoryNumber));
			break;
		case "+ -":
			//if + - is pressed
			String flipString = displayField.getText(); // takes whatever is displayed on the textfield
			String newText = ""; //intializes our new string where the new string we create is stored

			for (int x = flipString.length() - 1; x >= 0; x--) {
				//essentially cycles through the text displayed in the field backwards and checks
				//for the first operand hit.
				
				//then the program checks for what type of operand is hit and 
				//changes the sign accordingly. Exceptions must be added to 
				//filter out certain chains, for example, "/+" is replaced with "/"
				//This is because of the way my code calculates, two operands together will create and error
				
				if ((flipString.charAt(x) == 'X' || flipString.charAt(x) == '/') && hasFlipped == false) {
					newText = flipString.substring(0, x+1) + "-" + flipString.substring(x+1);
					//changes sign for X or /
					hasFlipped = true;
				} else if (flipString.charAt(x) == '+'&& hasFlipped == false) {
					newText = flipString.substring(0, x) + "-" + flipString.substring(x+1);
					//changes sign for addition
					hasFlipped = true;
				} else if (flipString.charAt(x) == '-'&& hasFlipped == false) {
					newText = (flipString.substring(0, x) + "+" + flipString.substring(x+1)).replace("++","+").replace("/+","/").replace("X+","X");
					//changes sign for --
					hasFlipped = true;
				} else if (x==0 && hasFlipped == false) { //if it reaches here, it is the first term hence we add a -
					newText = ("-"+flipString).replace("--","");
					hasFlipped = true;
				}
				
				//at any point, if the number is successfully flipped it will make hasFlipped=false so that it will not flip
				//again for the rest of the cycle
			}
			displayField.setText(newText); //sets our new text as the displayed text
			hasFlipped = false; //makes hasFlipped false again after our loop is done running for our next run
			break; 
		}

	}

	public void process(String buttonLabel) { //processes button pressess, adds them to the textfield and displays it
		displayField.setText(displayField.getText() + buttonLabel);

	}

	public void processOperation() { //processes our calculations when '=' is pressed
		String operatingString = displayField.getText().replace("X", "*");
		//takes the displayed string and replaces X with *
		operatingString = operatingString.replace("--", "+");
		//replaces any double negative with a  positive

		//string manipulation, checks if the code starts with a '+' and deletes if it it does
		if (operatingString.startsWith("+")) {
			operatingString = operatingString.substring(1);
		} 

		String[] tempString = operatingString.split(""); //splits our displayed text into an list
		for (int x = 0; x < tempString.length; x++) { //runs though each element in the array
			String numbers = "1234567890."; //makes a list of possible number characters for reference
			if (x != 0 && tempString[x].equals("-")) { //checks if the character is '-', and if the character ahead of it is a number
				if (numbers.contains(tempString[x - 1])) { //it changes the - to a '+-'
					tempString[x] = "+-";
				}
			}
		} //this makes it so that we can calculate our value with the raw string

		String finalString = ""; //converts our tempString array that we just edited back into a string
		for (int y = 0; y < tempString.length; y++) {
			finalString += tempString[y];
		}

		String[] values = finalString.split("[/*+]"); //splits our final string by the three operands and puts it in values

		List<String> operatorList = new ArrayList<>(); //runs through the value list and checks for operands, then adds it to a new list
		String[] operatorString = finalString.split("");
		for (int z = 0; z < operatorString.length; z++) {
			if (operatorString[z].equals("+") || operatorString[z].equals("/") || operatorString[z].equals("*")) {
				operatorList.add(operatorString[z]);
			}
		}

		List<String> valueList = new ArrayList<>(); //takes all the numerical values in the the individual value list split by operands
		for (String s : values)
			valueList.add(s);

		calculations(valueList, operatorList); //throws our two lists, one with numbers and one with operators, into our calculation method

		displayField.setText(check(valueList.get(0))); //checks if our final answer is an integer or a float and print it

	}

	public static String check(String value) { //method to check if our answer is an integer or a float
		float tempValue = Float.parseFloat(value);
		if ((int) tempValue == tempValue) { //if the int of our answer equals a float, changes the int to a string and returns it
			return Integer.toString((int) tempValue);
		} else {
			return value; //if not returns our original answer
		}
	}

	///method to do the calculations
	public static void calculations(List<String> valueList, List<String> operatorList) {
		//each loop in this method does the same thing but for different variables
		//runs through the operator list until it reaches the operand it is looking for, then takes the index value, then removes
		//the operand from the list
		//goes into our numbers list, and using our index value and index value +1, it calculates the new number according
		//to the operand and replaces those two elements with our new answer.
		
		while (operatorList.contains("/")) { //does the division calculations until there are no longer division commands 
			//left in the operator list
			for (int x = 0; x < operatorList.size(); x++) {
				if (operatorList.get(x).equals("/")) {
					float tempAns = Float.parseFloat(valueList.get(x)) / Float.parseFloat(valueList.get(x + 1));
					valueList.set(x, Float.toString(tempAns));

					valueList.remove(x + 1);
					operatorList.remove(x);
				}

			}
		}
		while (operatorList.contains("*")) { //does the multiplication calculations until there are no longer multiplication
			//commands in the operator list
			for (int x = 0; x < operatorList.size(); x++) {
				if (operatorList.get(x).equals("*")) {
					float tempAns = Float.parseFloat(valueList.get(x)) * Float.parseFloat(valueList.get(x + 1));
					valueList.set(x, Float.toString(tempAns));

					valueList.remove(x + 1);
					operatorList.remove(x);
				}

			}
		}
		while (operatorList.contains("+")) {//does the addition calculations until there are no longer addition
			//commands in the operator list
			for (int x = 0; x < operatorList.size(); x++) {
				if (operatorList.get(x).equals("+")) {
					float tempAns = Float.parseFloat(valueList.get(x)) + Float.parseFloat(valueList.get(x + 1));
					valueList.set(x, Float.toString(tempAns));

					valueList.remove(x + 1);
					operatorList.remove(x);
				}
			}
		}
	}
}


