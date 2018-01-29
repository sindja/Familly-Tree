/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;
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
import CH.ifa.draw.figures.*;
import Elements.Woman;
import Elements.Man;
import Elements.*;
import static familytree.StartDraw.creatorThread;
/**
 *
 * @author Milan
 */
public class TxtTool extends  TextTool {
    private FloatingTextField   fTextField;
    private TextHolder  fTypingTarget;
    private Man m=null;
    private Woman w=null;
    private Root r=null;
    
    
    public TxtTool(DrawingView view, Figure prototype) {
        super(view, prototype);
    }
    public void activate() {
        super.activate();
        view().clearSelection();
        // JDK1.1 TEXT_CURSOR has an incorrect hot spot
        view().setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
    }
     private Rectangle fieldBounds(TextHolder figure) {
    	Rectangle box = figure.textDisplayBox();
    	int nChars = figure.overlayColumns();
        Dimension d = fTextField.getPreferredSize(nChars);
        return new Rectangle(box.x, box.y, d.width, d.height);
    }
      public void deactivate() {
        super.deactivate();
        endEdit();
    }
     /*@Override
    public void mouseDown(MouseEvent e, int x, int y)
    {
            Figure pressedFigure;
	    TextFigure textFigure = null;
	    pressedFigure = drawing().findFigureInside(x, y);
            if (pressedFigure instanceof TextFigure)
            {
                textFigure=(TextFigure)pressedFigure;
                //textFigure.setAttribute("text", "ovo je moja");
                textFigure.setText("ovo je moja kuca ziveo sam tu");                
            }
    }*/
    @Override
    public void mouseDown(MouseEvent e, int x, int y)
    {
      Figure pressedFigure,pressedFigure1;    
           
            TextHolder textHolder = null;
              pressedFigure1 =  drawing().findFigure(x, y);
	    pressedFigure = drawing().findFigureInside(x, y);
            if(pressedFigure1 instanceof Man)
            {                
                m=(Man)pressedFigure1;                
            }
            else if (pressedFigure1 instanceof Woman)
                w=(Woman)pressedFigure1;
            else if(pressedFigure1 instanceof Root)
                r=(Root)pressedFigure1;
            
            if (pressedFigure instanceof TextFigure)
            {   
                textHolder=(TextFigure)pressedFigure;
                beginEdit(textHolder);  
                if (!textHolder.acceptsTyping())
	            textHolder = null;
            }
            if (textHolder != null) {
	        beginEdit(textHolder);
	        return;
	    }
	    if (fTypingTarget != null) {
	        editor().toolDone();
	        endEdit();
	    } 
             
    }
    /*@Override
    public void mouseDrag(MouseEvent e, int x, int y) {
        Figure pressedFigure;
	    TextFigure textFigure = null;
	    pressedFigure = drawing().findFigureInside(x, y);
            if (pressedFigure instanceof TextFigure)
            {
                
                textFigure=(TextFigure)pressedFigure;
                //textFigure.setAttribute("text", "ovo je moja");
                 beginEdit(textFigure);
                //textFigure.setText("ovo je moja kuca ziveo sam tu");                
            }
    }*/
    public void beginEdit(TextHolder figure) {
        if (fTextField == null)
            fTextField = new FloatingTextField();

	    if (figure != fTypingTarget && fTypingTarget != null)
	        endEdit();
        fTextField.createOverlay((Container)view(), figure.getFont());
	    fTextField.setBounds(fieldBounds(figure), figure.getText());
	    fTypingTarget = figure;
    }
     protected void endEdit() {
	    if (fTypingTarget != null) {	                  
              fTypingTarget.setText("    "+fTextField.getText());                    
              if(m != null)
              {
                  m.setTextLabel("  "+ fTextField.getText());
                  m.updateMan();
                  creatorThread.sendMessage("u");
              }
              if(w != null)
              {
                  w.setTextLabel("  "+fTextField.getText());
                  w.updateWoman();
                  creatorThread.sendMessage("u");
              }
              if(r != null)
              {
                  r.setTextLabel("  "+fTextField.getText());
                  r.updateRoot();
                  creatorThread.sendMessage("u");
              }
	      fTypingTarget = null;                
	      fTextField.endOverlay();
              fTextField=null;
              m = null;
              w = null;
              r = null;
	      view().checkDamage();
	    }
    }
}
