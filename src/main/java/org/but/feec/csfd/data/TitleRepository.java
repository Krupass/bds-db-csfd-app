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
                     "SELECT t.id_title, t.name, p.type_name, n.genre_name, c.country_name, t.year, t.lenght, t.description" +
                             " FROM titles t" +
                             " LEFT JOIN country c ON t.id_country = c.id_country" +
                             " LEFT JOIN type p ON t.id_type = p.id_type" +
                             " LEFT JOIN genre g ON t.id_title = g.id_title" +
                             " JOIN genre_name n ON g.id_genre_name = n.id_genre_name" +
                             " WHERE t.id_title = ? ORDER BY t.id_title")
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
                             " FROM titles t" +
                             " LEFT JOIN type p ON t.id_type = p.id_type" +
                             " LEFT JOIN country c ON t.id_country = c.id_country ORDER BY t.id_title");
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
            String insertTitleSQL = "INSERT INTO titles (id_title, id_type, id_country, name, year, lenght, description) VALUES (DEFAULT,?,?,?,?,?,?)";
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
        String deleteTitleSQL = "DELETE FROM titles t WHERE t.id_title = ?";
        String checkIfExists = "SELECT id_title FROM titles t WHERE t.id_title = ? ORDER BY t.id_title";
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
            String insertTitleSQL = "UPDATE titles t SET name = ?, id_type = ?, year = ?, lenght = ?, id_country = ? WHERE t.id_title = ?";
            String checkIfExists = "SELECT id_title FROM titles t WHERE t.id_title = ? ORDER BY t.id_title";
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
        titleBasicView.setId(rs.getLong("id_title"));
        titleBasicView.setTitle(rs.getString("name"));
        titleBasicView.setType(rs.getString("type_name"));
        titleBasicView.setTypeId(rs.getString("id_type"));
        titleBasicView.setYear(rs.getString("year"));
        titleBasicView.setLenght(rs.getString("lenght"));
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
