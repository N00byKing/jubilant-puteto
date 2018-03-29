import java.awt.*;
import java.awt.event.*;

public class DebugMenu extends Frame {
  // Anfang Attribute
  private EmuCore ecore;
  private EmuPanel corePanel;
  private Thread runthread;
  private Runnable emurun;
  
  private Button bRun = new Button();
  private Button bStep = new Button();
  
  private TextArea taRAM = new TextArea("", 1, 1, TextArea.SCROLLBARS_VERTICAL_ONLY);
  
  private Label lRegisters = new Label();
  private Label lRAMInspector = new Label();
  
  private Label[] lRegs;
  private Label lV0 = new Label();
  private Label lV1 = new Label();
  private Label lV2 = new Label();
  private Label lV3 = new Label();
  private Label lV4 = new Label();
  private Label lV5 = new Label();
  private Label lV6 = new Label();
  private Label lV7 = new Label();
  private Label lV8 = new Label();
  private Label lV9 = new Label();
  private Label lV10 = new Label();
  private Label lV11 = new Label();
  private Label lV12 = new Label();
  private Label lV13 = new Label();
  private Label lV14 = new Label();
  private Label lV15VF = new Label();
  
  private TextField[] tfRegs;
  private TextField tfV0 = new TextField();
  private TextField tfV1 = new TextField();
  private TextField tfV2 = new TextField();
  private TextField tfV3 = new TextField();
  private TextField tfV4 = new TextField();
  private TextField tfV5 = new TextField();
  private TextField tfV6 = new TextField();
  private TextField tfV7 = new TextField();
  private TextField tfV8 = new TextField();
  private TextField tfV9 = new TextField();
  private TextField tfV10 = new TextField();
  private TextField tfV11 = new TextField();
  private TextField tfV12 = new TextField();
  private TextField tfV13 = new TextField();
  private TextField tfV14 = new TextField();
  private TextField tfV15 = new TextField();
  private Button bStop = new Button();
  private Label lI = new Label();
  private TextField tfI = new TextField();
  private Label lTimers = new Label();
  private Label lDelay = new Label();
  private Label lSound = new Label();
  private TextField tfDelay = new TextField();
  private TextField tfSound = new TextField();
  private Label lNextOpcode = new Label();
  private TextField tfOpcode = new TextField();
  private Label lSettings = new Label();
  private Label lScale = new Label();
  private Label lCycleDelay = new Label();
  private TextField tfScale = new TextField();
  private TextField tfCycle = new TextField();
  private Button bSave = new Button();
  private Label lForeground = new Label();
  private TextField tfForeground = new TextField();
  private Label lBackground = new Label();
  private TextField tfBackground = new TextField();
  // Ende Attribute
  
