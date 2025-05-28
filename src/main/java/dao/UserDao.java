package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
		PreparedStatement pstmtResults = null ;
		ResultSet generatedKeys = null ;
		
		boolean result = false ;
		
		String sql = "insert into users (user_name, password) values (?, ?)" ;
		String sqlResults = "insert into results (user_id, number_of_games, victories) values (?, ?, ?) " ;
		try {
			conn = getConnection() ;
			//トランザクション開始
			conn.setAutoCommit(false);
			//第二引数は生成されたキーを後で取り出せるように準備の意
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, userName);
			pstmt.setString(2, password);
			
			int affectedRows = pstmt.executeUpdate();
			
            if (affectedRows > 0) {
               generatedKeys = pstmt.getGeneratedKeys() ;
               if(generatedKeys.next()) {
            	   int newUserId = generatedKeys.getInt(1) ; 
            	   
            	   pstmtResults = conn.prepareStatement(sqlResults) ;
       			   pstmtResults.setInt(1, newUserId);
       			   pstmtResults.setInt(2, 0);
       			   pstmtResults.setInt(3, 0) ;
       			   
       			   int affectedRowsResults = pstmtResults.executeUpdate();
       			   if(affectedRowsResults > 0) {
       				   result = true ;
       			   }
               }
            }
            if(result) {
            	conn.commit();
            }else {
            	conn.rollback();
            	System.err.println("新規登録処理のいずれかのステップで失敗しました。ロールバックします。");
            }
            
		}catch(SQLException e){
			if (conn != null) {
                try {
                    conn.rollback(); 
                    System.err.println("SQLException発生のためロールバックしました: " + e.getMessage());
                } catch (SQLException ex) {
                    System.err.println("ロールバック中にエラーが発生しました: " + ex.getMessage());
                    ex.printStackTrace();
                }
			}
			System.err.println("新規登録中にエラーが発生しました " + e.getMessage());
		    e.printStackTrace(); 
		    throw e ;
		}finally {
			closeResources(pstmt);
			closeResources(pstmtResults, conn) ;
		}
		return result ;
	}
	
	//戦績をとってくる
	public List<Integer> getRecord(User user) throws SQLException{
		Connection conn = null ;
		PreparedStatement pstmt = null ;
		ResultSet rs = null ;
		
		List<Integer> record = new ArrayList<>() ;
		
		String sql = "select from results where user_id = ?" ;
		try {
			conn = getConnection() ;

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user.getUserId());
			
			rs = pstmt.executeQuery() ;
			
            if (rs.next()) {
               record.add(rs.getInt("number_of_games")) ;
               record.add(rs.getInt("victories")) ;
            }          
		}catch(SQLException e){
			System.err.println("データ取得中にエラーが発生しました " + e.getMessage());
		    e.printStackTrace(); 
		    throw e ;
		}finally {
			closeResources(rs, pstmt, conn) ;
		}
		return record ;
	}

}
