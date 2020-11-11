package converter;

import modele.Monnaie;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(
                propertyName = "destination",
                propertyValue = "queue/MailContent"
        ),
        @ActivationConfigProperty(
            propertyName = "destinationType",
            propertyValue = "javax.jms.Queue"
)

})
public class MailerMBDBean implements MessageListener {
    @EJB
    IConverter converter;

    public MailerMBDBean() {
    }

    @Override
    public void onMessage(Message message) {
        try{
            if(message instanceof TextMessage){
                TextMessage mesg = (TextMessage)message;
                String content = mesg.getText();

                //Recup Montant a convertir
                String s = content.substring(0,content.indexOf("#"));
                double amount = Double.parseDouble(s);

                //Faire les convertion à partir du beanSession
                Map<Monnaie, Double> mapConvertion = converter.euroToOtherCurrencies(amount);

                Properties properties = new Properties();
                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", "true");

                javax.mail.Session session = javax.mail.Session.getInstance(properties);
                javax.mail.Message msg = new MimeMessage(session);

                try{
                    msg.setFrom(new InternetAddress("autopsiemassi@gmail.com"));
                    String destination = content.substring(content.indexOf("#")+1);
                    msg.setRecipient(javax.mail.Message.RecipientType.TO,
                                        new InternetAddress(destination));

                    String sujet = "Conversion de Monnaie";
                    msg.setSubject(sujet);

                    String mailBody = "<html>" +
                            "<head></head>" +
                            "<body> <h1> Convertion pour la somme de "+amount+" euros</h1>" +
                            "<table>" +
                            "<thead><tr style=\"background-color: #8C0000; color: #fff; font-family: Arial;\">" +
                            "<th style=\"padding: 10px;\">Currency</th>" +
                            "<th style=\"padding: 10px;\">Actual Rate</th>" +
                            "<th style=\"padding: 10px;\">Converted Amount</th>" +
                            "</tr></thead>" +
                            "<tbody>";

                    for(Monnaie monnaie : mapConvertion.keySet()){
                        double converted = mapConvertion.get(monnaie);

                        mailBody += "<tr style=\"font-family: Arial; background-color:#b5ffee; font-size: 12px;\">" +
                                        "<td style=\"padding: 10px;\">"+monnaie.getCurrency()+" ("+monnaie.getCountryOfMoney()+") </td>" +
                                        "<td style=\"padding: 10px;\">"+monnaie.getRate()+"</td>" +
                                        "<td style=\"padding: 10px;\">"+converted+" "+monnaie.getFullName()+"</td>" +
                                    "</tr>";
                    }


                    mailBody += "</tbody></table></body></html>";

                    msg.setContent(mailBody,"text/html;charset=utf8");
                    msg.setSentDate(Calendar.getInstance().getTime());
                    Transport transport = session.getTransport("smtp");
                    String email = System.getenv("email");
                    String password = System.getenv("password");
                    transport.connect("smtp.gmail.com",email,password);
                    transport.sendMessage(msg, msg.getAllRecipients());

                    transport.close();
                    System.out.println("email envoyé à : "+destination);
                }catch (MessagingException e){
                    e.printStackTrace();
                }
            }

        }catch (JMSException e){
            e.printStackTrace();
        }

    }
}
