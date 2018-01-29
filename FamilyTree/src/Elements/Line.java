/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Elements;

import CH.ifa.draw.figures.LineConnection;
import CH.ifa.draw.framework.Drawing;
import Elements.*;
/**
 *
 * @author Milan
 */
public class Line extends LineConnection{
    private int Id;
    private int startFigureId;
    private int endFigureId;
    private int startX;
    private int startY;    
    private int endX;
    private int endY;
    private Drawing drw;
    public Line()
    {
        drw = null;
    }
    public int getId()
    {
        return this.Id;
    }
    public void setId(int idP)
    {
        this.Id = idP;
    }
    public void setDrw(Drawing drws) {
        this.drw = drws;
    }
    public int getStartFigureId()
    {
        return this.startFigureId;
    }
    public void setStartFigureId(int x)
    {
        this.startFigureId = x;
    }
    public int getEndFigureId()
    {
        return this.endFigureId;
    }
    public void setEndFigureId(int x)
    {
        this.endFigureId = x;
    }
    public int getStartX()
    {
        return this.startX;
    }
    public void setStartX(int x)
    {
        this.startX = x;
    }
    public int getStartY()
    {
        return this.startY;
    }
    public void setStartY(int y)
    {
        this.startY = y;
    }
    public int getEndX()
    {
        return this.endX;
    }
    public void setEndX(int x)
    {
        this.endX = x;
    }
    public int getEndY()
    {
        return this.endY;
    }
    public void setEndY(int y)
    {
        this.endY = y;
    }
     @Override
    public void release() {      
        
        handleDisconnect(startFigure(), endFigure());
        if (fStart != null) 
        {
            startFigure().removeFigureChangeListener(this);
                    
                              
           
        }
        if (fEnd   != null)
        { 
            endFigure().removeFigureChangeListener(this);
           
           
        }               
    }  
}
