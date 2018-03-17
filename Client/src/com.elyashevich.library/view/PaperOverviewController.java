package com.elyashevich.library.view;

import com.elyashevich.library.demo.RemoteServiceClient;
import com.elyashevich.library.entity.genre.Genre;
import com.elyashevich.library.entity.paper.PaperEdition;
import com.elyashevich.library.exception.DAOTechnicalException;
import com.elyashevich.library.util.IdGenerator;
import com.elyashevich.library.util.TextConstant;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.rmi.RemoteException;

import static com.elyashevich.library.util.TextConstant.*;

public class PaperOverviewController {
    @FXML
    private TableView<PaperEdition> paperTable;
    @FXML
    private TableColumn<PaperEdition, String> titleColumn;
    @FXML
    private TableColumn<PaperEdition, String> priceColumn;

    @FXML
    private Label titleLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label periodicityLabel;
    @FXML
    private TextField searchField;
    @FXML
    private TextArea genresArea;
    @FXML
    private TextArea descriptionTextArea;

    private RemoteServiceClient main;

    public PaperOverviewController() {
    }

    @FXML
    private void initialize() {
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().getPriceProperty().asString());

        paperTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue));
    }

    private void showPersonDetails(PaperEdition paperEdition) {
        if (paperEdition != null) {
            titleLabel.setText(paperEdition.getTitle());
            typeLabel.setText(paperEdition.getCategory());
            priceLabel.setText(String.valueOf(paperEdition.getPrice()));
            periodicityLabel.setText(String.valueOf(paperEdition.getPeriodicity()));
            descriptionTextArea.setText(paperEdition.getDescription());
            try {
                StringBuilder stringBuilder = new StringBuilder();
                //System.out.println(main.defineRemoteService().findByPaperIDGenres(IdGenerator.defineID(paperEdition.getId())));
                System.out.println("IDDDD:"+paperEdition.getId());
                System.out.println("**************");
                    for (Genre genre : main.defineRemoteService().findByPaperIDGenres(Long.parseLong(paperEdition.getId()))) {
                        stringBuilder.append(genre.getName()).append(TextConstant.NEW_LINE);
                    }
                genresArea.setText(stringBuilder.toString());
            } catch (DAOTechnicalException | RemoteException e) {
                exceptionAlert(TITLE_ERROR, TITLE_ERROR, e.getMessage());
                System.exit(0);
            }
        } else {
            titleLabel.setText(TextConstant.EMPTY_STRING);
            typeLabel.setText(TextConstant.EMPTY_STRING);
            priceLabel.setText(TextConstant.EMPTY_STRING);
            periodicityLabel.setText(TextConstant.EMPTY_STRING);
            genresArea.setText(TextConstant.EMPTY_STRING);
            descriptionTextArea.setText(TextConstant.EMPTY_STRING);
        }
    }

    @FXML
    private void handleSearch() {
        String searchData = searchField.getText()!=null?searchField.getText():"";
            try {
                System.out.println(main.defineRemoteService().findByDescriptionPapers(searchData));
                main.getPaperEditionData().clear();
                main.getPaperEditionData().addAll(main.defineRemoteService().findByDescriptionPapers(searchData));
            } catch (DAOTechnicalException| RemoteException e) {
                exceptionAlert(TITLE_ERROR, TITLE_ERROR, e.getMessage());
                System.exit(0);
            }
    }

    @FXML
    private void handleNewPerson() {
        PaperEdition tempPaperEdition = new PaperEdition();
      /*  try {
            tempPaperEdition.setId(IdGenerator.generatePaperId((ArrayList<PaperEdition>) main.defineRemoteService().findAllPapers()));
        } catch (DAOTechnicalException | RemoteException e) {
            e.printStackTrace();
        }*/
        boolean okClicked = main.showPersonEditDialog(tempPaperEdition);
        if (okClicked) {
            System.out.println(main.getPaperEdition());
            main.getPaperEditionData().add(main.getPaperEdition());
        }
    }


    @FXML
    private void handleEditPerson() {
        PaperEdition selectedPaperEdition = paperTable.getSelectionModel().getSelectedItem();
        int index = main.getPaperEditionData().indexOf(selectedPaperEdition);
        if (selectedPaperEdition != null) {
            boolean okClicked = main.showPersonEditDialog(selectedPaperEdition);
            if (okClicked) {
                main.getPaperEditionData().set(index, selectedPaperEdition);
                showPersonDetails(selectedPaperEdition);
            }
        } else {
            exceptionAlert(TITLE_SELECTION_MESSAGE, HEADER_SELECTION_MESSAGE, CONTENT_SELECTION_MESSAGE);
        }
    }

    @FXML
    private void handleDeletePerson() {
        int selectedIndex = paperTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            try {
                main.defineRemoteService().deleteByIdPapers(IdGenerator.defineID(paperTable.getItems().get(selectedIndex).getId()));
            } catch (DAOTechnicalException|RemoteException e) {
                exceptionAlert(TITLE_ERROR, TITLE_ERROR, e.getMessage());
                System.exit(0);
            }
            paperTable.getItems().remove(selectedIndex);
        } else {
            exceptionAlert(TITLE_SELECTION_MESSAGE, HEADER_SELECTION_MESSAGE, CONTENT_SELECTION_MESSAGE);
        }
    }

    private void exceptionAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.initOwner(main.getPrimaryStage());
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

    public void setMain(RemoteServiceClient main) {
        this.main = main;
        paperTable.setItems(main.getPaperEditionData());
    }
}
