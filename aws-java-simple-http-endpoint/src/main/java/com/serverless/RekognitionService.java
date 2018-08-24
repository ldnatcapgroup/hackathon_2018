package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.rekognition.model.BoundingBox;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.ComparedFace;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import com.amazonaws.util.IOUtils;

public class RekognitionService {
	
	private static final String className = RekognitionService.class.getName();
	
	LambdaLogger logger;
	
	private S3ImageService s3ImageService;
	
	public RekognitionService(Context context) {
    	logger = context.getLogger();
    	s3ImageService = new S3ImageService(context);
	}

    /*public RekognitionResult compareRegistrationImage(String bucket, String imageName) throws Exception {
       Float similarityThreshold = 70F;
       String sourceImage = "source.jpg";
       String targetImage = "target.jpg";
       ByteBuffer sourceImageBytes=null;
       ByteBuffer targetImageBytes=null;

       AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.defaultClient();

       //Load source and target images and create input parameters
       try (InputStream inputStream = new FileInputStream(new File(sourceImage))) {
          sourceImageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
       }
       catch(Exception e)
       {
           System.out.println("Failed to load source image " + sourceImage);
           System.exit(1);
       }
       try (InputStream inputStream = new FileInputStream(new File(targetImage))) {
           targetImageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
       }
       catch(Exception e)
       {
           System.out.println("Failed to load target images: " + targetImage);
           System.exit(1);
       }

       Image source=new Image()
            .withBytes(sourceImageBytes);
       Image target=new Image()
            .withBytes(targetImageBytes);

       CompareFacesRequest request = new CompareFacesRequest()
               .withSourceImage(source)
               .withTargetImage(target)
               .withSimilarityThreshold(similarityThreshold);
       
       // Call operation
       CompareFacesResult compareFacesResult=rekognitionClient.compareFaces(request);


       // Display results
       List <CompareFacesMatch> faceDetails = compareFacesResult.getFaceMatches();
       for (CompareFacesMatch match: faceDetails){
         ComparedFace face= match.getFace();
         BoundingBox position = face.getBoundingBox();
         System.out.println("Face at " + position.getLeft().toString()
               + " " + position.getTop()
               + " matches with " + face.getConfidence().toString()
               + "% confidence.");

       }
       List<ComparedFace> uncompared = compareFacesResult.getUnmatchedFaces();

       System.out.println("There was " + uncompared.size()
            + " face(s) that did not match");
       System.out.println("Source image rotation: " + compareFacesResult.getSourceImageOrientationCorrection());
       System.out.println("target image rotation: " + compareFacesResult.getTargetImageOrientationCorrection());
   }*/
    
   public String compareRegistrationImage(String registrationFilename, Float matchAccuracy ) {
	   final String methodName = "compareS3Image";
	   
	   String foundImage = "";
	   
       Float similarityThreshold = matchAccuracy;
       AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.defaultClient();
	   
	   S3Object source = s3ImageService.getRegistrationImageObject(registrationFilename);
	   
	   for (String filename : s3ImageService.listKnownImages()) {
		   S3Object knownImage = s3ImageService.getBucketImageObject(filename);
		   logger.log(String.format("[%s.%s]: comparing [%s] to [%s] using similarity percent [%s]\n", className, methodName, source.getName(), knownImage.getName(), similarityThreshold));
		   
	       CompareFacesRequest request = new CompareFacesRequest()
	               .withSourceImage(new Image().withS3Object(source))
	               .withTargetImage(new Image().withS3Object(knownImage))
	               .withSimilarityThreshold(similarityThreshold);
	       
	       CompareFacesResult compareFacesResult = rekognitionClient.compareFaces(request);
	       
	       List <CompareFacesMatch> faceDetails = compareFacesResult.getFaceMatches();
	       for (CompareFacesMatch match: faceDetails){
	         ComparedFace face= match.getFace();
	         BoundingBox position = face.getBoundingBox();
	         logger.log(String.format("[%s.%s]: Face at " + position.getLeft().toString()
	               + " " + position.getTop()
	               + " matches with " + face.getConfidence().toString()
	               + "percent confidence.\n", className, methodName));
	         foundImage = filename;
	       }
	       List<ComparedFace> uncompared = compareFacesResult.getUnmatchedFaces();

	       logger.log(String.format("[%s.%s]: There was " + uncompared.size()
	            + " face(s) that did not match\n[%s]\n[%s]\n",className, methodName,
	            "Source image rotation: " + compareFacesResult.getSourceImageOrientationCorrection(), 
	            "target image rotation: " + compareFacesResult.getTargetImageOrientationCorrection()));
	       
		   logger.log(String.format("[%s.%s]: result of comparision [%s]\n", className, methodName, request));
	   }
	   
	   return foundImage;
   }
}
