/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package execution;
import clases.Ancestor;
import clases.QueueNumber;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import familytree.ConnectionSingleton;
import familytree.StartDraw;
import static familytree.StartDraw.rootName;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.List;
import java.util.Random;

import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaapplication3.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
/**
 *
 * @author Ivica
 */
public class ObserverThread extends Thread {
    private StartDraw startDraw;
    public boolean running = false;
    
    private String EXCHANGE_NAME = "logs";
    private String queueName;
    Consumer consumer;
    public  QueueNumber myQueueNumber;
    Connection connection;
    Channel channel;
    Random randomno = new Random();
    
  public  ObserverThread(StartDraw sd )
    {
       super("Client");
        this.startDraw = sd;
        EXCHANGE_NAME = startDraw.getRootName();
        try{
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query q = session.createQuery("from Ancestor a where a.name = '" + startDraw.getRootName() + "'");
        List drawings = q.list();
        if(!drawings.isEmpty())
        {
            Ancestor a = (Ancestor) drawings.get(0);
            myQueueNumber =new QueueNumber(a.getId());
            session.flush();
            session.save(myQueueNumber);
            session.getTransaction().commit();
        }
        session.close();
        }catch(HibernateException he)
             {
              he.printStackTrace();
             }
    }
    
    @Override
    public void start() {
        
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            try {
            
            connection = factory.newConnection();
            channel = connection.createChannel();
           final int random = randomno.nextInt(500);
            
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, "");
            
            consumer = new DefaultConsumer(channel) {
                
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                  throws IOException {
                    try {
                        sleep(random);
                        System.out.println(new String(body));
                        String g = new String(body, "UTF-8");
                        if(g.compareTo("u") == 0)
                            startDraw.setView();
                         else
                        {
                            startDraw.setView(new String(body, "UTF-8"));
                        }
                        sleep(random);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ObserverThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                        
                  }
            };     
            
            try {
            channel.basicConsume(queueName, true, consumer);
            } catch (IOException ex) {
                Logger.getLogger(ObserverThread.class.getName()).log(Level.SEVERE, null, ex);
            }    
            
        } catch (IOException ex) {
            Logger.getLogger(ObserverThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(ObserverThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }

    public void end() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }

    @Override
    public void run() {
        
        
    }
}
