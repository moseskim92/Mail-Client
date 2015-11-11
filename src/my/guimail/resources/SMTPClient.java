package my.guimail.resources;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.text.DocumentFilter;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import java.awt.event.*;
import java.awt.*;
import java.io.File;

public class SMTPClient extends JFrame {

  private JButton     sendButton   = new JButton("Send Message");
  ImageIcon attachmentimg = new ImageIcon(getClass().getResource("clip.png"));
  private JButton attachButton   = new JButton("",attachmentimg); 
  private JLabel      fromLabel    = new JLabel("From: "); 
  private JLabel      toLabel      = new JLabel("To: "); 
  private JLabel      ccLabel      = new JLabel("CC: "); 
  private JLabel      bccLabel      = new JLabel("BCC: "); 
  private JLabel      hostLabel    = new JLabel("SMTP Server: "); 
  private JLabel      subjectLabel = new JLabel("Subject: "); 
  private JTextField  fromField    = new JTextField(40); 
  private JTextField  toField      = new JTextField(40); 
  private JTextField  ccField      = new JTextField(40); 
  private JTextField  bccField      = new JTextField(40); 
  private JLabel  hostField    = new JLabel(""); 
  private JTextField  subjectField = new JTextField(40); 
  private JLabel  attachName = new JLabel(""); 
  private JTextArea   message      = new JTextArea(40, 32); 
  private JScrollPane jsp          = new JScrollPane(message);
  String attachFile;
  String attachFileName;
  static SMTPClient client = new SMTPClient();
  boolean window = false;
  
  public SMTPClient() {
    
    super("MailMe v1.0 - New Email");
    Container contentPane = this.getContentPane();
    contentPane.setLayout(new BorderLayout());
    contentPane.setPreferredSize(new Dimension(540,400));
    
    JPanel labels = new JPanel();
    labels.setLayout(new GridLayout(7, 1));
    labels.setPreferredSize(new Dimension(15,149));
    
    JPanel fields = new JPanel();
    fields.setLayout(new GridLayout(7, 1));
    String host = System.getProperty("mail.smtp.host", "smtp.gmail.com");
    hostField.setText(host);
    labels.add(hostLabel);
    fields.add(hostField);
    
    labels.add(toLabel);
    fields.add(toField);

    String from = System.getProperty("mail.from", "agroupece433@gmail.com");
    fromField.setText(from);
    labels.add(fromLabel);
    fields.add(fromField);
    
    labels.add(ccLabel);
    fields.add(ccField);
    
    labels.add(bccLabel);
    fields.add(bccField);
    
    labels.add(subjectLabel);
    fields.add(subjectField);
    
    attachButton.setPreferredSize(new Dimension(1,1));
    labels.add(attachButton);
    fields.add(attachName);
    attachButton.addActionListener(new AttachFile());
    Box north = Box.createHorizontalBox();
    north.add(labels);
    north.add(fields);
 
    
    contentPane.add(north, BorderLayout.NORTH);
    clearFields();
    
    message.setFont(new Font("Monospaced", Font.PLAIN, 12));
    contentPane.add(jsp, BorderLayout.CENTER);

    JPanel south = new JPanel();
    south.setLayout(new FlowLayout(FlowLayout.CENTER));
    south.add(sendButton);
    sendButton.addActionListener(new SendAction());
    contentPane.add(south, BorderLayout.SOUTH);       
    
    this.pack(); 
  }
  class AttachFile implements ActionListener{
	  public void actionPerformed(ActionEvent evt) {
	      try {
	    	  JFileChooser fileChooser = new JFileChooser();
	    	  File workingDirectory = new File(System.getProperty("user.home"));
	    	  fileChooser.setCurrentDirectory(workingDirectory);
	    	  int returnVal = fileChooser.showOpenDialog(SMTPClient.this);
	    	  File selectedFile = fileChooser.getSelectedFile();
	          attachFile = selectedFile.toString();
	          attachFileName = selectedFile.getName();
	          attachName.setText(attachFileName);
	    	  System.out.println("Working Directory: " + workingDirectory);
	    	  System.out.println("Selected file: " + selectedFile);
	    	  System.out.println("Selected file name: " + attachFileName);
	    	  //selectedFile.getAbsolutePath()
	      }
	      catch (Exception ex) {
	        // I should really bring up a more specific error dialog here.
                if(attachFileName == null)
                {
                    attachName.setText("");
                    System.out.println("No attachement added!");
                }
                else
	          ex.printStackTrace(); 
	      }
	      }
  }
  class SendAction implements ActionListener {
   
