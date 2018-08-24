package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class RegisterHandler implements RequestHandler<RegistrationInfo, RegistrationInfoResponse>{
	
	private static final String className = RegisterHandler.class.getName();

    public RegistrationInfoResponse handleRequest(RegistrationInfo request, Context context){
    	
    	LambdaLogger logger = context.getLogger();
        // Write log to CloudWatch using LambdaLogger.
        logger.log(String.format("[%s]: params email-[%s] photoName-[%s] topic-[%s] matchPercent-[%s]", className, request.email, request.photoName, request.topic, request.matchPercent));
        
        S3ImageService s3image = new S3ImageService(context);
        //s3image.listBuckets();
        //s3image.listRegistrationImages();
        //s3image.listKnownImages();
        
        RekognitionService rekognition = new RekognitionService(context);
        String matchedImagePath = rekognition.compareRegistrationImage(request.photoName, request.matchPercent == null ? 70F : request.matchPercent);
        boolean foundImage = false;
        
        if (!"".equals(matchedImagePath)) {
        	logger.log(String.format("[%s.handleRequest]: FOUND [%s] in known users at [%s]", className, request.photoName, matchedImagePath));
        	foundImage = true;
        	
        	
        	// Call Lam's service with matched photo name after last slash
        	
        	// LDN returns some DB ID 
        }
        else {
        	logger.log(String.format("[%s.handleRequest]: DID NOT FIND [%s] in known users", className, request.photoName));
        }
        
        //String greetingString = String.format("Hello %s, %s.", request.firstName, request.lastName);
        return new RegistrationInfoResponse(request.photoName, foundImage ? "found" : "not-found", foundImage ? matchedImagePath : "");
    }
}