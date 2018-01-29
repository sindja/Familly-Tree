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
import CH.ifa.draw.standard.CreationTool;
import CH.ifa.draw.standard.TextHolder;
import CH.ifa.draw.util.FloatingTextField;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import CH.ifa.draw.contrib.*;
import CH.ifa.draw.framework.FigureEnumeration;
import adapater.Adapter;
import clases.Marriage;
import clases.Person;
import familytree.ConnectionSingleton;
import familytree.DrawingClass;
import java.util.HashSet;
import java.util.Set;
import javaapplication3.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 *
 * @author Milan
 */
public class Mariage extends DiamondFigure {

    private int Id;
    private boolean hasMan;
    private boolean hasWoman;
    private boolean secondArrive;
    private int red;
    private int green;
    private int blue;
    private DrawingClass drawing;
    private boolean creator;
    private int cordX1;
    private int cordY1;
    private int cordX2;
    private int cordY2;
    private Set<GroupFigure> couple = new HashSet<GroupFigure>(2);
    private Set<GroupFigure> children = new HashSet<GroupFigure>();

    public void addCouple(GroupFigure m) {
        if (!couple.contains(m)) {
            couple.add(m);
        }
    }

    public void addChild(GroupFigure m) {
        if (!children.contains(m)) {
            children.add(m);
        }
    }

    public Set<GroupFigure> getChildren() {
        return children;
    }

    public void setChildren(Set<GroupFigure> c) {
        children = c;
    }

    public Set<GroupFigure> getCouple() {
        return couple;
    }

    public void setCouple(Set<GroupFigure> c) {
        couple = c;
    }

    public int getRed() {
        return this.red;
    }

    public void setRed(int x) {
        this.red = x;
    }

    public int getGreen() {
        return this.green;
    }

    public void setGreen(int x) {
        this.green = x;
    }

    public int getBlue() {
        return this.blue;
    }

    public void setBlue(int x) {
        this.blue = x;
    }

    public Mariage() {
        super(new Point(0, 0), new Point(70, 20));
        this.red = 204;
        this.green = 204;
        this.blue = 0;
        this.setAttribute("FillColor", new Color(red, green, blue));
        hasMan = false;
        hasWoman = false;
        secondArrive = false;
    }

    public int getId() {
        return this.Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public boolean getSecondArrived() {
        return this.secondArrive;
    }

    public void setSecondArrived(boolean x) {
        this.secondArrive = x;
    }

    public void setHasMan(boolean p) {
        this.hasMan = true;
    }

    public boolean getHasMan() {
        return this.hasMan;
    }

    public void setHasWoman(boolean p) {
        this.hasWoman = true;
    }

    public boolean getHasWoman() {
        return this.hasWoman;
    }

    public void setDrawing(DrawingClass drw) {
        this.drawing = drw;
    }

    public DrawingClass getDrawing() {
        return this.drawing;
    }

    public void setCreator(boolean p) {
        this.creator = p;
    }

    public boolean getCreator() {
        return this.creator;
    }

    public void setCordX1(int x) {
        this.cordX1 = x;
    }

    public int getCordX1() {
        return this.cordX1;
    }

    public void setCordY1(int x) {
        this.cordY1 = x;
    }

    public int getCordY1() {
        return this.cordY1;
    }

    public void setCordX2(int x) {
        this.cordX2 = x;
    }

    public int getCordX2() {
        return this.cordX2;
    }

    public void setCordY2(int x) {
        this.cordY2 = x;
    }

    public int getCordY2() {
        return this.cordY2;
    }

    @Override
    public boolean canConnect() {
        return true;
    }

    public void setColor(int x, int y, int z) {
        this.setRed(x);
        this.setGreen(y);
        this.setBlue(z);

        this.setAttribute("FillColor", new Color(x, y, z));

    }

    public void save() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();;
            session.beginTransaction();
            Adapter a = new Adapter();
            Marriage mm = a.toBaseMarriage(this);
            session.save(mm);
            session.getTransaction().commit();
            session.close();
            this.setId(mm.getId());
        } catch (HibernateException he) {
            he.printStackTrace();
        }
    }

    public void updateMariage() {

        Rectangle r = displayBox();
        this.cordX1 = r.x;
        this.cordY1 = r.y;
        this.cordX2 = r.x + r.width;
        this.cordY2 = r.y + r.height;

        Session session = ConnectionSingleton.getSession();
        session.beginTransaction();
        Marriage p = (Marriage) session.get(Marriage.class, this.getId());
        p.setCoordinateX1(this.cordX1);
        p.setCoordinateY1(this.cordY1);
        p.setCoordinateX2(this.cordX2);
        p.setCoordinateY2(this.cordY2);
        Adapter a = new Adapter();
        for (GroupFigure gf : this.getChildren()) {
            if (gf instanceof Man) {
                Man mm = (Man) gf;
                Person s = (Person) session.get(Person.class, mm.getId());
                p.addChild(s);
            } else if (gf instanceof Woman) {
                Woman mm = (Woman) gf;
                Person s = (Person) session.get(Person.class, mm.getId());
                p.addChild(s);
            }
        }
        for (GroupFigure gf : this.getCouple()) {
            if (gf instanceof Man) {
                Man mm = (Man) gf;
                Person s = (Person) session.get(Person.class, mm.getId());
                p.addCouple(s);
            } else if (gf instanceof Woman) {
                Woman mm = (Woman) gf;
                Person s = (Person) session.get(Person.class, mm.getId());
                p.addCouple(s);
            }
        }
        p.setColor(Integer.toString(this.getRed()) + "."
                + Integer.toString(this.getGreen()) + "."
                + Integer.toString(this.getBlue()));
        session.update(p);
        session.getTransaction().commit();
        session.close();
    }
}
