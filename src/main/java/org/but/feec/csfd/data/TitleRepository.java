package org.but.feec.csfd.data;

import org.but.feec.csfd.api.title.*;
import org.but.feec.csfd.config.DataSourceConfig;
import org.but.feec.csfd.exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TitleRepository {

    public TitleDetailView findTitleDetailedView(Long titleId) {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT t.id_title, name, p.type_name, n.genre_name, c.country_name, year, lenght, t.description" +
                             " FROM csfd_app.titles t" +
                             " LEFT JOIN csfd_app.country c ON t.id_country = c.id_country" +
                             " LEFT JOIN csfd_app.type p ON t.id_type = p.id_type" +
                             " LEFT JOIN csfd_app.genre g ON t.id_title = g.id_title" +
                             " LEFT JOIN csfd_app.genre_name n ON g.id_genre_name = n.id_genre_name" +
                             " WHERE t.id_title = ?" +
                             " ORDER BY t.id_title")
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
                     "SELECT t.id_title, name, p.type_name, year, lenght, c.country_name, t.description, t.id_type, t.id_country" +
                             " FROM csfd_app.titles t" +
                             " LEFT JOIN csfd_app.type p ON t.id_type = p.id_type" +
                             " LEFT JOIN csfd_app.country c ON t.id_country = c.id_country ORDER BY t.id_title");
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
            String insertTitleSQL = "INSERT INTO csfd_app.titles (id_title, id_type, id_country, name, year, lenght, description) VALUES (DEFAULT,?,?,?,?,?,?)";
            try (Connection connection = DataSourceConfig.getConnection();
                 // would be beneficial if I will return the created entity back
                 PreparedStatement preparedStatement = connection.prepareStatement(insertTitleSQL, Statement.RETURN_GENERATED_KEYS)) {
                // set prepared statement variables
                preparedStatement.setLong(1, titleCreateView.getType());
                preparedStatement.setLong(2, titleCreateView.getCountry());
                preparedStatement.setString(3, titleCreateView.getName());
                preparedStatement.setDate(4, titleCreateView.getYear());
                preparedStatement.setLong(5, titleCreateView.getLenght());
                preparedStatement.setString(6, titleCreateView.getDescription());

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DataAccessException("Creating title failed, no rows affected.");
                }
            } catch (SQLException e) {
                throw new DataAccessException("Creating title failed operation on the database failed.");
            }
    }

    public void deleteTitle(TitleDeleteView titlesDeleteView){
        String deleteTitleSQL = "DELETE FROM csfd_app.titles t WHERE t.id_title = ?";
        String checkIfExists = "SELECT id_title FROM csfd_app.titles t WHERE t.id_title = ? ORDER BY t.id_title";
        try (Connection connection = DataSourceConfig.getConnection();
             // would be beneficial if I will return the created entity back
             PreparedStatement preparedStatement = connection.prepareStatement(deleteTitleSQL, Statement.RETURN_GENERATED_KEYS)) {
            // set prepared statement variables
            preparedStatement.setLong(1, titlesDeleteView.getId());

            try {
                // TODO set connection autocommit to false
                connection.setAutoCommit(false);
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
                connection.commit();
            } catch (SQLException e) {
                // TODO rollback the transaction if something wrong occurs
                connection.rollback();
            } finally {
                // TODO set connection autocommit back to true
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Deleting title failed operation on the database failed.");
        }
    }

    public void editTitle(TitleEditView titleEditView) {
            String insertTitleSQL = "UPDATE csfd_app.titles t SET name = ?, id_type = ?, year = ?, lenght = ?, id_country = ? WHERE t.id_title = ?";
            String checkIfExists = "SELECT id_title FROM csfd_app.titles t WHERE t.id_title = ? ORDER BY t.id_title";
            try (Connection connection = DataSourceConfig.getConnection();
                 // would be beneficial if I will return the created entity back
                 PreparedStatement preparedStatement = connection.prepareStatement(insertTitleSQL, Statement.RETURN_GENERATED_KEYS)) {
                // set prepared statement variables
                preparedStatement.setString(1, titleEditView.getTitle());
                preparedStatement.setLong(2, titleEditView.getType());
                preparedStatement.setDate(3, titleEditView.getYear());
                preparedStatement.setLong(4, titleEditView.getLenght());
                preparedStatement.setLong(5, titleEditView.getCountry());
                preparedStatement.setLong(6, titleEditView.getId());

                try {
                    // TODO set connection autocommit to false
                    connection.setAutoCommit(false);
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
                    connection.commit();
                } catch (SQLException e) {
                    // TODO rollback the transaction if something wrong occurs
                    connection.rollback();
                } finally {
                    // TODO set connection autocommit back to true
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                throw new DataAccessException("Creating title failed operation on the database failed.");
            }
    }

    public List<TitleBasicView> getTitleFindView(String find, String choice) {
        try (Connection connection = DataSourceConfig.getConnection()){
            PreparedStatement preparedStatement;
            if(choice == "id"){
                preparedStatement = connection.prepareStatement(
                        "SELECT t.id_title, name, p.type_name, year, lenght, c.country_name, t.description, t.id_type, t.id_country" +
                                " FROM csfd_app.titles t" +
                                " LEFT JOIN csfd_app.type p ON t.id_type = p.id_type" +
                                " LEFT JOIN csfd_app.country c ON t.id_country = c.id_country" +
                                " WHERE t.id_title = ?" +
                                " ORDER BY t.id_title");

                preparedStatement.setLong(1, Long.parseLong(find));
            }
            else if(choice == "title"){
                preparedStatement = connection.prepareStatement(
                        "SELECT t.id_title, name, p.type_name, year, lenght, c.country_name, t.description, t.id_type, t.id_country" +
                                " FROM csfd_app.titles t" +
                                " LEFT JOIN csfd_app.type p ON t.id_type = p.id_type" +
                                " LEFT JOIN csfd_app.country c ON t.id_country = c.id_country" +
                                " WHERE name" +
                                " LIKE ?" +
                                " ORDER BY t.id_title");

                preparedStatement.setString(1, "%" + find + "%");
            }
            else if(choice == "type"){
                preparedStatement = connection.prepareStatement(
                        "SELECT t.id_title, name, p.type_name, year, lenght, c.country_name, t.description, t.id_type, t.id_country" +
                                " FROM csfd_app.titles t" +
                                " LEFT JOIN csfd_app.type p ON t.id_type = p.id_type" +
                                " LEFT JOIN csfd_app.country c ON t.id_country = c.id_country" +
                                " WHERE p.type_name" +
                                " LIKE ?" +
                                " ORDER BY t.id_title");

                preparedStatement.setString(1, "%" + find + "%");
            }
            else if(choice == "year"){
                preparedStatement = connection.prepareStatement(
                        "SELECT t.id_title, name, p.type_name, year, lenght, c.country_name, t.description, t.id_type, t.id_country" +
                                " FROM csfd_app.titles t" +
                                " LEFT JOIN csfd_app.type p ON t.id_type = p.id_type" +
                                " LEFT JOIN csfd_app.country c ON t.id_country = c.id_country" +
                                " WHERE EXTRACT(YEAR FROM year) = ?" +
                                " ORDER BY t.id_title");

                preparedStatement.setInt(1, Integer.parseInt(find));
            }
            else if(choice == "lenght"){
                preparedStatement = connection.prepareStatement(
                        "SELECT t.id_title, name, p.type_name, year, lenght, c.country_name, t.description, t.id_type, t.id_country" +
                                " FROM csfd_app.titles t" +
                                " LEFT JOIN csfd_app.type p ON t.id_type = p.id_type" +
                                " LEFT JOIN csfd_app.country c ON t.id_country = c.id_country" +
                                " WHERE lenght = ?" +
                                " ORDER BY t.id_title");

                preparedStatement.setInt(1, Integer.parseInt(find));
            }
            else if(choice == "country"){
                preparedStatement = connection.prepareStatement(
                        "SELECT t.id_title, name, p.type_name, year, lenght, c.country_name, t.description, t.id_type, t.id_country" +
                                " FROM csfd_app.titles t" +
                                " LEFT JOIN csfd_app.type p ON t.id_type = p.id_type" +
                                " LEFT JOIN csfd_app.country c ON t.id_country = c.id_country" +
                                " WHERE c.country_name" +
                                " LIKE ?" +
                                " ORDER BY t.id_title");

                preparedStatement.setString(1, "%" + find + "%");
            }
            else{
                preparedStatement = connection.prepareStatement(
                        "SELECT t.id_title, name, p.type_name, year, lenght, c.country_name, t.description, t.id_type, t.id_country" +
                                " FROM csfd_app.titles t" +
                                " LEFT JOIN csfd_app.type p ON t.id_type = p.id_type" +
                                " LEFT JOIN csfd_app.country c ON t.id_country = c.id_country" +
                                " ORDER BY t.id_title");

            }

            ResultSet resultSet = preparedStatement.executeQuery();
            List<TitleBasicView> titleBasicViews = new ArrayList<>();
            while (resultSet.next()) {
                titleBasicViews.add(mapToTitleBasicView(resultSet));
            }
            return titleBasicViews;

        } catch (SQLException e) {
            throw new DataAccessException("Find title by ID with addresses failed.", e);
        }
    }

    private TitleBasicView mapToTitleBasicView(ResultSet rs) throws SQLException {
        TitleBasicView titleBasicView = new TitleBasicView();
        titleBasicView.setId(rs.getLong("id_title"));
        titleBasicView.setTitle(rs.getString("name"));
        titleBasicView.setType(rs.getString("type_name"));
        titleBasicView.setTypeId(rs.getString("id_type"));
        titleBasicView.setYear(rs.getString("year"));
        titleBasicView.setLenght(rs.getLong("lenght"));
        titleBasicView.setCountry(rs.getString("country_name"));
        titleBasicView.setCountryId(rs.getString("id_country"));
        titleBasicView.setDescription(rs.getString("description"));
        return titleBasicView;
    }

    private TitleDetailView mapToTitleDetailView(ResultSet rs) throws SQLException {
        TitleDetailView titleDetailView = new TitleDetailView();
        titleDetailView.setId(rs.getLong("id_title"));
        titleDetailView.setName(rs.getString("name"));
        titleDetailView.setType(rs.getString("type_name"));
        titleDetailView.setGenre(rs.getString("genre_name"));
        titleDetailView.setCountry(rs.getString("country_name"));
        titleDetailView.setYear(rs.getString("year"));
        titleDetailView.setLenght(rs.getString("lenght"));
        titleDetailView.setDescription(rs.getString("description"));
        return titleDetailView;
    }

}
