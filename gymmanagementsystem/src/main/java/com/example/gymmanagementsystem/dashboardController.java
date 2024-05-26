package com.example.gymmanagementsystem;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class dashboardController implements Initializable {

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button close;

    @FXML
    private Button minimize;

    @FXML
    private Button dashboard_btn;

    @FXML
    private Button coaches_btn;

    @FXML
    private Button members_btn;

    @FXML
    private Button logout;

    @FXML
    private Button payment_btn;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private Label dashboard_NM;

    @FXML
    private Label dashboard_NC;

    @FXML
    private Label dashboard_TI;

    @FXML
    private AreaChart<?, ?> dashboard_incomeChart;

    @FXML
    private AnchorPane coaches_form;

    @FXML
    private TextField coaches_coachID;

    @FXML
    private TextField coaches_name;

    @FXML
    private TextArea coaches_address;

    @FXML
    private ComboBox<?> coaches_gender;

    @FXML
    private TextField coaches_phoneNum;

    @FXML
    private Button coaches_addBtn;

    @FXML
    private Button coaches_updateBtn;

    @FXML
    private Button coaches_resetBtn;

    @FXML
    private Button coaches_deleteBtn;

    @FXML
    private ComboBox<?> coaches_status;

    @FXML
    private TableView<coachData> coaches_tableView;

    @FXML
    private TableColumn<coachData, String> coaches_col_coachID;

    @FXML
    private TableColumn<coachData, String> coaches_col_name;

    @FXML
    private TableColumn<coachData, String> coaches_col_address;

    @FXML
    private TableColumn<coachData, String> coaches_col_gender;

    @FXML
    private TableColumn<coachData, String> coaches_col_phoneNum;

    @FXML
    private TableColumn<coachData, String> coaches_col_status;

    @FXML
    private AnchorPane members_form;

    @FXML
    private TextField members_customerId;

    @FXML
    private TextField members_name;

    @FXML
    private TextArea members_caddress;

    @FXML
    private TextField members_phoneNum;

    @FXML
    private ComboBox<?> members_gender;

    @FXML
    private ComboBox<?> members_schedule;

    @FXML
    private DatePicker members_startDate;

    @FXML
    private DatePicker members_endDate;

    @FXML
    private ComboBox<?> members_status;

    @FXML
    private Button members_addBtn;

    @FXML
    private Button members_clearBtn;

    @FXML
    private Button members_updateBtn;

    @FXML
    private Button members_deleteBtn;

    @FXML
    private TableView<memberData> members_tableView;

    @FXML
    private TableColumn<memberData, String> members_col_customerID;

    @FXML
    private TableColumn<memberData, String> members_col_name;

    @FXML
    private TableColumn<memberData, String> members_col_address;

    @FXML
    private TableColumn<memberData, String> members_col_phoneNum;

    @FXML
    private TableColumn<memberData, String> members_col_gender;

    @FXML
    private TableColumn<memberData, String> members_col_schedule;

    @FXML
    private TableColumn<memberData, String> members_col_startDate;

    @FXML
    private TableColumn<memberData, String> members_col_endDate;

    @FXML
    private TableColumn<memberData, String> members_col_status;

    @FXML
    private AnchorPane payment_Form;

    @FXML
    private TableView<memberData> payment_tableView;

    @FXML
    private TableColumn<memberData, String> payment_col_customerID;

    @FXML
    private TableColumn<memberData, String> payment_col_name;

    @FXML
    private TableColumn<memberData, String> payment_col_startDate;

    @FXML
    private TableColumn<memberData, String> payment_col_endDate;

    @FXML
    private TableColumn<memberData, String> payment_col_status;

    @FXML
    private ComboBox<?> payment_customerID;

    @FXML
    private ComboBox<?> payment_name;

    @FXML
    private Label payment_total;

    @FXML
    private TextField payment_amount;

    @FXML
    private Label payment_change;

    @FXML
    private Button payment_payBtn;

    @FXML
    private Label username;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;

    // Метод для отображения сообщения об ошибке при наличии пустых полей ввода
    public void emptyFields() {
    // Создание предупреждающего окна
    Alert alert = new Alert(AlertType.ERROR); 
    // Установка заголовка окна
    alert.setTitle("Error Message");
    // Удаление заголовка сообщения (устанавливается в null)
    alert.setHeaderText(null);
    // Установка текста сообщения
    alert.setContentText("Пожалуйста, заполните все пустые поля");
    // Отображение окна и ожидание, пока пользователь его закроет
    alert.showAndWait(); 
    }
    // Метод для обновления информации на панели управления
    public void dashboardNM() {

    // SQL запрос для подсчета количества членов с оплаченным статусом
    String sql = "SELECT COUNT(id) FROM member WHERE status = 'Оплачено'";

    // Подключение к базе данных
    connect = database.connectDb();

    // Переменная для хранения количества оплаченных членов
    int nm = 0;

    try {
        // Подготовка SQL запроса
        prepare = connect.prepareStatement(sql);
        // Выполнение запроса
        result = prepare.executeQuery();

        // Получение результата запроса
        if (result.next()) {
            nm = result.getInt("COUNT(id)"); // Получение значения количества из результата
        }

        // Обновление текста на панели управления
        dashboard_NM.setText(String.valueOf(nm));

            } catch (Exception e) {
        e.printStackTrace(); // Обработка исключений
        }

    }

   // Метод для обновления информации о тренерах на панели управления
public void dashboardTC() {

    // SQL запрос для подсчета количества активных тренеров
    String sql = "SELECT COUNT(id) FROM coach WHERE status = 'Активный'";

    // Подключение к базе данных
    connect = database.connectDb();

    // Переменная для хранения количества активных тренеров
    int tc = 0;

    try {
        // Подготовка SQL запроса
        prepare = connect.prepareStatement(sql);
        // Выполнение запроса
        result = prepare.executeQuery();

        // Получение результата запроса
        if (result.next()) {
            tc = result.getInt("COUNT(id)"); // Получение значения количества из результата
        }
        
        // Обновление текста на панели управления
        dashboard_NC.setText(String.valueOf(tc));

    } catch (Exception e) {
        e.printStackTrace(); // Обработка исключений
    }

}
    // Метод для обновления информации о доходах на панели управления
public void dashboardTI() {

    // SQL запрос для суммирования всех оплат членов клуба со статусом 'Оплачено'
    String sql = "SELECT SUM(price) FROM member WHERE status = 'Оплачено'";

    // Подключение к базе данных
    connect = database.connectDb();

    // Переменная для хранения общей суммы доходов
    double ti = 0;

    try {
        // Подготовка SQL запроса
        prepare = connect.prepareStatement(sql);
        // Выполнение запроса
        result = prepare.executeQuery();

        // Получение результата запроса
        if (result.next()) {
            ti = result.getDouble("SUM(price)"); // Получение суммы из результата
        }

        // Обновление текста на панели управления с отображением суммы в рублях
        dashboard_TI.setText("₽" + String.valueOf(ti));

    } catch (Exception e) {
        e.printStackTrace(); // Обработка исключений
    }

}

   // Метод для обновления данных на графике доходов на панели управления
public void dashboardChart() {

    // Очистка текущих данных графика
    dashboard_incomeChart.getData().clear();

    // SQL запрос для получения сумм оплат по датам с группировкой и сортировкой
    String sql = "SELECT startDate, SUM(price) FROM member WHERE status = 'Оплачено' GROUP BY startDate ORDER BY TIMESTAMP(startDate) ASC LIMIT 10";

    // Подключение к базе данных
    connect = database.connectDb();

    // Создание серии данных для графика
    XYChart.Series chart = new XYChart.Series();

    try {
        // Подготовка SQL запроса
        prepare = connect.prepareStatement(sql);
        // Выполнение запроса
        result = prepare.executeQuery();

        // Обработка результатов запроса
        while (result.next()) {
            // Добавление данных в серию графика
            chart.getData().add(new XYChart.Data(result.getString(1), result.getDouble(2)));
        }

        // Добавление серии данных на график
        dashboard_incomeChart.getData().add(chart);

    } catch (Exception e) {
        e.printStackTrace(); // Обработка исключений
    }

}

    // Метод для добавления тренера в базу данных
public void coachesAddBtn() {

    // SQL запрос для вставки нового тренера
    String sql = "INSERT INTO coach (coachId, name, address, gender, phoneNum, status) "
               + "VALUES(?,?,?,?,?,?)";

    // Подключение к базе данных
    connect = database.connectDb();

    try {

        Alert alert;

        // Проверка, что все поля заполнены
        if (coaches_coachID.getText().isEmpty() || coaches_name.getText().isEmpty()
                || coaches_address.getText().isEmpty()
                || coaches_gender.getSelectionModel().getSelectedItem() == null
                || coaches_phoneNum.getText().isEmpty()
                || coaches_status.getSelectionModel().getSelectedItem() == null) {
            emptyFields(); // Вызов метода для отображения сообщения об ошибке
        } else {
            // SQL запрос для проверки существования тренера с таким же ID
            String checkData = "SELECT coachId FROM coach WHERE coachId = '"
                             + coaches_coachID.getText() + "'";

            // Выполнение запроса
            statement = connect.createStatement();
            result = statement.executeQuery(checkData);

            // Проверка результата запроса
            if (result.next()) {
                // Отображение сообщения об ошибке, если тренер с таким ID уже существует
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Тренер ID: " + coaches_coachID.getText() + " уже существует!");
                alert.showAndWait();
            } else {
                // Подготовка SQL запроса для вставки данных тренера
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, coaches_coachID.getText());
                prepare.setString(2, coaches_name.getText());
                prepare.setString(3, coaches_address.getText());
                prepare.setString(4, (String) coaches_gender.getSelectionModel().getSelectedItem());
                prepare.setString(5, coaches_phoneNum.getText());
                prepare.setString(6, (String) coaches_status.getSelectionModel().getSelectedItem());

                // Отображение сообщения об успешном добавлении тренера
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Успешно!");
                alert.showAndWait();

                // Выполнение запроса на обновление базы данных
                prepare.executeUpdate();

                // Обновление данных тренеров на интерфейсе
                coachesShowData();

                // Очистка полей ввода
                coachesClearBtn();
            }
        }

    } catch (Exception e) {
        e.printStackTrace(); // Обработка исключений
    }

}

    // Метод для обновления информации о тренере в базе данных
