package ru.vlsoft;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;
import java.util.List;

public class CalcController {

    String currentInput = "0";
    private Operation lastOperation = Operation.EMPTY;
    private Double result = 0.;

    private boolean historyOpened = true;
    private double historyPaneWidth = 400.;

    @FXML
    private GridPane mainPane;

    @FXML
    private Button btn0;

    @FXML
    private Button btn1;

    @FXML
    private Button btn2;

    @FXML
    private Button btn3;

    @FXML
    private Button btn4;

    @FXML
    private Button btn5;

    @FXML
    private Button btn6;

    @FXML
    private Button btn7;

    @FXML
    private Button btn8;

    @FXML
    private Button btn9;

    @FXML
    private Button btnDot;

    @FXML
    private Button btnEqual;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnSubtr;

    @FXML
    private Button btnMult;

    @FXML
    private Button btnDiv;

    @FXML
    private Button btnSqrt;

    @FXML
    private Button btnDegr;

    @FXML
    private Button btnErase;

    @FXML
    private Button btnShowHideHistory;

    @FXML
    private ListView listViewHistory;

    @FXML
    private Label labelScreen;

    @FXML
    private void initialize() throws SQLException {

        showHistory();

        btn0.setOnAction((actionEvent) -> takeInput("0"));
        btn1.setOnAction((actionEvent) -> takeInput("1"));
        btn2.setOnAction((actionEvent) -> takeInput("2"));
        btn3.setOnAction((actionEvent) -> takeInput("3"));
        btn4.setOnAction((actionEvent) -> takeInput("4"));
        btn5.setOnAction((actionEvent) -> takeInput("5"));
        btn6.setOnAction((actionEvent) -> takeInput("6"));
        btn7.setOnAction((actionEvent) -> takeInput("7"));
        btn8.setOnAction((actionEvent) -> takeInput("8"));
        btn9.setOnAction((actionEvent) -> takeInput("9"));
        btnDot.setOnAction((actionEvent) -> {
            if (!labelScreen.getText().contains(".")) {
                takeInput(".");
            }
        });

        btnAdd.setOnAction((actionEvent) -> {
            evaluateExpression();
            lastOperation = Operation.ADDING;
        });

        btnSubtr.setOnAction((actionEvent) -> {
            evaluateExpression();
            lastOperation = Operation.SUBTRACTION;
        });

        btnMult.setOnAction((actionEvent) -> {
            evaluateExpression();
            lastOperation = Operation.MULTIPLICATION;
        });

        btnDiv.setOnAction((actionEvent) -> {
            evaluateExpression();
            lastOperation = Operation.DIVISION;
        });

        btnSqrt.setOnAction((actionEvent) -> {
            if (lastOperation != Operation.EMPTY) {
                evaluateExpression();
            }
            lastOperation = Operation.SQRT;
            evaluateExpression();
            lastOperation = Operation.EMPTY;
        });

        btnDegr.setOnAction((actionEvent) -> {
            evaluateExpression();
            lastOperation = Operation.DEGREE;
        });

        btnErase.setOnAction((actionEvent) -> {
            currentInput = "0";
            lastOperation = Operation.EMPTY;
            result = 0.;
            labelScreen.setText(currentInput);
        });

        btnShowHideHistory.setOnAction((actionEvent) -> {

            if (historyOpened) {

                mainPane.getColumnConstraints().get(1).setMaxWidth(0.);
                mainPane.getScene().getWindow().setWidth(mainPane.getScene().getWindow().getWidth() - historyPaneWidth);

            } else {

                mainPane.getColumnConstraints().get(1).setMaxWidth(historyPaneWidth);
                mainPane.getScene().getWindow().setWidth(mainPane.getScene().getWindow().getWidth() + historyPaneWidth);

            }

            historyOpened = !historyOpened;

        });

        btnEqual.setOnAction(actionEvent -> {
            evaluateExpression();
            lastOperation = Operation.EMPTY;
            currentInput = String.valueOf(result);
        });

    }

    private void takeInput(String s) {

        if (currentInput.equals("0")) {
            currentInput = s;
        } else {
            currentInput = currentInput + s;
        }
        labelScreen.setText(currentInput);

    }

    private void evaluateExpression() {

        double currentValue = Double.parseDouble(currentInput);
        double prevResult = result;

        switch (lastOperation) {
            case ADDING:
                result = result + currentValue;
                break;
            case SUBTRACTION:
                result = result - currentValue;
                break;
            case MULTIPLICATION:
                result = result * currentValue;
                break;
            case DIVISION:
                result = result / currentValue;
                break;
            case SQRT:
                result = Math.sqrt(currentValue);
                currentInput = String.valueOf(result);
                break;
            case DEGREE:
                result = Math.pow(result, currentValue);
                break;
            case EMPTY:
                result = currentValue;
                break;
            default:
                result = 0.;
        }

        if (lastOperation != Operation.EMPTY) {
            try {
                if (lastOperation == Operation.SQRT) {
                    DBUtils.log("" + lastOperation + " " + currentValue + " = " + result);
                } else {
                    DBUtils.log("" + prevResult + " " + lastOperation + " " + currentValue + " = " + result);
                }
                showHistory();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

        if (lastOperation != Operation.SQRT) {
            currentInput = "0";
        }
        labelScreen.setText(String.valueOf(result));

    }

    private void showHistory() throws SQLException {

        List<String> hitory = DBUtils.getLog();
        listViewHistory.setItems(FXCollections.observableArrayList(hitory));

    }

    private enum Operation {
        ADDING("+"),
        SUBTRACTION("-"),
        MULTIPLICATION("*"),
        DIVISION("/"),
        DEGREE("^"),
        SQRT("√"), // Square root
        EMPTY("");

        private String value;

        Operation(String s) {
            value = s;
        }

        @Override
        public String toString() {
            return value;
        }
    }

}
