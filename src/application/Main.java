package application;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;;

public class Main extends Application {
	Stage stage;
	TabPane tabPane;
	Tab tabNewContact;
	Tab tabShowContatcts;
	TextField textFieldFirstname;
	TextField textFieldLastname;
	TextField textFieldEmail;
	DatePicker datePickerBirthdate;
	ChoiceBox<String> categorySelector;
	TableView<Person> tableView;

	Button cancelBtn;
	Button submitBtn;
	Button deleteBtn;

	private static final ResourceBundle TEXTS = ResourceBundle.getBundle("locales/texts", Locale.getDefault());

	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage
					.setTitle(MessageFormat.format(TEXTS.getString("welcomeTitle"), System.getProperty("user.name")));

			primaryStage.setResizable(true);

			BorderPane root = new BorderPane();
			Scene scene = new Scene(root, 800, 450);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			// Initialisierung Menü
			initMenu(root);
			// Initialisierung Tabs
			initTabsRow(root);
			// Initialisierung Tab1 - Kontakte Formular
			initFormTab();
			// Inititalisierung Tab2 - Kontakte Tabelle anzeigen
			initTableTab();

			cancelBtnEventHandler();

			submitBtnEventHandler();

			List<Person> contacts = Person.fetchContacts();
			tableView.getItems().addAll(contacts);
			stage = primaryStage;
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void initMenu(BorderPane root) {
		MenuBar menuBar = new MenuBar();
		Menu menu = new Menu(TEXTS.getString("fileMenu"));
		MenuItem menuItem = new MenuItem(TEXTS.getString("close"));

		menuItem.setOnAction(e -> {
			stage.close();
		});
		;

		menu.getItems().add(menuItem);
		menuBar.getMenus().add(menu);
		root.setTop(menuBar);
	}

	public void initTabsRow(BorderPane root) {
		tabPane = new TabPane();
		tabNewContact = new Tab(TEXTS.getString("firstTab"));
		tabNewContact.setClosable(false);
		tabShowContatcts = new Tab(TEXTS.getString("secondTab"));
		tabShowContatcts.setClosable(false);
		tabPane.getTabs().addAll(tabNewContact, tabShowContatcts);
		root.setCenter(tabPane);
	}

	public void initFormTab() {
		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(15, 15, 15, 15));
		gridPane.setHgap(15);
		gridPane.setVgap(15);

		Label labelFirstname = new Label(TEXTS.getString("firstname") + ": ");
		textFieldFirstname = new TextField();
		Label labelLastname = new Label(TEXTS.getString("lastname") + ": ");
		textFieldLastname = new TextField();
		Label labelEmail = new Label(TEXTS.getString("email") + ": ");
		textFieldEmail = new TextField();
		Label labelBirthdate = new Label(TEXTS.getString("bday") + ": ");
		datePickerBirthdate = new DatePicker();
		Label labelCS = new Label(TEXTS.getString("category"));
		categorySelector = new ChoiceBox<>(
				FXCollections.observableArrayList(TEXTS.getString("private"), TEXTS.getString("business")));
		categorySelector.setValue(TEXTS.getString("private"));

		cancelBtn = new Button(TEXTS.getString("cancelBtn"));
		submitBtn = new Button(TEXTS.getString("submitBtn"));

		gridPane.add(labelFirstname, 0, 0);
		gridPane.add(textFieldFirstname, 1, 0);
		gridPane.add(labelLastname, 0, 1);
		gridPane.add(textFieldLastname, 1, 1);
		gridPane.add(labelEmail, 0, 2);
		gridPane.add(textFieldEmail, 1, 2);
		gridPane.add(labelBirthdate, 0, 3);
		gridPane.add(datePickerBirthdate, 1, 3);
		gridPane.add(labelCS, 0, 4);
		gridPane.add(categorySelector, 1, 4);
		gridPane.add(cancelBtn, 0, 5);
		gridPane.add(submitBtn, 1, 5);

