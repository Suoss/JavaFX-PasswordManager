package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.io.IOException;

public class Controller {

    @FXML
    CheckBox letters;
    @FXML
    CheckBox special;
    @FXML
    CheckBox numbers;
    @FXML
    Button generate;
    @FXML
    Slider lengthSlider;
    @FXML
    TextField key;
    @FXML
    TextField password;
    @FXML
    TextField encryptKey;
    @FXML
    Button readPassword;
    @FXML
    Button writeButton;
    @FXML
    Button passwordList;
    @FXML
    ImageView image;
    @FXML
    private void generatePass() {
        if (!letters.isSelected() && !special.isSelected() && !numbers.isSelected()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR");
            alert.setHeaderText("Please tick at least one of the three given options: \nLetters, Special, Numbers");
            alert.showAndWait();
        } else {
            System.out.println(Main.generate(letters.isSelected(), special.isSelected(), numbers.isSelected(), (int) lengthSlider.getValue()));
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();

            String password = Main.generate(letters.isSelected(), special.isSelected(), numbers.isSelected(), (int) lengthSlider.getValue());
            content.putString(password);
            clipboard.setContent(content);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Password Generated!");
            alert.setHeaderText("Your password has been copied to your clipboard.");
            alert.setContentText(password);
            alert.showAndWait();
        }
    }
    @FXML
    private void getPassword(){
        String password = Main.getPasswordInFile(key.getText(), encryptKey.getText());
        if(password != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Password Found!");
            alert.setHeaderText("Your password has been copied to your clipboard.");
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(password);
            clipboard.setContent(content);
            alert.setContentText(password);
            alert.showAndWait();
        }
    }
    @FXML
    private void writePassword(){
        Main.writePassword(key.getText(), password.getText(), encryptKey.getText());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Password written!");
        alert.setHeaderText("Your password has been encrypted to the disk.");
        alert.setContentText("DO NOT forget your encryption key, it is vital to retrieve the password from the disk!");
        alert.showAndWait();
    }
    @FXML
    private void listPasswords() throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Your password keys will be displayed below!");
        StringBuilder sb = new StringBuilder();
        for(String i : Main.listPasswords()){
            sb.append(i+" \n");
        }
        alert.setContentText(sb.toString());
        alert.showAndWait();
    }
}
