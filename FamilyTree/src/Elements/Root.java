/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Elements;

import CH.ifa.draw.figures.EllipseFigure;
import CH.ifa.draw.figures.GroupFigure;
import CH.ifa.draw.figures.RectangleFigure;
import CH.ifa.draw.figures.TextFigure;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.FigureEnumeration;
import adapater.Adapter;
import clases.Ancestor;
import clases.Marriage;
import clases.Person;
import familytree.ColorConverter;
import familytree.ConnectionSingleton;
import familytree.DrawingClass;
import static familytree.StartDraw.drawing;
import static familytree.StartDraw.rootName;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;
import javaapplication3.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 *
 * @author Milan
 */
public class Root extends GroupFigure {
    private int Id;
    private String Text;
    private int coorX;
    private int coorY;
    private int red;
    private int green;
    private int blue;
    private DrawingClass drawing;
    private boolean creator;
    private Set<GroupFigure> descendants = new HashSet<>();
    private Set<Mariage> mariages = new HashSet<>();

    public Set<Mariage> getMariages() {
        return mariages;
    }
    
    public void setMariages(Set<Mariage> m) {
        mariages = m;
    }

    public void addMariage(Mariage m) {
        if (!mariages.contains(m)) {
            mariages.add(m);
        }
    }
    public Set<GroupFigure> getDescendants() {
        return descendants;
    }
    
    public void setDescendants(Set<GroupFigure> c) {
        descendants = c;
    }

    public void addDescandan(GroupFigure gf) {
        if (!descendants.contains(gf)) {
            descendants.add(gf);
        }
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

    public void setTextLabel(String txt) {
        this.Text = txt;
        FigureEnumeration k = this.decompose();
        while (k.hasMoreElements()) {
            Figure n = k.nextFigure();
            if (n instanceof TextFigure) {
                ((TextFigure) n).setText(txt);
            }
        }
    }
    public String getTextLabel() {
        return this.Text;
    }

    public int getId() {
        return this.Id;
    }

    public void setId(int id) {
        this.Id = id;
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

    public void setCordX(int x) {
        this.coorX = x;
    }

    public int getCordX() {
        return this.coorX;
    }
    public void setCordY(int x) {
        this.coorY = x;
    }
    public int getCordY() {
        return this.coorY;
    }

    public Root() {
        super();
        EllipseFigure r1 = new EllipseFigure(new Point(0, 0), new Point(50, 50));
        TextFigure txt = new TextFigure();
        this.red = 0;
        this.green = 128;
        this.blue = 255;
        r1.setAttribute("FillColor", new Color(red, green, blue));
        txt.setText("Unesite ime");
        super.add(r1);
        super.add(txt);
    }
   /* public Root(String tx) {
        super();
        EllipseFigure r1 = new EllipseFigure(new Point(0, 0), new Point(50, 50));
        TextFigure txt = new TextFigure();
        this.red = 204;
        this.green = 204;
        this.blue = 0;
        r1.setAttribute("FillColor", new Color(red, green, blue));
        txt.setText(tx);
        super.add(r1);
        super.add(txt);
    }*/
    public void save() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();;
            session.beginTransaction();
            Adapter a = new Adapter();
            Ancestor aa = a.toAncestor(this);
            aa.setBeingModified(true);
            session.save(aa);
            session.getTransaction().commit();
            session.close();
            this.setId(aa.getId());

        } catch (HibernateException he) {
            he.printStackTrace();
        }
    }
    public void updateRoot() {

        Rectangle r = displayBox();
        this.coorX = r.x;
        this.coorY = r.y;
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Ancestor p = (Ancestor) session.get(Ancestor.class, this.getId());
        p.setCoordinateX1(this.coorX);
        p.setCoordinateY1(this.coorY);
        Adapter a = new Adapter();
        p.setName(this.getTextLabel());
        for (GroupFigure gf : this.getDescendants()) {
            if (gf instanceof Man) {
                Man mm = (Man) gf;
                Person s = (Person) session.get(Person.class, mm.getId());
                p.addDescandan(s);
            } else if (gf instanceof Woman) {
                Woman mm = (Woman) gf;
                Person s = (Person) session.get(Person.class, mm.getId());
                p.addDescandan(s);
            }
        }
        for (Mariage m : this.getMariages()) {
            Marriage mm = (Marriage) session.get(Marriage.class, m.getId());
            p.addMarriage(mm);
        }
        p.setColor(Integer.toString(this.getRed()) + "."
                + Integer.toString(this.getGreen()) + "."
                + Integer.toString(this.getBlue()));
        session.update(p);
        session.getTransaction().commit();
       session.close();
    }

    @Override
    public boolean canConnect() {
        return true;
    }

    @Override
    public void basicDisplayBox(Point origin, Point corner) {
        java.awt.Rectangle r = displayBox();
        coorX = r.x;
        coorY = r.y;
        basicMoveBy((int) origin.getX() - r.x, (int) origin.getY() - r.y);
    }
}
