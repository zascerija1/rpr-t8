package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sun.util.resources.cldr.ga.CurrencyNames_ga;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class ProzorController {


    public TextField Ime;
    public TextField Prezime;
    public TextField Adresa;
    public TextField Grad;
    public TextField postBroj;
    HashMap<Control, Boolean> greske = new HashMap<>();


    boolean greskaO = false;

    @FXML
    public void initialize() {
        postBroj.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean o, Boolean n) {
                if (!n) {
                    if (!greske.containsKey(postBroj)) greske.put(postBroj, false);
                    noviProzor a = new noviProzor();
                    Thread nit = new Thread(a);
                    nit.start();


                }
            }
        });


    }

    public void submit(ActionEvent actionEvent) {
        if (greske.containsValue(true) || (greske.size() != 5)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("POGREŠAN UNOS");
            if (greske.containsValue(true)) alert.setHeaderText("Neispravni podaci");
            else alert.setHeaderText("Nepotpuni podaci");
            alert.setContentText("Unesite ponovo!");

            alert.showAndWait();
        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Slanje ");
            alert.setHeaderText("Podaci poslani");
            alert.setContentText("Uspješan unos!");

            alert.showAndWait();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();

        }
    }

    class noviProzor implements Runnable {

        public void prikazi() {

            String adresa = "http://c9.etf.unsa.ba/proba/postanskiBroj.php?postanskiBroj=" + postBroj.getText();


            try {
                URL url = new URL(adresa);
                BufferedReader ulaz = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
                String json = "", line = null;
                while ((line = ulaz.readLine()) != null)
                    json = json + line;
                if (json.contains("NOT")) {
                    Platform.runLater(() -> {
                        postBroj.setStyle("-fx-background-color: rgba(255,45,86,0.74)");
                    });
                    greske.replace(postBroj, true);
                } else {
                    Platform.runLater(() -> {
                        postBroj.setStyle("-fx-background-color: rgba(72,128,85,0.4)");
                    });
                    greske.replace(postBroj, false);
                }
                ulaz.close();
            } catch (MalformedURLException e) {
                System.out.println("String " + adresa + " ne predstavlja validan URL");
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


        @Override
        public void run() {

            prikazi();

        }
    }

    private boolean oboji(TextField ime) {
        if (ime.getText().length() < 1 || ime.getText().length() > 20) greskaO = true;
        int n = ime.getText().length();
        for (int i = 0; i < n; i++) {
            if (!(Character.isLetter(ime.getText().charAt(i)) || Character.isWhitespace(ime.getText().charAt(i))))
                greskaO = true;
        }
        if (greskaO) ime.setStyle("-fx-background-color: rgba(255,45,86,0.74)");

        else {
            ime.setStyle("-fx-background-color: rgba(72,128,85,0.4)");
            greskaO = false;
        }

        return greskaO;
    }

    public void unosIme(KeyEvent keyEvent) {
        if (!greske.containsKey(Ime)) greske.put(Ime, false);
        greske.replace(Ime, oboji(Ime));
    }

    public void unosPrezime(KeyEvent keyEvent) {
        if (!greske.containsKey(Prezime)) greske.put(Prezime, false);
        greske.replace(Prezime, oboji(Prezime));
    }

    public void unosGrad(KeyEvent keyEvent) {
        if (!greske.containsKey(Grad)) greske.put(Grad, false);
        greske.replace(Grad, oboji(Grad));
    }

    public void unosAdresa(KeyEvent keyEvent) {
        boolean greskaO = false;

        if (Adresa.getText().length() > 0 && !(Character.isLetterOrDigit(Adresa.getText().charAt(Adresa.getText().length() - 1))
                || Character.isWhitespace(Adresa.getText().charAt(Adresa.getText().length() - 1)))) {

            Adresa.setStyle("-fx-background-color: rgba(255,45,86,0.74)");
            greskaO = true;
        } else {
            Adresa.setStyle("-fx-background-color: rgba(72,128,85,0.4)");
            greskaO = false;
        }
        if (!greske.containsKey(Adresa)) greske.put(Adresa, false);
        greske.replace(Adresa, greskaO);

    }


}