  public DebugMenu(EmuCore pEcore) { 
    // Frame-Initialisierung
    super();
    addWindowListener(new WindowAdapter() {
    public void windowClosing(WindowEvent evt) { dispose(); }
    });
    int frameWidth = 794; 
    int frameHeight = 487;
    setSize(frameWidth, frameHeight);
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (d.width - getSize().width) / 2;
    int y = (d.height - getSize().height) / 2;
    setLocation(x, y);
    setTitle("Debug Menu");
    setResizable(false);
    Panel cp = new Panel(null);
    add(cp);
    // Anfang Komponenten
    
    bRun.setBounds(16, 16, 105, 49);
    bRun.setLabel("Run");
    bRun.addActionListener(new ActionListener() { 
    public void actionPerformed(ActionEvent evt) { 
    bRun_ActionPerformed(evt);
    }
    });
    cp.add(bRun);
    bStep.setBounds(128, 16, 105, 49);
    bStep.setLabel("Step");
    bStep.addActionListener(new ActionListener() { 
    public void actionPerformed(ActionEvent evt) { 
    bStep_ActionPerformed(evt);
    }
    });
    cp.add(bStep);
    taRAM.setBounds(560, 72, 193, 313);
    taRAM.setEditable(false);
    taRAM.setFont(new Font("Courier", Font.PLAIN, 18));
    cp.add(taRAM);
    lRAMInspector.setBounds(560, 24, 190, 36);
    lRAMInspector.setText("RAM Inspector");
    lRAMInspector.setFont(new Font("Courier", Font.PLAIN, 24));
    cp.add(lRAMInspector);
    
    
    lRegisters.setBounds(384, 24, 134, 36);
    lRegisters.setText("Registers");
    lRegisters.setFont(new Font("Courier", Font.PLAIN, 24));
    cp.add(lRegisters);
    lV0.setBounds(328, 72, 54, 28);
    lV0.setText("V0");
    cp.add(lV0);
    lV2.setBounds(328, 112, 54, 28);
    lV2.setText("V2");
    cp.add(lV2);
    lV5.setBounds(448, 152, 54, 28);
    lV5.setText("V5");
    cp.add(lV5);
    lV7.setBounds(448, 192, 54, 28);
    lV7.setText("V7");
    cp.add(lV7);
    lV11.setBounds(448, 272, 54, 28);
    lV11.setText("V11");
    cp.add(lV11);
    lV10.setBounds(328, 272, 54, 28);
    lV10.setText("V10");
    cp.add(lV10);
    lV14.setBounds(328, 352, 54, 28);
    lV14.setText("V14");
    cp.add(lV14);
    lV3.setBounds(448, 112, 54, 28);
    lV3.setText("V3");
    cp.add(lV3);
    lV4.setBounds(328, 152, 54, 28);
    lV4.setText("V4");
    cp.add(lV4);
    lV1.setBounds(448, 72, 54, 28);
    lV1.setText("V1");
    cp.add(lV1);
    lV8.setBounds(328, 232, 54, 28);
    lV8.setText("V8");
    cp.add(lV8);
    lV9.setBounds(448, 232, 54, 28);
    lV9.setText("V9");
    cp.add(lV9);
    lV13.setBounds(448, 312, 54, 28);
    lV13.setText("V13");
    cp.add(lV13);
    lV12.setBounds(328, 312, 54, 28);
    lV12.setText("V12");
    cp.add(lV12);
    lV15VF.setBounds(448, 352, 54, 28);
    lV15VF.setText("V15/VF");
    cp.add(lV15VF);
    lV6.setBounds(328, 192, 54, 28);
    lV6.setText("V6");
    cp.add(lV6);
    tfV0.setBounds(384, 72, 49, 33);
    tfV0.setEditable(false);
    cp.add(tfV0);
    tfV2.setBounds(384, 112, 49, 33);
    tfV2.setEditable(false);
    cp.add(tfV2);
    tfV4.setBounds(384, 152, 49, 33);
    tfV4.setEditable(false);
    cp.add(tfV4);
    tfV6.setBounds(384, 192, 49, 33);
    tfV6.setEditable(false);
    cp.add(tfV6);
    tfV8.setBounds(384, 232, 49, 33);
    tfV8.setEditable(false);
    cp.add(tfV8);
    tfV10.setBounds(384, 272, 49, 33);
    tfV10.setEditable(false);
    cp.add(tfV10);
    tfV12.setBounds(384, 312, 49, 33);
    tfV12.setEditable(false);
    cp.add(tfV12);
    tfV1.setBounds(504, 72, 49, 33);
    tfV1.setEditable(false);
    cp.add(tfV1);
    tfV3.setBounds(504, 112, 49, 33);
    tfV3.setEditable(false);
    cp.add(tfV3);
    tfV5.setBounds(504, 152, 49, 33);
    tfV5.setEditable(false);
    cp.add(tfV5);
    tfV7.setBounds(504, 192, 49, 33);
    tfV7.setEditable(false);
    cp.add(tfV7);
    tfV9.setBounds(504, 232, 49, 33);
    tfV9.setEditable(false);
    cp.add(tfV9);
    tfV11.setBounds(504, 272, 49, 33);
    tfV11.setEditable(false);
    cp.add(tfV11);
    tfV13.setBounds(504, 312, 49, 33);
    tfV13.setEditable(false);
    cp.add(tfV13);
    tfV15.setBounds(504, 352, 49, 33);
    tfV15.setEditable(false);
    cp.add(tfV15);
    tfV14.setBounds(384, 352, 49, 33);
    tfV14.setEditable(false);
    cp.add(tfV14);
    bStop.setBounds(16, 72, 105, 49);
    bStop.setLabel("Stop");
    bStop.addActionListener(new ActionListener() { 
    public void actionPerformed(ActionEvent evt) { 
    bStop_ActionPerformed(evt);
    }
    });
    bStop.setVisible(false);
    cp.add(bStop);
    lI.setBounds(448, 392, 54, 28);
    lI.setText("I");
    cp.add(lI);
    tfI.setBounds(504, 392, 49, 33);
    tfI.setEditable(false);
    cp.add(tfI);
    lTimers.setBounds(200, 136, 91, 49);
    lTimers.setText("Timers");
    lTimers.setFont(new Font("Courier", Font.PLAIN, 24));
    cp.add(lTimers);
    lDelay.setBounds(192, 200, 54, 28);
    lDelay.setText("Delay");
    cp.add(lDelay);
    lSound.setBounds(188, 240, 54, 28);
    lSound.setText("Sound");
    cp.add(lSound);
    tfDelay.setBounds(256, 200, 49, 33);
    tfDelay.setEditable(false);
    cp.add(tfDelay);
    tfSound.setBounds(256, 240, 49, 33);
    tfSound.setEditable(false);
    cp.add(tfSound);
    lNextOpcode.setBounds(560, 400, 91, 28);
    lNextOpcode.setText("Next Opcode");
    cp.add(lNextOpcode);
    tfOpcode.setBounds(664, 400, 84, 33);
    tfOpcode.setEditable(false);
    cp.add(tfOpcode);
    lSettings.setBounds(32, 136, 115, 49);
    lSettings.setText("Settings");
    lSettings.setFont(new Font("Courier", Font.PLAIN, 24));
    cp.add(lSettings);
    lScale.setBounds(16, 200, 54, 28);
    lScale.setText("Scale");
    cp.add(lScale);
    lCycleDelay.setBounds(16, 240, 68, 28);
    lCycleDelay.setText("Cycle Delay");
    cp.add(lCycleDelay);
    tfScale.setBounds(96, 200, 49, 33);
    cp.add(tfScale);
    tfCycle.setBounds(96, 240, 49, 33);
    cp.add(tfCycle);
    bSave.setBounds(32, 376, 105, 49);
    bSave.setLabel("Save");
    bSave.addActionListener(new ActionListener() { 
    public void actionPerformed(ActionEvent evt) { 
    bSave_ActionPerformed(evt);
    }
    });
    cp.add(bSave);
    lForeground.setBounds(16, 280, 69, 28);
    lForeground.setText("Foreground");
    cp.add(lForeground);
    tfForeground.setBounds(96, 280, 49, 33);
    cp.add(tfForeground);
    lBackground.setBounds(16, 320, 75, 28);
    lBackground.setText("Background");
    cp.add(lBackground);
    tfBackground.setBounds(96, 320, 49, 33);
    cp.add(tfBackground);
    // Ende Komponenten
    
    tfRegs = new TextField[] {
    tfV0, tfV1, tfV2, tfV3, tfV4, tfV5, tfV6, tfV7,
    tfV8, tfV9, tfV10, tfV11, tfV12, tfV13, tfV14, tfV15
    };
    
    ecore = pEcore;
    
    emurun = () -> {
      ecore.run();
    };
     
    runthread = new Thread(emurun);
    
    update(); 
  } // end of public DebugMenu
  
