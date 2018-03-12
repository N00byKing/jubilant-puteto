import java.awt.*;
public class EmuPanel extends Panel {
  
  private EmuCore ecore;
  
  public EmuPanel(EmuCore emu) {
    super();
    ecore = emu;
  }
  
  public void paint(Graphics g) {
    super.paintComponents(g);
    boolean[][] graph = ecore.getGFX();
    int scale = ecore.getScale();
    g.setColor(Color.WHITE);
    for (int i = 0; i < 64 ; i++ ) {
      for (int j = 0; j < 32 ; j++ ) {
        if (graph[i][j]) {
          g.fillRect(i*scale, j*scale, scale, scale);
        }
      } // end of for
    } // end of for
  }
}
