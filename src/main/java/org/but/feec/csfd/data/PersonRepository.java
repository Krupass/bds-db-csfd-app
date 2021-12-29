package org.but.feec.csfd.data;

import org.but.feec.csfd.api.*;
import org.but.feec.csfd.config.DataSourceConfig;
import org.but.feec.csfd.controller.PersonsDeleteController;
import org.but.feec.csfd.exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class PersonRepository {

    public PersonAuthView findPersonByEmail(String email) {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT email, password" +
                             " FROM public.user u" +
                             " WHERE u.email = ? ORDER BY u.id_user")
        ) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToPersonAuth(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Find person by ID with addresses failed.", e);
        }
        return null;
    }

    public PersonDetailView findPersonDetailedView(Long personId) {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT id_person, first_name, surname, date_of_birth, city, street_name, house_number" +
                             " FROM person p" +
                             " LEFT JOIN address a ON p.id_address = a.id_address" +
                             " WHERE p.id_person = ? ORDER BY p.id_person")
        ) {
            preparedStatement.setLong(1, personId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToPersonDetailView(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Find person by ID with addresses failed.", e);
        }
        return null;
    }

    /**
     * What will happen if we do not use LEFT JOIN? What persons will be returned? Ask your self and repeat JOIN from the presentations
     *
     * @return list of persons
     */
    public List<PersonBasicView> getPersonsBasicView() {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT p.id_person, first_name, surname, date_of_birth, city, p.id_address" +
                             " FROM person p" +
                             " LEFT JOIN address a ON p.id_address = a.id_address ORDER BY p.id_person");
             ResultSet resultSet = preparedStatement.executeQuery();) {
            List<PersonBasicView> personBasicViews = new ArrayList<>();
            while (resultSet.next()) {
                personBasicViews.add(mapToPersonBasicView(resultSet));
            }
            return personBasicViews;
        } catch (SQLException e) {
            throw new DataAccessException("Persons basic view could not be loaded.", e);
        }
    }

    public void createPerson(PersonCreateView personCreateView) {
        if(personCreateView.getAddress() != null){
            String insertPersonSQL = "INSERT INTO person (id_person, first_name, surname, date_of_birth, id_address) VALUES (DEFAULT,?,?,?,?)";
            try (Connection connection = DataSourceConfig.getConnection();
                 // would be beneficial if I will return the created entity back
                 PreparedStatement preparedStatement = connection.prepareStatement(insertPersonSQL, Statement.RETURN_GENERATED_KEYS)) {
                // set prepared statement variables
                preparedStatement.setString(1, personCreateView.getFirstName());
                preparedStatement.setString(2, personCreateView.getSurname());
                preparedStatement.setDate(3, personCreateView.getBirthday());
                preparedStatement.setLong(4, personCreateView.getAddress());

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DataAccessException("Creating person failed, no rows affected.");
                }
            } catch (SQLException e) {
                throw new DataAccessException("Creating person failed operation on the database failed.");
            }
        }
        else{
            String insertPersonSQL = "INSERT INTO person (id_person, first_name, surname, date_of_birth, id_address) VALUES (DEFAULT,?,?,?, NULL)";
            try (Connection connection = DataSourceConfig.getConnection();
                 // would be beneficial if I will return the created entity back
                 PreparedStatement preparedStatement = connection.prepareStatement(insertPersonSQL, Statement.RETURN_GENERATED_KEYS)) {
                // set prepared statement variables
                preparedStatement.setString(1, personCreateView.getFirstName());
                preparedStatement.setString(2, personCreateView.getSurname());
                preparedStatement.setDate(3, personCreateView.getBirthday());

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DataAccessException("Creating person failed, no rows affected.");
                }
            } catch (SQLException e) {
                throw new DataAccessException("Creating person failed operation on the database failed.");
            }
        }
    }

    public void deletePerson(PersonDeleteView personsDeleteView){
        String deletePersonSQL = "DELETE FROM person p WHERE p.id_person = ?";
        String checkIfExists = "SELECT id_person FROM person p WHERE p.id_person = ? ORDER BY p.id_person";
        try (Connection connection = DataSourceConfig.getConnection();
             // would be beneficial if I will return the created entity back
             PreparedStatement preparedStatement = connection.prepareStatement(deletePersonSQL, Statement.RETURN_GENERATED_KEYS)) {
            // set prepared statement variables
            preparedStatement.setLong(1, personsDeleteView.getId());

            try {
                // TODO set connection autocommit to false
                /* HERE */
                try (PreparedStatement ps = connection.prepareStatement(checkIfExists, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1, personsDeleteView.getId());
                    ps.execute();
                } catch (SQLException e) {
                    throw new DataAccessException("This person for delete do not exists.");
                }

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DataAccessException("Deleting person failed, no rows affected.");
                }
                // TODO commit the transaction (both queries were performed)
                /* HERE */
            } catch (SQLException e) {
                // TODO rollback the transaction if something wrong occurs
                /* HERE */
            } finally {
                // TODO set connection autocommit back to true
                /* HERE */
            }
        } catch (SQLException e) {
            throw new DataAccessException("Deleting person failed operation on the database failed.");
        }
    }

    public void editPerson(PersonEditView personEditView) {
            String insertPersonSQL = "UPDATE person SET first_name = ?, surname = ?, date_of_birth = ?, id_address = ? WHERE id_person = ?";
            String checkIfExists = "SELECT id_person FROM person p WHERE p.id_person = ? ORDER BY p.id_person";
            try (Connection connection = DataSourceConfig.getConnection();
                 // would be beneficial if I will return the created entity back
                 PreparedStatement preparedStatement = connection.prepareStatement(insertPersonSQL, Statement.RETURN_GENERATED_KEYS)) {
                // set prepared statement variables
                preparedStatement.setString(1, personEditView.getFirstName());
                preparedStatement.setString(2, personEditView.getSurname());
                preparedStatement.setDate(3, personEditView.getBirthday());
                preparedStatement.setLong(4, personEditView.getAddress());
                preparedStatement.setLong(5, personEditView.getId());

                try {
                    // TODO set connection autocommit to false
                    /* HERE */
                    try (PreparedStatement ps = connection.prepareStatement(checkIfExists, Statement.RETURN_GENERATED_KEYS)) {
                        ps.setLong(1, personEditView.getId());
                        ps.execute();
                    } catch (SQLException e) {
                        throw new DataAccessException("This person for edit do not exists.");
                    }

                    int affectedRows = preparedStatement.executeUpdate();

                    if (affectedRows == 0) {
                        throw new DataAccessException("Creating person failed, no rows affected.");
                    }
                    // TODO commit the transaction (both queries were performed)
                    /* HERE */
                } catch (SQLException e) {
                    // TODO rollback the transaction if something wrong occurs
                    /* HERE */
                } finally {
                    // TODO set connection autocommit back to true
                    /* HERE */
                }
            } catch (SQLException e) {
                throw new DataAccessException("Creating person failed operation on the database failed.");
            }
    }


    /**
     * <p>
     * Note: In practice reflection or other mapping frameworks can be used (e.g., MapStruct)
     * </p>
     */
    private PersonAuthView mapToPersonAuth(ResultSet rs) throws SQLException {
        PersonAuthView person = new PersonAuthView();
        person.setEmail(rs.getString("email"));
        person.setPassword(rs.getString("password"));
        return person;
    }

    private PersonBasicView mapToPersonBasicView(ResultSet rs) throws SQLException {
        PersonBasicView personBasicView = new PersonBasicView();
        personBasicView.setId(rs.getLong("id_person"));
        personBasicView.setGivenName(rs.getString("first_name"));
        personBasicView.setFamilyName(rs.getString("surname"));
        personBasicView.setBirthday(rs.getString("date_of_birth"));
        personBasicView.setCity(rs.getString("city"));
        personBasicView.setAddress(rs.getString("id_address"));
        return personBasicView;
    }

    private PersonDetailView mapToPersonDetailView(ResultSet rs) throws SQLException {
        PersonDetailView personDetailView = new PersonDetailView();
        personDetailView.setId(rs.getLong("id_person"));
        personDetailView.setGivenName(rs.getString("first_name"));
        personDetailView.setFamilyName(rs.getString("surname"));
        personDetailView.setBirthday(rs.getString("date_of_birth"));
        personDetailView.setCity(rs.getString("city"));
        personDetailView.setStreet(rs.getString("street_name"));
        personDetailView.sethouseNumber(rs.getString("house_number"));
        return personDetailView;
    }

}
