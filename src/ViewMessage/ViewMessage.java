/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ViewMessage;

/**
 *
 * @author Moses
 */
import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import javax.swing.JFrame;

public class ViewMessage extends javax.swing.JFrame {
   
    /**
     * Creates new form ViewMessage
     */
    Folder inbox;
    //String[] from, to, sub, rec = new String[100];
    int fromct, toct, subct, recct=0;
    /**
     * Creates new form Lab5gui
     */
    public ViewMessage(int mesnum) {
        initComponents();
        
	 Properties props = System.getProperties();
	 props.setProperty("mail.store.protocol", "imaps");
	 try
	 {
	 /*  Create the session and get the store for read the mail. */
	 Session session = Session.getInstance(props, null);
	 Store store = session.getStore("imaps");
	 store.connect("imap.gmail.com","agroupece433@gmail.com", "ece433test");
	 
	 /*  Mention the folder name which you want to read. */
	 inbox = store.getFolder("Inbox");
	 //jTextArea1.append("No of Unread Messages : " + inbox.getUnreadMessageCount()+"\n\n");
	 
	 /*Open the inbox using store.*/
	 inbox.open(Folder.READ_ONLY);
	 
	 /*  Get the messages which is unread in the Inbox*/
	 Message messages[] = inbox.search(new FlagTerm(new Flags(Flag.SEEN), false));
	 
	 /* Use a suitable FetchProfile    */
	 FetchProfile fp = new FetchProfile();
	 fp.add(FetchProfile.Item.ENVELOPE);
	 fp.add(FetchProfile.Item.CONTENT_INFO);
	 inbox.fetch(messages, fp);
	 
	 try
	 {
	 printAllMessages(messages, mesnum);
	 inbox.close(true);
	 store.close();
	 }
	 catch (Exception ex)
	 {
	 jTextArea1.append("Exception arise at the time of read mail");
	 ex.printStackTrace();
	 }
	 }
	 catch (NoSuchProviderException e)
	 {
	 e.printStackTrace();
	 System.exit(1);
	 }
	 catch (MessagingException e)
	 {
	 e.printStackTrace();
	 System.exit(2);
	 }
	 }
	 
	 public void printAllMessages(Message[] msgs, int mesnum) throws Exception
	 
	 //for (int i = 0; i < msgs.length; i++)
	 {
	 //System.out.println("MESSAGE #" + (i + 1) + ":");
             //TextArea1.append("Message #" + (i + 1));
             printEnvelope(msgs[mesnum]);
	 }
	 
	 
	 /*  Print the envelope(FromAddress,ReceivedDate,Subject)  */
	 public void printEnvelope(Message message) throws Exception
	 {
	 Address[] a;
	 // FROM
	 if ((a = message.getFrom()) != null)
	 {
	 for (int j = 0; j < a.length; j++)
	 {
	//jTextArea1.append("\n" + "FROM: " + a[j].toString() + "\n");
        String test = a[j].toString();
       // from[fromct]=test;
       // fromct++;
	 }
	 }
	 // TO
	 if ((a = message.getRecipients(Message.RecipientType.TO)) != null)
	 {
	 for (int j = 0; j < a.length; j++)
	 {
	 //jTextArea1.append("TO: " + a[j].toString()+ "\n");
         String testt=a[j].toString();
        // to[toct]=testt;
         //toct++;
	 }
         
        
	 }
	 String subject = message.getSubject();
	 Date receivedDate = message.getReceivedDate();
	 String content = message.getContent().toString();
	 //jTextArea1.append("Subject : " + subject+ "\n");
         //sub[subct]=subject;
         //subct++;
	 //jTextArea1.append("Received Date : " + receivedDate.toString()+ "\n\n");
         String testtt = receivedDate.toString();
         //rec[recct]=testtt;
         //recct++;
	 //jTextArea1.append("Content : " + content+ "\n");
	 getContent(message);
	 }
	 
	 public void getContent(Message msg)
	 {
	 try
	 {
	 String contentType = msg.getContentType();
	//jTextArea1.append("Content Type : " + contentType+ "\n");
	 if(msg.getContent() instanceof String)
	 {
		 String mp = (String)msg.getContent();
                 //jTextArea1.append(mp);
		 //dumpPart(mp);
	 }
	 else if(msg.getContent() instanceof Multipart)
	 {
		 Multipart mp = (Multipart) msg.getContent();
		 int count = mp.getCount();
		 for (int i = 0; i < count; i++)
		 {
		 dumpPart(mp.getBodyPart(i));
		 }
		 }
	 }
	 catch (Exception ex)
	 {
	 jTextArea1.append("Exception arise at get Content");
	 //ex.printStackTrace();
	 }
	 }
	 
	 public void dumpPart(Part p) throws Exception
	 {
	 // Dump input stream ..
	 InputStream is = p.getInputStream();
	 // If "is" is not already buffered, wrap a BufferedInputStream
	 // around it.
	 if (!(is instanceof BufferedInputStream))
	 {
	 is = new BufferedInputStream(is);
	 }
	 int c;
	 //jTextArea1.append("Message : ");
	 while ((c = is.read()) != -1)
	 {
         char ascii = (char)c;
         String Ascii = Character.toString(ascii);
	 //System.out.write(c);
         jTextArea1.append(Ascii);
	 }
         
    
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>                        

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ViewMessage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewMessage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewMessage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewMessage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        //java.awt.EventQueue.invokeLater(new Runnable() {
            //public void run() {
                //new ViewMessage().setVisible(true);
            //}
        //});
    }

    // Variables declaration - do not modify                     
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration                   
}
