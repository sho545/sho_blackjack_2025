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
			if(conn != null) {
				conn.setAutoCommit(true);
			}
			closeResources(pstmt);
			closeResources(pstmtResults, conn) ;
		}
		return result ;
	}
	
	//resultsテーブルから試合数が0でない勝率上位(最大)5人を取得(勝率が同じときは試合数が多いほうが上、それも同じならid順)
	//試合数0でないユーザーがいないときは空のListを返す
	public List<User> getRanking5() throws SQLException{
		
		Connection conn = null ;
		PreparedStatement pstmt = null ;
		ResultSet rs = null ;
		List<User> ranks = new ArrayList<>() ;
		String sql = "select r.user_id, r.number_of_games, r.victories, u.user_name"
				   + " from results r join users u on r.user_id = u.user_id"
				   + " where r.number_of_games > 0" 
				   + " order by (cast (r.victories as double) / r.number_of_games ) DESC,"
				   + " r.number_of_games DESC, r.user_id ASC"
				   + " limit 5" ;		
		try {
			conn = getConnection() ;
			pstmt = conn.prepareStatement(sql) ;
			rs = pstmt.executeQuery() ;
			
			while(rs.next()) {
				User user = new User() ;
				user.setUserId(rs.getInt("user_id")) ;
				user.setNumberOfGames(rs.getInt("number_of_games")) ;
				user.setVictories(rs.getInt("victories")) ;
				user.setUserName(rs.getString("user_name")) ;
				
				ranks.add(user) ;
			}
		}catch(SQLException e) {
			System.err.println("ランキングを取得中にエラーが起きました " + e.getMessage());
		    e.printStackTrace(); 
		    throw e ;
		}finally {
			closeResources(rs,pstmt,conn);
		}
		return ranks ;	
	}
	
	//ユーザー削除(usersテーブルから削除しますon delete cascadeによってresultsも削除されます)
	public boolean deleteUser(int userId) throws SQLException{
		
		Connection conn = null ;
		PreparedStatement pstmt = null ;
		String sql = "delete from users where user_id = ?" ;
		boolean result = false ;

		try {
			conn = getConnection() ;
			
			conn.setAutoCommit(false) ;
			pstmt = conn.prepareStatement(sql) ;
			pstmt.setInt(1, userId);
			int affectedRows = pstmt.executeUpdate() ;
			
			if(affectedRows > 0) {
				result = true ;
				conn.commit();
			}else {
				conn.rollback() ;
				System.err.println("ユーザーの削除ができませんでした。ロールバックします");
			}
		}catch(SQLException e) {
			try {
				if(conn != null) {
					conn.rollback();
					System.err.println("SQLExceptionが発生したのでロールバックします");
				}
			}catch(SQLException ex) {
				System.err.println("ロールバック中にエラーが発生しました: " + ex.getMessage());
                ex.printStackTrace();
			}
			System.err.println("ユーザー削除中にエラーが起きました " + e.getMessage());
		    e.printStackTrace(); 
		    throw e ;
		}finally {
			try {
				if(conn != null) {
					conn.setAutoCommit(true);
				}
			}catch(SQLException ey){
				 System.err.println("AutoCommit状態の復元中にエラーが発生しました: " + ey.getMessage());
	             ey.printStackTrace();
			}
			closeResources(pstmt,conn);
		}
		return result ;	
	}
	
}
