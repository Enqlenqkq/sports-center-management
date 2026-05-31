package com.taeyoonkim;

import java.sql.*;

import com.taeyoonkim.util.DBConnectionUtil;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = DBConnectionUtil.getConnection()) {
            System.out.println(conn + " --> 연결 성공");
        } catch (SQLException e) {
            System.out.println("Error: SQL Exception -> " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Class Not Found Exception");
        }
    }
}