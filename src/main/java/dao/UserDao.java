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
		String sqlFindResult = "select r.number_of_games, r.victories, c.chips from"
				             + " results as r inner join chips as c on r.user_id = c.user_id"
				             + " where r.user_id = ?" ;
		
		User user = null ;
		//ユーザーが見つからなかったときはuserはnullのまま
		
		try {
			
			conn = getConnection() ;
			//useNameとpasswordでuserId取得
			pstmt = conn.prepareStatement(sqlFindId) ;
			pstmt.setString(1,name) ;
			pstmt.setString(2, pass);
			rs = pstmt.executeQuery() ;
			//userIdが見つかったときの処理
			if(rs.next()) {
				user = new User() ;
				int userId = rs.getInt("user_id") ;
				user.setUserId(userId) ;
				user.setUserName(name);
				user.setPassword(pass);
			
				//取得したuserIdからresults,chipsを取得してセット
				pstmt1 = conn.prepareStatement(sqlFindResult) ;
				pstmt1.setInt(1, userId);
				rs1 = pstmt1.executeQuery() ;
				if(rs1.next()) {
					user.setNumberOfGames(rs1.getInt("number_of_games"));
					user.setVictories(rs1.getInt("victories"));
					user.setChips(rs1.getInt("chips"));	
				}else {
					//result,chipsのデータがないときはそれぞれ初期値を代入
					user.setNumberOfGames(0);
					user.setVictories(0);
					user.setChips(100);
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
	
	//userNameとPasswordで新規登録
	//このメソッドの終了時にautoCommitを強制的にtrueにしているので、
	//トランザクション中にこのメソッドを呼び出す際は注意してください
	public boolean registerUser(String userName, String password) throws SQLException{
		Connection conn = null ;
		PreparedStatement pstmt = null ;
		PreparedStatement pstmtResults = null ;
		PreparedStatement pstmtChips = null ;
		ResultSet generatedKeys = null ;
		
		boolean result = false ;
		
		String sql = "insert into users (user_name, password) values (?, ?)" ;
		String sqlResults = "insert into results (user_id, number_of_games, victories) values (?, ?, ?)" ;
		String sqlChips = "insert into chips (user_id, chips) values (?, ?)" ;
		
		try {
			conn = getConnection() ;
			
			//トランザクション開始
			conn.setAutoCommit(false);
			//第二引数は生成されたキーを後で取り出せるように準備の意
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, userName);
			pstmt.setString(2, password);
			
			int affectedRows = pstmt.executeUpdate();
			//usersに挿入できた時の処理
            if (affectedRows > 0) {
               generatedKeys = pstmt.getGeneratedKeys() ;
               if(generatedKeys.next()) {
            	   int newUserId = generatedKeys.getInt(1) ; 
            	   //pstmtに自動採番されたuserIdと初期値セット
            	   pstmtResults = conn.prepareStatement(sqlResults) ;
       			   pstmtResults.setInt(1, newUserId);
       			   pstmtResults.setInt(2, 0);
       			   pstmtResults.setInt(3, 0) ;
       			   //pstmtに自動採番されたuserIdと初期値セット
       			   pstmtChips = conn.prepareStatement(sqlChips) ;
       			   pstmtChips.setInt(1, newUserId);
       			   pstmtChips.setInt(2, 100);
       			   
       			   int affectedRowsResults = pstmtResults.executeUpdate();
       			   int affectedRowsChips = pstmtChips.executeUpdate() ;
       			   
       			   if(affectedRowsResults > 0 && affectedRowsChips > 0) {
       				   result = true ;
       			   }
               }
            }
            //全ての処理が完了したらcommit
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
			//autoCommitを自動に(今回は上位メソッドでトランザクションを行っていないためこの記述)
			if(conn != null) {
				conn.setAutoCommit(true);
			}
			closeResources(pstmt);
			closeResources(pstmtResults) ;
			closeResources(pstmtChips, conn) ;
		}
		return result ;
	}
	
	//受け取ったuserIdから戦績と名前をとってくる
	public User getRecord(int userId) throws SQLException{
		
		Connection conn = null ;
		PreparedStatement pstmt = null ;
		ResultSet rs = null ;
		User user = new User() ;
		String sql = "select r.number_of_games, r.victories, u.user_name"
				   + " from results r join users u on r.user_id = u.user_id"
				   + " where u.user_id = ?" ;
		try {
			conn = getConnection() ;
			pstmt = conn.prepareStatement(sql) ;
			pstmt.setInt(1, userId) ;
			rs = pstmt.executeQuery() ;
			
			while(rs.next()) {
				user.setNumberOfGames(rs.getInt("number_of_games")) ;
				user.setVictories(rs.getInt("victories")) ;
				user.setUserName(rs.getString("user_name")) ;
			}
		}catch(SQLException e) {
			System.err.println("データ取得中にエラーが起きました " + e.getMessage());
		    e.printStackTrace(); 
		    throw e ;
		}finally {
			closeResources(rs,pstmt,conn);
		}
		return user ;	
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
	
	//ユーザー削除(usersテーブルから削除しますon delete cascadeによってresults,chipsも削除されます)
	//このメソッドの終了時にautoCommitを強制的にtrueにしているので、
	//トランザクション中にこのメソッドを呼び出す際は注意してください
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
	
	//受け取ったuserIdのvictoriesを+1する(number_of_gamesも+1)
	public boolean victoriesPlus1(int userId) throws SQLException{
		
		Connection conn = null ;
		PreparedStatement pstmt = null ;
		String sql = "update results set victories = victories + 1, number_of_games = number_of_games + 1"
				   + " where user_id = ?" ;
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
				System.err.println("戦績の変更ができませんでした。ロールバックします");
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
			System.err.println("戦績変更中にエラーが起きました " + e.getMessage());
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
	
	//受け取ったuserIdのnumber_of_gamesだけ+1
	public boolean numberOfGamesPlus1(int userId) throws SQLException{
		
		Connection conn = null ;
		PreparedStatement pstmt = null ;
		String sql = "update results set number_of_games = number_of_games + 1"
				   + " where user_id = ?" ;
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
				System.err.println("戦績の変更ができませんでした。ロールバックします");
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
			System.err.println("戦績変更中にエラーが起きました " + e.getMessage());
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
	
	//chipの枚数を更新するメソッド
	//ゲーム終了時に呼び出されます
	//autocommitは最後にtrueにしています
	public boolean updateChips(User user) throws SQLException{
		
		Connection conn = null ;
		PreparedStatement pstmt = null ;
		String sql = "update chips set chips = ? where user_id = ?" ;
		boolean result = false ;
		
		int userId = user.getUserId() ;
		int chips = user.getChips() ;
		
		try {
			conn = getConnection() ;
			//トランザクション開始
			conn.setAutoCommit(false) ;
			pstmt = conn.prepareStatement(sql) ;
			pstmt.setInt(1, chips);
			pstmt.setInt(2, userId);
			int affectedRows = pstmt.executeUpdate() ;
			//上手く実行できた時commit
			if(affectedRows > 0) {
				result = true ;
				conn.commit();
			//実行がうまくいかなかったらロールバック
			}else {
				conn.rollback() ;
				System.err.println("戦績の変更ができませんでした。ロールバックします");
			}
		//SQLExceptionが起こったときもロールバック
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
			System.err.println("戦績変更中にエラーが起きました " + e.getMessage());
		    e.printStackTrace(); 
		    throw e ;
		//autoCommitをtrueに
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
	
	//chipssテーブルから上位(最大)5人を取得(chip枚数が同じときはゲーム数が高いほうが上、それも同じならid順)
	//試合数0でないユーザーがいないときは空のListを返す
	public List<User> getChipRanking5() throws SQLException{
		
		Connection conn = null ;
		PreparedStatement pstmt = null ;
		ResultSet rs = null ;
		List<User> ranks = new ArrayList<>() ;
		String sql = "select u.user_id, u.user_name, c.chips, r.number_of_games, r.victories "
				   + " from users as u inner join chips as c on u.user_id = c.user_id "
				   + " inner join results as r on u.user_id = r.user_id "
				   + " order by c.chips DESC,"
				   + " r.number_of_games DESC, r.user_id ASC"
				   + " limit 5" ;		
		try {
			conn = getConnection() ;
			pstmt = conn.prepareStatement(sql) ;
			rs = pstmt.executeQuery() ;
			
			while(rs.next()) {
				User user = new User() ;
				user.setUserId(rs.getInt("user_id")) ;
				user.setChips(rs.getInt("chips")) ;
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
	
}
