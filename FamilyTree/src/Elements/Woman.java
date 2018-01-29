/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Elements;
import CH.ifa.draw.figures.*;
import CH.ifa.draw.figures.RectangleFigure;
import java.awt.*;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.FigureEnumeration;
import CH.ifa.draw.standard.CreationTool;
import CH.ifa.draw.standard.TextHolder;
import CH.ifa.draw.util.FloatingTextField;
import adapater.Adapter;
import clases.Person;
import familytree.ColorConverter;
import familytree.ConnectionSingleton;
import familytree.DrawingClass;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javaapplication3.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 *
 * @author Milan
 */
public class Woman extends GroupFigure{
    private int Id;
    private String Text = "  Unesite ime";
    private int coorX;
    private int coorY;
    private int red;
    private int green;
    private int blue;
    private DrawingClass drawing;
    private boolean creator;
    public int getRed()
    {
        return this.red;
    }
    public void setRed(int x)
    {
        this.red = x;
    }
    public int getGreen()
    {
        return this.green;
    }
    public void setGreen(int x)
    {
        this.green = x;
    }
    public int getBlue()
    {
        return this.blue;
    }
    public void setBlue(int x)
    {
        this.blue = x;
    }
    @Override
    public boolean canConnect() {
        return true;
    } 

    public void setTextLabel(String txt)
    {
        this.Text = txt;
    }
    public String getTextLabel()
    {
        return this.Text;
    }
    public int getId()
    {
        return this.Id;
    }
    public void setId(int id)
    {
        this.Id = id;
    }
     public void setDrawing(DrawingClass drw)
    {
        this.drawing = drw;
    }
    public DrawingClass getDrawing()
    {
        return this.drawing;
    }
    public void setCreator(boolean p)
    {
        this.creator = p;
    }
    public boolean getCreator()
    {
        return this.creator;
    }
    public void setColor(String colorP)
    {
         ColorConverter cc=new ColorConverter();
        this.setAttribute("FillColor", cc.getRGB("green"));
    }
     public void setColor(int x,int y,int z)
    {   
        this.setRed(x);
        this.setGreen(y);
        this.setBlue(z);
       FigureEnumeration k =  this.decompose();
       while( k.hasMoreElements())
       {
           Figure n = k.nextFigure();
           if (n instanceof EllipseFigure)
               n.setAttribute("FillColor", new Color(x,y,z));
           
       }
    }
     public void setCordX(int x)
    {
        this.coorX = x;
    }
    public int getCordX()
    {
        return this.coorX;
    }
    public void setCordY(int x)
    {
        this.coorY = x;
    }
    public int getCordY()
    {
        return this.coorY;
    }
     public Woman()
    {
        super();
        EllipseFigure r1=new EllipseFigure(new Point(0,0), new Point(80,25));
       
       this.red = 204;
        this.green = 204;
        this.blue = 0;
         r1.setAttribute("FillColor", new Color(red,green,blue));
        TextFigure txt = new TextFigure();
         txt.setText(Text);
         super.add(r1);
         super.add(txt);
    }
      public Woman(String tx)
    {
        super();
        EllipseFigure r1=new EllipseFigure(new Point(0,0), new Point(80,25));
       
       this.red = 204;
        this.green = 204;
        this.blue = 0;
         r1.setAttribute("FillColor", new Color(red,green,blue));
        TextFigure txt = new TextFigure();
         txt.setText(tx);
         super.add(r1);
         super.add(txt);
    }
     public void save()
   {
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();;
            session.beginTransaction();
            Adapter a = new Adapter();
            Person p = a.toPerson(this);       
             session.save(p);
             this.setId(p.getId());
             session.getTransaction().commit();
             session.close();
        }
        catch(HibernateException he)
        {
            he.printStackTrace();
        } 
   }
   public void updateWoman()
   {
              
                 Rectangle r = displayBox();
                 this.coorX = r.x;
                this.coorY = r.y;
                Session session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                Person p = (Person)session.get(Person.class, this.getId());
                p.setName(this.getTextLabel());
                p.setCoordinateX1(this.coorX);
                 p.setCoordinateY1( this.coorY);
                 p.setColor(Integer.toString(this.getRed())+"."+
                Integer.toString(this.getGreen())+"."+
                Integer.toString(this.getBlue()));
                session.update(p);
                session.getTransaction().commit();
                session.close();
   }
    @Override
    public void basicDisplayBox(Point origin, Point corner) {   
       
        Rectangle r = displayBox();
        
        coorX = origin.x;
        coorY = origin.y; 
        
        moveBy((int)origin.getX()-r.x, (int)origin.getY()-r.y);
        
        super.basicDisplayBox(origin, corner);     
    }
    
}
