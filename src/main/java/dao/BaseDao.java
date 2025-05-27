package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseDao {
	
	private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver" ;
	private static final String DB_URL = "jdbc:mariadb://localhost:3306/blackjack" ;
	private static final String DB_USER = "root" ;
	private static final String DB_PASSWORD = "null" ;
	
	static {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(System.err);
            throw new RuntimeException("JDBCドライバのロードに失敗しました。根本原因: " + e.getMessage(), e);
        }
    }
	
	//DB接続
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	}
	
	//DB切断
	public static void closeResources(ResultSet rs, PreparedStatement pstmt, Connection conn) {
		
		if(rs != null) {
			try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("ResultSetのクローズに失敗しました: " + e.getMessage());
            }
        }
		if(pstmt != null) {
			try {
				pstmt.close();
			}catch(SQLException e) {
				System.err.println("PreparedStatementのクローズに失敗しました: " + e.getMessage());
			}
		}
		if(conn != null) {
			try {
				conn.close();
			}catch(SQLException e) {
				System.err.println("Connectionのクローズに失敗しました: " + e.getMessage());
			}
		}
	}
	public static void closeResources(ResultSet rs, PreparedStatement pstmt) {
        closeResources(rs, pstmt, null); 
    }
	public static void closeResources(PreparedStatement pstmt, Connection conn) {
        closeResources(pstmt, conn); 
    }
	public static void closeResources(Connection conn) {
        closeResources(null, null, conn); 
    }
}
