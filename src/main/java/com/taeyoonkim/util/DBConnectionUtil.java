package com.taeyoonkim.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class DBConnectionUtil {
    // 환경 변수(dotenv) 로드
    private static final Dotenv dotenv = Dotenv.load();

    // MariaDB 컨테이너 접속 정보
    private static final String URL = "jdbc:mariadb://localhost:3306/sportscenter_db";
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    /**
     * 데이터베이스 커넥션을 반환합니다.
     */
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
