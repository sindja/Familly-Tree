/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package familytree;

import CH.ifa.draw.application.DrawApplication;
import static CH.ifa.draw.application.DrawApplication.IMAGES;
import CH.ifa.draw.figures.GroupFigure;
import CH.ifa.draw.framework.ConnectionFigure;
import CH.ifa.draw.framework.Drawing;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.FigureEnumeration;
import CH.ifa.draw.standard.StandardDrawingView;
import CH.ifa.draw.standard.ToolButton;
import Elements.Line;
import Elements.Man;
import Elements.Mariage;
import Elements.Root;
import Elements.Text;
import Elements.Woman;
import Tools.ManTool;
import Tools.MariageTool;
import Tools.RootTool;
import Tools.TxtTool;
import Tools.WomanTool;
import execution.CreatorThread;
import execution.ObserverThread;
import java.awt.Adjustable;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.ScrollPane;
import static java.awt.ScrollPane.SCROLLBARS_ALWAYS;
import Tools.CreatorSelectionTool;
import adapater.Adapter;
import clases.Ancestor;
import clases.QueueNumber;
import java.awt.Color;
import java.awt.Point;
import java.util.List;
import javaapplication3.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import static javaapplication3.Pocetna.openDrawing;
import org.hibernate.HibernateException;

/**
 *
 * @author Milan
 */
public class StartDraw extends DrawApplication {

    public static CreatorThread creatorThread;
    private ObserverThread observerThread;

    public static DrawingClass drawing;
    public boolean creator;
    public static String rootName;

    public StartDraw(DrawingClass drawing, boolean creator, String str, String txt) {
        super("Porodicno stablo- " + str + " application : " + txt);
        this.drawing = drawing;
        this.rootName = txt;
        this.creator = creator;

        if (creator) {
            creatorThread = new CreatorThread(this);
            creatorThread.start();
        } else {
            observerThread = new ObserverThread(this);
            observerThread.start();
            observerThread.run();
        }
    }

    public String getRootName() {
        return this.rootName;
    }

    public StartDraw(String title) {
        super(title);
        this.rootName = title;
    }

    @Override
    protected void createTools(Panel palette) {
        if (!creator) {
            super.createTools(palette);
        } else {
            CreatorSelectionTool sel = new CreatorSelectionTool(view(), true);
            ToolButton tb = createToolButton(IMAGES + "SEL", "Selection Tool", sel);
            palette.add(tb);
        
        ManTool manT = new ManTool(view(), new Man(), true);
        ToolButton manButton = new ToolButton(this, IMAGES + "MALE", "Ellipse Tool", manT);
        palette.add(manButton);
        WomanTool womanT = new WomanTool(view(), new Woman(), true);
        ToolButton womanButton = new ToolButton(this, IMAGES + "FEMALE", "Ellipse Tool", womanT);
        palette.add(womanButton);
        RootTool rootT = new RootTool(view(), new Root(), true);
        ToolButton rootButton = new ToolButton(this, IMAGES + "ROOT", "Ellipse Tool", rootT);
        palette.add(rootButton);
        TxtTool txtT = new TxtTool((view()), new Text());
        ToolButton txtTb = new ToolButton(this, IMAGES + "NAME", "Ellipse Tool", txtT);
        palette.add(txtTb);
        MariageTool mariageT = new MariageTool((view()), new Mariage(), true);
        ToolButton mariageButton = new ToolButton(this, IMAGES + "HEART", "Ellipse Tool", mariageT);
        palette.add(mariageButton);
        Tools.ConnectionTool connT = new Tools.ConnectionTool((view()), new Line(), true);
        ToolButton connButton = new ToolButton(this, IMAGES + "LINE", "Ellipse Tool", connT);
        connButton.setSize(40, 40);
        palette.setBackground(Color.yellow);
        palette.add(connButton);        
        setBackground(Color.red);
        palette.setSize(70, 5);
        }
    }

