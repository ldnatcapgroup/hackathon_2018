package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class RegisterHandler implements RequestHandler<RegistrationInfo, RegistrationInfoResponse>{
	
	private static final String className = RegisterHandler.class.getName();

    public RegistrationInfoResponse handleRequest(RegistrationInfo request, Context context){
    	
    	LambdaLogger logger = context.getLogger();
        // Write log to CloudWatch using LambdaLogger.
        logger.log(String.format("[%s]: params email-[%s] photoName-[%s]", className, request.email, request.photoName));
        
        S3Image s3image = new S3Image();
        s3image.listBuckets(context);
        
        //String greetingString = String.format("Hello %s, %s.", request.firstName, request.lastName);
        return new RegistrationInfoResponse("someid", "new");
    }
}