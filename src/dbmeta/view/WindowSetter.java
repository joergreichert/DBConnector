package dbmeta.view;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
/**
 * positioniert ein gegebenes Fenster auf dem Bildschirm 
 * @author Jörg Reichert
 */
public class WindowSetter {

    /**
     * öffnet ein gegebenes Fenster an Hand seiner Größe und
     * der aktuellen Bildschirmauflösung genau in der Mitte
     * des Bildschirms 
     * @param comp gegebenes Fensters
     */
    public static void openWindow(Container comp) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int width  = (int) d.getWidth();
        int height = (int) d.getHeight();
        int x = comp.getPreferredSize().width;
        int y = comp.getPreferredSize().height;
        int startX = (int) ((width -  x) / 2);
        int startY = (int) ((height - y) / 2);
        comp.setBounds(startX, startY, x, y);
      } // setWindow
} // WindowSetter
