/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import CH.ifa.draw.standard.CreationTool;
import CH.ifa.draw.framework.Drawing.*;
import CH.ifa.draw.framework.ConnectionFigure;
import CH.ifa.draw.framework.Connector;
import CH.ifa.draw.framework.Drawing;
import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.FigureEnumeration;
import CH.ifa.draw.standard.CreationTool;
import CH.ifa.draw.util.Geom;
import Elements.*;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import static familytree.StartDraw.creatorThread;
/**
 *
 * @author Milan
 */
public class ConnectionTool extends CreationTool {

    private ConnectionFigure fPrototype;

    private Figure fTarget = null;
    private Man targetElementM = null;
    private Woman targetElementW = null;
    private Mariage targetElementMM = null;
    private Root targetElementR = null;
    private Connector fStartConnector;
    private Connector fEndConnector;
    private Connector fConnectorTarget = null;
    private Line connectlline = null;

    private ConnectionFigure fConnection;
    private boolean creator;
    private int fSplitPoint;

    private ConnectionFigure fEditedConnection = null;

    public ConnectionTool(DrawingView view, ConnectionFigure prototype, boolean p) {
        super(view);
        fPrototype = prototype;
        this.creator = p;
    }

    @Override
    public void activate() {
        view().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }

    @Override
    public void mouseDown(MouseEvent e, int x, int y) {

        int ex = e.getX();
        int ey = e.getY();
        fTarget = findConnectionStart(ex, ey, drawing());
        if (fTarget instanceof Man) {
            targetElementM = (Man) findConnectionStart(ex, ey, drawing());
        }
        if (fTarget instanceof Woman) {
            targetElementW = (Woman) findConnectionStart(ex, ey, drawing());
        }
        if (fTarget instanceof Root) {
            targetElementR = (Root) findConnectionStart(ex, ey, drawing());
        }
        if (fTarget != null && (fTarget instanceof Man || fTarget instanceof Woman || fTarget instanceof Root)) {

            fStartConnector = findConnector(ex, ey, fTarget);
            if (fStartConnector != null) {
                Point p = new Point(ex, ey);
                fConnection = createConnection();
                connectlline = (Line) fConnection;
                fConnection.startPoint(p.x, p.y);
                fConnection.endPoint(p.x, p.y);
                connectlline.setStartX(p.x);
                connectlline.setStartY(p.y);
                connectlline.setEndX(p.x);
                connectlline.setEndY(p.y);
                view().add(fConnection);
            }
        }
        /* else {
            ConnectionFigure connection = findConnection(ex, ey, drawing());
            if (connection != null) {
                if (!connection.joinSegments(ex, ey)) {
                    fSplitPoint = connection.splitSegment(ex, ey);
                    fEditedConnection = connection;
                    
                } else {
                    fEditedConnection = null;
                    
                }
            }
        }*/
    }

    @Override
    public void mouseDrag(MouseEvent e, int x, int y) {
        if (creator) {
            Point p = new Point(e.getX(), e.getY());
            if (fConnection != null) {
                trackConnectors(e, x, y);
                if (fConnectorTarget != null) {
                    p = Geom.center(fConnectorTarget.displayBox());
                }
                fConnection.endPoint(p.x, p.y);
            } else if (fEditedConnection != null) {
                Point pp = new Point(x, y);
                fEditedConnection.setPointAt(pp, fSplitPoint);
            }
        }
    }

