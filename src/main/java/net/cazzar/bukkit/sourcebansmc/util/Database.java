/*
 * {one line to give the program's name and a brief idea of what it does
 * Copyright (C) 2013 cazzar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see [http://www.gnu.org/licenses/].
 */

package net.cazzar.bukkit.sourcebansmc.util;

import java.sql.*;

public class Database {

    private Connection conn = null;
    private Statement statement;

    private String db_host;
    private String db_port;

    private String db_database;
    private String db_user;
    private String db_pass;

    public Database(String host, String port, String db, String user, String pass) {
        db_host = host;
        db_port = port;

        db_database = db;
        db_user = user;
        db_pass = pass;
    }


    public Connection getConnection() {
        if (conn == null) {
            return open();
        }

        return conn;
    }

    public Connection open() {
        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String url = "jdbc:mysql://" + db_host + ":" + db_port + "/" + db_database;

            conn = DriverManager.getConnection(url, db_user, db_pass);

            return conn;
        } catch (Exception e) {
            LogHelper.warning(e.getMessage());
        }
        return null;
    }

    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                LogHelper.warning(e.getMessage());
            }
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isClosed() {
        try {

            return conn.isClosed();

        } catch (SQLException e) {
            return true;
        }
    }

    public ResultSet query(String query) {
        try {
            statement = conn.createStatement();
            return statement.executeQuery(query);

        } catch (Exception e) {
            if (!e.getMessage().contains("not return ResultSet") || (e.getMessage().contains("not return ResultSet") && query.startsWith("SELECT"))) {
                LogHelper.warning(e.getMessage());
            }
        }
        return null;
    }

    public boolean update(String query) {
        try {
            statement = conn.createStatement();
            statement.executeUpdate(query);

            return true;

        } catch (Exception e) {

            LogHelper.warning(e.getMessage());
            return false;
        }
    }


}
