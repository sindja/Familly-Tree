/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package execution;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import familytree.StartDraw;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Ivica
 */
public class CreatorThread extends Thread {
    Connection connection;
    Channel channel;
    StartDraw startDraw;
    private String EXCHANGE_NAME = "logs";
    
  public  CreatorThread(StartDraw sd)
    {
        super("Creator");
        this.startDraw = sd;
        EXCHANGE_NAME = startDraw.getRootName();
    }
    
    public void start() {
    
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection;
            try {
                connection = factory.newConnection();
                channel = connection.createChannel();                
                channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            } catch (TimeoutException ex) {
                Logger.getLogger(CreatorThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
            Logger.getLogger(CreatorThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        super.start();        
    }

    public void end() {
        try {
            channel.close();
            connection.close();
        } catch (IOException ex) {
            Logger.getLogger(CreatorThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(CreatorThread.class.getName()).log(Level.SEVERE, null, ex);
        }
               
    }

    public void run() {
        
    }    
    
    public void sendMessage(String ModiffiedObjectId)
    {
        try {           
            
           String message = ModiffiedObjectId;
           channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
           System.out.println(" Sent '" + message + "'");
            
        } catch (IOException ex) {
            Logger.getLogger(CreatorThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
