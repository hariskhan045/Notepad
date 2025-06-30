package note__pad_final;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

public class NotepadApp extends Application {

    private TextArea textArea;
    private Stage primaryStage; 

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage; 
        primaryStage.setTitle(" Notepad");

        BorderPane root = new BorderPane();
        textArea = new TextArea();
        textArea.setFont(new javafx.scene.text.Font("Monospaced", 14));
        root.setCenter(textArea);

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");

        
        MenuItem saveToFileMenuItem = new MenuItem("Save to File...");
        saveToFileMenuItem.setOnAction(event -> saveToFileAction());

        
        MenuItem saveToDbMenuItem = new MenuItem("Save in data base ?");
        saveToDbMenuItem.setOnAction(event -> saveToDbAction());

       
        SeparatorMenuItem separator = new SeparatorMenuItem();
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(event -> primaryStage.close());

 
        fileMenu.getItems().addAll(saveToFileMenuItem, saveToDbMenuItem, separator, exitMenuItem);
        menuBar.getMenus().add(fileMenu);

        root.setTop(menuBar);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

  
    private void saveToFileAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save in computer File");

       
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

  
        File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(textArea.getText());
                showAlert(Alert.AlertType.INFORMATION, "", "File saved successfully at:\n" + file.getAbsolutePath());
            } catch (IOException ex) {
                showAlert(Alert.AlertType.ERROR, "File Error", "Could not save the file: " + ex.getMessage());
            }
        }
    }

    
     
    private void saveToDbAction() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Save Note to Database");
        dialog.setHeaderText("Enter a title for your note:");
        dialog.setContentText("Title:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(title -> {
            if (title.trim().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "Title cannot be empty.");
            } else {
                String content = textArea.getText();
                boolean success = DatabaseManager.saveNote(title, content);

                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Note saved successfully to the database!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to save the note to the database.");
                }
            }
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}