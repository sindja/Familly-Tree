/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.HJDError;
import CH.ifa.draw.standard.AbstractTool;
import Elements.Mariage;
import adapater.Adapter;
import clases.Marriage;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import static familytree.StartDraw.creatorThread;
import static familytree.StartDraw.drawing;

import javaapplication3.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 *
 * @author Milan
 */
public class MariageTool extends AbstractTool {   
   
    private Point   fAnchorPoint;
    private Figure  fCreatedFigure;
    private Figure  fPrototype;    
    private boolean creator;
    /**
     * Initializes a CreationTool with the given prototype.
     */
    public MariageTool(DrawingView view, Figure prototype, boolean p) {
        super(view);
        fPrototype = prototype;
        creator = p;
    }
     public MariageTool(DrawingView view, Figure prototype)
    {
        super(view);
    }
    public void activate() {
        view().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }
    public void mouseDown(MouseEvent e, int x, int y) {
        if (creator) {
            int tmp = 0;
            fAnchorPoint = new Point(x, y);
            fCreatedFigure = createFigure();
            fCreatedFigure.displayBox(fAnchorPoint, fAnchorPoint);
            Mariage m = (Mariage) fCreatedFigure;
            m.setDrawing(drawing);
            m.setCreator(creator);
            m.setCordX1(fAnchorPoint.x);
            m.setCordY1(fAnchorPoint.y);
            m.setCordX2(fAnchorPoint.x);
            m.setCordY2(fAnchorPoint.y);
            m.save();
            drawing.addMariage(m);
            drawing.getRoot().addMariage(m);
            drawing.getRoot().updateRoot();
            view().add(fCreatedFigure);
            creatorThread.sendMessage("u");
        }
    }


    /**
     * Creates a new figure by cloning the prototype.
     */
    protected Figure createFigure() {        
        if (fPrototype == null)
		    throw new HJDError("No protoype defined");
        return (Figure) fPrototype.clone();
    }

    /**
     * Adjusts the extent of the created figure
     */
    public void mouseDrag(MouseEvent e, int x, int y) {
        if(creator)
        {
            fCreatedFigure.displayBox(fAnchorPoint, new Point(x,y));
            
        }
        
    }

    /**
     * Checks if the created figure is empty. If it is, the figure
     * is removed from the drawing.
     * @see Figure#isEmpty
     */
    public void mouseUp(MouseEvent e, int x, int y) {
        if(creator)
        {
        if (fCreatedFigure.isEmpty())
            drawing().remove(fCreatedFigure);
        Mariage m = (Mariage) fCreatedFigure;
        m.updateMariage();
        fCreatedFigure = null;
        editor().toolDone();
        }
        
    }

    /**
     * Gets the currently created figure
     */
    protected Figure createdFigure() {
        return fCreatedFigure;
    } 
}