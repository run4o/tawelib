package Core;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * Handles the creating and sending of email notifications to users.
 *
 * @author Martin Trifonov
 * @version 1.2
 */
public class NotificationCenter {

    /**
     * Creates and sends a notification to user for upcoming event.
     *
     * @param event attending event.
     * @param name user's name.
     * @param to user's email.
     */
    public void sendEventNotification(Event event, String name, String to) {
        String msg = "Hello " + name + ", \n";
        msg += "This is a reminder for following event you have register for: \n";
        msg += event.getTitle() + " at " + event.getTime() + "\n";
        msg += "See you there! \n" + "TaweLib ";
        String sub = "Event Reminder!";
        sendEmail(sub, msg, to);
    }

    /**
     * Creates and sends a notification to user for event reservation.
     *
     * @param event Reservation event.
     * @param name user's name.
     * @param to user's email.
     */
    public void sendEventReservation(Event event, String name, String to) {
        String msg = "Hello " + name + ", \n";
        msg += "You successfully register for: \n";
        msg += event.getTitle() + " at " + event.getTime() + "\n";
        msg += "See you there! \n" + "TaweLib ";
        String sub = "Event Reservation!";
        sendEmail(sub, msg, to);
    }

    /**
     * Creates and sends a notification to user for a canceled event.
     *
     * @param event canceled event.
     * @param name user's name.
     * @param to user's email.
     */
    public void sendEventCancelation(Event event, String name, String to) {
        String msg = "Hello " + name + ", \n";
        msg += "Unfortenoutly : ";
        msg += event.getTitle() + " has been canceled! \n";
        msg += "Kind Regards! \n" + "TaweLib ";
        String sub = "Event Cancelation!";
        sendEmail(sub, msg, to);
    }

    /**
     * Creates and sends a notification to user for a canceled event
     * reservation.
     *
     * @param event canceled event's reservation.
     * @param name user's name.
     * @param to user's email.
     */
    public void sendEventReservationCanceled(Event event, String name, String to) {
        String msg = "Hello " + name + ", \n";
        msg += "You successfully removed your registration for: \n";
        msg += event.getTitle() + " at " + event.getTime() + "\n";
        msg += "Kind Regards,! \n" + "TaweLib ";
        String sub = "Event Reservation Canceled!";
        sendEmail(sub, msg, to);
    }

    /**
     * Creates and sends a notification to user for a Due date set on item.
     *
     * @param resource resource which has its due date set.
     * @param dueDate the due date.
     * @param name user's name.
     * @param to user's email.
     */
    public void sendDueItemNotification(Resource resource, String dueDate, String name, String to) {
        String msg = "Hello " + name + ", \n";
        msg += "This is a reminder that one of the resources you have has a due date set: \n";
        msg += resource.getTitle() + " due at " + dueDate;
        msg += "\n" + "Kind Regards, \n" + "TaweLib ";
        String sub = "Resource has a due date set!";
        sendEmail(sub, msg, to);
    }

    /**
     * Creates and sends a notification to user for a reserved item.
     *
     * @param resource resource which has been reserved.
     * @param name user's name.
     * @param to user's email.
     */
    public void sendReservedItemNotification(Resource resource, String name, String to) {
        String msg = "Hello " + name + ", \n";
        msg += "This is a reminder that one of the resources you have requested is now reserved and can be picked up: \n";
        msg += resource.getTitle();
        msg += "\n" + "Kind Regards, \n" + "TaweLib ";
        String sub = "A resources has been reserved!";
        sendEmail(sub, msg, to);
    }

    /**
     * Creates and sends a notification to user for a returned item.
     *
     * @param resource resource which has been returned.
     * @param fine the fine amount.
     * @param name user's name.
     * @param to user's email.
     */
    public void sendReturnedItemNotification(Resource resource, float fine, String name, String to) {
        String msg = "Hello " + name + ", \n";
        msg += "You have successfully returned: \n";
        msg += resource.getTitle();
        if (-1 * fine > 0.f) {
            msg += "\n However you were fined £" + (-1 * fine) + " for returning it late";
        }
        msg += "\n" + "Kind Regards, \n" + "TaweLib ";
        String sub = "You have returned a resource!";
        sendEmail(sub, msg, to);
    }

    /**
     * Creates and sends a notification to user for requested item.
     *
     * @param resource resource which has been requested.
     * @param name user's name.
     * @param to user's email.
     */
    public void sendRequestedItemNotification(Resource resource, String name, String to) {
        String msg = "Hello " + name + ", \n";
        msg += "You have successfully requested, however it is currently unavailable: \n";
        msg += resource.getTitle();
        msg += "\n" + "Kind Regards, \n" + "TaweLib ";
        String sub = "A resources has been requested!";
        sendEmail(sub, msg, to);
    }

    /**
     * Creates and sends a notification to user for a payment made.
     *
     * @param change payment amount.
     * @param name user's name.
     * @param to user's email.
     */
    public void sendPaymentNotification(float change, String name, String to) {
        String msg = "Hello " + name + ", \n";
        msg += "You have successfully made a £" + change + " payment!";
        msg += "\n" + "Kind Regards, \n" + "TaweLib ";
        String sub = "Payment Notification!";
        sendEmail(sub, msg, to);
    }

    /**
     * Creates and sends a notification to new user.
     *
     * @param id login username.
     * @param name user's name.
     * @param to user's email.
     */
    public void sendNewAccountEmail(int id, String name, String to) {
        String msg = "Hello " + name + ", \n";
        msg += "You have successfully register in TaweLib.\n";
        msg += "Your login username is " + id + "\n";
        msg += "\n" + "Kind Regards, \n" + "TaweLib ";
        String sub = "Welcome to TaweLib!";
        sendEmail(sub, msg, to);
    }

    /**
     * Sends the specified message to the specified email.
     *
     * @param sub Email's subject.
     * @param msg Email's message.
     * @param to Email recipient.
     */
    public void sendEmail(String sub, String msg, String to) {
        //Sets up connection to gmail SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //Loging in the email account  
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                /// Account was created just for this assigment and is
                /// just to showcase the system working
                return new PasswordAuthentication("tawelibrary123@gmail.com", "passWORD321");
            }
        });

        //Creating the email and sending it  
        try {
            MimeMessage message = new MimeMessage(session);
            message.setText(msg);
            message.setSubject(sub);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            Transport.send(message);
        } catch (MessagingException e) {
            System.out.println("Email not send: " + e);
            e.printStackTrace();
        }
    }

}
