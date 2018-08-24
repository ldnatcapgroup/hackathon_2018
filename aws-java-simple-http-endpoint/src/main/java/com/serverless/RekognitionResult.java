package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.s3.model.S3Object;

public class RekognitionResult {
	
	LambdaLogger logger;
	
	public boolean foundMatch = false;
	
	public S3Object registrationUser;
	public S3Object knownUser;
	
	public RekognitionResult(Context context) {
    	logger = context.getLogger();
	}

	public LambdaLogger getLogger() {
		return logger;
	}

	public void setLogger(LambdaLogger logger) {
		this.logger = logger;
	}

	public boolean isFoundMatch() {
		return foundMatch;
	}

	public void setFoundMatch(boolean foundMatch) {
		this.foundMatch = foundMatch;
	}

	public S3Object getRegistrationUser() {
		return registrationUser;
	}

	public void setRegistrationUser(S3Object registrationUser) {
		this.registrationUser = registrationUser;
	}

	public S3Object getKnownUser() {
		return knownUser;
	}

	public void setKnownUser(S3Object knownUser) {
		this.knownUser = knownUser;
	}

	

}
