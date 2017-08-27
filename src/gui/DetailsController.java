package gui;

import classes.Tribe;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class DetailsController implements Initializable {
    @FXML
    public TextField colour;

    @FXML
    public TextField type;

    @FXML
    public TextField explore;

    @FXML
    public TextField agri;

    @FXML
    public TextField military;

    @FXML
    public TextField battles;


    public void setFields(Tribe tribe) {
        colour.setText(tribe.getColour().toString());
        // type.setText();
        // explore.setText(tribe);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initialising...");
    }
}
