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
import Elements.Man;
import adapater.Adapter;
import clases.Person;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import static familytree.StartDraw.creatorThread;
import static familytree.StartDraw.drawing;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import javaapplication3.HibernateUtil;
/**
 *
 * @author Milan
 */
public class ManTool extends AbstractTool {
    
   
    private Point   fAnchorPoint;

    /**
     * the currently created figure
     */
    private Figure  fCreatedFigure;

    /**
     * the prototypical figure that is used to create new figures.
     */
    private Figure  fPrototype;
    
    private boolean creator;


    /**
     * Initializes a CreationTool with the given prototype.
     */
    public ManTool(DrawingView view, Figure prototype, boolean p) {
        super(view);
        fPrototype = prototype;
        creator = p;
    }
     public ManTool(DrawingView view, Figure prototype)
    {
        super(view);
    }
    /**
     * Sets the cross hair cursor.
     */
    public void activate() {
        view().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }
    /**
     * Creates a new figure by cloning the prototype.
     */
    public void mouseDown(MouseEvent e, int x, int y) {
        if (creator) {
            fAnchorPoint = new Point(x, y);
            fCreatedFigure = createFigure();
            fCreatedFigure.displayBox(fAnchorPoint, fAnchorPoint);

            Man m = (Man) fCreatedFigure;
            m.setDrawing(drawing);
            m.setCordX(x);
            m.setCordY(y);
            m.setCreator(creator);
            m.save();
            drawing.addMan(m);
            view().add(fCreatedFigure);
          //  creatorThread.sendMessage("u");
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
          fCreatedFigure.displayBox(fAnchorPoint, new Point(x,y));
    }

    /**
     * Checks if the created figure is empty. If it is, the figure
     * is removed from the drawing.
     * @see Figure#isEmpty
     */
    public void mouseUp(MouseEvent e, int x, int y) {
        if (creator) {
            if (fCreatedFigure.isEmpty()) {
                drawing().remove(fCreatedFigure);
            }
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
