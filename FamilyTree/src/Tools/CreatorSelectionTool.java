/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;
import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.FigureEnumeration;
import CH.ifa.draw.framework.Handle;
import CH.ifa.draw.framework.Tool;
import CH.ifa.draw.standard.AbstractTool;
import CH.ifa.draw.standard.DragTracker;
import CH.ifa.draw.standard.HandleTracker;
import CH.ifa.draw.standard.SelectAreaTracker;
import java.awt.event.MouseEvent; 
import static familytree.StartDraw.creatorThread;
import Elements.*;
/**
 *
 * @author Milan
 */
public class CreatorSelectionTool extends AbstractTool {

    private Tool fChild = null;    
    public static boolean send = true;
    
    private boolean creator;

    public CreatorSelectionTool(DrawingView view, boolean p) {
        super(view);
        this.creator = p;
    }

    /**
     * Handles mouse down events and starts the corresponding tracker.
     */
    public void mouseDown(MouseEvent e, int x, int y) {
        // on Windows NT: AWT generates additional mouse down events
        // when the left button is down && right button is clicked.
        // To avoid dead locks we ignore such events
        if (creator) {
            if (fChild != null) {
                return;
            }
            view().freezeView();
            Handle handle = view().findHandle(e.getX(), e.getY());
            if (handle != null) {
                fChild = createHandleTracker(view(), handle);
            } else {
                Figure figure = drawing().findFigure(e.getX(), e.getY());

                if (figure != null) {
                    fChild = createDragTracker(view(), figure);           
                } else {
                    if (!e.isShiftDown()) {
                        view().clearSelection();
                    }
                    fChild = createAreaTracker(view());
                }
            }
            fChild.mouseDown(e, x, y);
        }
    }

    /**
     * Handles mouse drag events. The events are forwarded to the
     * current tracker.
     */
    public void mouseDrag(MouseEvent e, int x, int y) {
        if(creator)
        {
            if (fChild != null) // JDK1.1 doesn't guarantee mouseDown, mouseDrag, mouseUp
                fChild.mouseDrag(e, x, y);
        }
    }

    /**
     * Handles mouse up events. The events are forwarded to the
     * current tracker.
     */
    public void mouseUp(MouseEvent e, int x, int y) {
        if (creator) {
            view().unfreezeView();
            if (fChild != null) // JDK1.1 doesn't guarantee mouseDown, mouseDrag, mouseUp
            {
                fChild.mouseUp(e, x, y);
                FigureEnumeration fe = view().selectionElements();
                Man m = null;
                Woman w = null;
                Mariage mm = null;
                Root r = null;

                while (fe.hasMoreElements()) {
                    Object f = fe.nextElement();
                    if (f instanceof Man) {
                        m = (Man) f;
                        m.updateMan();
                        creatorThread.sendMessage("u");
                    } else if (f instanceof Woman) {
                        w = (Woman) f;
                        w.updateWoman();
                        creatorThread.sendMessage("u");
                    } else if (f instanceof Root) {
                        r = (Root) f;
                        r.updateRoot();
                        creatorThread.sendMessage("u");
                    } else if (f instanceof Mariage) {
                        mm = (Mariage) f;
                        mm.updateMariage();
                        creatorThread.sendMessage("u");
                    }
                }
            }
            fChild = null;
        }
    }

    /**
     * Factory method to create a Handle tracker. It is used to track a handle.
     */
    protected Tool createHandleTracker(DrawingView view, Handle handle) {
        return new HandleTracker(view, handle);
    }

    /**
     * Factory method to create a Drag tracker. It is used to drag a figure.
     */
    protected Tool createDragTracker(DrawingView view, Figure f) {
        return new DragTracker(view, f);
    }

    /**
     * Factory method to create an area tracker. It is used to select an
     * area.
     */
    protected Tool createAreaTracker(DrawingView view) {
        return new SelectAreaTracker(view);
    }
}
