package com.serverless;

public class RegistrationInfo {
	
	public String email;
	
	public String photoName;
	
	public RegistrationInfo() {
		
	}

	public RegistrationInfo(String email, String photoName) {
		super();
		this.email = email;
		this.photoName = photoName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}
	
	

}
