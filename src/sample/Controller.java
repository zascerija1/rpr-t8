package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.File;

public class Controller {


    public TextField unos;
    public Button trazi;
    public ListView lista;
    public ObservableList<String> putanje = FXCollections.observableArrayList();
    public Button prekini;

    File dat1 = new File(System.getProperty("user.home"));
    String ime;

    @FXML
    void initialize() {
        prekini.setDisable(true);

    }

    public void zaustaviNit(ActionEvent actionEvent) {
        prekini.setDisable(true);
    }

    class Izvrsna implements Runnable {


        public void ispisi(File dat) {

            try {
                if (!prekini.isDisabled()) {
                    if (dat.isDirectory()) {
                        File[] fajlovi = dat.listFiles();
                        for (int i = 0; i < fajlovi.length; i++) ispisi(fajlovi[i]);
                    } else if (dat.isFile()) {
                        if (dat.getName().contains(ime)) {
                            Platform.runLater(() -> putanje.add(dat.getAbsolutePath()));
                        }
                    }
                    Platform.runLater(() -> lista.setItems(putanje));
                } else return;
            } catch (Exception e) {
            }
        }

        @Override
        public void run() {

            ispisi(dat1);
            trazi.setDisable(false);
            prekini.setDisable(true);
        }
    }

    public void pressed(ActionEvent actionEvent) {

        Izvrsna nova = new Izvrsna();
        ime = unos.getText();
        prekini.setDisable(false);
        trazi.setDisable(true);
        Thread nit = new Thread(nova);
        nit.start();

        lista.getItems().removeAll(putanje);
        putanje.clear();
    }

}


