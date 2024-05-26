package com.example.gymmanagementsystem;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class FXMLDocumentController implements Initializable {

    @FXML
    private AnchorPane main_form;

    @FXML
    private AnchorPane sub_form;

    @FXML
    private Button sub_signupBtn;

    @FXML
    private Button sub_loginBtn;

    @FXML
    private Label edit_label;

    @FXML
    private AnchorPane signup_form;

    @FXML
    private TextField su_email;

    @FXML
    private TextField su_username;

    @FXML
    private PasswordField su_password;

    @FXML
    private Button su_signupBtn;

    @FXML
    private AnchorPane login_form;

    @FXML
    private TextField si_username;

    @FXML
    private PasswordField si_password;

    @FXML
    private Button si_loginBtn;

    @FXML
    private Button close;

    @FXML
    private FontAwesomeIcon close_icon;


 // Поля для работы с базой данных
private Connection connect; // Подключение к базе данных
private PreparedStatement prepare; // Подготовленный SQL запрос
private ResultSet result; // Результат выполнения запроса

// Метод для выполнения входа в систему
public void login() {
    // SQL запрос для проверки учетных данных
    String sql = "SELECT * FROM admin WHERE username = ? and password = ?";

    connect = database.connectDb(); // Подключение к базе данных

    try {
        prepare = connect.prepareStatement(sql); // Подготовка SQL запроса
        prepare.setString(1, si_username.getText()); // Установка имени пользователя
        prepare.setString(2, si_password.getText()); // Установка пароля
        result = prepare.executeQuery(); // Выполнение запроса

        Alert alert;

        // Проверка, заполнены ли поля
        if (si_username.getText().isEmpty() || si_password.getText().isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Пожалуйста, заполните все пустые поля");
            alert.showAndWait();
        } else {
            // Проверка правильности учетных данных
            if (result.next()) {
                data.username = si_username.getText(); // Сохранение имени пользователя

                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Успешно!");
                alert.showAndWait();

                si_loginBtn.getScene().getWindow().hide(); // Скрытие текущего окна

                // Загрузка и отображение новой сцены (dashboard)
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("dashboard.fxml")));
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.initStyle(StageStyle.TRANSPARENT); // Установка прозрачного стиля
                stage.setScene(scene);
                stage.show();
            } else {
                // Уведомление об ошибке, если учетные данные неверны
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Неверное имя пользователя/пароль");
                alert.showAndWait();
            }
        }
    } catch (Exception e) {
        e.printStackTrace(); // Обработка исключений
    }
}

// Метод для регистрации нового пользователя
public void signup() {
    // SQL запрос для добавления нового пользователя
    String sql = "INSERT INTO admin (email, username, password) VALUES(?,?,?)";

    connect = database.connectDb(); // Подключение к базе данных

    try {
        Alert alert;

        // Проверка, заполнены ли поля
        if (su_email.getText().isEmpty() || su_username.getText().isEmpty() || su_password.getText().isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Пожалуйста, заполните все пустые поля");
            alert.showAndWait();
        } else {
            // Проверка длины пароля
            if (su_password.getText().length() < 5) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Пароль должен содержать не менее 6 символов!");
                alert.showAndWait();
            } else {
                // Выполнение SQL запроса для регистрации
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, su_email.getText());
                prepare.setString(2, su_username.getText());
                prepare.setString(3, su_password.getText());

                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Успешно!");
                alert.showAndWait();

                prepare.executeUpdate(); // Выполнение запроса на добавление пользователя

                su_email.setText(""); // Очистка полей
                su_username.setText("");
                su_password.setText("");
            }
        }
    } catch (Exception e) {
        e.printStackTrace(); // Обработка исключений
    }
}

// Метод для анимации перехода к форме регистрации
public void signupSlider() {
    TranslateTransition slider1 = new TranslateTransition(); // Создание объекта для анимации
    slider1.setNode(sub_form); // Установка узла для анимации
    slider1.setToX(300); // Установка конечной позиции по оси X
    slider1.setDuration(Duration.seconds(.5)); // Установка длительности анимации
    slider1.play(); // Запуск анимации

    slider1.setOnFinished((ActionEvent event) -> {
        edit_label.setText("Войти в аккаунт"); // Изменение текста метки

        sub_signupBtn.setVisible(false); // Скрытие кнопки регистрации
        sub_loginBtn.setVisible(true); // Отображение кнопки входа

        close_icon.setFill(Color.valueOf("#fff")); // Изменение цвета иконки закрытия
    });
}

// Метод для анимации перехода к форме входа
public void loginSlider() {
    TranslateTransition slider1 = new TranslateTransition(); // Создание объекта для анимации
    slider1.setNode(sub_form); // Установка узла для анимации
    slider1.setToX(0); // Установка конечной позиции по оси X
    slider1.setDuration(Duration.seconds(.5)); // Установка длительности анимации
    slider1.play(); // Запуск анимации

    slider1.setOnFinished((ActionEvent event) -> {
        edit_label.setText("Создайте аккаунт"); // Изменение текста метки

        sub_signupBtn.setVisible(true); // Отображение кнопки регистрации
        sub_loginBtn.setVisible(false); // Скрытие кнопки входа
        close_icon.setFill(Color.valueOf("#000")); // Изменение цвета иконки закрытия
    });
}

// Метод для закрытия приложения
public void close() {
    javafx.application.Platform.exit(); // Завершение работы приложения
}
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}


