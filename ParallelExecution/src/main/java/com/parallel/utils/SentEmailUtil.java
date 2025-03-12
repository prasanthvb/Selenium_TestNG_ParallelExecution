package com.parallel.utils;

import static com.parallel.utils.ExcelUtils.getColumnValuesInArray;
import static com.parallel.utils.FrameworkConstant.TEST_RESULT_SUMMARY;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.Authenticator;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class SentEmailUtil {
    static String allEmailAccounts = "prasanthvb1995@gmail.com, prasanthvb28@gmail.com";

        static final DecimalFormat df = new DecimalFormat("0.00");

        public static void sendEmail(List<Map> testSummary,List<Map> testResultFiles) throws Exception {

            // Recipient's email ID.
            // If no email id mentioned in Parameter email sent to all
            ArrayList emailAddressArray = getColumnValuesInArray(TEST_RESULT_SUMMARY,"EmailAddress",1,0);
            String to = (emailAddressArray.size()==0||emailAddressArray.equals(null)) ? allEmailAccounts :
                    testResultFiles.toString().contains("Regression") ? allEmailAccounts : String.join(",", emailAddressArray);

            //Needed
            // Sender's email ID
            String from = "TestAutomationResults@ProductName.com";

            final String username = "";
            final String password = "";

            //Needed
            // Host Email account information
            String host = "mailgate1.productName.com";

            Properties props = new Properties();
            props.put("mail.smtp.auth", "false");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "25");
            props.put("mail.debug", "true");
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.ssl.trust", "*");

            // Get the Session object.
            Session session = Session.getInstance(props,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

            try {
                // Create a default MimeMessage object.
            	Message message = new MimeMessage(session);

                // Set From: header field of the header.
                message.setFrom(new InternetAddress(from));

                // Set To: header field of the header.
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(to));

                // Create the message part
                BodyPart messageBodyPart = new MimeBodyPart();

                // Now set the actual message
                messageBodyPart.setContent(tables(testSummary),"text/html; charset=utf-8");

                // Create a multipar message
                Multipart multipart = new MimeMultipart();
	
                // Set text message part
                multipart.addBodyPart(messageBodyPart);

                for (Map fileDetailsMap : testResultFiles){
                    messageBodyPart = new MimeBodyPart();
                    String filename = fileDetailsMap.get("File Path").toString();
                    DataSource sources = new FileDataSource(filename);
                    messageBodyPart.setDataHandler(new DataHandler(sources));
                    messageBodyPart.setFileName(fileDetailsMap.get("File Name").toString());
                    multipart.addBodyPart(messageBodyPart);
                }

                // Send the complete message parts
                message.setContent(multipart);
                // Set Subject: header field
                message.setSubject("Test Automation Results");
                // Send message
                Transport.send(message);

            } catch (MessagingException | IOException e) {
                throw new RuntimeException(e);
            }

        }

        public static String tables(List<Map> testSummary) throws IOException
        {
            return  "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<style>\n" +
                    "#Product-Automation {\n" +
                    "  font-family: Arial, Helvetica, sans-serif;\n" +
                    "  border-collapse: collapse;\n" +
                    "  width: 60%;\n" +
                    "}\n" +
                    "\n" +
                    "#Product-Automation td, #Product-Automation th {\n" +
                    "  border: 1px solid #ddd;\n" +
                    "  padding: 8px;\n" +
                    "}\n" +
                    "\n" +
                    "#Product-Automation tr:nth-child(even){background-color: #f2f2f2;}\n" +
                    "\n" +
                    "#Product-Automation tr:hover {background-color: #ddd;}\n" +
                    "\n" +
                    "#Product-Automation th {\n" +
                    "  padding-top: 12px;\n" +
                    "  padding-bottom: 12px;\n" +
                    "  text-align: left;\n" +
                    "  background-color: #207D9D;\n" +
                    "  color: white;\n" +
                    "}\n" +
                    "</style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "\n" +
                    "<h1>Product UI - Test Automation Results</h1>\n" +
                    "\n" +
                    "<table id=\"Product-Automation\">\n" +
                    "  <tr>\n" +
                    "    <th>Module-Name</th>\n" +
                    "    <th>Passed</th>\n" +
                    "    <th>Failed</th>\n" +
                    "    <th>Skipped</th>\n" +
                    "    <th>Passed%</th>\n" +
                    "  </tr>\n" + getRowInfo(testSummary) +
                    "</table>\n" +
                    "\n<br><br>" +
                    "<Font size=\"+1.5\"><b>Thanks & Regards,</b></Font><br><br>\n" +
                    "<Font size=\"+1.5\"><b>Product QA Test Automation Team.</b></Font><br>\n" +
                    "</body>\n" +
                    "</html>\n" +
                    "\n" +
                    "\n";
        }

        private static String getRowInfo(List<Map> listMap) {
            String returnVal = "";
            for(Map map : listMap){
                returnVal = returnVal +
                        "  <tr>\n" +
                        "    <td>"+map.get("Module Name")+"</td>\n" +
                        "    <td>"+map.get("Tests Passed")+"</td>\n" +
                        "    <td>"+map.get("Tests Failed")+"</td>\n" +
                        "    <td>"+map.get("Tests Skipped")+"</td>\n" +
                        "    <td>"+getPercentage(Float.parseFloat(map.get("Tests Passed").toString()),
                        Float.parseFloat(map.get("Tests Failed").toString()),Float.parseFloat(map.get("Tests Skipped").toString()))+" %</td>\n" +
                        "  </tr>\n";
            }

            return  returnVal;
        }

        private static String getPercentage(float passed, float failed, float skipped) {
            float totalTestCases = passed+failed+skipped;
            return String.valueOf(df.format((double)(passed * 100f)/totalTestCases));
        }


}
