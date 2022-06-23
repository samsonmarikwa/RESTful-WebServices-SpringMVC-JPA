package com.samsonmarikwa.appws.shared;

import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.samsonmarikwa.appws.shared.dto.UserDto;

public class AmazonSES {
	
	@Value("${aws.accessKeyId}")
	private String accessKeyId;
	
	@Value("${aws.secretKey}")
	private String secretKey;
	
	// This address must be verified with Amazon SES
	final String FROM = "sammari4249@gmail.com";

	// The subject line for the email
	final String SUBJECT = "One last step to complete your registration with PhotoApp";

	// The body for the email
	final String HTMLBODY = "<h1>Please verify your e-mail address</h1>"
			+ "<p>Thank you for registering with our mobile app. To complete registration process and be able to log in"
			+ " click on the following link: "
			+ "<a href='http://ec2-3-83-147-140.compute-1.amazonaws.com:8080/verification-service/email-verification.html?token=$tokenValue'>"
			+ "Final step to complete your registration" + "</a><br/><br/>"
			+ "Thank you! And we are waiting for you inside!</p>";

	// The email body for recipients with non-HTML email clients
	final String TEXTBODY = "Please verify your e-mail address. "
			+ "Thank you for registering with our mobile app. To complete registration process and be able to log in"
			+ " open the following URL in your browser window: "
			+ "href=http://ec2-3-83-147-140.compute-1.amazonaws.com:8080/verification-service/email-verification.html?token=$tokenValue"
			+ "Thank you! And we are waiting for you inside!";

	public void verifyEmail(UserDto userDto) {

		System.setProperty("aws.accessKeyId", accessKeyId); 	
		System.setProperty("aws.secretKey", secretKey); 	

		AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

		String htmlBodyWithToken = HTMLBODY.replace("$tokenValue", userDto.getEmailVerificationToken());
		String textBodyWithToken = TEXTBODY.replace("$tokenValue", userDto.getEmailVerificationToken());

		SendEmailRequest request = new SendEmailRequest()
				.withDestination(new Destination().withToAddresses(userDto.getEmail()))
				.withMessage(new Message().withBody(new Body()
						.withHtml(new Content().withCharset("UTF-8").withData(htmlBodyWithToken))
						.withText(new Content().withCharset("UTF-8").withData(textBodyWithToken)))
						.withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
				.withSource(FROM);

		client.sendEmail(request);
	}
}
