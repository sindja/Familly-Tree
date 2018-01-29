/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import Elements.Mariage;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Ivica
 */
public class Ancestor {
    private int id;
    private boolean gender;
    private boolean beingModified;
    private String name;
    private int coordinateX1;
    private int coordinateY1;
    private String color;
    private Set<Person> descendants = new HashSet<>();
    private Set<Marriage> marriages = new HashSet<>();
    public Ancestor()
    {
        
    }
    
    public Ancestor( boolean cg,String cn,int cx1,int cy1,String cc)
    {
        gender=cg;
        name=cn;
        coordinateX1=cx1;
        coordinateY1=cy1;
        color=cc;
    }
    
    public void removeMarriages(Marriage m) {
       Marriage s = null;
            for (Marriage mm : marriages) {              
                        if (mm.getId() == m.getId()) {                        
                        s = mm;
                    }               
            }
          if(s!=null)
          {
              marriages.remove(s);
          }
    }
    public void removeDescandants(Person m) {
        Person s = null;
            for (Person mm : descendants) {              
                        if (mm.getId() == m.getId()) {                        
                        s = mm;
                    }               
            }
          if(s!=null)
          {
              descendants.remove(s);
          }
    }
    public int getId()
    {
        return id;
    }
    public void setId(int id_a)
    {
        this.id=id_a;
    }
    public boolean getGender()
    {
        return gender;
    }
    public void setGender(boolean g)
    {
        this.gender=g;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String  n)
    {
        this.name=n;
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
    public String getColor()
    {
        return color;
    }
    public void setColor(String ca)
    {
        color=ca;
    }
    public Set<Person> getDescendants()
    {
        return descendants;
    }
    
    public void setDescendants( Set<Person> c)
    {
        descendants=c;
    }
    public void addDescandan( Person gf)
    {
        if(!descendants.contains(gf))
            descendants.add(gf);
    }
     public Set<Marriage> getMarriages()
    {
        return marriages;
    }
    
    public void setMarriages( Set<Marriage> m)
    {
        marriages=m;
    }
     public void addMarriage( Marriage m)
    {
        if(!marriages.contains(m))
            marriages.add(m);
    }
     public boolean getBeingModified()
     {
         return beingModified;
     }
     public void setBeingModified(boolean b)
     {
         beingModified=b;
     }
}