		ColumnConstraints columnConstraints = new ColumnConstraints();
		columnConstraints.setHgrow(Priority.NEVER);
		ColumnConstraints columnConstraints2 = new ColumnConstraints();
		columnConstraints2.setHgrow(Priority.ALWAYS);
		gridPane.getColumnConstraints().addAll(columnConstraints, columnConstraints2);
		AnchorPane anchorPane = new AnchorPane();
		AnchorPane.setTopAnchor(gridPane, 10.0);
		AnchorPane.setLeftAnchor(gridPane, 10.0);
		AnchorPane.setRightAnchor(gridPane, 10.0);
		anchorPane.getChildren().add(gridPane);
		tabNewContact.setContent(anchorPane);
	}

	public void initTableTab() {
		tableView = new TableView<>();

		TableColumn<Person, Integer> columnId = new TableColumn<>(TEXTS.getString("id"));
		TableColumn<Person, String> columnFirstname = new TableColumn<>(TEXTS.getString("firstname"));
		TableColumn<Person, String> columnLastname = new TableColumn<>(TEXTS.getString("lastname"));
		TableColumn<Person, String> columnEmail = new TableColumn<>(TEXTS.getString("email"));
		TableColumn<Person, LocalDate> columnBirthdate = new TableColumn<>(TEXTS.getString("bday"));
		TableColumn<Person, Void> columnDelete = new TableColumn<>();
		TableColumn<Person, String> columnCategory = new TableColumn<>(TEXTS.getString("category"));
		TableColumn<Person, String> columnCreatedAt = new TableColumn<>(TEXTS.getString("created_at"));
		TableColumn<Person, String> columnModifiedAt = new TableColumn<>(TEXTS.getString("modified_at"));

		columnId.setCellValueFactory(new PropertyValueFactory<Person, Integer>("id"));
		columnFirstname.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
		columnLastname.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));
		columnEmail.setCellValueFactory(new PropertyValueFactory<Person, String>("email"));
		columnBirthdate.setCellValueFactory(new PropertyValueFactory<Person, LocalDate>("birthdate"));
		columnCreatedAt.setCellValueFactory(new PropertyValueFactory<Person, String>("created_at"));
		columnModifiedAt.setCellValueFactory(new PropertyValueFactory<Person, String>("modified_at"));
		columnCreatedAt.setEditable(false);
		columnModifiedAt.setEditable(false);

		columnCategory.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
		columnCategory.setCellFactory(ChoiceBoxTableCell.forTableColumn(
				FXCollections.observableArrayList(TEXTS.getString("private"), TEXTS.getString("business"))));
		columnCategory.setOnEditCommit(event -> {
			Person editedPerson = event.getRowValue();
			editedPerson.setCategory(event.getNewValue());
			editedPerson.updateContact();
			tableView.refresh();
		});

		columnDelete.setCellFactory(param -> new TableCell<>() {
			private final Button deleteButton = new Button(TEXTS.getString("deleteBtn"));

			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
				} else {
					setGraphic(deleteButton);
					deleteButton.setOnAction(event -> {

						Alert confirm = new Alert(AlertType.CONFIRMATION);
						confirm.setTitle(TEXTS.getString("delete_confirm_title"));
						confirm.setHeaderText(null);
						confirm.setContentText(TEXTS.getString("delete_confirm_content"));
						Optional<ButtonType> result = confirm.showAndWait();
						if (result.get() == ButtonType.OK) {
							Person selectedPerson = getTableView().getItems().get(getIndex());
							tableView.getItems().remove(selectedPerson);
							Person.deleteContact(selectedPerson);
						} else {
							confirm.close();
						}
					});
				}
			}
		});

		tableView.setEditable(true);
		columnFirstname.setCellFactory(TextFieldTableCell.forTableColumn());
		columnFirstname.setOnEditCommit(event -> {
			Person editedPerson = event.getRowValue();
			editedPerson.setFirstname(event.getNewValue());
			editedPerson.updateContact();
			tableView.refresh();
		});

		columnLastname.setCellFactory(TextFieldTableCell.forTableColumn());
		columnLastname.setOnEditCommit(event -> {
			Person editedPerson = event.getRowValue();
			editedPerson.setLastname(event.getNewValue());
			editedPerson.updateContact();
			tableView.refresh();
		});

		columnEmail.setCellFactory(TextFieldTableCell.forTableColumn());
		columnEmail.setOnEditCommit(event -> {
			Person editedPerson = event.getRowValue();
			editedPerson.setEmail(event.getNewValue());
			editedPerson.updateContact();
			tableView.refresh();
		});

		columnBirthdate.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
		columnBirthdate.setOnEditCommit(event -> {
			Person editedPerson = event.getRowValue();
			editedPerson.setBirthdate(event.getNewValue());
			editedPerson.updateContact();
			tableView.refresh();
		});

		tableView.getColumns().add(columnId);
		tableView.getColumns().add(columnFirstname);
		tableView.getColumns().add(columnLastname);
		tableView.getColumns().add(columnEmail);
		tableView.getColumns().add(columnBirthdate);
		tableView.getColumns().add(columnCategory);
		tableView.getColumns().add(columnCreatedAt);
		tableView.getColumns().add(columnModifiedAt);
		tableView.getColumns().add(columnDelete);

		AnchorPane anchorPane = new AnchorPane();
		anchorPane.getChildren().add(tableView);
		AnchorPane.setTopAnchor(tableView, 10.0);
		AnchorPane.setBottomAnchor(tableView, 10.0);
		AnchorPane.setRightAnchor(tableView, 10.0);
		AnchorPane.setLeftAnchor(tableView, 10.0);

		tabShowContatcts.setContent(anchorPane);
	}

	public void clearFields() {
		textFieldFirstname.clear();
		textFieldLastname.clear();
		textFieldEmail.clear();
		datePickerBirthdate.setValue(null);
	}

	public void cancelBtnEventHandler() {
		cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				clearFields();
			}
		});
	}

	String emailPattern = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

	public void submitBtnEventHandler() {
		submitBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (textFieldFirstname.getText().equals("") || textFieldLastname.getText().equals("")
						|| textFieldEmail.getText().equals("") || datePickerBirthdate.getValue() == null) {
					showErrorAlert(TEXTS.getString("invalid_input_title"), TEXTS.getString("invalid_input_content"));
					return;
				}
				if (!textFieldEmail.getText().matches(emailPattern)) {
					showErrorAlert(TEXTS.getString("invalid_email_title"), TEXTS.getString("invalid_email_content"));
					return;
				}

				Person contact = new Person(textFieldFirstname.getText(), textFieldLastname.getText(),
						textFieldEmail.getText(), datePickerBirthdate.getValue(), categorySelector.getValue());

				contact.saveContact();
				List<Person> newContacts = Person.fetchContacts();
				tableView.getItems().add(newContacts.get(newContacts.size() - 1));
				clearFields();
			}
		});
	}

	private void showErrorAlert(String title, String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
