package src;

import java.io.File;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This is App class mainly control and display GUI, such as home scene, alert
 * scene.
 * 
 * @author Yi-Ming Chen
 * @version 2020-08-04
 *
 */

public class App extends Application {
    private static Stage stage;
    private static Scene scene;
    public static String excel;
    public static String png;

    /**
     * Display the home screen for user
     * 
     */
    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("App.fxml")); // load fxml
            scene = new Scene(root);
            stage.setResizable(false);
            stage.setTitle("Detection of Battery Rating label drawing");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private TextField excelPath;
    @FXML
    private TextField pngPath;

    /**
     * This method display the [Browse file] scene with excel file
     * 
     * @param event click Browse file button
     * @throws Exception
     * @reference https://www.yiibai.com/javafx/javafx_filechooser.html
     */
    @FXML
    void BrowseExcel(ActionEvent event) throws Exception {
        FileChooser fileChooser = new FileChooser();
        // initial open file position in desktop
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "\\desktop"));
        // limit open file format
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XLS", "*.xls"));
        fileChooser.setTitle("Select Excel File");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            excelPath.setText(file.getPath());
            excel = file.getPath();
            System.out.println(excel);
        } else {
            System.out.println("no entry path");
        }

    }

    /**
     * This method display the [Browse file] scene with image file
     * 
     * @param event click Browse file button
     * @throws Exception
     */
    @FXML
    void BrowseImg(ActionEvent event) throws Exception {
        FileChooser fileChooser = new FileChooser();
        // initial open file position in desktop
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "\\desktop"));
        // limit open file format
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
        fileChooser.setTitle("Select PNG File");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            pngPath.setText(file.getPath());
            png = file.getPath();
        } else {
            System.out.println("no entry path");
        }
        System.out.println(png);
    }

    /**
     * This method start to run the programme of matching
     * 
     * @param event click start detection button
     * @throws Exception
     */
    @FXML
    void StartDetection(ActionEvent event) throws Exception {
        System.out.println("start Detection");
        if (excel == null || png == null) {// import empty path
            App a = new App();
            a.showAlertWindow();
            System.out.println("Display empty path file scene ");
        } else if (png != null && fileManager.getImgSize(png) > 5000.0) {// import size >5MB
            App a = new App();
            a.showAlertSizeWindow();
            System.out.println("Display empty path file scene ");
        } else {
            if (fileManager.checkOpening(excel) == true) { // whether file is opening
                App a = new App();
                a.showFileOpeningWindow();
                System.out.println("Display alarm with file opening scene");
            } else if (fileManager.checkContent(excel) == false) {// whether import empty content
                App a = new App();
                a.showErrorFileWindow();
                System.out.println("Display empty content with file scene ");
            } else {
                System.out.println("start match");
                ProgressDisplay pa = new ProgressDisplay();
                Stage ss = new Stage();
                pa.start(ss);
            }
        }
    }

    public static Stage window;

    /**
     * This method display alert scene when user did not import any file
     * 
     */
    public void showAlertWindow() {
        try {
            window = new Stage();
            window.initModality((Modality.APPLICATION_MODAL));
            Parent root = FXMLLoader.load(getClass().getResource("AlertWindow.fxml")); // load fxml
            Scene scene = new Scene(root);
            window.setScene(scene);
            window.setResizable(false);
            window.setTitle("Please import file");
            window.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method display alert scene when user import PNG size is more than 5MB
     * 
     */
    public void showAlertSizeWindow() {
        try {
            window = new Stage();
            window.initModality((Modality.APPLICATION_MODAL));
            Parent root = FXMLLoader.load(getClass().getResource("AlertSizeWindow.fxml")); // load fxml
            Scene scene = new Scene(root);
            window.setScene(scene);
            window.setResizable(false);
            window.setTitle("Error size import file");
            window.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method display alert scene when user import an excel file with empty
     * content
     * 
     */
    public void showErrorFileWindow() {
        try {
            window = new Stage();
            window.initModality((Modality.APPLICATION_MODAL));
            Parent root = FXMLLoader.load(getClass().getResource("ErrorFileWindow.fxml")); // load fxml
            Scene scene = new Scene(root);
            window.setScene(scene);
            window.setResizable(false);
            window.setTitle("Empty excel content file");
            window.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * This method display alert scene when user import an excel file which is
     * opening
     * 
     */
    public void showFileOpeningWindow() {
        try {
            window = new Stage();
            window.initModality((Modality.APPLICATION_MODAL));
            Parent root = FXMLLoader.load(getClass().getResource("fileOpening.fxml")); // load fxml
            Scene scene = new Scene(root);
            window.setScene(scene);
            window.setResizable(false);
            window.setTitle("File is opening");
            window.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is to close the scene.
     * 
     * @param event click close button
     */
    @FXML
    void closeWindow(ActionEvent event) {
        window.close();
        System.out.println("close Window");
    }

    public static void main(String[] args) {
        launch(args);
    }
}