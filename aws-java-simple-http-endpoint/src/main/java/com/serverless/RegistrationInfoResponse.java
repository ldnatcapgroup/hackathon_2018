package com.serverless;

public class RegistrationInfoResponse {
	
	public String id;
	
	public String status;
	
	public String knownPhoto;
	
	public RegistrationInfoResponse() {
		
	}	

	public RegistrationInfoResponse(String id, String status, String knownPhoto) {
		super();
		this.id = id;
		this.status = status;
		this.knownPhoto = knownPhoto;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getKnownPhoto() {
		return knownPhoto;
	}

	public void setKnownPhoto(String knownPhoto) {
		this.knownPhoto = knownPhoto;
	}
	
	

}
