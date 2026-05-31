package com.taeyoonkim.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionUtil {
    // MariaDB 컨테이너 접속 정보
    private static final String URL = "jdbc:mariadb://localhost:3306/sportscenter_db";
    private static final String USER = "root";
    private static final String PASSWORD = "5058";

    /**
     * 데이터베이스 커넥션을 반환합니다.
     */
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
