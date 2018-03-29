import java.awt.*;
public class EmuPanel extends Panel {
  
  private EmuCore ecore;
  private Color foreground;
  public EmuPanel(EmuCore emu, Color fg, Color bg) {
    super();
    setBackground(bg);
    ecore = emu;
    foreground = fg;
  }
  
  public void paint(Graphics g) {
    super.paintComponents(g);
    boolean[][] graph = ecore.getGFX();
    int scale = ecore.getScale();
    g.setColor(foreground);
    for (int i = 0; i < 64 ; i++ ) {
      for (int j = 0; j < 32 ; j++ ) {
        if (graph[i][j]) {
          g.fillRect(i*scale, j*scale, scale, scale);
        }
      } // end of for
    } // end of for
  }
  
  public void setDrawColor(Color fg) {
    foreground = fg;  
    
  }
  
  public Color getDrawColor() {
    return foreground;  
  }
}
