import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.event.*;
import java.io.File;

public class ControlCenter extends Frame {
  // Anfang Attribute
  private Button bShowEmulation = new Button();
  private Button bHideEmulation = new Button();
  private Button bShowDebugMenu = new Button();
  private Button bHideDebugMenu = new Button();
  private Label lbDebugMenuControl = new Label();
  private Label lbEmulatorControl = new Label();
  private Button bResetEmulator = new Button();
  private Button bLoadROM = new Button();
  private final JFileChooser fc;
  
  //Custom Attributes
  private DebugMenu dmenu;
  private EmuCore ecore;
  
  // Ende Attribute
  
  public ControlCenter() { 
    // Frame-Initialisierung
    super();
    addWindowListener(new WindowAdapter() {
    public void windowClosing(WindowEvent evt) { System.exit(0);; }
    });
    addComponentListener(new ComponentAdapter() { 
    public void componentMoved(ComponentEvent evt) { 
    ControlCenter_ComponentMoved(evt);
    }
    });
    int frameWidth = 473; 
    int frameHeight = 467;
    setSize(frameWidth, frameHeight);
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (d.width - getSize().width) / 2;
    int y = (d.height - getSize().height) / 2;
    setLocation(x, y);
    setTitle("ControlCenter");
    setResizable(false);
    Panel cp = new Panel(null);
    add(cp);
    // Anfang Komponenten
    
    bShowEmulation.setBounds(24, 112, 201, 57);
    bShowEmulation.setLabel("Show Emulation");
    bShowEmulation.addActionListener(new ActionListener() { 
    public void actionPerformed(ActionEvent evt) { 
    bShowEmulation_ActionPerformed(evt);
    }
    });
    bShowEmulation.setVisible(false);
    cp.add(bShowEmulation);
    bHideEmulation.setBounds(232, 112, 201, 57);
    bHideEmulation.setLabel("Hide Emulation");
    bHideEmulation.addActionListener(new ActionListener() { 
    public void actionPerformed(ActionEvent evt) { 
    bHideEmulation_ActionPerformed(evt);
    }
    });
    bHideEmulation.setVisible(false);
    cp.add(bHideEmulation);
    bShowDebugMenu.setBounds(24, 328, 201, 89);
    bShowDebugMenu.setLabel("Show Debug Menu");
    bShowDebugMenu.addActionListener(new ActionListener() { 
    public void actionPerformed(ActionEvent evt) { 
    bShowDebugMenu_ActionPerformed(evt);
    }
    });
    bShowDebugMenu.setVisible(false);
    cp.add(bShowDebugMenu);
    bHideDebugMenu.setBounds(232, 328, 201, 89);
    bHideDebugMenu.setLabel("Hide Debug Menu");
    bHideDebugMenu.addActionListener(new ActionListener() { 
    public void actionPerformed(ActionEvent evt) { 
    bHideDebugMenu_ActionPerformed(evt);
    }
    });
    bHideDebugMenu.setVisible(false);
    cp.add(bHideDebugMenu);
    
    lbDebugMenuControl.setBounds(96, 264, 270, 44);
    lbDebugMenuControl.setText("Debug Menu Control");
    lbDebugMenuControl.setFont(new Font("Dialog", Font.PLAIN, 24));
    lbDebugMenuControl.setAlignment(Label.CENTER);
    cp.add(lbDebugMenuControl);
    
    lbEmulatorControl.setBounds(96, 40, 283, 65);
    lbEmulatorControl.setText("Emulator Control");
    lbEmulatorControl.setFont(new Font("Dialog", Font.PLAIN, 24));
    lbEmulatorControl.setAlignment(Label.CENTER);
    cp.add(lbEmulatorControl);
    bResetEmulator.setBounds(24, 176, 201, 57);
    bResetEmulator.setLabel("Reset Emulator");
    bResetEmulator.addActionListener(new ActionListener() { 
    public void actionPerformed(ActionEvent evt) { 
    bResetEmulator_ActionPerformed(evt);
    }
    });
    bResetEmulator.setVisible(false);
    cp.add(bResetEmulator);
    bLoadROM.setBounds(232, 176, 201, 57);
    bLoadROM.setLabel("Load ROM");
    bLoadROM.addActionListener(new ActionListener() { 
    public void actionPerformed(ActionEvent evt) { 
    bLoadROM_ActionPerformed(evt);
    }
    });
    cp.add(bLoadROM);
    
    fc = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Chip-8 ROM", new String[] {"ch8", "c8"});
    fc.setFileFilter(filter);
    // Ende Komponenten
    
    setVisible(true);
    
  } // end of public ControlCenter
  
  // Anfang Methoden
  
  public static void main(String[] args) {
    new ControlCenter();
  } // end of main
  
  public void bShowEmulation_ActionPerformed(ActionEvent evt) {
    ecore.setVisible(true);
  } // end of bShowEmulation_ActionPerformed

  public void bHideEmulation_ActionPerformed(ActionEvent evt) {
    ecore.setVisible(false);
  } // end of bHideEmulation_ActionPerformed

  public void bShowDebugMenu_ActionPerformed(ActionEvent evt) {
    dmenu.setLocation((getLocationOnScreen().x + getWidth()), getLocationOnScreen().y);
    dmenu.setVisible(true);
  } // end of bShowDebugMenu_ActionPerformed

  public void bHideDebugMenu_ActionPerformed(ActionEvent evt) {
    dmenu.setVisible(false);
    
  } // end of bHideDebugMenu_ActionPerformed

  public void ControlCenter_ComponentMoved(ComponentEvent evt) {
    if (dmenu != null && dmenu.isVisible()) {
      dmenu.setLocation((getLocationOnScreen().x + getWidth()), getLocationOnScreen().y);
    }
  } // end of ControlCenter_ComponentMoved

  public void bResetEmulator_ActionPerformed(ActionEvent evt) {
    ecore.reset(); 
    dmenu.reset();
  } // end of bResetEmulator_ActionPerformed

  public void bLoadROM_ActionPerformed(ActionEvent evt) {
    
    int returnVal = fc.showOpenDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File romfile = fc.getSelectedFile();
      
      ecore = new EmuCore("CHIP-8 Emulator", romfile, 10, 10, false);
      dmenu = new DebugMenu(ecore);
      bShowEmulation.setVisible(true);
      bShowDebugMenu.setVisible(true);
      bHideEmulation.setVisible(true);
      bHideDebugMenu.setVisible(true);
      bResetEmulator.setVisible(true);
      bLoadROM.setVisible(false);
    }
  } // end of bLoadROM_ActionPerformed
    
    // Ende Methoden
} // end of class ControlCenter