    @Override
    public void exit() {
        destroy();
        setVisible(false);
        if (!creator) {
            observerThread.running = false;
        }
        dispose();
        openDrawing = null;
        if (creator) {
            try{
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query q = session.createQuery("from Ancestor a where a.name = '" + rootName + "'");
            List drawings = q.list();
            Ancestor a = (Ancestor) drawings.get(0);            
            a.setBeingModified(false);            
            session.update(a);
            session.getTransaction().commit();
            q = session.createQuery("from QueueNumber a where a.rootId ="+ a.getId()+" ORDER BY a.id asc LIMIT 1  ");
            
            List queue = q.list();
            session.close();
            if (!queue.isEmpty())
            {
                 session = HibernateUtil.getSessionFactory().openSession();
                 session.beginTransaction();
                 q = session.createQuery("from Ancestor a where a.name = '" + rootName + "'");
                drawings = q.list();
                if(!drawings.isEmpty())
                    {
                        a = (Ancestor) drawings.get(0);
                     a.setBeingModified(true);            
                     session.update(a);
                     session.getTransaction().commit();
                    QueueNumber que = (QueueNumber) queue.get(0);
                    creatorThread.sendMessage(Integer.toString(que.getId()));
                }
                session.close();
            }
           
            }catch(HibernateException he)
             {
              he.printStackTrace();
             }      
             
        }
       /* else
        {
            try
            {
                Session session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                Query q = session.createQuery("from QueueNumber a where a.id = '" + observerThread.myQueueNumber.getId() + "'");
                List drawings = q.list();
                if(!drawings.isEmpty())
                 { 
                    QueueNumber   qq = (QueueNumber) drawings.get(0); 
                    session.delete(qq);
                    session.getTransaction().commit();
                 }
            }catch(HibernateException he)
             {
              he.printStackTrace();
             }
        }*/
    }

    @Override
    protected Component createContents(StandardDrawingView view) {
        ScrollPane sp = new ScrollPane(SCROLLBARS_ALWAYS);
        Adjustable vadjust = sp.getVAdjustable();
        Adjustable hadjust = sp.getHAdjustable();
        hadjust.setUnitIncrement(1);
        vadjust.setUnitIncrement(1);

        sp.add(view);
        return sp;
    }

