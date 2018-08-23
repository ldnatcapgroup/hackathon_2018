package com.serverless;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import java.util.List;

public class S3Image {
	
	public void listBuckets(Context context) {
		
    	LambdaLogger logger = context.getLogger();
        // Write log to CloudWatch using LambdaLogger.
		
		final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
		List<Bucket> buckets = s3.listBuckets();
		logger.log("Your Amazon S3 buckets are:");
		for (Bucket b : buckets) {
		    logger.log("* " + b.getName());
		}
	}
}
