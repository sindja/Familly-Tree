/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication3;


import clases.Ancestor;
import clases.Marriage;
import clases.Person;
import familytree.ConnectionSingleton;
import familytree.DrawingClass;
import familytree.StartDraw;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
/**
 *
 * @author Ivica
 */
public class Pocetna extends javax.swing.JFrame {

    private  void fillList() {         
        try 
        {
            Session session =  HibernateUtil.getSessionFactory().openSession();;
            session.beginTransaction();
            Query q = session.createQuery("from Ancestor a");
            List drawing = q.list();
            String [] listData = new String[drawing.size()];
            int i = 0;
            for (Object d : drawing)
             {
                String a = ((Ancestor)d).getId()+":"+((Ancestor)d).getName();
                listData[i] = a;
                i++;
             }         
            jList1.setListData(listData);
             session.getTransaction().commit();
             session.close();
         }
        catch(HibernateException he)
        {  he.printStackTrace(); }    
    }
    /**
     * Creates new form Pocetna
     */    
    DrawingClass dc;
    public static DrawingClass openDrawing = null; 
    
    public Pocetna() {
        initComponents();
        fillList();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setText("Napravi stablo za porodicu:");

        jButton1.setText("Napravi");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(jList1);

        jLabel1.setText("Postojeca porodicna stabla");

        jButton2.setText("Ucitaj");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Obrisi");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addContainerGap(226, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1)))
                        .addGap(12, 12, 12))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3)))
                .addGap(19, 19, 19))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       
       String name = jTextField1.getText();        
        if(!name.isEmpty() && name != null)
        {              
            if(openDrawing == null)
            {
                dc = new DrawingClass(name);
                openDrawing = dc;
                StartDraw window = new StartDraw(dc, true, "Server",name);
                window.open();
            }
            else
            {
                String infoMessage = "Other Drawing is open... Close it if you want to create some other drawing";
                JOptionPane.showMessageDialog(null, infoMessage, "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
        else
        {
            String infoMessage = "Enter no empty name for drawing name! ";
            JOptionPane.showMessageDialog(null, infoMessage, "Warning", JOptionPane.WARNING_MESSAGE);
        } 
       fillList();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       dc = new DrawingClass("ime");       
       if(!jList1.isSelectionEmpty())
        {
                Ancestor r = new Ancestor();
                try
                {
                    Session session =  HibernateUtil.getSessionFactory().openSession();;
                    session.beginTransaction();                    
                    Query q = session.createQuery("from Ancestor a where a.id = "+ jList1.getSelectedValue().toString().split(":")[0]);
                    List drawing = q.list();
                    r = (Ancestor) drawing.get(0);
                    //r = (Ancestor) session.get(Ancestor.class,jList1.getSelectedValue().toString().split(":")[0]);            
                    session.getTransaction().commit();
                    session.close();
                
                } catch(HibernateException he)
                {
                    he.printStackTrace();
                }
            
            
            if(r.getBeingModified())
            {              
                    StartDraw window = new StartDraw(dc, false, "Client", r.getName());
                    window.open();              
            }
            else
            {
                 openDrawing = dc;
                 try
                {
                    Session session =  HibernateUtil.getSessionFactory().openSession();
                    session.beginTransaction();
                    r.setBeingModified(true);
                    session.update(r);
                    session.getTransaction().commit();
                    
                
                } catch(HibernateException he)
                {
                    he.printStackTrace();
                }
                r.setBeingModified(true);
                StartDraw window2 = new StartDraw(dc, true, "Server", r.getName());                
                window2.open();
                
            }
                        
         
        }
            else
            {
             String infoMessage = "You must select which drawing you want to open!!!";
             JOptionPane.showMessageDialog(null, infoMessage, "Information", JOptionPane.INFORMATION_MESSAGE);
             }
       fillList();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
       
        if(!jList1.isSelectionEmpty())
        {
            Ancestor r = new Ancestor();
                try
                {
                     Session session = HibernateUtil.getSessionFactory().openSession();
                     session.beginTransaction();
                    
                    Query q = session.createQuery("from Ancestor a where a.id = "+ jList1.getSelectedValue().toString().split(":")[0]);
                    List drawing = q.list();
                    r = (Ancestor) drawing.get(0);               
                    
                    session.getTransaction().commit();
                   session.close();
                
                } catch(HibernateException he)
                {
                    he.printStackTrace();
                }
            
            
            if(r.getBeingModified())
            {
                    String infoMessage = "You don't have permission to delete this drawing!!!";
                    JOptionPane.showMessageDialog(null, infoMessage, "Information", JOptionPane.INFORMATION_MESSAGE);               
            }
            else
            {
               try
                {
                        Session session =  HibernateUtil.getSessionFactory().openSession();;
                        session.beginTransaction();
                        Query q = session.createQuery("from Ancestor a where a.id = "+ jList1.getSelectedValue().toString().split(":")[0]);
                        List drawing = q.list();
                        r = (Ancestor) drawing.get(0);
                        Set<Marriage> setM = new HashSet<>();
                        setM = r.getMarriages();
                        Marriage [] arrM = setM.toArray(new Marriage [setM.size()]);
                        for( Marriage m : arrM)
                        {
                            Set<Person> setP = new HashSet<>();
                            setP = m.getCouple();
                          
                            Person [] pern =  setP.toArray(new Person[setP.size()]);
                            for(Person p :  pern)
                            {
                                m.removeCouple(p);
                                session.delete(p);  
                            }
                            
                            
                            Set<Person> setPC = m.getChildren();
                            pern =  setPC.toArray(new Person[setPC.size()]);
                            for(Person p : pern)
                            {
                               
                                m.removeChildren(p);
                                 session.delete(p);
                                /*session.getTransaction().commit();
                                session.flush();*/
                            }
                            r.removeMarriages(m);
                        }
                        Set<Person> setPD = r.getDescendants();
                        Person [] pern =  setPD.toArray(new Person[setPD.size()]);
                        for(Person p : pern)
                        {
                            
                            r.removeDescandants(p);
                            session.delete(p);
                            /*session.getTransaction().commit();
                            session.flush();*/
                        }
                        session.delete(r);
                        session.getTransaction().commit();
                         session.close();
                }
               catch(HibernateException he)
                {
                    he.printStackTrace();
                }
            }
        }
         else
            {
             String infoMessage = "You must select which drawing you want to delete!!!";
             JOptionPane.showMessageDialog(null, infoMessage, "Information", JOptionPane.INFORMATION_MESSAGE);
             }
        fillList();
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(Pocetna.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Pocetna.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Pocetna.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Pocetna.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query q = session.createQuery(" from Person where idOsoba=5");
            List resultList = q.list();
           // displayResult(resultList);
            session.getTransaction().commit();
        } catch (HibernateException he) {
            he.printStackTrace();
        }*/

        
        
        
        
        
        /*  Person janko = new Person(true,"Marko",21,43,"234211060");
           janko.setId(5);

        try{
            
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        session.save(janko);

        session.getTransaction().commit();
        session.close();
        } catch (HibernateException he) {
            he.printStackTrace();
        }
    */
        
        
        
        
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {               
                new Pocetna().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