    @Override
    public void open() {
        super.open();

        if (creator) {
            Adapter ada = new Adapter();
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query q = session.createQuery("from Ancestor a where a.name = '" + rootName + "'");
            List drawings = q.list();
             
            //Ancestor a = (Ancestor)session.get(Ancestor.class,100);
            if (drawings.size() != 0) {
                Ancestor a = (Ancestor) drawings.get(0);
               /* a.setBeingModified(true);
                session.update(a);
                session.getTransaction().commit();*/
                Root r = ada.toRoot(a);
                drawing.setRoot(r);

                Point t = new Point(r.getCordX(), r.getCordY());
                r.displayBox(t, t);
                drawing.setRoot(r);
                view().drawing().add(r);
                for (GroupFigure gf : r.getDescendants()) {
                    if (gf instanceof Man) {

                        //Figure f = (Figure) gf;
                        Man m = (Man) gf;
                        Point c = new Point(m.getCordX(), m.getCordY());
                        m.displayBox(c, c);
                        drawing.addMan(m);
                        view().drawing().add(m);
                        ConnectionFigure cf = new Line();
                        ConnectionFigure fConnection = (ConnectionFigure) cf.clone();
                        Line connectlline = (Line) fConnection;
                        fConnection.startPoint(c.x, c.y);
                        fConnection.endPoint(t.x, t.y);

                        connectlline.setStartX(c.x);
                        connectlline.setStartY(c.y);
                        connectlline.setEndX(t.x);
                        connectlline.setEndY(t.y);
                        fConnection.connectStart(m.connectorAt(c.x, c.y));
                        fConnection.connectEnd(r.connectorAt(t.x, t.y));
                        fConnection.updateConnection();
                        view().add(fConnection);
                    }
                    if (gf instanceof Woman) {
                        //Figure f = (Figure) gf;
                        Woman m = (Woman) gf;
                        Point c = new Point(m.getCordX(), m.getCordY());
                        m.displayBox(c, c);
                        view().drawing().add(m);
                        drawing.addWoman(m);
                        ConnectionFigure cf = new Line();
                        ConnectionFigure fConnection = (ConnectionFigure) cf.clone();
                        Line connectlline = (Line) fConnection;
                        fConnection.startPoint(c.x, c.y);
                        fConnection.endPoint(t.x, t.y);

                        connectlline.setStartX(c.x);
                        connectlline.setStartY(c.y);
                        connectlline.setEndX(t.x);
                        connectlline.setEndY(t.y);
                        fConnection.connectStart(m.connectorAt(c.x, c.y));
                        fConnection.connectEnd(r.connectorAt(t.x, t.y));
                        fConnection.updateConnection();
                        view().add(fConnection);
                    }

                }
                for (Mariage mm : r.getMariages()) {
                    mm.displayBox(new Point(mm.getCordX1(), mm.getCordY1()), new Point(mm.getCordX2(), mm.getCordY2()));
                    view().drawing().add(mm);
                    drawing.addMariage(mm);
                    for (GroupFigure gf : mm.getChildren()) {
                        if (gf instanceof Man) {
                            Man m = (Man) gf;
                            Point c = new Point(m.getCordX(), m.getCordY());
                            m.displayBox(c, c);
                            drawing.addMan(m);
                            view().drawing().add(m);
                            Point s = new Point(mm.getCordX1(), mm.getCordY1());
                            ConnectionFigure cf = new Line();
                            ConnectionFigure fConnection = (ConnectionFigure) cf.clone();
                            Line connectlline = (Line) fConnection;
                            fConnection.startPoint(c.x, c.y);
                            fConnection.endPoint(s.x, s.y);

                            connectlline.setStartX(c.x);
                            connectlline.setStartY(c.y);
                            connectlline.setEndX(s.x);
                            connectlline.setEndY(s.y);
                            fConnection.connectStart(m.connectorAt(c.x, c.y));
                            fConnection.connectEnd(mm.connectorAt(s.x, s.y));
                            fConnection.updateConnection();
                            view().add(fConnection);
                        } else if (gf instanceof Woman) {
                            Woman m = (Woman) gf;
                            Point c = new Point(m.getCordX(), m.getCordY());
                            m.displayBox(c, c);
                            view().drawing().add(m);
                            drawing.addWoman(m);
                            Point s = new Point(mm.getCordX1(), mm.getCordY1());
                            ConnectionFigure cf = new Line();
                            ConnectionFigure fConnection = (ConnectionFigure) cf.clone();
                            Line connectlline = (Line) fConnection;
                            fConnection.startPoint(c.x, c.y);
                            fConnection.endPoint(s.x, s.y);

                            connectlline.setStartX(c.x);
                            connectlline.setStartY(c.y);
                            connectlline.setEndX(s.x);
                            connectlline.setEndY(s.y);
                            fConnection.connectStart(m.connectorAt(c.x, c.y));
                            fConnection.connectEnd(mm.connectorAt(s.x, s.y));
                            fConnection.updateConnection();
                            view().add(fConnection);
                        }
                    }
                    for (GroupFigure gf : mm.getCouple()) {
                        if (gf instanceof Man) {
                            Man man = (Man) gf;
                            if (!checkMan(man)) {
                                Point c = new Point(man.getCordX(), man.getCordY());
                                man.displayBox(c, c);
                                drawing.addMan(man);
                                view().drawing().add(man);
                                Point s = new Point(mm.getCordX1(), mm.getCordY1());
                                ConnectionFigure cf = new Line();
                                ConnectionFigure fConnection = (ConnectionFigure) cf.clone();
                                Line connectlline = (Line) fConnection;
                                fConnection.startPoint(c.x, c.y);
                                fConnection.endPoint(s.x, s.y);

                                connectlline.setStartX(c.x);
                                connectlline.setStartY(c.y);
                                connectlline.setEndX(s.x);
                                connectlline.setEndY(s.y);
                                fConnection.connectStart(man.connectorAt(c.x, c.y));
                                fConnection.connectEnd(mm.connectorAt(s.x, s.y));
                                fConnection.updateConnection();
                                view().add(fConnection);
                            } else {

                                Figure f = findConnectableFigure(man.getCordX(), man.getCordY());
                                Man ma = (Man) f;
                                Point c = new Point(ma.getCordX(), ma.getCordY());

                                Point s = new Point(mm.getCordX2(), mm.getCordY2());
                                ConnectionFigure cf = new Line();
                                ConnectionFigure fConnection = (ConnectionFigure) cf.clone();
                                Line connectlline = (Line) fConnection;
                                fConnection.startPoint(c.x, c.y);
                                fConnection.endPoint(s.x, s.y);

                                connectlline.setStartX(c.x);
                                connectlline.setStartY(c.y);
                                connectlline.setEndX(s.x);
                                connectlline.setEndY(s.y);
                                fConnection.connectStart(ma.connectorAt(c.x, c.y));
                                fConnection.connectEnd(mm.connectorAt(s.x, s.y));
                                fConnection.updateConnection();
                                view().add(fConnection);
                            }
                        } else if (gf instanceof Woman) {
                            Woman woman = (Woman) gf;
                            if (!checkWoman(woman)) {
                                Point c = new Point(woman.getCordX(), woman.getCordY());
                                woman.displayBox(c, c);
                                drawing.addWoman(woman);
                                view().drawing().add(woman);
                                Point s = new Point(mm.getCordX1(), mm.getCordY1());
                                ConnectionFigure cf = new Line();
                                ConnectionFigure fConnection = (ConnectionFigure) cf.clone();
                                Line connectlline = (Line) fConnection;
                                fConnection.startPoint(c.x, c.y);
                                fConnection.endPoint(s.x, s.y);

                                connectlline.setStartX(c.x);
                                connectlline.setStartY(c.y);
                                connectlline.setEndX(s.x);
                                connectlline.setEndY(s.y);
                                fConnection.connectStart(woman.connectorAt(c.x, c.y));
                                fConnection.connectEnd(mm.connectorAt(s.x, s.y));
                                fConnection.updateConnection();
                                view().add(fConnection);
                            } else {
                                Figure f = findConnectableFigure(woman.getCordX(), woman.getCordY());
                                Woman ma = (Woman) f;
                                Point c = new Point(ma.getCordX(), ma.getCordY());
                                Point s = new Point(mm.getCordX1(), mm.getCordY1());
                                ConnectionFigure cf = new Line();
                                ConnectionFigure fConnection = (ConnectionFigure) cf.clone();
                                Line connectlline = (Line) fConnection;
                                fConnection.startPoint(c.x, c.y);
                                fConnection.endPoint(s.x, s.y);

                                connectlline.setStartX(c.x);
                                connectlline.setStartY(c.y);
                                connectlline.setEndX(s.x);
                                connectlline.setEndY(s.y);
                                fConnection.connectStart(ma.connectorAt(c.x, c.y));
                                fConnection.connectEnd(mm.connectorAt(s.x, s.y));
                                fConnection.updateConnection();
                                view().add(fConnection);
                            }
                        }
                    }
                }
            }
         session.close();
        } else {
            setView();
        }

    }

