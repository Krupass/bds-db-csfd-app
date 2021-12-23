package org.but.feec.csfd.data;

import org.but.feec.csfd.api.*;
import org.but.feec.csfd.config.DataSourceConfig;
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
                     "SELECT id_user, email, first_name, surname, nick, city, house_number, street_name" +
                             " FROM public.user u" +
                             " LEFT JOIN address a ON u.id_address = a.id_address" +
                             " WHERE u.id_user = ? ORDER BY u.id_user")
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
                     "SELECT id_user, email, first_name, surname, nick, city, u.id_address" +
                             " FROM public.user u" +
                             " LEFT JOIN address a ON u.id_address = a.id_address ORDER BY u.id_user");
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
        if(personCreateView.getAddress() != "NULL"){
            String insertPersonSQL = "INSERT INTO public.user (id_user, first_name, surname, nick, email, password, user_created, id_address) VALUES (DEFAULT,?,?,?,?,?, CURRENT_TIMESTAMP, ?)";
            try (Connection connection = DataSourceConfig.getConnection();
                 // would be beneficial if I will return the created entity back
                 PreparedStatement preparedStatement = connection.prepareStatement(insertPersonSQL, Statement.RETURN_GENERATED_KEYS)) {
                // set prepared statement variables
                preparedStatement.setString(4, personCreateView.getEmail());
                preparedStatement.setString(1, personCreateView.getFirstName());
                preparedStatement.setString(3, personCreateView.getNickname());
                preparedStatement.setString(5, String.valueOf(personCreateView.getPwd()));
                preparedStatement.setString(2, personCreateView.getSurname());
                preparedStatement.setInt(6, Integer.parseInt(personCreateView.getAddress()));

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DataAccessException("Creating person failed, no rows affected.");
                }
            } catch (SQLException e) {
                throw new DataAccessException("Creating person failed operation on the database failed.");
            }
        }
        else{
            String insertPersonSQL = "INSERT INTO public.user (id_user, first_name, surname, nick, email, password, user_created, id_address) VALUES (DEFAULT,?,?,?,?,?, CURRENT_TIMESTAMP, NULL)";
            try (Connection connection = DataSourceConfig.getConnection();
                 // would be beneficial if I will return the created entity back
                 PreparedStatement preparedStatement = connection.prepareStatement(insertPersonSQL, Statement.RETURN_GENERATED_KEYS)) {
                // set prepared statement variables
                preparedStatement.setString(4, personCreateView.getEmail());
                preparedStatement.setString(1, personCreateView.getFirstName());
                preparedStatement.setString(3, personCreateView.getNickname());
                preparedStatement.setString(5, String.valueOf(personCreateView.getPwd()));
                preparedStatement.setString(2, personCreateView.getSurname());

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DataAccessException("Creating person failed, no rows affected.");
                }
            } catch (SQLException e) {
                throw new DataAccessException("Creating person failed operation on the database failed.");
            }
        }
    }

    public void editPerson(PersonEditView personEditView) {
        if(personEditView.getAddress() != "NULL"){
            String insertPersonSQL = "UPDATE public.user u SET email = ?, first_name = ?, nick = ?, surname = ?, id_address = ? WHERE u.id_user = ?";
            String checkIfExists = "SELECT email FROM public.user u WHERE u.id_user = ? ORDER BY u.id_user";
            try (Connection connection = DataSourceConfig.getConnection();
                 // would be beneficial if I will return the created entity back
                 PreparedStatement preparedStatement = connection.prepareStatement(insertPersonSQL, Statement.RETURN_GENERATED_KEYS)) {
                // set prepared statement variables
                preparedStatement.setString(1, personEditView.getEmail());
                preparedStatement.setString(2, personEditView.getFirstName());
                preparedStatement.setString(3, personEditView.getNickname());
                preparedStatement.setString(4, personEditView.getSurname());
                preparedStatement.setInt(5, Integer.parseInt(personEditView.getAddress()));
                preparedStatement.setLong(6, personEditView.getId());

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
        else{
            String insertPersonSQL = "UPDATE public.user u SET email = ?, first_name = ?, nick = ?, surname = ?, id_address = NULL WHERE u.id_user = ?";
            String checkIfExists = "SELECT email FROM public.user u WHERE u.id_user = ? ORDER BY u.id_user";
            try (Connection connection = DataSourceConfig.getConnection();
                 // would be beneficial if I will return the created entity back
                 PreparedStatement preparedStatement = connection.prepareStatement(insertPersonSQL, Statement.RETURN_GENERATED_KEYS)) {
                // set prepared statement variables
                preparedStatement.setString(1, personEditView.getEmail());
                preparedStatement.setString(2, personEditView.getFirstName());
                preparedStatement.setString(3, personEditView.getNickname());
                preparedStatement.setString(4, personEditView.getSurname());
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
        personBasicView.setId(rs.getLong("id_user"));
        personBasicView.setEmail(rs.getString("email"));
        personBasicView.setGivenName(rs.getString("first_name"));
        personBasicView.setFamilyName(rs.getString("surname"));
        personBasicView.setNickname(rs.getString("nick"));
        personBasicView.setAddress(rs.getString("id_address"));
        personBasicView.setCity(rs.getString("city"));
        return personBasicView;
    }

    private PersonDetailView mapToPersonDetailView(ResultSet rs) throws SQLException {
        PersonDetailView personDetailView = new PersonDetailView();
        personDetailView.setId(rs.getLong("id_user"));
        personDetailView.setEmail(rs.getString("email"));
        personDetailView.setGivenName(rs.getString("first_name"));
        personDetailView.setFamilyName(rs.getString("surname"));
        personDetailView.setNickname(rs.getString("nick"));
        personDetailView.setCity(rs.getString("city"));
        personDetailView.sethouseNumber(rs.getString("house_number"));
        personDetailView.setStreet(rs.getString("street_name"));
        return personDetailView;
    }

}
