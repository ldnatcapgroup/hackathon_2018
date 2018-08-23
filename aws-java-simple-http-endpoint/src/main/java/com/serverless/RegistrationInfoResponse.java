package com.serverless;

public class RegistrationInfoResponse {
	
	public String id;
	
	public String status;
	
	public RegistrationInfoResponse() {
		
	}	

	public RegistrationInfoResponse(String id, String status) {
		super();
		this.id = id;
		this.status = status;
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
	
	

}
