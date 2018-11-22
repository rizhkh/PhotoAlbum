package app;

import java.io.Serializable;

/**
 * Stores the user login Info
 * @author Rizwan Khan(mrk150) && Ahmed Ghoneim(asg179)
 *
 */
public class LoginInfo implements Serializable {
	String userName;
	String passWord;
	
	/**
	 * Constructor
	 * @param userName The username
	 * @param passWord The password
	 */
	public LoginInfo(String userName, String passWord){
		this.userName = userName;
		this.passWord = passWord;
	}
	
	/**
	 * Default constructor if needed
	 */
	public LoginInfo(){
		this.userName = null;
		this.passWord = null;
	}
	
	/**
	 * Sets the user name
	 * @param user The username to be set
	 */
	public void setUser(String user){
		this.userName = user;
	}
	
	/**
	 * Sets the password
	 * @param pass The password to be set
	 */
	public void setPass(String pass){
		this.passWord = pass;
	}
	
	/**
	 * Gets the username
	 * @return The username to be returned
	 */
	public String getUser(){
		return this.userName;
	}
	
	/**
	 * Gets the password
	 * @return The password to be returned
	 */
	public String getPass(){
		return this.passWord;
	}
	
	/**
	 * Overrides default toString method to return the userName only
	 */
	@Override
	public String toString(){
		return getUser();
	}
}