    public void setView() {
        
        Adapter ada = new Adapter();
        Session session =HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query q = session.createQuery("from Ancestor a where a.name = '" + rootName + "'");
        List drawings = q.list();
       
        //Ancestor a = (Ancestor)session.get(Ancestor.class,100);
        if (drawings != null) {
            FigureEnumeration k = view().drawing().figuresReverse();
            while (k.hasMoreElements()) {
                view().remove(k.nextFigure());

            }
            //view().drawing().removeAll(k);
            Ancestor a = (Ancestor) drawings.get(0);
            Root r = ada.toRoot(a);
            drawing.setRoot(r);
            Point t = new Point(r.getCordX(), r.getCordY());
            r.displayBox(t, t);
            drawing.setRoot(r);

            view().drawing().add(r);
            for (GroupFigure gf : r.getDescendants()) {
                if (gf instanceof Man) {

                    //Figure f = (Figure) gf;
                    Man m = (Man) gf;
                    Point c = new Point(m.getCordX(), m.getCordY());
                    m.displayBox(c, c);
                    drawing.addMan(m);
                    view().drawing().add(m);

                    // hajmoRekurzivno(m);  
                    ConnectionFigure cf = new Line();
                    ConnectionFigure fConnection = (ConnectionFigure) cf.clone();
                    Line connectlline = (Line) fConnection;
                    fConnection.startPoint(c.x, c.y);
                    fConnection.endPoint(t.x, t.y);

                    connectlline.setStartX(c.x);
                    connectlline.setStartY(c.y);
                    connectlline.setEndX(t.x);
                    connectlline.setEndY(t.y);
                    fConnection.connectStart(m.connectorAt(c.x, c.y));
                    fConnection.connectEnd(r.connectorAt(t.x, t.y));
                    fConnection.updateConnection();
                    view().add(fConnection);
                }
                if (gf instanceof Woman) {
                    //Figure f = (Figure) gf;
                    Woman m = (Woman) gf;
                    Point c = new Point(m.getCordX(), m.getCordY());
                    m.displayBox(c, c);
                    m.setTextLabel(m.getTextLabel());
                    view().drawing().add(m);
                    drawing.addWoman(m);
                    ConnectionFigure cf = new Line();
                    ConnectionFigure fConnection = (ConnectionFigure) cf.clone();
                    Line connectlline = (Line) fConnection;
                    fConnection.startPoint(c.x, c.y);
                    fConnection.endPoint(t.x, t.y);

                    connectlline.setStartX(c.x);
                    connectlline.setStartY(c.y);
                    connectlline.setEndX(t.x);
                    connectlline.setEndY(t.y);
                    fConnection.connectStart(m.connectorAt(c.x, c.y));
                    fConnection.connectEnd(r.connectorAt(t.x, t.y));
                    fConnection.updateConnection();
                    view().add(fConnection);
                }

            }
            for (Mariage mm : r.getMariages()) {
                mm.displayBox(new Point(mm.getCordX1(), mm.getCordY1()), new Point(mm.getCordX2(), mm.getCordY2()));
                view().drawing().add(mm);
                drawing.addMariage(mm);
                for (GroupFigure gf : mm.getChildren()) {
                    if (gf instanceof Man) {
                        Man m = (Man) gf;
                        Point c = new Point(m.getCordX(), m.getCordY());
                        m.displayBox(c, c);
                        view().drawing().add(m);
                        Point s = new Point(mm.getCordX1(), mm.getCordY1());
                        ConnectionFigure cf = new Line();
                        ConnectionFigure fConnection = (ConnectionFigure) cf.clone();
                        Line connectlline = (Line) fConnection;
                        fConnection.startPoint(c.x, c.y);
                        fConnection.endPoint(s.x, s.y);

                        connectlline.setStartX(c.x);
                        connectlline.setStartY(c.y);
                        connectlline.setEndX(s.x);
                        connectlline.setEndY(s.y);
                        fConnection.connectStart(m.connectorAt(c.x, c.y));
                        fConnection.connectEnd(mm.connectorAt(s.x, s.y));
                        fConnection.updateConnection();
                        view().add(fConnection);
                    } else if (gf instanceof Woman) {
                        Woman m = (Woman) gf;
                        Point c = new Point(m.getCordX(), m.getCordY());
                        m.displayBox(c, c);
                        view().drawing().add(m);
                        drawing.addWoman(m);
                        Point s = new Point(mm.getCordX1(), mm.getCordY1());
                        ConnectionFigure cf = new Line();
                        ConnectionFigure fConnection = (ConnectionFigure) cf.clone();
                        Line connectlline = (Line) fConnection;
                        fConnection.startPoint(c.x, c.y);
                        fConnection.endPoint(s.x, s.y);

                        connectlline.setStartX(c.x);
                        connectlline.setStartY(c.y);
                        connectlline.setEndX(s.x);
                        connectlline.setEndY(s.y);
                        fConnection.connectStart(m.connectorAt(c.x, c.y));
                        fConnection.connectEnd(mm.connectorAt(s.x, s.y));
                        fConnection.updateConnection();
                        view().add(fConnection);
                    }
                }
                for (GroupFigure gf : mm.getCouple()) {
                    if (gf instanceof Man) {
                        Man man = (Man) gf;
                        if (!checkMan(man)) {
                            Point c = new Point(man.getCordX(), man.getCordY());
                            man.displayBox(c, c); //drawing.addMan(man);
                            view().drawing().add(man);
                            Point s = new Point(mm.getCordX1(), mm.getCordY1());
                            ConnectionFigure cf = new Line();
                            ConnectionFigure fConnection = (ConnectionFigure) cf.clone();
                            Line connectlline = (Line) fConnection;
                            fConnection.startPoint(c.x, c.y);
                            fConnection.endPoint(s.x, s.y);
                            connectlline.setStartX(c.x);
                            connectlline.setStartY(c.y);
                            connectlline.setEndX(s.x);
                            connectlline.setEndY(s.y);
                            fConnection.connectStart(man.connectorAt(c.x, c.y));
                            fConnection.connectEnd(mm.connectorAt(s.x, s.y));
                            fConnection.updateConnection();
                            view().add(fConnection);
                        } else {

                            Figure f = findConnectableFigure(man.getCordX(), man.getCordY());
                            Man ma = (Man) f;
                            Point c = new Point(ma.getCordX(), ma.getCordY());

                            Point s = new Point(mm.getCordX2(), mm.getCordY2());
                            ConnectionFigure cf = new Line();
                            ConnectionFigure fConnection = (ConnectionFigure) cf.clone();
                            Line connectlline = (Line) fConnection;
                            fConnection.startPoint(c.x, c.y);
                            fConnection.endPoint(s.x, s.y);

                            connectlline.setStartX(c.x);
                            connectlline.setStartY(c.y);
                            connectlline.setEndX(s.x);
                            connectlline.setEndY(s.y);
                            fConnection.connectStart(ma.connectorAt(c.x, c.y));
                            fConnection.connectEnd(mm.connectorAt(s.x, s.y));
                            fConnection.updateConnection();
                            view().add(fConnection);
                        }
                    } else if (gf instanceof Woman) {
                        Woman woman = (Woman) gf;
                        if (!checkWoman(woman)) {
                            Point c = new Point(woman.getCordX(), woman.getCordY());
                            woman.displayBox(c, c);
                            //drawing.addWoman(woman);
                            view().drawing().add(woman);
                            Point s = new Point(mm.getCordX1(), mm.getCordY1());
                            ConnectionFigure cf = new Line();
                            ConnectionFigure fConnection = (ConnectionFigure) cf.clone();
                            Line connectlline = (Line) fConnection;
                            fConnection.startPoint(c.x, c.y);
                            fConnection.endPoint(s.x, s.y);

                            connectlline.setStartX(c.x);
                            connectlline.setStartY(c.y);
                            connectlline.setEndX(s.x);
                            connectlline.setEndY(s.y);
                            fConnection.connectStart(woman.connectorAt(c.x, c.y));
                            fConnection.connectEnd(mm.connectorAt(s.x, s.y));
                            fConnection.updateConnection();
                            view().add(fConnection);
                        } else {
                            Figure f = findConnectableFigure(woman.getCordX(), woman.getCordY());
                            Woman ma = (Woman) f;
                            Point c = new Point(ma.getCordX(), ma.getCordY());
                            Point s = new Point(mm.getCordX1(), mm.getCordY1());
                            ConnectionFigure cf = new Line();
                            ConnectionFigure fConnection = (ConnectionFigure) cf.clone();
                            Line connectlline = (Line) fConnection;
                            fConnection.startPoint(c.x, c.y);
                            fConnection.endPoint(s.x, s.y);

                            connectlline.setStartX(c.x);
                            connectlline.setStartY(c.y);
                            connectlline.setEndX(s.x);
                            connectlline.setEndY(s.y);
                            fConnection.connectStart(ma.connectorAt(c.x, c.y));
                            fConnection.connectEnd(mm.connectorAt(s.x, s.y));
                            fConnection.updateConnection();
                            view().add(fConnection);
                        }
                    }
                }
            }
        }
        session.close();
    }
    public void setView(String tx) { 
        
        if(observerThread != null  && observerThread.myQueueNumber.getId() == Integer.parseInt(tx))
        { 
           this.exit();
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query q = session.createQuery("from QueueNumber a where a.id = '" + Integer.parseInt(tx) + "'");
            List drawings = q.list();
            if(!drawings.isEmpty())
             { 
             QueueNumber   qq = (QueueNumber) drawings.get(0);           
             q = session.createQuery("from Ancestor a where a.id = '" + qq.getRootId() + "'");
             drawings = q.list();
            if (!drawings.isEmpty()) {
                 Ancestor t = (Ancestor) drawings.get(0);
                 t.setBeingModified(true);
                 session.delete(qq);
                 session.update(t);
                 session.getTransaction().commit();
                 StartDraw s = new StartDraw(new DrawingClass(t.getName()),true,"Server",t.getName());
                 s.open();
                
            }             
        }
        session.close();
        }
    }
    private Figure findConnectableFigure(int x, int y) {
        FigureEnumeration k = drawing().figuresReverse();
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

    public boolean checkMan(Man m) {
        int k = 0;
        for (Man ww : drawing.getMen()) {
            if (ww.getId() == m.getId()) {
                k = 1;
            }
        }
        if (k == 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkWoman(Woman w) {
        int k = 0;
        for (Woman ww : drawing.getWomen()) {
            if (ww.getId() == w.getId()) {
                k = 1;
            }
        }
        if (k == 0) {
            return false;
        } else {
            return true;
        }
    }
    @Override
    protected Dimension defaultSize() {
        return new Dimension(1000, 600);
    }
}
