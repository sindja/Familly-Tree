/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;
import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.Handle;
import CH.ifa.draw.framework.Tool;
import CH.ifa.draw.standard.AbstractTool;
import CH.ifa.draw.standard.DragTracker;
import CH.ifa.draw.standard.HandleTracker;
import CH.ifa.draw.standard.SelectAreaTracker;
import java.awt.event.MouseEvent;
/**
 *
 * @author Milan
 */
public class ObserverSelectionTool extends AbstractTool {
    
    private Tool fChild = null;
    private boolean ind = false;

    public ObserverSelectionTool(DrawingView view) {
        super(view);
    }

    /**
     * Handles mouse down events and starts the corresponding tracker.
     */
    public void mouseDown(MouseEvent e, int x, int y)
    {
        // Do nothing..       
    }

    /**
     * Handles mouse drag events. The events are forwarded to the
     * current tracker.
     */
    public void mouseDrag(MouseEvent e, int x, int y) {
        // Do nothing.. 
    }

    /**
     * Handles mouse up events. The events are forwarded to the
     * current tracker.
     */
    public void mouseUp(MouseEvent e, int x, int y) {
        // Do nothing.. 
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