public void coachesUpdateBtn() {

    // SQL запрос для обновления данных тренера
    String sql = "UPDATE coach SET name = '"
               + coaches_name.getText() + "', address = '"
               + coaches_address.getText() + "', gender = '"
               + coaches_gender.getSelectionModel().getSelectedItem() + "', phoneNum = '"
               + coaches_phoneNum.getText() + "', status = '"
               + coaches_status.getSelectionModel().getSelectedItem() + "' WHERE coachId = '"
               + coaches_coachID.getText() + "'";

    // Подключение к базе данных
    connect = database.connectDb();

    try {
        Alert alert;
        // Проверка, что все поля заполнены
        if (coaches_coachID.getText().isEmpty() || coaches_name.getText().isEmpty()
                || coaches_address.getText().isEmpty()
                || coaches_gender.getSelectionModel().getSelectedItem() == null
                || coaches_phoneNum.getText().isEmpty()
                || coaches_status.getSelectionModel().getSelectedItem() == null) {
            emptyFields(); // Вызов метода для отображения сообщения об ошибке
        } else {
            // Отображение диалогового окна подтверждения изменения данных тренера
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Вы точно хотите изменить ID: " + coaches_coachID.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();

            // Обработка выбора пользователя в диалоговом окне
            if (option.get().equals(ButtonType.OK)) {
                // Подготовка и выполнение SQL запроса на обновление данных тренера
                prepare = connect.prepareStatement(sql);
                prepare.executeUpdate();
                // Отображение сообщения об успешном обновлении данных
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Успешно!");
                alert.showAndWait();
                // Обновление данных тренеров на интерфейсе
                coachesShowData();
                // Очистка полей ввода
                coachesClearBtn();
            } else {
                // Отображение сообщения об отмене операции
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Отмена!");
                alert.showAndWait();
            }
        }

    } catch (Exception e) {
        e.printStackTrace(); // Обработка исключений
    }

}

    // Метод для удаления тренера из базы данных
