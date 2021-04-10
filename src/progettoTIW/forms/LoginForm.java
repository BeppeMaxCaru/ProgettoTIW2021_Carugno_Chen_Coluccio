package progettoTIW.forms;

public class LoginForm {
	
	private String username;
	private String password;
	private String usernameError;
	private String passwordError;
	
	public LoginForm() {
		super();
	}
	
	public LoginForm(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
		if (username == null || username.isEmpty()) {
			this.usernameError = "Username required";
		} else {
			this.usernameError = null;
		}
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
		if (password == null || password.isEmpty()) {
			this.passwordError = "Password required";
		} else {
			this.passwordError = null;
		}
	}
	
	public String getUsernameError() {
		return this.usernameError;
	}
	
	public String getPasswordError() {
		return this.passwordError;
	}
	
	public boolean isValid() {
		return this.usernameError == null && this.passwordError == null;
	}
	
}