    @Override
    public void mouseUp(MouseEvent e, int x, int y) {
        if (creator) {
            Figure c = null;
            Man elM = null;
            Woman elW = null;
            Mariage elMM = null;
            Root elR = null;

            if (fStartConnector != null) {
                c = findTarget(e.getX(), e.getY(), drawing());
                if (c instanceof Mariage) {
                    elMM = (Mariage) findTarget(e.getX(), e.getY(), drawing());
                }
                if (c instanceof Root) {
                    elR = (Root) c;
                }

            }
            connectlline.setDrw(drawing());
            if (c != null && elR != null && c instanceof Root) {
                fEndConnector = findConnector(e.getX(), e.getY(), c);
                if (fEndConnector != null) {
                    fConnection.connectStart(fStartConnector);
                    fConnection.connectEnd(fEndConnector);
                    fConnection.updateConnection();
                    if (targetElementM != null || targetElementR != null) {
                        targetElementM.setColor(Math.abs(elR.getRed() - 40), Math.abs(elR.getGreen() - 40), Math.abs(elR.getBlue() - 40));
                        targetElementM.updateMan();
                        elR.addDescandan(targetElementM);
                        elR.updateRoot();

                    }
                    if (targetElementW != null) {

                        targetElementW.setColor(Math.abs(elR.getRed() - 40), Math.abs(elR.getGreen() - 40), Math.abs(elR.getBlue() - 40));
                        targetElementW.updateWoman();
                        elR.addDescandan(targetElementW);
                        elR.updateRoot();
                    }
                    connectlline.setEndX(e.getX());
                    connectlline.setEndY(e.getY());

                }
            } else if (c != null && elMM != null && c instanceof Mariage) {

                if (!elMM.getHasWoman() && !elMM.getHasMan()) {
                    fEndConnector = findConnector(e.getX(), e.getY(), c);
                    if (fEndConnector != null) {
                        fConnection.connectStart(fStartConnector);
                        fConnection.connectEnd(fEndConnector);
                        fConnection.updateConnection();

                        connectlline.setEndX(e.getX());
                        connectlline.setEndY(e.getY());
                        if (targetElementM != null || targetElementR != null) {
                            elMM.setHasMan(true);
                            elMM.setColor(Math.abs(targetElementM.getRed()), Math.abs(targetElementM.getGreen()), Math.abs(targetElementM.getBlue()));
                            if (targetElementM != null) {
                                elMM.addCouple(targetElementM);
                            } else {
                                elMM.addCouple(targetElementR);
                            }
                            elMM.updateMariage();
                        }
                        if (targetElementW != null) {
                            elMM.setHasWoman(true);
                            elMM.setColor(Math.abs(targetElementW.getRed()), Math.abs(targetElementW.getGreen()), Math.abs(targetElementW.getBlue()));
                            elMM.addCouple(targetElementW);
                            elMM.updateMariage();
                        }
                    }
                } else if (targetElementM != null && elMM.getHasWoman()) {

                    elMM.setHasMan(true);
                    fEndConnector = findConnector(e.getX(), e.getY(), c);
                    if (fEndConnector != null) {
                        fConnection.connectStart(fStartConnector);
                        fConnection.connectEnd(fEndConnector);
                        fConnection.updateConnection();

                        connectlline.setEndX(e.getX());
                        connectlline.setEndY(e.getY());
                    }
                    if (elMM.getSecondArrived()) {
                        targetElementM.setColor(Math.abs(elMM.getRed() - 40), Math.abs(elMM.getGreen() - 40), Math.abs(elMM.getBlue() - 40));
                        targetElementM.updateMan();
                        elMM.addChild(targetElementM);
                        elMM.updateMariage();
                    } else {
                        elMM.setSecondArrived(true);
                        elMM.addCouple(targetElementM);
                        elMM.updateMariage();
                    }
                } else if (targetElementW != null && elMM.getHasMan()) {
                    elMM.setHasWoman(true);
                    fEndConnector = findConnector(e.getX(), e.getY(), c);
                    if (fEndConnector != null) {
                        fConnection.connectStart(fStartConnector);
                        fConnection.connectEnd(fEndConnector);
                        fConnection.updateConnection();

                        connectlline.setEndX(e.getX());
                        connectlline.setEndY(e.getY());
                    }
                    if (elMM.getSecondArrived()) {
                        targetElementW.setColor(Math.abs(elMM.getRed() - 40), Math.abs(elMM.getGreen() - 40), Math.abs(elMM.getBlue() - 40));
                        targetElementW.updateWoman();
                        elMM.addChild(targetElementW);
                        elMM.updateMariage();
                    } else {
                        elMM.setSecondArrived(true);
                        elMM.addCouple(targetElementW);
                        elMM.updateMariage();
                    }
                } else {
                    drawing().remove(connectlline);
                }

            } else {
                drawing().remove(connectlline);
            }
            creatorThread.sendMessage("u");
            fConnection = null;
            connectlline = null;
            fStartConnector = fEndConnector = null;
            targetElementM = null;
            targetElementW = null;
            targetElementR = null;
            editor().toolDone();

        }
    }

    @Override
    public void mouseMove(MouseEvent e, int x, int y) {
        if (creator) {
            trackConnectors(e, x, y);
        }
    }

    protected void trackConnectors(MouseEvent e, int x, int y) {
        Figure c = null;

        if (fStartConnector == null) {
            c = findSource(x, y, drawing());
        } else {
            c = findTarget(x, y, drawing());
        }
        if (c != fTarget) {
            if (fTarget != null) {
                fTarget.connectorVisibility(false);
            }
            fTarget = c;
            if (fTarget != null) {
                fTarget.connectorVisibility(true);
            }
        }
    }

    protected Figure findConnectionStart(int x, int y, Drawing drawing) {
        Figure target = findConnectableFigure(x, y, drawing);
        if ((target != null)) {
            return target;
        }
        return null;
    }

    protected Figure findSource(int x, int y, Drawing drawing) {
        return findConnectableFigure(x, y, drawing);
    }

    protected Figure findTarget(int x, int y, Drawing drawing) {
        Figure target = findConnectableFigureT(x, y, drawing);
        Figure start = fStartConnector.owner();

        if (target != null
                && fConnection != null
                && target.canConnect()
                && !target.includes(start) //&& fConnection.canConnect(start, target)
                ) {
            return target;
        }
        return null;
    }

    private Figure findConnectableFigureT(int x, int y, Drawing drawing) {
        FigureEnumeration k = drawing.figuresReverse();
        while (k.hasMoreElements()) {
            Figure figure = k.nextFigure();
            if (figure.canConnect() && !(figure instanceof Line) && (figure instanceof Mariage || figure instanceof Root)) {
                if (figure.containsPoint(x, y)) {
                    return figure;
                }
            }
        }
        return null;
    }

    private Figure findConnectableFigure(int x, int y, Drawing drawing) {
        FigureEnumeration k = drawing.figuresReverse();
        while (k.hasMoreElements()) {
            Figure figure = k.nextFigure();
            if (figure.canConnect() && !(figure instanceof Line)) {
                if (figure.containsPoint(x, y)) {
                    return figure;
                }
            }
        }
        return null;
    }

    public Connector findConnector(int x, int y, Figure f) {
        return f.connectorAt(x, y);
    }

    protected ConnectionFigure createConnection() {
        return (ConnectionFigure) fPrototype.clone();
    }

    protected ConnectionFigure findConnection(int x, int y, Drawing drawing) {
        Enumeration k = drawing.figuresReverse();
        while (k.hasMoreElements()) {
            Figure figure = (Figure) k.nextElement();
            figure = figure.findFigureInside(x, y);
            if (figure != null && (figure instanceof ConnectionFigure)) {
                return (ConnectionFigure) figure;
            }
        }
        return null;
    }
}
