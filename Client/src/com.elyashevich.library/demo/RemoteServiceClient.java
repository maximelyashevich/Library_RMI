package com.elyashevich.library.demo;

import com.elyashevich.library.service.RemoteService;
import com.elyashevich.library.entity.paper.PaperEdition;
import com.elyashevich.library.exception.DAOTechnicalException;
import com.elyashevich.library.view.PaperEditDialogController;
import com.elyashevich.library.view.PaperOverviewController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RemoteServiceClient extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<PaperEdition> paperEditionData = FXCollections.observableArrayList();
    private RemoteService service;
    private PaperEdition paperEdition;

    public RemoteServiceClient() {
        Registry registry = null;
        try {
             //registry = LocateRegistry.getRegistry("192.168.43.34", 4396);
            registry = LocateRegistry.getRegistry("localhost", 4399);
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        }
        try {
            assert registry != null;
            System.out.println(registry.lookup("local/LibraryService").getClass());
            service = (RemoteService) registry.lookup("local/LibraryService");
        } catch (RemoteException | NotBoundException e) {
            JOptionPane.showMessageDialog(null, "Connection refused!"+e.getMessage());
            System.exit(0);
        }
        try {
            paperEditionData.addAll(service.findAllPapers());
        } catch (DAOTechnicalException | RemoteException e) {
            e.printStackTrace();
        }
    }


    public ObservableList<PaperEdition> getPaperEditionData() {
        return paperEditionData;
    }

    public RemoteService defineRemoteService(){
        return service;
    }

    public PaperEdition getPaperEdition() {
        return paperEdition;
    }

    public void setPaperEdition(PaperEdition paperEdition) {
        this.paperEdition = paperEdition;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("PaperEditionApp");
        this.primaryStage.setHeight(610);
        this.primaryStage.setWidth(800);
        this.primaryStage.setResizable(false);
        this.primaryStage.getIcons().add(new Image("file:resource/image/if_Address_Book_86957.png"));

        initRootLayout();
        showPersonOverview();
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RemoteServiceClient.class.getResource("/com.elyashevich.library/view/RootLayout.fxml"));
            rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showPersonOverview() {
       try {
           FXMLLoader loader = new FXMLLoader();
           loader.setLocation(RemoteServiceClient.class.getResource("/com.elyashevich.library/view/LibraryOverview.fxml"));
           AnchorPane personOverview = loader.load();

           rootLayout.setCenter(personOverview);

           PaperOverviewController controller = loader.getController();

           controller.setMain(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showPersonEditDialog(PaperEdition paperEdition) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RemoteServiceClient.class.getResource("/com.elyashevich.library/view/PaperEditDialog.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit paper");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            PaperEditDialogController controller = loader.getController();
            controller.setRemoteService(service);
            controller.setDialogStage(dialogStage);
            controller.setPaperEdition(paperEdition);
            dialogStage.showAndWait();
            System.out.println("Changed: "+controller.getPaperEdition());
            setPaperEdition(controller.getPaperEdition());
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public Stage getPrimaryStage() {
        return primaryStage;
    }
    public static void main(String... args) {
        launch(args);
    }
}