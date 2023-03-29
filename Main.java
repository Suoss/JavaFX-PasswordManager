package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Password Manager");
        primaryStage.setScene(new Scene(root, 350, 600));
        primaryStage.show();
    }


    private static void checkFile() throws IOException {
        if(!Files.exists(Paths.get("passwords.txt"))){
            Properties prop = new Properties();
            prop.store(new FileWriter("passwords.txt"), null);
        }
    }


    public static String generate(boolean l, boolean s, boolean n, int length) {
        String available_characters = "";
        if(l){
            available_characters += "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        }
        if(n) {
            available_characters += "0123456789";
        }
        if(s){
            available_characters += "#*%$!";
        }
        char[] characters= available_characters.toCharArray();
        String password = "";
        for(int i = 0; i < length; i++){
            password+=characters[ThreadLocalRandom.current().nextInt(0, characters.length)];
        }
        return password;
    }

    public static String getPasswordInFile(String key, String decrypt){
        String x = lengther(decrypt);
        try {
            Properties properties = new Properties();
            properties.load(new FileReader("passwords.txt"));
            String encrypted_pass = properties.getProperty(key);
            SecretKeySpec secretKey = new SecretKeySpec(x.getBytes(), "AES");
            return decryptText(Base64.getDecoder().decode(encrypted_pass.getBytes()), secretKey); // https://en.wikipedia.org/wiki/Base64
        }catch(Exception ex){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Wrong Password or Encryption key!");
            alert.setHeaderText("Invalid key!");
            alert.showAndWait();
        }
        return null;
    }

    public static void writePassword(String key, String password, String decrypt){
        String x = lengther(decrypt);
        try{
            Properties properties = new Properties(); //puts it in memory
            properties.load(new FileReader("passwords.txt")); // loads file, reads it
            SecretKeySpec secretKey = new SecretKeySpec(x.getBytes(), "AES");
            properties.put(key, new String(Base64.getEncoder().encode(encryptText(password, secretKey)))); // writes into memory version (also sends into encryption)
            properties.store(new FileWriter("passwords.txt"), null); // writes our version from memory into file
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            System.exit(-1);
        }
    }

    public static byte[] encryptText(String plainText, SecretKey secKey) throws Exception{
        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
        return byteCipherText;
    }

    public static String decryptText(byte[] byteCipherText, SecretKey secKey) throws Exception {
        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
        byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
        return new String(bytePlainText);
    }

    private static String lengther(String x){ //any string to make it 16 long
        while(x.length() < 16){
            x += "x";
        }
        if(x.length() > 16){
            return x.substring(0, 16);
        } else {
            return x;
        }
    }
    public static ArrayList<String> listPasswords() {
        File file = new File("");
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(file.getAbsolutePath()+"/passwords.txt"));
            lines.remove(0);
            ArrayList<String> keyList = new ArrayList<>();
            for(String x : lines){
                keyList.add(x.split("=")[0]);
            }
            return keyList;

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        try{
            checkFile();
        } catch (Exception ex){
            System.exit(-1);
        }
        launch(args);
    }
}
