/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

/**
 *
 * @author Ivica
 */
public class Person {
    private int id;
    private boolean gender;
    private String name;
    private int coordinateX1;
    private int coordinateY1;
   // private int coordinateX2;
   // private int coordinateY2;
    private String color;
    private Ancestor ancestor;
    public Person()
    {
        
    }
    
    public Person( boolean cg,String cn,int cx1,int cy1,String cc)
    {
        gender=cg;
        name=cn;
        coordinateX1=cx1;
        coordinateY1=cy1;
    //    coordinateX2=cx2;
    //    coordinateY2=cy2;
        color=cc;
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
  /*  public int getCoordinateX2()
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
    }*/
    public String getColor()
    {
        return color;
    }
    public void setColor(String ca)
    {
        color=ca;
    }
    public Ancestor getAncestor()
    {
        return ancestor;
    }
    public void setAncestor(Ancestor ca)
    {
        ancestor=ca;
    }
    
}
