package application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.beans.property.SimpleStringProperty;
import lib.DBConnection;
import lib.DateTime;

public class Person {
	private String firstName;
	private String lastName;
	private String email;
	private LocalDate birthdate;
	private int id;
	private SimpleStringProperty category;
	private String created_at;
	private String modified_at;

	public static List<Person> persons = new ArrayList<>();

	public Person(String firstName, String lastName, String email, LocalDate birthdate, String category) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.birthdate = birthdate;
		this.category = new SimpleStringProperty(category);
	}

	public Person(String firstName, String lastName, String email, LocalDate birthdate, Integer id, String category,
			String created_at, String modified_at) {
		this(firstName, lastName, email, birthdate, category);
		this.id = id;
		this.created_at = created_at;
		this.modified_at = modified_at;
	}

	public SimpleStringProperty categoryProperty() {
		return category;
	}

	public Integer getId() {
		return this.id;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstname(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastname(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getBirthdate() {
		return this.birthdate;
	}

	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}

	public String getCategory() {
		return this.category.get();
	}

	public void setCategory(String category) {
		this.category.set(category);
	}

	public String getCreated_at() {
		return this.created_at;
	}

	public String getModified_at() {
		return this.modified_at;
	}

	private void setModifiedAt(String modified_at) {
		this.modified_at = modified_at;
	}

	public void saveContact() {
		try {
			Connection connection = DBConnection.getDB();
			Statement statement = connection.createStatement();

			String formattedDateTime = DateTime.getFormattedDateTime();

			String sql = "INSERT INTO contacts (firstname, lastname, email, birthdate, category, created_at) VALUES ('"
					+ this.firstName + "', '" + this.lastName + "', '" + this.email + "', '" + this.birthdate + "', '"
					+ this.category.getValue() + "', '" + formattedDateTime + "')";

			statement.executeUpdate(sql);
			connection.close();

		} catch (Exception e) {
			System.out.println(e);
			throw new RuntimeException("Could not write to db.");
		}
	}

	/**
	 * Fetches all Contacts from DB
	 * 
	 * @return ArrayList of contacts
	 */
	public static List<Person> fetchContacts() {
		try {
			Connection connection = DBConnection.getDB();
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery("SELECT * FROM contacts");

			while (results.next()) {
				LocalDate bdate = LocalDate.parse(results.getString("birthdate"));
				Person pers = new Person(results.getString("firstName"), results.getString("lastName"),
						results.getString("email"), bdate, results.getInt("id"), results.getString("category"),
						results.getString("created_at"), results.getString("modified_at"));

				persons.add(pers);
			}

			connection.close();
		} catch (Exception e) {
			System.out.println(e);
			throw new RuntimeException("Could not fetch from db");
		}

		return persons;
	}

	public void updateContact() {
		try {
			Connection connection = DBConnection.getDB();
			Statement statement = connection.createStatement();

			String formattedDateTime = DateTime.getFormattedDateTime();

			String sql = "UPDATE contacts SET firstname = '" + this.firstName + "', lastname = '" + this.lastName
					+ "', email = '" + this.email + "', birthdate = '" + this.birthdate + "', category = '"
					+ this.category.getValue() + "', modified_at = '" + formattedDateTime + "' WHERE id = " + this.id;

			statement.executeUpdate(sql);
			connection.close();

			this.setModifiedAt(formattedDateTime);
		} catch (Exception e) {
			System.out.println(e);
			throw new RuntimeException("Could not update the contact in the database.");
		}
	}

	public static void deleteContact(Person contact) {
		try {
			Connection connection = DBConnection.getDB();
			Statement statement = connection.createStatement();

			String sql = "DELETE FROM `javacontacts`.`contacts` WHERE (`id` = '" + contact.id + "');";

			statement.executeUpdate(sql);
			connection.close();

		} catch (Exception e) {
			System.out.println(e);
			throw new RuntimeException("Could not delete from db.");
		}
	}

}