  // Anfang Methoden
  public void update() {
    corePanel = ecore.getPanel();
    
    updateRAM(ecore.getMemory());
    updateRegisters(ecore.getRegisters(), ecore.getI());
    updateTimers(ecore.getSDelay(), ecore.getDDelay());
    updateSettings(ecore.getScale(), ecore.getCycleDelay(), corePanel.getDrawColor(), corePanel.getBackground());
    tfOpcode.setText("0x" + String.format("%04X", ecore.getOpcode()));
  }
  
  public void reset() {
    ecore.stop();
    bStop.setVisible(false);
    bStep.setVisible(true);
    update();
  }
  
  private void updateRAM(int[] ram) {
    String result = "";
    
    for (int i = 0; i < 4096 ; i++) {
      int tempPC = ecore.getPC();
      if (i == tempPC) {
        taRAM.setCaretPosition(result.length());
        result = result + "*0x" + String.format("%04X", i) + ": " + String.format("%02X", ram[i]) + "\n";
        
      } else {
        result = result + " 0x" + String.format("%04X", i) + ": " + String.format("%02X", ram[i]) + "\n";
      } // end of if-else
    } // end of for
    
    taRAM.setText(result);
  }
  
  private void updateTimers(int st, int dt) {
    tfSound.setText(Integer.toString(st));
    tfDelay.setText(Integer.toString(dt));
  } 
    
  private void updateRegisters(int[] regs, int I) {
    for (int i = 0; i < 16 ; i++ ) {
      tfRegs[i].setText(Integer.toString(regs[i]));
    } // end of for
    
    tfI.setText(Integer.toString(I));
  }
  
  private void updateSettings(int scl, int cdly, Color fg, Color bg)  {
    tfScale.setText(Integer.toString(scl));
    tfCycle.setText(Integer.toString(cdly));
    tfForeground.setText(Integer.toHexString(fg.getRGB()).substring(2).toUpperCase());
    tfBackground.setText(Integer.toHexString(bg.getRGB()).substring(2).toUpperCase());
  }
  
  public void bRun_ActionPerformed(ActionEvent evt) {
    runthread = new Thread(emurun);
    bStop.setVisible(true);
    bStep.setVisible(false);
    runthread.start();
  } // end of bRun_ActionPerformed

  public void bStep_ActionPerformed(ActionEvent evt) {
    ecore.step();
    update();  
  } // end of bStep_ActionPerformed

  public void bStop_ActionPerformed(ActionEvent evt) {
    ecore.stop();
    bStop.setVisible(false);
    bStep.setVisible(true);
    update();
  } // end of bStop_ActionPerformed

  public void bSave_ActionPerformed(ActionEvent evt) {
    ecore.setCycleDelay(Integer.parseInt(tfCycle.getText()));
    ecore.setScale(Integer.parseInt(tfScale.getText()));
    corePanel.setDrawColor(Color.decode("0x" + tfForeground.getText()));
    corePanel.setBackground(Color.decode("0x" + tfBackground.getText()));
  } // end of bSave_ActionPerformed

  // Ende Methoden
} // end of class DebugMenu

