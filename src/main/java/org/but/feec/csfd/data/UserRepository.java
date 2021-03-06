package org.but.feec.csfd.data;

import org.but.feec.csfd.api.title.TitleBasicView;
import org.but.feec.csfd.api.user.*;
import org.but.feec.csfd.config.DataSourceConfig;
import org.but.feec.csfd.exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    public UserAuthView findUserByNick(String nickname) {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT nick, password" +
                             " FROM csfd_app.user u" +
                             " WHERE u.nick = ? ORDER BY u.id_user")
        ) {
            preparedStatement.setString(1, nickname);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToUserAuth(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Find user by ID with addresses failed.", e);
        }
        return null;
    }

    public UserDetailView findUserDetailedView(Long userId) {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT id_user, pgp_sym_decrypt(first_name, 'SuperTajneH3slo2') AS first_name," +
                             " pgp_sym_decrypt(surname, 'SuperTajneH3slo2') AS surname, nick," +
                             " pgp_sym_decrypt(email, 'SuperTajneH3slo2') AS email, city, street_name, house_number, user_created" +
                             " FROM csfd_app.user u" +
                             " LEFT JOIN csfd_app.address a ON u.id_address = a.id_address" +
                             " WHERE u.id_user = ? ORDER BY u.id_user")
        ) {
            preparedStatement.setLong(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToUserDetailView(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Find user by ID with addresses failed.", e);
        }
        return null;
    }

    public List<UserBasicView> getUsersBasicView() {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT id_user, pgp_sym_decrypt(email, 'SuperTajneH3slo2') AS email," +
                             " pgp_sym_decrypt(first_name, 'SuperTajneH3slo2') AS first_name," +
                             " pgp_sym_decrypt(surname, 'SuperTajneH3slo2') AS surname, nick, city, u.id_address" +
                             " FROM csfd_app.user u" +
                             " LEFT JOIN csfd_app.address a ON u.id_address = a.id_address ORDER BY u.id_user");
             ResultSet resultSet = preparedStatement.executeQuery();) {
            List<UserBasicView> userBasicViews = new ArrayList<>();
            while (resultSet.next()) {
                userBasicViews.add(mapToUserBasicView(resultSet));
            }
            return userBasicViews;
        } catch (SQLException e) {
            throw new DataAccessException("Users basic view could not be loaded.", e);
        }
    }

    public void createUser(UserCreateView userCreateView) {
        if(userCreateView.getAddress() != null){
            String insertUserSQL = "INSERT INTO csfd_app.user (id_user, first_name, surname, nick, email, password, user_created, id_address) VALUES (DEFAULT,pgp_sym_encrypt(?,'SuperTajneH3slo2'),pgp_sym_encrypt(?,'SuperTajneH3slo2'),?,pgp_sym_encrypt(?,'SuperTajneH3slo2'),?, CURRENT_TIMESTAMP, ?)";
            try (Connection connection = DataSourceConfig.getConnection();
                 // would be beneficial if I will return the created entity back
                 PreparedStatement preparedStatement = connection.prepareStatement(insertUserSQL, Statement.RETURN_GENERATED_KEYS)) {
                // set prepared statement variables
                preparedStatement.setString(1, userCreateView.getFirstName());
                preparedStatement.setString(2, userCreateView.getSurname());
                preparedStatement.setString(3, userCreateView.getNickname());
                preparedStatement.setString(4, userCreateView.getEmail());
                preparedStatement.setString(5, String.valueOf(userCreateView.getPwd()));
                preparedStatement.setLong(6, userCreateView.getAddress());

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DataAccessException("Creating user failed, no rows affected.");
                }
            } catch (SQLException e) {
                throw new DataAccessException("Creating user failed operation on the database failed.");
            }
        }
        else{
            String insertUserSQL = "INSERT INTO csfd_app.user (id_user, first_name, surname, nick, email, password, user_created, id_address) VALUES (DEFAULT,pgp_sym_encrypt(?,'SuperTajneH3slo2'),pgp_sym_encrypt(?,'SuperTajneH3slo2'),?,pgp_sym_encrypt(?,'SuperTajneH3slo2'),?, CURRENT_TIMESTAMP, NULL)";
            try (Connection connection = DataSourceConfig.getConnection();
                 // would be beneficial if I will return the created entity back
                 PreparedStatement preparedStatement = connection.prepareStatement(insertUserSQL, Statement.RETURN_GENERATED_KEYS)) {
                // set prepared statement variables
                preparedStatement.setString(1, userCreateView.getFirstName());
                preparedStatement.setString(2, userCreateView.getSurname());
                preparedStatement.setString(3, userCreateView.getNickname());
                preparedStatement.setString(4, userCreateView.getEmail());
                preparedStatement.setString(5, String.valueOf(userCreateView.getPwd()));

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DataAccessException("Creating user failed, no rows affected.");
                }
            } catch (SQLException e) {
                throw new DataAccessException("Creating user failed operation on the database failed.");
            }
        }
    }

    public void deleteUser(UserDeleteView usersDeleteView){
        String deleteUserSQL = "DELETE FROM csfd_app.user u WHERE u.id_user = ?";
        String checkIfExists = "SELECT email FROM csfd_app.user u WHERE u.id_user = ? ORDER BY u.id_user";
        try (Connection connection = DataSourceConfig.getConnection();
             // would be beneficial if I will return the created entity back
             PreparedStatement preparedStatement = connection.prepareStatement(deleteUserSQL, Statement.RETURN_GENERATED_KEYS)) {
            // set prepared statement variables
            preparedStatement.setLong(1, usersDeleteView.getId());

            try {
                connection.setAutoCommit(false);
                try (PreparedStatement ps = connection.prepareStatement(checkIfExists, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1, usersDeleteView.getId());
                    ps.execute();
                } catch (SQLException e) {
                    throw new DataAccessException("This user for delete do not exists.");
                }

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DataAccessException("Deleting user failed, no rows affected.");
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Deleting user failed operation on the database failed.");
        }
    }

    public void editUser(UserEditView userEditView) {
        if(userEditView.getAddress() != null){
            String insertUserSQL = "UPDATE csfd_app.user u SET email = pgp_sym_encrypt(?,'SuperTajneH3slo2'), first_name = pgp_sym_encrypt(?,'SuperTajneH3slo2'), nick = ?, surname = pgp_sym_encrypt(?,'SuperTajneH3slo2'), id_address = ? WHERE u.id_user = ?";
            String checkIfExists = "SELECT email FROM csfd_app.user u WHERE u.id_user = ? ORDER BY u.id_user";
            try (Connection connection = DataSourceConfig.getConnection();
                 // would be beneficial if I will return the created entity back
                 PreparedStatement preparedStatement = connection.prepareStatement(insertUserSQL, Statement.RETURN_GENERATED_KEYS)) {
                // set prepared statement variables
                preparedStatement.setString(1, userEditView.getEmail());
                preparedStatement.setString(2, userEditView.getFirstName());
                preparedStatement.setString(3, userEditView.getNickname());
                preparedStatement.setString(4, userEditView.getSurname());
                preparedStatement.setLong(5, userEditView.getAddress());
                preparedStatement.setLong(6, userEditView.getId());

                try {
                    connection.setAutoCommit(false);
                    try (PreparedStatement ps = connection.prepareStatement(checkIfExists, Statement.RETURN_GENERATED_KEYS)) {
                        ps.setLong(1, userEditView.getId());
                        ps.execute();
                    } catch (SQLException e) {
                        throw new DataAccessException("This user for edit do not exists.");
                    }

                    int affectedRows = preparedStatement.executeUpdate();

                    if (affectedRows == 0) {
                        throw new DataAccessException("Creating user failed, no rows affected.");
                    }
                    connection.commit();
                } catch (SQLException e) {
                    connection.rollback();
                } finally {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                throw new DataAccessException("Creating user failed operation on the database failed.");
            }
        }
        else{
            String insertUserSQL = "UPDATE csfd_app.user u SET email = pgp_sym_encrypt(?,'SuperTajneH3slo2'), first_name = pgp_sym_encrypt(?,'SuperTajneH3slo2'), nick = ?, surname = pgp_sym_encrypt(?,'SuperTajneH3slo2'), id_address = NULL WHERE u.id_user = ?";
            String checkIfExists = "SELECT email FROM csfd_app.user u WHERE u.id_user = ? ORDER BY u.id_user";
            try (Connection connection = DataSourceConfig.getConnection();
                 // would be beneficial if I will return the created entity back
                 PreparedStatement preparedStatement = connection.prepareStatement(insertUserSQL, Statement.RETURN_GENERATED_KEYS)) {
                // set prepared statement variables
                preparedStatement.setString(1, userEditView.getEmail());
                preparedStatement.setString(2, userEditView.getFirstName());
                preparedStatement.setString(3, userEditView.getNickname());
                preparedStatement.setString(4, userEditView.getSurname());
                preparedStatement.setLong(5, userEditView.getId());

                try {
                    connection.setAutoCommit(false);
                    try (PreparedStatement ps = connection.prepareStatement(checkIfExists, Statement.RETURN_GENERATED_KEYS)) {
                        ps.setLong(1, userEditView.getId());
                        ps.execute();
                    } catch (SQLException e) {
                        throw new DataAccessException("This user for edit do not exists.");
                    }

                    int affectedRows = preparedStatement.executeUpdate();

                    if (affectedRows == 0) {
                        throw new DataAccessException("Creating user failed, no rows affected.");
                    }
                    connection.commit();
                } catch (SQLException e) {
                    connection.rollback();
                } finally {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                throw new DataAccessException("Creating user failed operation on the database failed.");
            }
        }
    }

    public List<UserBasicView> getUserFindView(String find, String choice) {
        try (Connection connection = DataSourceConfig.getConnection()){
            PreparedStatement preparedStatement;
            if(choice == "id"){
                preparedStatement = connection.prepareStatement(
                        "SELECT id_user, pgp_sym_decrypt(email,'SuperTajneH3slo2') AS email," +
                                " pgp_sym_decrypt(first_name,'SuperTajneH3slo2') AS first_name," +
                                " pgp_sym_decrypt(surname,'SuperTajneH3slo2') AS surname, nick, city, u.id_address" +
                                " FROM csfd_app.user u" +
                                " LEFT JOIN csfd_app.address a ON u.id_address = a.id_address" +
                                " WHERE id_user = ?" +
                                " ORDER BY u.id_user");

                preparedStatement.setLong(1, Long.parseLong(find));
            }
            else if(choice == "given name"){
                preparedStatement = connection.prepareStatement(
                        "SELECT id_user, pgp_sym_decrypt(email,'SuperTajneH3slo2') AS email," +
                                " pgp_sym_decrypt(first_name,'SuperTajneH3slo2') AS first_name," +
                                " pgp_sym_decrypt(surname,'SuperTajneH3slo2') AS surname, nick, city, u.id_address" +
                                " FROM csfd_app.user u" +
                                " LEFT JOIN csfd_app.address a ON u.id_address = a.id_address" +
                                " WHERE pgp_sym_decrypt(first_name,'SuperTajneH3slo2')" +
                                " LIKE ?" +
                                " ORDER BY u.id_user");

                preparedStatement.setString(1, "%" + find + "%");
            }
            else if(choice == "family name"){
                preparedStatement = connection.prepareStatement(
                        "SELECT id_user, pgp_sym_decrypt(email,'SuperTajneH3slo2') AS email,"+
                                " pgp_sym_decrypt(first_name,'SuperTajneH3slo2') AS first_name," +
                                " pgp_sym_decrypt(surname,'SuperTajneH3slo2') AS surname, nick, city, u.id_address" +
                                " FROM csfd_app.user u" +
                                " LEFT JOIN csfd_app.address a ON u.id_address = a.id_address" +
                                " WHERE pgp_sym_decrypt(surname,'SuperTajneH3slo2')" +
                                " LIKE ?" +
                                " ORDER BY u.id_user");

                preparedStatement.setString(1, "%" + find + "%");
            }
            else if(choice == "nickname"){
                preparedStatement = connection.prepareStatement(
                        "SELECT id_user, pgp_sym_decrypt(email,'SuperTajneH3slo2') AS email," +
                                " pgp_sym_decrypt(first_name,'SuperTajneH3slo2') AS first_name," +
                                " pgp_sym_decrypt(surname,'SuperTajneH3slo2') AS surname, nick, city, u.id_address" +
                                " FROM csfd_app.user u" +
                                " LEFT JOIN csfd_app.address a ON u.id_address = a.id_address" +
                                " WHERE nick" +
                                " LIKE ?" +
                                " ORDER BY u.id_user");

                preparedStatement.setString(1, "%" + find + "%");
            }
            else if(choice == "email"){
                preparedStatement = connection.prepareStatement(
                        "SELECT id_user, pgp_sym_decrypt(email,'SuperTajneH3slo2') AS email," +
                                " pgp_sym_decrypt(first_name,'SuperTajneH3slo2') AS first_name," +
                                " pgp_sym_decrypt(surname,'SuperTajneH3slo2') AS surname, nick, city, u.id_address" +
                                " FROM csfd_app.user u" +
                                " LEFT JOIN csfd_app.address a ON u.id_address = a.id_address" +
                                " WHERE pgp_sym_decrypt(email,'SuperTajneH3slo2')" +
                                " LIKE ?" +
                                " ORDER BY u.id_user");

                preparedStatement.setString(1, "%" + find + "%");
            }
            else if(choice == "city"){
                preparedStatement = connection.prepareStatement(
                        "SELECT id_user, pgp_sym_decrypt(email,'SuperTajneH3slo2') AS email," +
                                " pgp_sym_decrypt(first_name,'SuperTajneH3slo2') AS first_name," +
                                " pgp_sym_decrypt(surname,'SuperTajneH3slo2') AS surname, nick, city, u.id_address" +
                                " FROM csfd_app.user u" +
                                " LEFT JOIN csfd_app.address a ON u.id_address = a.id_address" +
                                " WHERE city" +
                                " LIKE ?" +
                                " ORDER BY u.id_user");

                preparedStatement.setString(1, "%" + find + "%");
            }
            else{
                preparedStatement = connection.prepareStatement(
                        "SELECT id_user, pgp_sym_decrypt(email,'SuperTajneH3slo2') AS email," +
                                " pgp_sym_decrypt(first_name,'SuperTajneH3slo2') AS first_name," +
                                " pgp_sym_decrypt(surname,'SuperTajneH3slo2') AS surname, nick, city, u.id_address" +
                                " FROM csfd_app.user u" +
                                " LEFT JOIN csfd_app.address a ON u.id_address = a.id_address" +
                                " ORDER BY u.id_user");

            }

            ResultSet resultSet = preparedStatement.executeQuery();
            List<UserBasicView> userBasicViews = new ArrayList<>();
            while (resultSet.next()) {
                userBasicViews.add(mapToUserBasicView(resultSet));
            }
            return userBasicViews;

        } catch (SQLException e) {
            throw new DataAccessException("Find user by ID with addresses failed.", e);
        }
    }

    private UserAuthView mapToUserAuth(ResultSet rs) throws SQLException {
        UserAuthView user = new UserAuthView();
        user.setNickname(rs.getString("nick"));
        user.setPassword(rs.getString("password"));
        return user;
    }

    private UserBasicView mapToUserBasicView(ResultSet rs) throws SQLException {
        UserBasicView userBasicView = new UserBasicView();
        userBasicView.setId(rs.getLong("id_user"));
        userBasicView.setEmail(rs.getString("email"));
        userBasicView.setGivenName(rs.getString("first_name"));
        userBasicView.setFamilyName(rs.getString("surname"));
        userBasicView.setNickname(rs.getString("nick"));
        userBasicView.setAddress(rs.getString("id_address"));
        userBasicView.setCity(rs.getString("city"));
        return userBasicView;
    }

    private UserDetailView mapToUserDetailView(ResultSet rs) throws SQLException {
        UserDetailView userDetailView = new UserDetailView();
        userDetailView.setId(rs.getLong("id_user"));
        userDetailView.setGivenName(rs.getString("first_name"));
        userDetailView.setFamilyName(rs.getString("surname"));
        userDetailView.setNickname(rs.getString("nick"));
        userDetailView.setEmail(rs.getString("email"));
        userDetailView.setCity(rs.getString("city"));
        userDetailView.setStreet(rs.getString("street_name"));
        userDetailView.sethouseNumber(rs.getString("house_number"));
        userDetailView.setCreated(rs.getString("user_created"));
        return userDetailView;
    }

}
