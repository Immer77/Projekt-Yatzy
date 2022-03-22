package yatzy.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.w3c.dom.events.Event;
import yatzy.model.Yatzy;

import java.util.Arrays;

public class YatzyGui extends Application {


    @Override
    public void start(Stage stage) {
        stage.setTitle("Yatzy");
        GridPane pane = new GridPane();
        this.initContent(pane);

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    // -------------------------------------------------------------------------

    // Shows the face values of the 5 dice.
    private TextField[] txfValues = new TextField[5];
    // Shows the hold status of the 5 dice.
    private CheckBox[] chbHolds = new CheckBox[5];
    // Shows the results previously selected .
    // For free results (results not set yet), the results
    // corresponding to the actual face values of the 5 dice are shown.
    private TextField[] txfResults = new TextField[15];
    // Shows points in sums, bonus and total.
    private TextField txfSumSame, txfBonus, txfSumOther, txfTotal,txf;
    // Shows the number of times the dice has been rolled.
    private Label lblNamesOfCombinations, lblRolled, lblSumSame, lblBonus, lblSumOther, lblTotal;

    private Button btnRoll;

    private Yatzy yatzy = new Yatzy();


    private void initContent(GridPane pane) {
        pane.setGridLinesVisible(false);
        pane.setPadding(new Insets(10));
        pane.setHgap(10);
        pane.setVgap(10);

        // ---------------------------------------------------------------------

        // Terningen pane som er den øverste del
        GridPane dicePane = new GridPane();
        pane.add(dicePane, 0, 0);
        dicePane.setGridLinesVisible(false);
        dicePane.setPadding(new Insets(10));
        dicePane.setHgap(10);
        dicePane.setVgap(10);
        dicePane.setStyle("-fx-border-color: black"); //Ændrer border color
        dicePane.setStyle("-fx-background-color: green"); //Ændrer background farve
        btnRoll = new Button("Roll");
        lblRolled = new Label("Rolled: ");


        Image img = new Image("dicepicture.png"); //Tilføjer billede
        ImageView view = new ImageView(img);
        view.setFitHeight(10);
        view.setFitWidth(10);
        btnRoll.setGraphic(view); // SÆtter billede på knap
        dicePane.add(btnRoll, 3, 2);
        dicePane.add(lblRolled, 4, 2);

        // initialize txfValues, chbHolds, btnRoll and lblRolled
        // TODO
        int x = 110; //Størrelsen på textfelterne
        int y = 100;
        for (int i = 0; i < txfValues.length; i++) { //For loop der laver alle 5 teksfelter
            txfValues[i] = new TextField();
            dicePane.add(txfValues[i], i, 0);
            txfValues[i].setEditable(false);
            txfValues[i].setPrefHeight(y);
            txfValues[i].setPrefWidth(x);
            txfValues[i].setFont(Font.font("Times New Roman", FontWeight.BLACK,40));
            txfValues[i].setAlignment(Pos.CENTER);

        }

        // For loop der laver alle checkbokse
        for (int i = 0; i < chbHolds.length; i++) {
            chbHolds[i] = new CheckBox("Hold");
            dicePane.add(chbHolds[i], i, 1);
			chbHolds[i].setDisable(true);
        }


        // ---------------------------------------------------------------------

        // Scorepane som er den nedeerste del hvor alle scorene kan blive set
        GridPane scorePane = new GridPane();
        pane.add(scorePane, 0, 1);
        scorePane.setGridLinesVisible(false);
        scorePane.setPadding(new Insets(10));
        scorePane.setVgap(5);
        scorePane.setHgap(10);
        scorePane.setStyle("-fx-border-color: black");
        scorePane.setStyle("-fx-background-color: green");
        int w = 50; // width of the text fields

        // Initialize labels for results, txfResults,
        // labels and text fields for sums, bonus and total.
        // TODO

        lblSumSame = new Label("Sum: ");
        scorePane.add(lblSumSame, 3, 5);
        txfSumSame = new TextField();
        txtFieldSpecs(txfSumSame); // Metode til at ændre tekstfelterne til editable og størrelse
        scorePane.add(txfSumSame, 4, 5);

        lblBonus = new Label("Bonus: ");
        scorePane.add(lblBonus, 5, 5);
        txfBonus = new TextField();
        txtFieldSpecs(txfBonus); // Metode til at ændre tekstfelterne til editable og størrelse
        scorePane.add(txfBonus, 6, 5);

        lblSumOther = new Label("Sum: ");
        scorePane.add(lblSumOther, 3, 14);
        txfSumOther = new TextField();
        txtFieldSpecs(txfSumOther); // Metode til at ændre tekstfelterne til editable og størrelse
        scorePane.add(txfSumOther, 4, 14);

        lblTotal = new Label("Total: ");
        scorePane.add(lblTotal, 5, 14);
        txfTotal = new TextField();
        txtFieldSpecs(txfTotal); // Metode til at ændre tekstfelterne til editable og størrelse
        scorePane.add(txfTotal, 6, 14);


        // String array til alle navnene
        String[] combinationsNames = {"1-s", "2-s", "3-s", "4-s", "5-s", "6-s", "One-Pair", "Two-Pair", "Three-Same", "Four-Same", "Full-House", "Small-Straight", "Large-Straight", "Chance", "Yatzy"};
        for (int i = 0; i < yatzy.getResults().length; i++) { //Længden på yatzy result funktionen for at oprette labels på alle navenne''e samt tekstfelter
            lblNamesOfCombinations = new Label(combinationsNames[i]);
            scorePane.add(lblNamesOfCombinations, 1, i);
            txfResults[i] = new TextField();
            txtFieldSpecs(txfResults[i]);
            scorePane.add(txfResults[i], 2, i);
			txfResults[i].setOnMouseClicked(event -> this.chooseFieldAction(event)); //Ændrer på tekstfelterne ved mouseclick event
        }

        btnRoll.setOnAction(event -> this.btnRollOnClick()); // btnroll til når der skal rulles med terningerne
    }

    /**
     * pre: et ikke redigeret teksfelt
     * post: tekstfelt er nu sat til standard størrelse
     * @param textField
     * @return
     */
	private TextField txtFieldSpecs(TextField textField) {
        int w = 50;
        textField.setPrefWidth(w);
        textField.setEditable(false);
        return textField;
    }

    // -------------------------------------------------------------------------

    /**
     * btnRollOnClick metoden er den der sker når der trykkes på knappen
     */
    private void btnRollOnClick() {
		enableHoldsButtons(); // Når der er blevet rullet en gang bliver holds knappen enabled igen så
        yatzy.throwDice(holds()); // Kaster terningene tager holds metoden for at se hvilke terninger der bliver beholdt
        lblRolled.setText("Rolled: " + yatzy.getThrowCount()); // Sætter antallet af rul så når man også rammer 3 skal man vælge
		updateTextFieldWithFaceValues(); // Opdatere de tekstfelter der viser terninge slagen oppe i dicepane
		updatingScoreBoard(); // Opdatere alle tekstfelter med yatzy
		disableRollButton(); // Metode til når der kommer 3 rul at roll button skal disables


    }

    /**
     * // Metode til når der kommer 3 rul at roll button skal disables
     */
	private void disableRollButton() {
		if(yatzy.getThrowCount() == 3){
			btnRoll.setDisable(true);
		}
	}

    /**
     * Returner 5 boolske værdier som viser om checkboksene er blevet valgt
     * @return
     */
	private boolean[] holds(){
		boolean[] holds = new boolean[chbHolds.length];
		for (int i = 0; i < holds.length; i++) {
			if(chbHolds[i].isSelected()){
				holds[i]=true;
			}
		}
		return holds;
	}

    /**
     * Enabler holds knappen igen efter første rul
     */
	private void enableHoldsButtons(){
		for (CheckBox chbHold : chbHolds) {
			chbHold.setDisable(false);
		}
	}

    /**
     * Opdatere alle tekstfelter med yatzy.getresults så vi har værdierne
     */
	private void updatingScoreBoard(){
		for (int i = 0; i < yatzy.getResults().length; i++) {
            if(txfResults[i].isDisabled()){
                //
            }else {
                txfResults[i].setText(String.valueOf(yatzy.getResults()[i]));
            }

		}
	}

    /**
     * opdaterer tekstfelterne i toppen med de nye værdier
     */
	private void updateTextFieldWithFaceValues(){
		for (int i = 0; i < txfValues.length; i++) {
			if (!chbHolds[i].isSelected()) {
				txfValues[i].setText(String.valueOf(yatzy.getValues()[i]));
			}
		}
	}

    /**
     * Mouse click event som trigger når man klikker på et tekstfelterne
     * @param event
     */

	private void chooseFieldAction(MouseEvent event) {
		TextField txf = (TextField) event.getSource();
		txf.setDisable(true);
		int sumSame = 0;
		int sumOther = 0;
		int bonusPoints = 0;
        int total = 0;
		for (int i = 0; i < txfResults.length; i++) {
			if(txfResults[i].isDisabled() && i < 6){
				sumSame += Integer.parseInt(txfResults[i].getText());
                total += Integer.parseInt(txfResults[i].getText());

			}else if(txfResults[i].isDisabled() && i >= 6){
				sumOther += Integer.parseInt(txfResults[i].getText());
                total += Integer.parseInt(txfResults[i].getText());
			}
		}
		if(sumSame >= 63){
			bonusPoints = 50;
            total += bonusPoints;
		}
		txfSumSame.setText(String.valueOf(sumSame));
		txfSumOther.setText(String.valueOf(sumOther));
		txfBonus.setText(String.valueOf(bonusPoints));
        txfTotal.setText(String.valueOf(total));
		updateField();
	}

    /**
     * opdaterer felterne og tjekker hvilke der er blevet valgt.
     *
     */
	private void updateField(){

        for (CheckBox chbHold : chbHolds) {
            if (chbHold.isSelected()) {
                chbHold.setSelected(false);
            }
        }
        for (TextField txfResult : txfResults) {
            if (!txfResult.isDisabled()) {
                txfResult.setText("0");
            }

        }

		yatzy.resetThrowCount();
		btnRoll.setDisable(false);
		lblRolled.setText("Rolled: " + yatzy.getThrowCount());

        for (TextField txfValue : txfValues) {
            if (!txfValue.isDisabled()) {
                txfValue.setText(null);
            }
        }
	}
}
