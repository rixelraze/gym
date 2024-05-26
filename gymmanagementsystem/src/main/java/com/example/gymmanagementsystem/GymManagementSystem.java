package com.example.gymmanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class GymManagementSystem extends Application {

    // Поля для хранения начальных координат мыши
    private double x = 0;
    private double y = 0;

    @Override
    public void start(Stage stage) throws Exception {
        // Загрузка FXML-документа и создание корневого узла сцены
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FXMLDocument.fxml")));

        // Создание новой сцены с корневым узлом
        Scene scene = new Scene(root);

        // Обработчик события нажатия мыши
        root.setOnMousePressed((MouseEvent event) -> {
            x = event.getSceneX(); // Сохранение координаты X при нажатии
            y = event.getSceneY(); // Сохранение координаты Y при нажатии
        });

        // Обработчик события перемещения мыши
        root.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - x); // Установка новой позиции окна по X
            stage.setY(event.getScreenY() - y); // Установка новой позиции окна по Y

            stage.setOpacity(.8); // Изменение прозрачности окна при перемещении
        });

        // Обработчик события отпускания мыши
        root.setOnMouseReleased((MouseEvent event) -> {
            stage.setOpacity(1); // Восстановление прозрачности окна
        });

        // Установка прозрачного стиля окна
        stage.initStyle(StageStyle.TRANSPARENT);

        // Установка сцены для окна
        stage.setScene(scene);
        // Отображение окна
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

}

