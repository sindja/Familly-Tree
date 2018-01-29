/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

/**
 *
 * @author Milan
 */
public class QueueNumber {
    private int id;
    private int rootId;
    //private int myId;
   // private int number;
    public QueueNumber()
    {
        
    }
    public QueueNumber(int m)
    {
        this.rootId = m;
    }
     public int getId()
    {
        return id;
    }
    public void setId(int mn){
       id=mn;
    }
    public int getRootId()
    {
        return rootId;
    }
    public void setRootId(int sn){
        rootId=sn;
    }
   /* public int getMyId()
    {
        return myId;
    }
    public void setMyId(int mn){
       myId=mn;
    }
     public int getNumber()
    {
        return number;
    }
    public void setNumber(int mn){
       number=mn;
    }

     */
    
}
