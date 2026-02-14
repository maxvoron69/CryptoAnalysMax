package ru.javarush.pastukhov.cryptoanalysmax;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainAppFX extends Application {

    private final TextArea inputArea = new TextArea();
    private final TextArea outputArea = new TextArea();
    private final TextField keyField = new TextField("3");
    private final Label statusLabel = new Label("Готово");

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Шифр Цезаря — JavaFX");

        // Верхняя панель: кнопки и поле ключа
        HBox topPanel = new HBox(10);
        topPanel.setPadding(new Insets(10));
        topPanel.getChildren().addAll(
                new Label("Ключ:"), keyField,
                createButton("Зашифровать", this::encrypt),
                createButton("Дешифровать", this::decrypt),
                createButton("Прочитать файл", this::loadFile),
                createButton("Сохранить", this::saveFile),
                createButton("Взломать (Brute Force)", this::bruteForce)
        );

        // Центральная панель: вход и выход
        SplitPane center = new SplitPane();
        inputArea.setPromptText("Введите текст...");
        outputArea.setPromptText("Результат...");
        center.getItems().addAll(inputArea, outputArea);
        center.setDividerPositions(0.5);

        // Нижняя панель: статус
        VBox root = new VBox(
                topPanel,
                center,
                statusLabel
        );
        root.setSpacing(10);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        Button button = new Button(text);
        button.setOnAction(handler);
        return button;
    }

    private void encrypt(javafx.event.ActionEvent event) {
        try {
            String text = inputArea.getText();
            int key = Integer.parseInt(keyField.getText());
            char[] encrypted = CodeCaesars.encryption(text.toCharArray(), key);
            outputArea.setText(new String(encrypted));
            statusLabel.setText("Текст зашифрован.");
        } catch (Exception e) {
            showError("Ошибка шифрования", e);
        }
    }

    private void decrypt(javafx.event.ActionEvent event) {
        try {
            String text = inputArea.getText();
            int key = Integer.parseInt(keyField.getText());
            char[] decrypted = CodeCaesars.decryption(text.toCharArray(), key);
            outputArea.setText(new String(decrypted));
            statusLabel.setText("Текст дешифрован.");
        } catch (Exception e) {
            showError("Ошибка дешифрования", e);
        }
    }

    private void loadFile(javafx.event.ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Текстовые файлы", "*.txt"));
        File file = chooser.showOpenDialog(null);
        if (file != null) {
            try {
                String content = Files.readString(Paths.get(file.getAbsolutePath()));
                inputArea.setText(content);
                statusLabel.setText("Файл загружен: " + file.getName());
            } catch (Exception e) {
                showError("Не удалось прочитать файл", e);
            }
        }
    }

    private void saveFile(javafx.event.ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Текстовые файлы", "*.txt"));
        File file = chooser.showSaveDialog(null);
        if (file != null) {
            try {
                Files.writeString(Paths.get(file.getAbsolutePath()), outputArea.getText());
                statusLabel.setText("Файл сохранён: " + file.getName());
            } catch (Exception e) {
                showError("Не удалось сохранить файл", e);
            }
        }
    }

    private void bruteForce(javafx.event.ActionEvent event) {
        try {
            String text = inputArea.getText();
            if (text.isEmpty()) {
                statusLabel.setText("Введите текст для взлома");
                return;
            }

            // Временно сохраняем в файл для BruteForce
            Files.writeString(Paths.get("temp_encrypted.txt"), text);
            String result = BruteForce.decryptByBruteForce();

            outputArea.setText(Files.readString(Paths.get("DecryptedBF.txt")));
            statusLabel.setText(result);
        } catch (Exception e) {
            showError("Ошибка взлома", e);
        }
    }

    private void showError(String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(message);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    static void main(String[] args) {
        launch(args);
    }
}

