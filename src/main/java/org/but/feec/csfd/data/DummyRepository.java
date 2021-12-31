package org.but.feec.csfd.data;

import org.but.feec.csfd.api.dummy.*;
import org.but.feec.csfd.api.title.TitleBasicView;
import org.but.feec.csfd.config.DataSourceConfig;
import org.but.feec.csfd.exception.DataAccessException;
import org.but.feec.csfd.exception.ExceptionHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DummyRepository {

    public List<DummyBasicView> getDummyBasicView() {
        String selectQuerySQL = "SELECT string FROM public.dummy_table ORDER BY string;";
        try (Connection connection = DataSourceConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuerySQL);) {
            List<DummyBasicView> dummyBasicViews = new ArrayList<>();
            while (resultSet.next()) {
                dummyBasicViews.add(mapToDummyBasicView(resultSet));
            }
            return dummyBasicViews;
        } catch (SQLException e) {
            throw new DataAccessException("Strings basic view could not be loaded.", e);
        }
    }

    public void createString(DummyBasicView dummyBasicView) {
        String string = dummyBasicView.getString();
        String insertQuerySQL = "INSERT INTO public.dummy_table (string) VALUES('" + string + "');";

        try(Connection connection = DataSourceConfig.getConnection()){
            Statement statement = connection.createStatement();
            statement.executeUpdate(insertQuerySQL);

        }catch (SQLException e){
            throw new DataAccessException("Strings create exception. ", e);
        }
    }

    public void addNdelString(DummyBasicView dummyBasicView) {
        String string = dummyBasicView.getString();
        String insertQuerySQL = "INSERT INTO public.dummy_table (string) VALUES('" + string + "');";
        String deleteQuerySQL = "DELETE FROM public.dummy_table" +
                " WHERE string IN (" +
                " SELECT string" +
                " FROM dummy_table" +
                " ORDER BY string ASC" +
                " LIMIT 1)";

        try(Connection connection = DataSourceConfig.getConnection()){
            try{
                connection.setAutoCommit(false);
                Statement insertStatement = connection.createStatement();
                insertStatement.executeUpdate(insertQuerySQL);
                Statement deleteStatement = connection.createStatement();
                deleteStatement.executeUpdate(deleteQuerySQL);

                connection.commit();
            }catch (SQLException e){
                connection.rollback();
                throw new DataAccessException("SQL Exception query exception.", e);
            }finally {
                connection.setAutoCommit(true);
            }
        }catch (SQLException e){
            throw new DataAccessException("Add and delete connection failed.", e);
        }
    }

    public List<DummyBasicView> getDummyFindView(String find) {
        String selectSQL = "SELECT string" +
                " FROM public.dummy_table" +
                " WHERE string" +
                " LIKE '%" + find + "%';";

        try (Connection connection = DataSourceConfig.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectSQL);

            List<DummyBasicView> dummyBasicViews = new ArrayList<>();
            while (resultSet.next()) {
                dummyBasicViews.add(mapToDummyBasicView(resultSet));
            }
            return dummyBasicViews;

        } catch (SQLException e) {
            throw new DataAccessException("Find dummy by ID with addresses failed.", e);
        }
    }

    private DummyBasicView mapToDummyBasicView(ResultSet rs) throws SQLException {
        DummyBasicView dummyBasicView = new DummyBasicView();
        dummyBasicView.setString(rs.getString("string"));
        return dummyBasicView;
    }
}
