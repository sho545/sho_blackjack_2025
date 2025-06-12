package user;

public class User {
	
	private int userId ;
	private String userName ;
	private String password ;
	private int numberOfGames ;
	private int victories ;
	private int chips ;
	
	//getter,setter
	
	public int getChips() {
		return chips;
	}

	public void setChips(int chips) {
		this.chips = chips;
	}

	public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    

    
    public int getNumberOfGames() {
        return numberOfGames;
    }

    public void setNumberOfGames(int numbberOfGames) {
        this.numberOfGames = numbberOfGames;
    }

    
    
    public int getVictories() {
        return victories;
    }

    public void setVictories(int victories) {
        this.victories = victories;
    }


}
