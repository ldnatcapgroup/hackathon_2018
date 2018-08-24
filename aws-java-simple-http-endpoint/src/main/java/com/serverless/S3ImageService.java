package com.serverless;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class S3ImageService {
	
	public static String registrationBucketName = "elasticbeanstalk-us-west-2-246183476098";
	public static String registrationBucketFolder = "hackathon-images/";
	public static String knownImagesBucketFolder = registrationBucketFolder + "known-users/";
	public static String registrationImagesBucketFolder = registrationBucketFolder + "registration-users/";
	
	
	private static final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();

	LambdaLogger logger;
	
	public S3ImageService(Context context) {
    	logger = context.getLogger();
	}
	
	public void listBuckets() {		
		List<Bucket> buckets = s3.listBuckets();
		logger.log("Your Amazon S3 buckets are:");
		for (Bucket b : buckets) {
		    logger.log("* " + b.getName() + "\n");
		}
	}
	
	public void listRegistrationImages() {
		final String methodName = "listRegistrationImages"; 
		ListObjectsV2Result result = s3.listObjectsV2(registrationBucketName, registrationImagesBucketFolder);
		List<S3ObjectSummary> objects = result.getObjectSummaries();
		for (S3ObjectSummary os: objects) {
		    logger.log(String.format("[%s]*:  [%s]\n", methodName, os.getKey()));
		}
	}
	
	public List<String> listKnownImages() {
		final String methodName = "listKnownImages";
		
		List<String> knownImagenames = new ArrayList<String>();
		ListObjectsV2Result result = s3.listObjectsV2(registrationBucketName, knownImagesBucketFolder);
		List<S3ObjectSummary> objects = result.getObjectSummaries();
		for (S3ObjectSummary os: objects) {
			if (os.getKey().endsWith(".jpg")) {
				knownImagenames.add(os.getKey());
			}
		    logger.log(String.format("[%s]*:  [%s]\n", methodName, os.getKey()));
		}
		
		return knownImagenames;
	}
	
	public com.amazonaws.services.rekognition.model.S3Object getRegistrationImageObject(String filename) {
		final String methodName = "getRegistrationImageObject";
		com.amazonaws.services.rekognition.model.S3Object imageObject = null;
		
		try {
		    imageObject = new com.amazonaws.services.rekognition.model.S3Object().withName(registrationImagesBucketFolder + filename).withBucket(registrationBucketName);
		} catch (AmazonServiceException e) {
		    logger.log(String.format("[%s]: ERROR: [%s]\n", methodName, e.getErrorMessage()));
		    System.exit(1);
		}/* catch (FileNotFoundException e) {
		    logger.log(String.format("[%s]: ERROR: [%s]", methodName, e.getMessage()));
		    System.exit(1);
		} catch (IOException e) {
		    logger.log(String.format("[%s]: ERROR: [%s]", methodName, e.getMessage()));
		    System.exit(1);
		}*/

	    return imageObject;
	}

	public com.amazonaws.services.rekognition.model.S3Object getKnownUserImageObject(String filename) {
		final String methodName = "getRegistrationImageObject";
		com.amazonaws.services.rekognition.model.S3Object imageObject = null;
		
		try {
		    imageObject = new com.amazonaws.services.rekognition.model.S3Object().withName(knownImagesBucketFolder + filename).withBucket(registrationBucketName);
		} catch (AmazonServiceException e) {
		    logger.log(String.format("[%s]: ERROR: [%s]\n", methodName, e.getErrorMessage()));
		    System.exit(1);
		}/* catch (FileNotFoundException e) {
		    logger.log(String.format("[%s]: ERROR: [%s]", methodName, e.getMessage()));
		    System.exit(1);
		} catch (IOException e) {
		    logger.log(String.format("[%s]: ERROR: [%s]", methodName, e.getMessage()));
		    System.exit(1);
		}*/

	    return imageObject;
	}

	public com.amazonaws.services.rekognition.model.S3Object getBucketImageObject(String filename) {
		final String methodName = "getBucketImageObject";
		com.amazonaws.services.rekognition.model.S3Object imageObject = null;
		
		try {
		    imageObject = new com.amazonaws.services.rekognition.model.S3Object().withName(filename).withBucket(registrationBucketName);
		} catch (AmazonServiceException e) {
		    logger.log(String.format("[%s]: ERROR: [%s]\n", methodName, e.getErrorMessage()));
		    System.exit(1);
		}/* catch (FileNotFoundException e) {
		    logger.log(String.format("[%s]: ERROR: [%s]", methodName, e.getMessage()));
		    System.exit(1);
		} catch (IOException e) {
		    logger.log(String.format("[%s]: ERROR: [%s]", methodName, e.getMessage()));
		    System.exit(1);
		}*/

	    return imageObject;
	}

}
