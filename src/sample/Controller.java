package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

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

        class RowSelectChangeListener implements ChangeListener<Number> {

            @Override
            public void changed(ObservableValue<? extends Number> ov,
                                Number oldVal, Number newVal) {

                int ix = newVal.intValue();

                if ((ix < 0) || (ix >= putanje.size())) {

                    return; // invalid data
                }
                try {
                    Stage scena = new Stage();
                    Parent root = null;
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("prozor.fxml"));
                    //loader.setController(prozor);
                    root = loader.load();
                    root.setVisible(true);
                    scena.setTitle(ime);
                    scena.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
                    scena.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
        lista.getSelectionModel().selectedIndexProperty().addListener(new RowSelectChangeListener());

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