    public void actionPerformed(ActionEvent evt) {
      try {
        Properties props = new Properties();
        props.put("mail.host", hostField.getText());
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.EnableSSL.enable", "true");
         
        //Session mailConnection = Session.getInstance(props, null);
        
        Session mailConnection = Session.getDefaultInstance(props, new javax.mail.Authenticator()
        {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication("agroupece433@gmail.com", "ece433test");
            }
        });
        final Message msg = new MimeMessage(mailConnection);
        props.setProperty("mail.store.protocol", "imaps");
     
        Address to = new InternetAddress(toField.getText());
        Address from = new InternetAddress(fromField.getText());
        if(isValidEmail(toField.getText()))
        {
        	System.out.println("ok");
        }
        String[] ccList = ccField.getText().replaceAll("[,\\s]+",",").split(",");
        InternetAddress[] cc = new InternetAddress[ccList.length];
        int ccVerify= 0;
        
        String[] bccList = bccField.getText().replaceAll("[,\\s]+",",").split(",");
        InternetAddress[] bcc = new InternetAddress[bccList.length];
        int bccVerify= 0;
        if(!ccField.getText().isEmpty())
        {
	        for(int i = 0 ; i <ccList.length;i++)
	        {
	        	if(isValidEmail(ccList[i]))
	        	{
	        		ccVerify++;
	        		cc[i] = new InternetAddress(ccList[i]);
	        	}
	        }
        }
        if(ccVerify == ccList.length && ccVerify != 0)
        {
        	msg.addRecipients(Message.RecipientType.CC, cc);
        	System.out.println("we are good!");
        }
        if(!bccField.getText().isEmpty())
        {
	        for(int i = 0 ; i <bccList.length;i++)
	        {
	        	if(isValidEmail(bccList[i]))
	        	{
	        		bccVerify++;
	        		bcc[i] = new InternetAddress(bccList[i]);
	        	}
	        }
        }
        if(bccVerify == bccList.length && bccVerify!=0)
        {
        	msg.addRecipients(Message.RecipientType.BCC, bcc);
        	System.out.println("we are good!");
        }
      
        msg.setContent(message.getText(), "text/plain");
        msg.setFrom(from);
        msg.setRecipient(Message.RecipientType.TO, to);
        msg.setSubject(subjectField.getText());
        
        //attach file 
        if(attachFile != null)
        {
	        MimeBodyPart messageBody = new MimeBodyPart();
	        Multipart attachMultiPart = new MimeMultipart();
	        messageBody = new MimeBodyPart();
	        
	        DataSource source= new FileDataSource(attachFile);
	        messageBody.setDataHandler(new DataHandler(source));
	        messageBody.setFileName(attachFileName);
	        attachMultiPart.addBodyPart(messageBody);
	        msg.setContent(attachMultiPart);
	        System.out.println("\nAttaching..."+attachFile);
        }
        
        // This can take a non-trivial amount of time so 
        // spawn a thread to handle it. 
        Runnable r = new Runnable() {
          public void run() {
            try {
              Transport.send(msg);
              window = true;
            }
            catch (Exception ex) {
              ex.printStackTrace(); 
            }
          } 
        };
        Thread t = new Thread(r);
        t.start();
      }
      catch (Exception ex) {
        // I should really bring up a more specific error dialog here.
        ex.printStackTrace(); 
      }
      
    } 
    
  }
  public void clearFields()
  {
	  message.setText("");
	  subjectField.setText("");
	  toField.setText("");
	  ccField.setText("");
	  bccField.setText("");
	  attachName.setText("");
	  
  }
  public boolean complete()
  {
      return window;
  }
  public static boolean isValidEmail(String email)
  {
	  boolean result = true;
	  try
	  {
		  InternetAddress emailAdd= new InternetAddress(email);
		  emailAdd.validate();
	  }
	  catch(AddressException ex)
	  {
		  JOptionPane.showMessageDialog(new JFrame(), "Invalid email","Error",JOptionPane.ERROR_MESSAGE);
		  result = false;
	  }
	return result;
	  
  }
  public static void main(String[] args) {
  
    client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    client.show();
  }
}
