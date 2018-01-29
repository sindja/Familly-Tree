/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adapater;

import CH.ifa.draw.figures.GroupFigure;
import Elements.Man;
import Elements.Mariage;
import Elements.Root;
import Elements.Woman;
import clases.Ancestor;
import clases.Marriage;
import clases.Person;
import javaapplication3.HibernateUtil;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Session;

/**
 *
 * @author Milan
 */
public class Adapter {

    public static Person toPerson(GroupFigure gf) {
        Person a = null;
        if (gf instanceof Man) {
            Man m = (Man) gf;
            a = new Person(true, m.getTextLabel(), m.getCordX(), m.getCordY(), Integer.toString(m.getRed()) + "." + Integer.toString(m.getGreen()) + "."
                    + Integer.toString(m.getBlue()));
        } else if (gf instanceof Woman) {
            Woman m = (Woman) gf;
            a = new Person(false, m.getTextLabel(), m.getCordX(), m.getCordY(), Integer.toString(m.getRed()) + "." + Integer.toString(m.getGreen()) + "."
                    + Integer.toString(m.getBlue()));
        }
        return a;
    }

    public GroupFigure toGroupFigure(Person a) {
        GroupFigure gf = null;
        if (a.getGender()) {
            Man m = new Man(a.getName());
            m.setId(a.getId());
            m.setTextLabel(a.getName());
            m.setCordX(a.getCoordinateX1());
            m.setCordY(a.getCoordinateY1());
            String rgb[] = a.getColor().split("\\.");
            m.setRed(Integer.parseInt(rgb[0]));
            m.setGreen(Integer.parseInt(rgb[1]));
            m.setBlue(Integer.parseInt(rgb[2]));
            m.setColor(m.getRed(), m.getGreen(), m.getBlue());
            gf = m;
        } else if (!a.getGender()) {
            Woman m = new Woman(a.getName());
            m.setId(a.getId());
            m.setTextLabel(a.getName());
            m.setCordX(a.getCoordinateX1());
            m.setCordY(a.getCoordinateY1());
            String rgb[] = a.getColor().split("\\.");
            m.setRed(Integer.parseInt(rgb[0]));
            m.setGreen(Integer.parseInt(rgb[1]));
            m.setBlue(Integer.parseInt(rgb[2]));
            m.setColor(m.getRed(), m.getGreen(), m.getBlue());
            gf = m;
        }
        return gf;
    }

    public Marriage toBaseMarriage(Mariage m) {
        Marriage mm = new Marriage();
        mm.setCoordinateX1(m.getCordX1());
        mm.setCoordinateY1(m.getCordY1());
        mm.setCoordinateX2(m.getCordX2());
        mm.setCoordinateY2(m.getCordY2());
        mm.setColor(Integer.toString(m.getRed()) + "."
                + Integer.toString(m.getGreen()) + "."
                + Integer.toString(m.getBlue()));

        for (GroupFigure gf : m.getCouple()) {
            mm.addCouple(this.toPerson(gf));
        }
        for (GroupFigure gf : m.getChildren()) {
            mm.addChild(this.toPerson(gf));
        }
        return mm;
    }

    public Mariage toAppMariage(Marriage mm) {
        Mariage m = new Mariage();
        m.setCordX1(mm.getCoordinateX1());
        m.setCordY1(mm.getCoordinateY1());
        m.setCordX2(mm.getCoordinateX2());
        m.setCordY2(mm.getCoordinateY2());
        m.setId(mm.getId());
        String colors[] = mm.getColor().split("\\.");

        m.setColor(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2]));

        for (Person p : mm.getCouple()) {
            m.addCouple(this.toGroupFigure(p));
        }
        for (Person p : mm.getChildren()) {
            m.addChild(this.toGroupFigure(p));
        }
        return m;
    }

    public Ancestor toAncestor(Root r) {
        Ancestor a = new Ancestor();
        a.setCoordinateX1(r.getCordX());
        a.setCoordinateY1(r.getCordY());
        a.setName(r.getTextLabel());
        a.setColor(Integer.toString(r.getRed()) + "." + Integer.toString(r.getGreen()) + "." + Integer.toString(r.getBlue()));
        for (GroupFigure gf : r.getDescendants()) {
            a.addDescandan(this.toPerson(gf));
        }
        for (Mariage gf : r.getMariages()) {
            a.addMarriage(this.toBaseMarriage(gf));
        }
        return a;
    }

    public Root toRoot(Ancestor a) {
        Root r = new Root();
        r.setCordX(a.getCoordinateX1());
        r.setCordY(a.getCoordinateY1());
        r.setTextLabel(a.getName());
        r.setId(a.getId());
        String colorss = a.getColor();
        String[] colors;
        colors = colorss.split("\\.");
        r.setRed(Integer.parseInt(colors[0]));
        r.setGreen(Integer.parseInt(colors[1]));
        r.setBlue(Integer.parseInt(colors[2]));
        for (Marriage m : a.getMarriages()) {
            r.addMariage(this.toAppMariage(m));
        }
        for (Person p : a.getDescendants()) {
            r.addDescandan(this.toGroupFigure(p));
        }
        return r;
    }
}
