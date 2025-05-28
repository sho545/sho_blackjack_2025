package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import user.User;

public class UserDao extends BaseDao {
	
	//userNameとPasswordからユーザーを見つけてくる
	public User findUser(String name, String pass) throws SQLException {
		
		Connection conn = null ;
		PreparedStatement pstmt = null ;
		PreparedStatement pstmt1 = null ;
		ResultSet rs = null ;
		ResultSet rs1 = null ;
		
		String sqlFindId = "select user_id from users where user_name = ? and password = ?" ;
		String sqlFindResult = "select number_of_games, victories from results where user_id = ?" ;
		
		User user = null ;
		//ユーザーが見つからなかったときはuserはnullのまま
		
		try {
			conn = getConnection() ;
			
			pstmt = conn.prepareStatement(sqlFindId) ;
			pstmt.setString(1,name) ;
			pstmt.setString(2, pass);
			rs = pstmt.executeQuery() ;
			
			if(rs.next()) {
				user = new User() ;
				int userId = rs.getInt("user_id") ;
				user.setUserId(userId) ;
				user.setUserName(name);
				user.setPassword(pass);
			
				
				pstmt1 = conn.prepareStatement(sqlFindResult) ;
				pstmt1.setInt(1, userId);
				rs1 = pstmt1.executeQuery() ;
				
				if(rs1.next()) {
					user.setNumberOfGames(rs1.getInt("number_of_games"));
					user.setVictories(rs1.getInt("victories"));
				}else {
					user.setNumberOfGames(0);
					user.setVictories(0);
				}
			}
			
		}catch(SQLException e) {
			 System.err.println("ユーザーの取得中にエラーが発生しました " + e.getMessage());
		     e.printStackTrace(); 
		     throw e ;
		}finally {
			closeResources(rs, pstmt);
			closeResources(rs1, pstmt1, conn);
		}
		return user ;
	}
	
	//新規登録
	public boolean registerUser(String userName, String password) throws SQLException{
		Connection conn = null ;
		PreparedStatement pstmt = null ;
		
		boolean result = false ;
		
		String sql = "insert into users (user_name, password) values (?, ?)" ;
		try {
			conn = getConnection() ;
			
			pstmt = conn.prepareStatement(sql) ;
			pstmt.setString(1, userName);
			pstmt.setString(2, password);
			
			int affectedRows = pstmt.executeUpdate();
			
            if (affectedRows > 0) {
                result = true;
            }
		}catch(SQLException e){
			System.err.println("新規登録中にエラーが発生しました " + e.getMessage());
		     e.printStackTrace(); 
		     throw e ;
		}finally {
			closeResources(pstmt,conn);
		}
		return result ;
	}

}
