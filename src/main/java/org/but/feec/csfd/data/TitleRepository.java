package org.but.feec.csfd.data;

import org.but.feec.csfd.api.title.*;
import org.but.feec.csfd.config.DataSourceConfig;
import org.but.feec.csfd.exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TitleRepository {

    public TitleAuthView findTitleByEmail(String email) {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT email, password" +
                             " FROM public.user u" +
                             " WHERE u.email = ? ORDER BY u.id_user")
        ) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToTitleAuth(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Find title by ID with addresses failed.", e);
        }
        return null;
    }

    public TitleDetailView findTitleDetailedView(Long titleId) {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT id_user, first_name, surname, nick, email, city, street_name, house_number, user_created" +
                             " FROM public.user u" +
                             " LEFT JOIN address a ON u.id_address = a.id_address" +
                             " WHERE u.id_user = ? ORDER BY u.id_user")
        ) {
            preparedStatement.setLong(1, titleId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToTitleDetailView(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Find title by ID with addresses failed.", e);
        }
        return null;
    }

    /**
     * What will happen if we do not use LEFT JOIN? What titles will be returned? Ask your self and repeat JOIN from the presentations
     *
     * @return list of titles
     */
    public List<TitleBasicView> getTitlesBasicView() {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT id_user, email, first_name, surname, nick, city, u.id_address" +
                             " FROM public.user u" +
                             " LEFT JOIN address a ON u.id_address = a.id_address ORDER BY u.id_user");
             ResultSet resultSet = preparedStatement.executeQuery();) {
            List<TitleBasicView> titleBasicViews = new ArrayList<>();
            while (resultSet.next()) {
                titleBasicViews.add(mapToTitleBasicView(resultSet));
            }
            return titleBasicViews;
        } catch (SQLException e) {
            throw new DataAccessException("Titles basic view could not be loaded.", e);
        }
    }

    public void createTitle(TitleCreateView titleCreateView) {
        if(titleCreateView.getAddress() != "NULL"){
            String insertTitleSQL = "INSERT INTO public.user (id_user, first_name, surname, nick, email, password, user_created, id_address) VALUES (DEFAULT,?,?,?,?,?, CURRENT_TIMESTAMP, ?)";
            try (Connection connection = DataSourceConfig.getConnection();
                 // would be beneficial if I will return the created entity back
                 PreparedStatement preparedStatement = connection.prepareStatement(insertTitleSQL, Statement.RETURN_GENERATED_KEYS)) {
                // set prepared statement variables
                preparedStatement.setString(4, titleCreateView.getEmail());
                preparedStatement.setString(1, titleCreateView.getFirstName());
                preparedStatement.setString(3, titleCreateView.getNickname());
                preparedStatement.setString(5, String.valueOf(titleCreateView.getPwd()));
                preparedStatement.setString(2, titleCreateView.getSurname());
                preparedStatement.setInt(6, Integer.parseInt(titleCreateView.getAddress()));

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DataAccessException("Creating title failed, no rows affected.");
                }
            } catch (SQLException e) {
                throw new DataAccessException("Creating title failed operation on the database failed.");
            }
        }
        else{
            String insertTitleSQL = "INSERT INTO public.user (id_user, first_name, surname, nick, email, password, user_created, id_address) VALUES (DEFAULT,?,?,?,?,?, CURRENT_TIMESTAMP, NULL)";
            try (Connection connection = DataSourceConfig.getConnection();
                 // would be beneficial if I will return the created entity back
                 PreparedStatement preparedStatement = connection.prepareStatement(insertTitleSQL, Statement.RETURN_GENERATED_KEYS)) {
                // set prepared statement variables
                preparedStatement.setString(4, titleCreateView.getEmail());
                preparedStatement.setString(1, titleCreateView.getFirstName());
                preparedStatement.setString(3, titleCreateView.getNickname());
                preparedStatement.setString(5, String.valueOf(titleCreateView.getPwd()));
                preparedStatement.setString(2, titleCreateView.getSurname());

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DataAccessException("Creating title failed, no rows affected.");
                }
            } catch (SQLException e) {
                throw new DataAccessException("Creating title failed operation on the database failed.");
            }
        }
    }

    public void deleteTitle(TitleDeleteView titlesDeleteView){
        String deleteTitleSQL = "DELETE FROM public.user u WHERE u.id_user = ?";
        String checkIfExists = "SELECT email FROM public.user u WHERE u.id_user = ? ORDER BY u.id_user";
        try (Connection connection = DataSourceConfig.getConnection();
             // would be beneficial if I will return the created entity back
             PreparedStatement preparedStatement = connection.prepareStatement(deleteTitleSQL, Statement.RETURN_GENERATED_KEYS)) {
            // set prepared statement variables
            preparedStatement.setLong(1, titlesDeleteView.getId());

            try {
                // TODO set connection autocommit to false
                /* HERE */
                try (PreparedStatement ps = connection.prepareStatement(checkIfExists, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1, titlesDeleteView.getId());
                    ps.execute();
                } catch (SQLException e) {
                    throw new DataAccessException("This title for delete do not exists.");
                }

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DataAccessException("Deleting title failed, no rows affected.");
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
            throw new DataAccessException("Deleting title failed operation on the database failed.");
        }
    }

    public void editTitle(TitleEditView titleEditView) {
        if(titleEditView.getAddress() != "NULL"){
            String insertTitleSQL = "UPDATE public.user u SET email = ?, first_name = ?, nick = ?, surname = ?, id_address = ? WHERE u.id_user = ?";
            String checkIfExists = "SELECT email FROM public.user u WHERE u.id_user = ? ORDER BY u.id_user";
            try (Connection connection = DataSourceConfig.getConnection();
                 // would be beneficial if I will return the created entity back
                 PreparedStatement preparedStatement = connection.prepareStatement(insertTitleSQL, Statement.RETURN_GENERATED_KEYS)) {
                // set prepared statement variables
                preparedStatement.setString(1, titleEditView.getEmail());
                preparedStatement.setString(2, titleEditView.getFirstName());
                preparedStatement.setString(3, titleEditView.getNickname());
                preparedStatement.setString(4, titleEditView.getSurname());
                preparedStatement.setInt(5, Integer.parseInt(titleEditView.getAddress()));
                preparedStatement.setLong(6, titleEditView.getId());

                try {
                    // TODO set connection autocommit to false
                    /* HERE */
                    try (PreparedStatement ps = connection.prepareStatement(checkIfExists, Statement.RETURN_GENERATED_KEYS)) {
                        ps.setLong(1, titleEditView.getId());
                        ps.execute();
                    } catch (SQLException e) {
                        throw new DataAccessException("This title for edit do not exists.");
                    }

                    int affectedRows = preparedStatement.executeUpdate();

                    if (affectedRows == 0) {
                        throw new DataAccessException("Creating title failed, no rows affected.");
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
                throw new DataAccessException("Creating title failed operation on the database failed.");
            }
        }
        else{
            String insertTitleSQL = "UPDATE public.user u SET email = ?, first_name = ?, nick = ?, surname = ?, id_address = NULL WHERE u.id_user = ?";
            String checkIfExists = "SELECT email FROM public.user u WHERE u.id_user = ? ORDER BY u.id_user";
            try (Connection connection = DataSourceConfig.getConnection();
                 // would be beneficial if I will return the created entity back
                 PreparedStatement preparedStatement = connection.prepareStatement(insertTitleSQL, Statement.RETURN_GENERATED_KEYS)) {
                // set prepared statement variables
                preparedStatement.setString(1, titleEditView.getEmail());
                preparedStatement.setString(2, titleEditView.getFirstName());
                preparedStatement.setString(3, titleEditView.getNickname());
                preparedStatement.setString(4, titleEditView.getSurname());
                preparedStatement.setLong(5, titleEditView.getId());

                try {
                    // TODO set connection autocommit to false
                    /* HERE */
                    try (PreparedStatement ps = connection.prepareStatement(checkIfExists, Statement.RETURN_GENERATED_KEYS)) {
                        ps.setLong(1, titleEditView.getId());
                        ps.execute();
                    } catch (SQLException e) {
                        throw new DataAccessException("This title for edit do not exists.");
                    }

                    int affectedRows = preparedStatement.executeUpdate();

                    if (affectedRows == 0) {
                        throw new DataAccessException("Creating title failed, no rows affected.");
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
                throw new DataAccessException("Creating title failed operation on the database failed.");
            }
        }
    }


    /**
     * <p>
     * Note: In practice reflection or other mapping frameworks can be used (e.g., MapStruct)
     * </p>
     */
    private TitleAuthView mapToTitleAuth(ResultSet rs) throws SQLException {
        TitleAuthView title = new TitleAuthView();
        title.setEmail(rs.getString("email"));
        title.setPassword(rs.getString("password"));
        return title;
    }

    private TitleBasicView mapToTitleBasicView(ResultSet rs) throws SQLException {
        TitleBasicView titleBasicView = new TitleBasicView();
        titleBasicView.setId(rs.getLong("id_user"));
        titleBasicView.setEmail(rs.getString("email"));
        titleBasicView.setGivenName(rs.getString("first_name"));
        titleBasicView.setFamilyName(rs.getString("surname"));
        titleBasicView.setNickname(rs.getString("nick"));
        titleBasicView.setAddress(rs.getString("id_address"));
        titleBasicView.setCity(rs.getString("city"));
        return titleBasicView;
    }

    private TitleDetailView mapToTitleDetailView(ResultSet rs) throws SQLException {
        TitleDetailView titleDetailView = new TitleDetailView();
        titleDetailView.setId(rs.getLong("id_user"));
        titleDetailView.setGivenName(rs.getString("first_name"));
        titleDetailView.setFamilyName(rs.getString("surname"));
        titleDetailView.setNickname(rs.getString("nick"));
        titleDetailView.setEmail(rs.getString("email"));
        titleDetailView.setCity(rs.getString("city"));
        titleDetailView.setStreet(rs.getString("street_name"));
        titleDetailView.sethouseNumber(rs.getString("house_number"));
        titleDetailView.setCreated(rs.getString("user_created"));
        return titleDetailView;
    }

}
