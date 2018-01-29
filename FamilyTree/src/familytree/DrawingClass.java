/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package familytree;

import java.util.HashSet;
import java.util.Set;
import Elements.*;

/**
 *
 * @author Milan
 */
public class DrawingClass {

    int drawingID;
    String drawingName;
    private Set<Man> men = new HashSet<>();
    private Set<Woman> women = new HashSet<>();
    private Set<Mariage> mariages = new HashSet<>();
    private Set<Root> roots = new HashSet<>();
    private Root root;

    public void makeRoot() {
        root = new Root();
    }

    public void setRoot(Root r) {
       // makeRoot();
        root = r;
    }

    public Root getRoot() {
        return root;
    }

    public DrawingClass() {

    }

    public DrawingClass(String drawingName) {
        this.drawingName = drawingName;
    }

    public int getDrawingID() {
        return drawingID;
    }

    public void setDrawingID(int drawingID) {
        this.drawingID = drawingID;
    }

    public String getDrawingName() {
        return drawingName;
    }

    public void setDrawingName(String drawingName) {
        this.drawingName = drawingName;
    }

    public Set<Man> getMen() {
        return men;
    }

    public void setMen(Set<Man> Men) {
        this.men = Men;
    }

    public Set<Woman> getWomen() {
        return women;
    }

    public void setWomen(Set<Woman> Women) {
        this.women = Women;
    }

    public Set<Mariage> getMariages() {
        return mariages;
    }

    public void setMariages(Set<Mariage> Mariages) {
        this.mariages = Mariages;
    }

    public Set<Root> getRoots() {
        return roots;
    }

    public void setRoots(Set<Root> roots) {
        this.roots = roots;
    }

    public void addMan(Man w) {
        int k = 0;
        for (Man ww : men) {
            if (ww.getId() == w.getId()) {
                k = 1;
            }
        }
        if (k == 0) {
            men.add(w);
        }
    }

    public void addWoman(Woman w) {

        int k = 0;
        for (Woman ww : women) {
            if (ww.getId() == w.getId()) {
                k = 1;
            }
        }
        if (k == 0) {
            women.add(w);
        }

    }

    public void addMariage(Mariage m) {
        if (!mariages.contains(m)) {
            mariages.add(m);
        }
    }
    public void addRoot(Root r) {
        if (!roots.contains(r)) {
            roots.add(r);
        }
    }

    public void removeMan(Man m) {
        for (Man mm : men) {
            if (mm.getId() == m.getId()) {
                men.remove(mm);
            }
        }
    }

    public void removeWoman(Woman m) {
        for (Woman mm : women) {
            if (mm.getId() == m.getId()) {
                women.remove(mm);
            }
        }
    }

    public void removeMariage(Mariage m) {
        for (Mariage mm : mariages) {
            if (mm.getId() == m.getId()) {
                mariages.remove(mm);
            }
        }
    }

    public void removeRoot(Root m) {
        for (Root mm : roots) {
            if (mm.getId() == m.getId()) {
                roots.remove(mm);
            }
        }
    }
}
