module FrankChenCalculator {
	requires javafx.controls;
	requires javafx.fxml;
	
	opens mycalculator to javafx.graphics, javafx.fxml;
}
