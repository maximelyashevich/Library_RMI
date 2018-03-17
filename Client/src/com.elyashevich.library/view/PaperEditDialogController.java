package com.elyashevich.library.view;

import com.elyashevich.library.entity.genre.Genre;
import com.elyashevich.library.entity.paper.PaperEdition;
import com.elyashevich.library.exception.DAOTechnicalException;
import com.elyashevich.library.service.RemoteService;
import com.elyashevich.library.util.IdGenerator;
import com.elyashevich.library.util.TextConstant;
import com.elyashevich.library.validator.LibraryValidator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

import static com.elyashevich.library.util.TextConstant.TITLE_ERROR;

public class PaperEditDialogController {

    @FXML
    private TextField titleField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField periodicityField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private ListView<Item> listView;
    @FXML
    private RadioButton radioButton1;
    @FXML
    private RadioButton radioButton2;
    @FXML
    private RadioButton radioButton3;

    @FXML
    private ToggleGroup group = new ToggleGroup();

    private Stage dialogStage;
    private PaperEdition paperEdition;
    private boolean okClicked = false;
    private HashSet<String> genresName = new HashSet<>();

    private static final String TITLE_MESSAGE = "Invalid Fields";
    private static final String HEADER_MESSAGE = "Please correct invalid fields";
    private RemoteService service;

    public void setRemoteService(RemoteService service){
        this.service = service;
    }

    @FXML
    private void initialize() {
        radioButton1.setUserData(TextConstant.NEWSPAPER);
        radioButton1.setToggleGroup(group);
        radioButton2.setUserData(TextConstant.MAGAZINE);
        radioButton2.setToggleGroup(group);
        radioButton3.setUserData(TextConstant.BOOK);
        radioButton3.setToggleGroup(group);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void identifyEdition(PaperEdition paperEdition){
        this.paperEdition = paperEdition;
    }
    private void initRadioToggle(String string) {
        switch (string) {
            case TextConstant.NEWSPAPER:
                radioButton1.setSelected(true);
                break;
            case TextConstant.MAGAZINE:
                radioButton2.setSelected(true);
                break;
            case TextConstant.BOOK:
                radioButton3.setSelected(true);
                break;
            default:
                radioButton1.setSelected(true);
                break;
        }
    }

    public PaperEdition getPaperEdition() {
        return paperEdition;
    }

    private void exceptionAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

    public void setPaperEdition(PaperEdition paperEdition) {
        this.paperEdition = paperEdition;

        titleField.setText(paperEdition.getTitle());

        initRadioToggle(paperEdition.getCategory());
        priceField.setText(Double.toString(paperEdition.getPrice()));
        descriptionTextArea.setText(paperEdition.getDescription());
        periodicityField.setText(String.valueOf(paperEdition.getPeriodicity()));
        setTypeFromRadioButton(paperEdition);
        if (service != null) {
            try {
                Set<Genre> list = new HashSet<>(service.findAllGenres());

                for (Genre genre : list) {
                    Item item = new Item(genre.getName(), false);
                    if (paperEdition.getId() != null) {
                        for (Genre genre1 : service.findByPaperIDGenres(IdGenerator.defineID(paperEdition.getId()))) {
                            if (genre.equals(genre1)) {
                                System.out.println("current genre: " + item);
                                item.setOn(true);
                            }
                        }
                    }
                    item.onProperty().addListener((obs, wasOn, isNowOn) -> item.setOn(isNowOn));
                    listView.getItems().add(item);
                }

                listView.setCellFactory(CheckBoxListCell.forListView(Item::onProperty));
            } catch (DAOTechnicalException | RemoteException e) {
                exceptionAlert(TITLE_ERROR, TITLE_ERROR, e.getMessage());
            }
        }
    }

    public class Item {
        private final StringProperty name = new SimpleStringProperty();
        private final BooleanProperty on = new SimpleBooleanProperty();

        Item(String name, boolean on) {
            setName(name);
            setOn(on);
        }

        final StringProperty nameProperty() {
            return this.name;
        }

        final String getName() {
            return this.nameProperty().get();
        }

        final void setName(final String name) {
            this.nameProperty().set(name);
        }

        final BooleanProperty onProperty() {
            return this.on;
        }

        final boolean isOn() {
            return this.onProperty().get();
        }

        final void setOn(final boolean on) {
            this.onProperty().set(on);
        }

        @Override
        public String toString() {
            return getName();
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    private void setTypeFromRadioButton(PaperEdition paperEdition){
        group.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {

            if (group.getSelectedToggle() != null) {
                String resultString = group.getSelectedToggle().getUserData().toString();
                System.out.println(resultString);
                paperEdition.setCategory(resultString);
            }
        });
    }
    
    @FXML
    private void handleOk() {
        genresName.clear();
        if (isInputValid()) {
            paperEdition.setTitle(titleField.getText());
            paperEdition.setPrice(new Double(priceField.getText()));
            paperEdition.setPeriodicity(Integer.parseInt(periodicityField.getText()));
            paperEdition.setDescription(descriptionTextArea.getText());
            try {
                if (listView.getItems() != null) {
                    genresName.clear();
                    for (Item item : listView.getItems()) {
                        if (item.isOn()) {
                            genresName.add(item.getName());
                        }
                    }
                    if (paperEdition.getId()==null) {
                        paperEdition = service.createPapers(paperEdition);
                        System.out.println("New id: "+paperEdition.getId());
                    } else {
                        service.updatePapers(paperEdition);
                    }
                    service.updateGenrePaper(paperEdition, genresName);
                    identifyEdition(paperEdition);
                }
            } catch (DAOTechnicalException| RemoteException e) {
                e.printStackTrace();
            }


            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = TextConstant.EMPTY_STRING;

        if (!LibraryValidator.isTitleCorrect(titleField.getText())) {
            errorMessage += TextConstant.E_TITLE;
        }

        if (!LibraryValidator.isLibraryDataCorrect(descriptionTextArea.getText())) {
            errorMessage += TextConstant.E_DESCRIPTION;
        }
        if (!LibraryValidator.isPeriodicityCorrect(periodicityField.getText())) {
            errorMessage += TextConstant.E_PERIODICITY;
        }
        if (!LibraryValidator.isMoneyCorrect(priceField.getText())) {
            errorMessage += TextConstant.E_PRICE;
        } else {
            try {
                Double.parseDouble(priceField.getText());
            } catch (NumberFormatException e) {
                errorMessage += TextConstant.E_PRICE_FORMAT;
            }
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle(TITLE_MESSAGE);
            alert.setHeaderText(HEADER_MESSAGE);
            alert.setContentText(errorMessage);

            alert.showAndWait();
            return false;
        }
    }
}