public void coachesDeleteBtn() {
    // SQL запрос для удаления тренера
    String sql = "DELETE FROM coach WHERE coachId = '"
                + coaches_coachID.getText() + "'";

    // Подключение к базе данных
    connect = database.connectDb();

    try {
        Alert alert;
        // Проверка, что все поля заполнены
        if (coaches_coachID.getText().isEmpty() || coaches_name.getText().isEmpty()
                || coaches_address.getText().isEmpty()
                || coaches_gender.getSelectionModel().getSelectedItem() == null
                || coaches_phoneNum.getText().isEmpty()
                || coaches_status.getSelectionModel().getSelectedItem() == null) {
            emptyFields(); // Вызов метода для отображения сообщения об ошибке
        } else {
            // Отображение диалогового окна подтверждения удаления тренера
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("вы точно хотите удалить ID: " + coaches_coachID.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();

            // Обработка выбора пользователя в диалоговом окне
            if (option.get().equals(ButtonType.OK)) {
                // Подготовка и выполнение SQL запроса на удаление тренера
                prepare = connect.prepareStatement(sql);
                prepare.executeUpdate();
                // Отображение сообщения об успешном удалении тренера
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Успешно!");
                alert.showAndWait();
                // Обновление данных тренеров на интерфейсе
                coachesShowData();
                // Очистка полей ввода
                coachesClearBtn();
            } else {
                // Отображение сообщения об отмене операции
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Отмена!");
                alert.showAndWait();
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

}

// Метод для очистки полей ввода на форме добавления/редактирования тренера
public void coachesClearBtn() {
    // Очистка текстовых полей и сброс выбранных значений в списках
    coaches_coachID.setText("");
    coaches_name.setText("");
    coaches_address.setText("");
    coaches_gender.getSelectionModel().clearSelection();
    coaches_phoneNum.setText("");
    coaches_status.getSelectionModel().clearSelection();
}
// Метод для получения данных о тренерах из базы данных
public ObservableList<coachData> coachesDataList() {
    ObservableList<coachData> listData = FXCollections.observableArrayList();
    // SQL запрос для выборки данных о тренерах
    String sql = "SELECT * FROM coach";
    // Подключение к базе данных
    connect = database.connectDb();

    try {
        // Подготовка и выполнение SQL запроса
        prepare = connect.prepareStatement(sql);
        result = prepare.executeQuery();
        // Обработка результатов запроса
        coachData cd;
        while (result.next()) {
            // Создание объекта coachData для каждой строки результата
            cd = new coachData(result.getInt("id"), result.getString("coachId"),
                    result.getString("name"), result.getString("address"),
                    result.getString("gender"), result.getInt("phoneNum"),
                    result.getString("status"));
            // Добавление объекта в список
            listData.add(cd);
        }
    } catch (Exception e) {
        e.printStackTrace(); // Вывод стека вызовов в случае исключения
    }
    return listData; // Возвращение списка данных
}

private ObservableList<coachData> coachesListData;

// Метод для отображения данных о тренерах на интерфейсе
public void coachesShowData() {
    // Получение данных о тренерах
    coachesListData = coachesDataList();
    // Привязка данных к столбцам таблицы
    coaches_col_coachID.setCellValueFactory(new PropertyValueFactory<>("coachId"));
    coaches_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
    coaches_col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
    coaches_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
    coaches_col_phoneNum.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
    coaches_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
    // Установка данных в таблицу
    coaches_tableView.setItems(coachesListData);
}
// Метод для выбора тренера из таблицы и отображения его данных в соответствующих полях интерфейса
public void coachesSelect() {
    coachData cd = coaches_tableView.getSelectionModel().getSelectedItem();
    int num = coaches_tableView.getSelectionModel().getSelectedIndex();

    // Проверка, выбран ли какой-либо элемент в таблице
    if ((num - 1) < -1) {
        return; // Возвращение, если ничего не выбрано
    }

    // Установка данных выбранного тренера в соответствующие поля интерфейса
    coaches_coachID.setText(cd.getCoachId());
    coaches_name.setText(cd.getName());
    coaches_address.setText(cd.getAddress());
    coaches_phoneNum.setText(String.valueOf(cd.getPhoneNum()));
}

// Список полов тренеров
private String gender[] = {"Мужской", "Женский"};

// Метод для заполнения выпадающего списка полов тренеров
public void coachGenderList() {
    List<String> genderList = new ArrayList<>();

    // Заполнение списка полов из массива
    for (String data : gender) {
        genderList.add(data);
    }

    // Создание ObservableList из списка полов и привязка его к выпадающему списку
    ObservableList listData = FXCollections.observableArrayList(genderList);
    coaches_gender.setItems(listData);
}

// Список статусов тренеров
private String coachStatus[] = {"Активный", "Неактивный"};

// Метод для заполнения выпадающего списка статусов тренеров
public void coachStatusList() {
    List<String> coachList = new ArrayList<>();

    // Заполнение списка статусов из массива
    for (String data : coachStatus) {
        coachList.add(data);
    }

    // Создание ObservableList из списка статусов и привязка его к выпадающему списку
    ObservableList listData = FXCollections.observableArrayList(coachList);
    coaches_status.setItems(listData);
}

    private int totalDay;
    private double price;

  public void membersAddBtn() {
    String sql = "INSERT INTO member (memberId, name, address, phoneNum, gender, schedule, startDate, endDate, price, status) "
            + "VALUES(?,?,?,?,?,?,?,?,?,?)";

    connect = database.connectDb();

    try {
        Alert alert;

        // Проверка наличия пустых полей
        if (members_customerId.getText().isEmpty() || members_name.getText().isEmpty()
                || members_phoneNum.getText().isEmpty() || members_caddress.getText().isEmpty()
                || members_gender.getSelectionModel().getSelectedItem() == null
                || members_schedule.getSelectionModel().getSelectedItem() == null
                || members_startDate.getValue() == null
                || members_endDate.getValue() == null) {
            emptyFields(); // Вывод сообщения об ошибке, если есть пустые поля
        } else {
            // Проверка на уникальность ID
            String checkData = "SELECT memberId FROM member WHERE memberId = '"
                    + members_customerId.getText() + "'";

            statement = connect.createStatement();
            result = statement.executeQuery(checkData);

            if (result.next()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText(" ID: " + members_customerId.getText() + " уже существует!");
                alert.showAndWait();
            } else {
                // Вставка данных в базу данных
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, members_customerId.getText());
                prepare.setString(2, members_name.getText());
                prepare.setString(4, members_phoneNum.getText());
                prepare.setString(3, members_caddress.getText());
                prepare.setString(5, (String) members_gender.getSelectionModel().getSelectedItem());
                prepare.setString(6, (String) members_schedule.getSelectionModel().getSelectedItem());
                prepare.setString(7, String.valueOf(members_startDate.getValue()));
                prepare.setString(8, String.valueOf(members_endDate.getValue()));

                // Вычисление цены в зависимости от длительности
                totalDay = (int) ChronoUnit.DAYS.between(members_startDate.getValue(), members_endDate.getValue());
                price = (totalDay * 50);
                prepare.setString(9, String.valueOf(price));
                prepare.setString(10, (String) members_status.getSelectionModel().getSelectedItem());

                prepare.executeUpdate(); // Выполнение запроса на добавление в базу данных

                // Вывод успешного сообщения
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Успешно!");
                alert.showAndWait();

                membersShowData(); // Обновление отображаемых данных
                membersClear(); // Очистка полей ввода
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

 public void membersUpdate() {
    // Рассчитываем общее количество дней и цену на основе дат начала и окончания
    totalDay = (int) ChronoUnit.DAYS.between(members_startDate.getValue(), members_endDate.getValue());
    price = (totalDay * 50);

    // Формируем SQL запрос для обновления данных
    String sql = "UPDATE member SET name = '"
            + members_name.getText() + "', address = '"
            + members_caddress.getText() + "', phoneNum = '"
            + members_phoneNum.getText() + "', gender = '"
            + members_gender.getSelectionModel().getSelectedItem() + "', schedule = '"
            + members_schedule.getSelectionModel().getSelectedItem() + "', startDate = '"
            + members_startDate.getValue() + "', endDate = '"
            + members_endDate.getValue() + "', price = '"
            + String.valueOf(price) + "', status = '"
            + members_status.getSelectionModel().getSelectedItem() + "' WHERE memberId = '"
            + members_customerId.getText() + "'";

    connect = database.connectDb();

    try {
        Alert alert;

        // Проверка наличия пустых полей
        if (members_customerId.getText().isEmpty() || members_name.getText().isEmpty()
                || members_phoneNum.getText().isEmpty() || members_caddress.getText().isEmpty()
                || members_gender.getSelectionModel().getSelectedItem() == null
                || members_schedule.getSelectionModel().getSelectedItem() == null
                || members_startDate.getValue() == null
                || members_endDate.getValue() == null) {
            emptyFields(); // Вывод сообщения об ошибке, если есть пустые поля
        } else {
            // Подтверждение операции обновления данных
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Вы точно хотите изменить ID: " + members_customerId.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();

            // Если пользователь нажал OK, выполняем запрос на обновление данных
            if (option.get().equals(ButtonType.OK)) {
                prepare = connect.prepareStatement(sql);
                prepare.executeUpdate(); // Выполнение запроса на обновление данных

                // Вывод успешного сообщения
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Успешно!");
                alert.showAndWait();

                membersShowData(); // Обновление отображаемых данных
                membersClear(); // Очистка полей ввода

            } else {
                // Вывод сообщения об отмене операции
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Отмена!");
                alert.showAndWait();
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

  public void membersDelete() {
    // Формируем SQL запрос для удаления данных о члене клуба
    String sql = "DELETE FROM member WHERE memberId = '"
            + members_customerId.getText() + "'";

    connect = database.connectDb();

    try {
        Alert alert;

        // Проверка наличия пустых полей
        if (members_customerId.getText().isEmpty() || members_name.getText().isEmpty()
                || members_phoneNum.getText().isEmpty() || members_caddress.getText().isEmpty()
                || members_gender.getSelectionModel().getSelectedItem() == null
                || members_schedule.getSelectionModel().getSelectedItem() == null
                || members_startDate.getValue() == null
                || members_endDate.getValue() == null) {
            emptyFields(); // Вывод сообщения об ошибке, если есть пустые поля
        } else {
            // Подтверждение операции удаления
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Вы точно хотите удалить ID: " + members_customerId.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();

            // Если пользователь нажал OK, выполняем запрос на удаление данных
            if (option.get().equals(ButtonType.OK)) {
                prepare = connect.prepareStatement(sql);
                prepare.executeUpdate(); // Выполнение запроса на удаление данных

                // Вывод успешного сообщения
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Успешно!");
                alert.showAndWait();

                membersShowData(); // Обновление отображаемых данных
                membersClear(); // Очистка полей ввода

            } else {
                // Вывод сообщения об отмене операции
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Отмена!");
                alert.showAndWait();
            }

        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void membersClear() {
    // Очищаем поля ввода данных о члене клуба
    members_customerId.setText("");
    members_name.setText("");
    members_caddress.setText("");
    members_phoneNum.setText("");
    members_gender.getSelectionModel().clearSelection();
    members_schedule.getSelectionModel().clearSelection();
    members_startDate.setValue(null);
    members_endDate.setValue(null);
    members_status.getSelectionModel().clearSelection();
}
 public void membersGender() {
    // Создание списка для выбора пола члена клуба
    List<String> genderList = new ArrayList<>();

    // Заполнение списка значениями из массива gender
    for (String data : gender) {
        genderList.add(data);
    }

    // Создание ObservableList и установка его в элементы выпадающего списка для выбора пола
    ObservableList<String> listData = FXCollections.observableArrayList(genderList);
    members_gender.setItems(listData);
}

public void membersSchedule() {
    // Создание списка для выбора графика члена клуба
    List<String> sl = new ArrayList<>();

    // Заполнение списка значениями из массива scheduleList
    for (String data : scheduleList) {
        sl.add(data);
    }

    // Создание ObservableList и установка его в элементы выпадающего списка для выбора графика
    ObservableList<String> listData = FXCollections.observableArrayList(sl);
    members_schedule.setItems(listData);
}

public void membersStatus() {
    // Создание списка для выбора статуса члена клуба
    List<String> ms = new ArrayList<>();

    // Заполнение списка значениями из массива memberStatus
    for (String data : memberStatus) {
        ms.add(data);
    }

    // Создание ObservableList и установка его в элементы выпадающего списка для выбора статуса
    ObservableList<String> listData = FXCollections.observableArrayList(memberStatus);
    members_status.setItems(listData);
}

public ObservableList<memberData> membersDataList() {
    // Создание ObservableList для данных членов клуба
    ObservableList<memberData> listData = FXCollections.observableArrayList();

    // SQL-запрос для выборки данных из таблицы member
    String sql = "SELECT * FROM member";

    connect = database.connectDb();

    try {
        // Подготовка запроса
        prepare = connect.prepareStatement(sql);
        // Выполнение запроса и получение результата
        result = prepare.executeQuery();

        memberData md;

        // Обход результатов запроса и создание объектов memberData для каждой записи
        while (result.next()) {
            md = new memberData(result.getInt("id"),
                    result.getString("memberId"),
                    result.getString("name"),
                    result.getString("address"),
                    result.getInt("phoneNum"),
                    result.getString("gender"),
                    result.getString("schedule"),
                    result.getDate("startDate"),
                    result.getDate("endDate"),
                    result.getDouble("price"),
                    result.getString("status"));

            // Добавление объекта memberData в список
            listData.add(md);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return listData;
}

private ObservableList<memberData> membersListData;

public void membersShowData() {
    // Получение данных членов клуба
    membersListData = membersDataList();

    // Установка значений свойств объектов memberData в соответствующие столбцы таблицы
    members_col_customerID.setCellValueFactory(new PropertyValueFactory<>("memberId"));
    members_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
    members_col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
    members_col_phoneNum.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
    members_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
    members_col_schedule.setCellValueFactory(new PropertyValueFactory<>("schedule"));
    members_col_startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
    members_col_endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
    members_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

    // Установка данных в таблицу
    members_tableView.setItems(membersListData);
}

 public void membersSelect() {
    // Получение выбранного объекта memberData из таблицы
    memberData md = members_tableView.getSelectionModel().getSelectedItem();
    // Получение индекса выбранной строки
    int num = members_tableView.getSelectionModel().getSelectedIndex();

    // Проверка на некорректный индекс
    if ((num - 1) < -1) {
        return;
    }

    // Установка значений полей интерфейса на основе выбранного объекта memberData
    members_customerId.setText(md.getMemberId());
    members_name.setText(md.getName());
    members_caddress.setText(md.getAddress());
    members_phoneNum.setText(String.valueOf(md.getPhoneNum()));
    members_startDate.setValue(LocalDate.parse(String.valueOf(md.getStartDate())));
    members_endDate.setValue(LocalDate.parse(String.valueOf(md.getEndDate())));
}

public void paymentMemberId() {
    // Получение списка memberId членов клуба, которые имеют статус "Неоплачено"
    String sql = "SELECT memberId FROM member WHERE status = 'Неоплачено'";

    connect = database.connectDb();

    try {
        ObservableList listData = FXCollections.observableArrayList();

        prepare = connect.prepareStatement(sql);
        result = prepare.executeQuery();

        // Добавление memberId в список
        while (result.next()) {
            listData.add(result.getString("memberId"));
        }
        // Установка списка в элемент управления ComboBox
        payment_customerID.setItems(listData);

        // Вызов метода для установки имени члена клуба на основе выбранного memberId
        paymentName();

    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void paymentName() {
    // Получение имени члена клуба на основе выбранного memberId
    String sql = "SELECT name FROM member WHERE memberId = '"
            + payment_customerID.getSelectionModel().getSelectedItem() + "'";

    connect = database.connectDb();

    try {
        ObservableList listData = FXCollections.observableArrayList();
        prepare = connect.prepareStatement(sql);
        result = prepare.executeQuery();

        if (result.next()) {
            listData.add(result.getString("name"));
        }
        payment_name.setItems(listData);

        // Вызов метода для отображения общей суммы к оплате
        paymentDisplayTotal();

    } catch (Exception e) {
        e.printStackTrace();
    }
}

private double totalP;
  public void displayTotal() {
    // Получение общей суммы к оплате на основе выбранного имени члена клуба
    String sql = "SELECT price FROM member WHERE name = '"
            + payment_name.getSelectionModel().getSelectedItem() + "' and memberId = '"
            + payment_customerID.getSelectionModel().getSelectedItem() + "'";

    connect = database.connectDb();

    try {
        prepare = connect.prepareStatement(sql);
        result = prepare.executeQuery();

        if (result.next()) {
            totalP = result.getDouble("price");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}


public void paymentDisplayTotal() {
    // Вызов метода для отображения общей суммы к оплате
    displayTotal();
    payment_total.setText("₽" + String.valueOf(totalP));
}

private double amount;
private double change;

public void paymentAmount() {
    // Получение общей суммы к оплате
    displayTotal();

    Alert alert;

    // Проверка наличия введенной суммы и ненулевой общей суммы к оплате
    if (payment_amount.getText().isEmpty() || totalP == 0) {
        alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText("Недопустимо");
        alert.showAndWait();

        payment_amount.setText("");
    } else {
        // Парсинг введенной суммы
        amount = Double.parseDouble(payment_amount.getText());

        // Проверка наличия достаточной суммы к оплате
        if (amount >= totalP) {
            change = (amount - totalP);
            payment_change.setText("₽" + String.valueOf(change));
        } else {
            // Сброс введенной суммы и сдачи
            payment_amount.setText("");
            change = 0;
            amount = 0;
        }
    }
}


public void paymentPayBtn() {
    // SQL-запрос для обновления статуса оплаты у выбранного члена клуба
    String sql = "UPDATE member SET status = 'Оплачено' WHERE memberId = '"
            + payment_customerID.getSelectionModel().getSelectedItem() + "'";

    connect = database.connectDb();

    try {
        Alert alert;
        // Проверка на наличие общей суммы к оплате и введенной суммы
        if (totalP == 0 || change == 0 || payment_amount.getText().isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Недопустимо");
            alert.showAndWait();
        } else {
            // Отображение подтверждающего сообщения
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Вы уверены?");
            Optional<ButtonType> option = alert.showAndWait();

            // Обработка подтверждения
            if (option.get().equals(ButtonType.OK)) {
                prepare = connect.prepareStatement(sql);
                prepare.executeUpdate();

                // Отображение сообщения об успешной операции
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Успешно!");
                alert.showAndWait();

                // Обновление данных и очистка полей
                paymentShowData();
                paymentClear();
            } else {
                // Отмена операции
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Отмена!");
                alert.showAndWait();
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void paymentClear() {
    // Очистка полей ввода и сброс переменных
    payment_customerID.getSelectionModel().clearSelection();
    payment_name.getSelectionModel().clearSelection();
    payment_total.setText("₽0.0");
    payment_amount.setText("");
    payment_change.setText("₽0.0");

    amount = 0;
    change = 0;
    totalP = 0;
}

public ObservableList<memberData> paymentDataList() {
    // Получение списка данных о членах клуба
    ObservableList<memberData> listData = FXCollections.observableArrayList();

    String sql = "SELECT * FROM member";

    connect = database.connectDb();

    try {
        memberData md;
        prepare = connect.prepareStatement(sql);
        result = prepare.executeQuery();

        // Заполнение списка данными из базы данных
        while (result.next()) {
            md = new memberData(result.getInt("id"),
                    result.getString("memberId"),
                    result.getString("name"),
                    result.getString("address"),
                    result.getInt("phoneNum"),
                    result.getString("gender"),
                    result.getString("schedule"),
                    result.getDate("startDate"),
                    result.getDate("endDate"),
                    result.getDouble("price"),
                    result.getString("status"));

            listData.add(md);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return listData;
}public void paymentPayBtn() {
    // SQL-запрос для обновления статуса оплаты у выбранного члена клуба
    String sql = "UPDATE member SET status = 'Оплачено' WHERE memberId = '"
            + payment_customerID.getSelectionModel().getSelectedItem() + "'";

    connect = database.connectDb();

    try {
        Alert alert;
        // Проверка на наличие общей суммы к оплате и введенной суммы
        if (totalP == 0 || change == 0 || payment_amount.getText().isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Недопустимо");
            alert.showAndWait();
        } else {
            // Отображение подтверждающего сообщения
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Вы уверены?");
            Optional<ButtonType> option = alert.showAndWait();

            // Обработка подтверждения
            if (option.get().equals(ButtonType.OK)) {
                prepare = connect.prepareStatement(sql);
                prepare.executeUpdate();

                // Отображение сообщения об успешной операции
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Успешно!");
                alert.showAndWait();

                // Обновление данных и очистка полей
                paymentShowData();
                paymentClear();
            } else {
                // Отмена операции
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Отмена!");
                alert.showAndWait();
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void paymentClear() {
    // Очистка полей ввода и сброс переменных
    payment_customerID.getSelectionModel().clearSelection();
    payment_name.getSelectionModel().clearSelection();
    payment_total.setText("₽0.0");
    payment_amount.setText("");
    payment_change.setText("₽0.0");

    amount = 0;
    change = 0;
    totalP = 0;
}

public ObservableList<memberData> paymentDataList() {
    // Получение списка данных о членах клуба
    ObservableList<memberData> listData = FXCollections.observableArrayList();

    String sql = "SELECT * FROM member";

    connect = database.connectDb();

    try {
        memberData md;
        prepare = connect.prepareStatement(sql);
        result = prepare.executeQuery();

        // Заполнение списка данными из базы данных
        while (result.next()) {
            md = new memberData(result.getInt("id"),
                    result.getString("memberId"),
                    result.getString("name"),
                    result.getString("address"),
                    result.getInt("phoneNum"),
                    result.getString("gender"),
                    result.getString("schedule"),
                    result.getDate("startDate"),
                    result.getDate("endDate"),
                    result.getDouble("price"),
                    result.getString("status"));

            listData.add(md);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return listData;
}

private ObservableList<memberData> paymentListData;

public void paymentShowData() {
    // Отображение данных об оплате членов клуба
    paymentListData = paymentDataList();

    payment_col_customerID.setCellValueFactory(new PropertyValueFactory<>("memberId"));
    payment_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
    payment_col_startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
    payment_col_endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
    payment_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

    payment_tableView.setItems(paymentListData);
}

public void displayUsername() {
    // Отображение имени пользователя
    String user = data.username;
    user = user.substring(0, 1).toUpperCase() + user.substring(1);
    username.setText(user);
}

public void switchForm(ActionEvent event) {
    // Переключение между различными формами приложения
    if (event.getSource() == dashboard_btn) {
        // Переключение на форму "Панель инструментов"
        dashboard_form.setVisible(true);
        coaches_form.setVisible(false);
        members_form.setVisible(false);
        payment_Form.setVisible(false);
        dashboardNM();
        dashboardTC();
        dashboardTI();
        dashboardChart();
    } else if (event.getSource() == coaches_btn) {
        // Переключение на форму "Тренеры"
        dashboard_form.setVisible(false);
        coaches_form.setVisible(true);
        members_form.setVisible(false);
        payment_Form.setVisible(false);
        coachGenderList();
        coachStatusList();
        coachesShowData();
    } else if (event.getSource() == members_btn) {
        // Переключение на форму "Члены клуба"
        dashboard_form.setVisible(false);
        coaches_form.setVisible(false);
        members_form.setVisible(true);
        payment_Form.setVisible(false);
        membersShowData();
        membersGender();
        membersSchedule();
        membersStatus();
    } else if (event.getSource() == payment_btn) {
        // Переключение на форму "Оплата"
        dashboard_form.setVisible(false);
        coaches_form.setVisible(false);
        members_form.setVisible(false);
        payment_Form.setVisible(true);
        paymentShowData();
        paymentMemberId();
        paymentName();
    }
}

private double x = 0;
private double y = 0;

public void logout() {
    // Метод для выхода из системы
    try {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Вы уверены, что хотите выйти из системы?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get().equals(ButtonType.OK)) {
            // Закрытие текущего окна и открытие формы входа
            logout.getScene().getWindow().hide();
            Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            root.setOnMousePressed((MouseEvent event) -> {
                x = event.getSceneX();
                y = event.getSceneY();
            });
            root.setOnMouseDragged((MouseEvent event) -> {
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
                stage.setOpacity(.8);
            });
            root.setOnMouseReleased((MouseEvent event) -> {
                stage.setOpacity(1);
            });
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public void minimize() {

        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);

    }

    public void close() {

        javafx.application.Platform.exit();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        displayUsername();

        dashboardNM();
        dashboardTC();
        dashboardTI();
        dashboardChart();

        coachGenderList();
        coachStatusList();
        coachesShowData();

        membersShowData();
        membersGender();
        membersSchedule();
        membersStatus();

        paymentMemberId();
        paymentName();
        paymentShowData();

    }

}
