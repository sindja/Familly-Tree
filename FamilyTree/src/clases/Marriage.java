/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;
import java.util.Set;
import java.util.HashSet;
/**
 *
 * @author Ivica
 */
public class Marriage {
    private int id;
     private Set<Person> couple = new HashSet<Person>(2);
    private Set<Person> children=new HashSet<Person>();
    private int coordinateX1;
    private int coordinateY1;
     private int coordinateX2;
    private int coordinateY2;
    private String color; 
    
    
    public Marriage()
    {
        
    }
    public int getId()
    {
        return id;
    }
    public void setId(int i)
    {
        id=i;
    }
    public void removeCouple(Person m) {
       Person s = null;
            for (Person mm : couple) {              
                        if (mm.getId() == m.getId()) {                        
                        s = mm;
                    }               
            }
          if(s!=null)
          {
              couple.remove(s);
          }
        
    }
    public void removeChildren(Person m) {
        Person s = null;
            for (Person mm : children) {              
                        if (mm.getId() == m.getId()) {                        
                        s = mm;
                    }               
            }
          if(s!=null)
          {
              children.remove(s);
          }
    }
    public Set<Person> getChildren()
    {
        return children;
    }
    
    public void setChildren( Set<Person> c)
    {
        children=c;
    }
     public Set<Person> getCouple()
    {
        return couple;
    }
    
    public void setCouple( Set<Person> c)
    {
        couple=c;
    }
     public void addCouple(Person m)
    {
        if (!couple.contains(m)) {
            couple.add(m);
        }
    }
      public void addChild(Person m)
    {
        if (!children.contains(m)) {
            children.add(m);
        }
    }
    public int getCoordinateX1()
    {
        return coordinateX1;
    }
    public void setCoordinateX1(int g)
    {
        this.coordinateX1=g;
    }
    public int getCoordinateY1()
    {
        return coordinateY1;
    }
    public void setCoordinateY1(int g)
    {
        this.coordinateY1=g;
    }
     public int getCoordinateX2()
    {
        return coordinateX2;
    }
    public void setCoordinateX2(int g)
    {
        this.coordinateX2=g;
    }
    public int getCoordinateY2()
    {
        return coordinateY2;
    }
    public void setCoordinateY2(int g)
    {
        this.coordinateY2=g;
    }
    public String getColor()
    {
        return color;
    }
    public void setColor(String ca)
    {
        color=ca;
    }
}
