import java.awt.*;
import java.io.*;
import java.util.Random;
import java.awt.event.*;

public class EmuCore extends Frame {
  
  //
  // CHIP-8 System Attributes
  //
    
    //opcode: Current CPU Instruction
  private int opcode;
    
    //memory[4096]: CHIP-8 Memory
  private int[] memory;
    
    //V[16]: CPU Registers
  private int[] V;
   
    //I: Index Register
  private int I;
    
    //pc: Program Counter
  private int pc;
  
    //stack: CHIP-8 Stack
  private int[] stack;
  
    //sp: CHIP-8 Stack pointer
  private byte sp;
  
    //dTime & sTime: Delay and Sound Timers
  private int dTime;
  private int sTime;
  
    //chipFont: Integrated CHIP-8 Font storage
  private final short[] chipFont;
  
    //gfx: Array storing the Display Values
  private boolean[][] gfx;
   
  //
  // Emulator Attributes (irrelevant to the emulated CPU)
  //
  
    //EPanel: Emulator Panel
  private EmuPanel EPanel;
  
    //scale: Scales the Size of the Window
  private int scale;
  
    //isLogging: Determines whether or not the Emulator will log its operations
  private boolean isLogging;
  
    //ROMStorage: CHIP-8 ROM Storage
  private byte[] ROMStorage;
  
    //ROMFile: The CHIP-8 ROM
  private File ROMFile;
  
    //logger: The log file writer
  private BufferedWriter logger;
  
    //running: true if the Emulator is running
  private boolean running;
  
    //romSize: Size of ROM File
  private int romSize;
  
    //cycleDelay: Fakes a passage of time to better represent CHIP-8 Timings
  private int cycleDelay;
  
    //keyList: Keymap Array
  private boolean[] keyList;
  
    //rand: Instance of Class Random
  private Random rand;
  
  // Ende Attribute
  
  //
  // Constructor
  //
  
  public EmuCore(String title, File romfile, int scl, int cdelay, boolean logging) {
    super(title);
    
    setResizable(false);
    EPanel = new EmuPanel(this, Color.WHITE, Color.BLACK);
    add(EPanel);
    setScale(scl);
    
    
    ROMFile = romfile;
    isLogging = logging;
    cycleDelay = cdelay;
    
    rand = new Random();
    gfx = new boolean[64][32];
    memory = new int[4096];
    V = new int[16];
    stack = new int[16];
    keyList = new boolean[16];
    pc = 0x200;
    chipFont = new short[] {
    0xF0, 0x90, 0x90, 0x90, 0xF0, // 0
    0x20, 0x60, 0x20, 0x20, 0x70, // 1
    0xF0, 0x10, 0xF0, 0x80, 0xF0, // 2
    0xF0, 0x10, 0xF0, 0x10, 0xF0, // 3
    0x90, 0x90, 0xF0, 0x10, 0x10, // 4
    0xF0, 0x80, 0xF0, 0x10, 0xF0, // 5
    0xF0, 0x80, 0xF0, 0x90, 0xF0, // 6
    0xF0, 0x10, 0x20, 0x40, 0x40, // 7
    0xF0, 0x90, 0xF0, 0x90, 0xF0, // 8
    0xF0, 0x90, 0xF0, 0x10, 0xF0, // 9
    0xF0, 0x90, 0xF0, 0x90, 0x90, // A
    0xE0, 0x90, 0xE0, 0x90, 0xE0, // B
    0xF0, 0x80, 0x80, 0x80, 0xF0, // C
    0xE0, 0x90, 0x90, 0x90, 0xE0, // D
    0xF0, 0x80, 0xF0, 0x80, 0xF0, // E
    0xF0, 0x80, 0xF0, 0x80, 0x80  // F
    };
    
    FileWriter filewriter = null;
    if (logging) {
      try {
        filewriter = new FileWriter("emu.log");
        logger = new BufferedWriter(filewriter);
        logger.write("Emulator Initialization finished"  + "\n");
      } catch(Exception e) {
        System.out.println("Error during Logger Initialization" + e.toString());
        System.exit(1);
      }
    }
    
    initializeMemory();
    
    // Anfang Komponenten
    EPanel.addKeyListener(new KeyAdapter() { 
    public void keyPressed(KeyEvent evt) { 
    EPanel_KeyPressed(evt);
    }
    });
  }// Ende Komponenten
  // Anfang Methoden
  
  public boolean[][] getGFX() {
    return gfx;
  }
  
  public int getScale() {
    return scale;
  }
  
  public void setScale(int scl) {
    scale = scl;
    EPanel.setPreferredSize(new Dimension(64*scale,32*scale));
    revalidate();
    pack();
    EPanel.repaint();
  }
  
  public int getCycleDelay() {
    return cycleDelay;
  }
  
  public void setCycleDelay(int cdelay) {
    cycleDelay = cdelay;
  }
  
  public int[] getMemory() {
    return memory;
  }
  
  public int[] getRegisters() {
    return V;
  }
  
  public int getI() {
    return I;
  }
  
  public int getOpcode() {
    return ((memory[pc] << 8) | memory[pc + 1]); 
  }
  
  public int getPC() {
    return pc;
  }
  
  public int getSDelay() {
    return sTime;
  }
  
  public int getDDelay() {
    return dTime;
  }
  
  public EmuPanel getPanel(){
    return EPanel;
  }
  
  public boolean isRunning() {
    return running;
  }
  
  public void run() {
    running = true;
    while (running) { 
      cycleCPU();
    } // end of while
  }
  
  public void step() {
    cycleCPU();
  }
  
  public void stop() {
    running = false;
  }
  
  public void reset() {
    running = false;
    gfx = new boolean[64][32];
    memory = new int[4096];
    V = new int[16];
    keyList = new boolean[16];
    stack = new int[16];
    pc = 0x200;
    I = 0;
    
    EPanel.repaint();
    
    initializeMemory();
  }
  
  private void cycleCPU() {
    opcode = getOpcode();
    if (isLogging) {
      try {
        logger.write("Running Instruction: " + String.format("%04X", opcode) + "\n");
        logger.flush();
      } catch(Exception e) {
        System.out.println("Error during logging of opcode");
      } 
    }
    
    switch (opcode & 0xF000) {
      case 0x0000:
        switch (opcode) {
          case 0x00E0: 
            for (int i = 0; i < 64 ; i++ ) {
              for (int j = 0; j < 32 ; j++ ) {
                gfx[i][j] = false;
              } // end of for
            } // end of for
            pc += 2;
            break;
          case 0x00EE: 
            pc = (short)stack[(sp - 1)];
            sp--;
            break;
        } // end of switch
        break;
      case 0x1000:
        pc = (opcode & 0x0FFF);
        break;
      case 0x2000:
        stack[sp] = pc + 2;
        sp++;
        pc = (opcode & 0x0FFF);
        break;
      case 0x3000:
        if (V[((opcode & 0x0F00) >> 8)] == (opcode & 0x00FF)) {
          pc += 4;
        } else {
          pc += 2;
        }
        break;
      case 0x4000:
        if (V[((opcode & 0x0F00) >> 8)] != (opcode & 0x00FF)) {
          pc += 4;
        } else {
          pc += 2;
        }
        break;
      case 0x5000:
        if (V[((opcode & 0x0F00) >> 8)] == V[((opcode & 0x00F0) >> 4)]) {
          pc += 4;
        }
        else {
          pc += 2;
        }
        break;
      case 0x6000:
        V[(opcode & 0x0F00) >> 8] = (opcode & 0x00FF);
        pc += 2;
        break;
      case 0x7000:
        int targ7000 = ((opcode & 0x0F00) >> 8);
        V[targ7000] = (opcode & 0x00FF) + V[targ7000];
        pc += 2;
        break;
      case 0x8000:
        switch (opcode & 0x000F) {
          case 0x0000:
            V[((opcode & 0x0F00) >> 8)] = V[((opcode & 0x00F0) >> 4)];
            pc += 2;
            break;
          case 0x0001: 
            int targ8001 = ((opcode & 0x0F00) >> 8);
            
            V[targ8001] = V[((opcode & 0x00F0) >> 4)] | V[targ8001];
            pc += 2;
            break;
          case 0x0002: 
            int targ8002 = ((opcode & 0x0F00) >> 8);
            
            V[targ8002] = V[((opcode & 0x00F0) >> 4)] & V[targ8002];
            pc += 2;
            break;
          case 0x0003: 
            int targ8003 = ((opcode & 0x0F00) >> 8);
            
            V[targ8003] = V[((opcode & 0x00F0) >> 4)] ^ V[targ8003];
            pc += 2;
            break;
          case 0x0004: 
            int targ8004 = ((opcode & 0x0F00) >> 8);
            
            V[targ8004] = V[((opcode & 0x00F0) >> 4)] + V[targ8004];
            if (V[targ8004] > 255) {
              V[15] = 1;
              V[targ8004] = V[targ8004] & 0xFF; 
            } else {
              V[15] = 0;
            }
            pc += 2;
            break;
          case 0x0005: 
            int targ8005 = ((opcode & 0x0F00) >> 8);
            int targ80052 = ((opcode & 0x00F0) >> 4);
            
            if (V[targ8005] > V[targ80052]) {
              V[15] = 1;
            } else {
              V[15] = 0;
            }
            V[targ8005] = V[targ8005] - V[targ80052];
            pc += 2;
            break;
          case 0x0006: 
            int targ8006 = ((opcode & 0x0F00) >> 8);
            
            if ((V[targ8006] & 1) == 1) {
              V[15] = 1;
            } else {
              V[15] = 0;
            } // end of if-else
            V[targ8006] = V[targ8006] / 2;
            pc += 2;
            break;
          case 0x0007: 
            int targ8007 = ((opcode & 0x0F00) >> 8);
            int targ80072 = ((opcode & 0x00F0) >> 4);
            
            if (V[targ80072] > V[targ8007]) {
              V[15] = 1;
            } else {
              V[15] = 0;
            }
            V[targ8007] = V[targ80072] - V[targ8007];
            pc += 2;
            break;
          case 0x000E: 
            int targ800E = ((opcode & 0x0F00) >> 8);
            
            if (((V[targ800E] & 0xFF) >> 7 ) == 1) {
              V[15] = 1;
            } else {
              V[15] = 0;
            } // end of if-else
            V[targ800E] = V[targ800E] * 2;
            pc += 2;
            break;
            
        } // end of switch
        break;
      case 0x9000:
        if (V[((opcode & 0x0F00) >> 8)] != V[((opcode & 0x00F0) >> 4)]) {
          pc += 4;
        }
        else {
          pc += 2;
        }
        break;
      case 0xA000:
        I = (opcode & 0x0FFF);
        pc += 2;
        break;
      case 0xB000:
        pc = (opcode & 0xFFF) + V[0];
        break;
      case 0xC000:
        V[((opcode & 0x0F00) >> 8)] = (rand.nextInt(256) + 1) & (opcode & 0x00FF);
        pc += 2;
        break;
      case 0xD000:
        int xpos = V[((opcode & 0x0F00) >> 8)];
        int ypos = V[((opcode & 0x00F0) >> 4)];
        int bytelength = (opcode & 0x000F);
        String byteholder;
        
        V[15] = 0;
        
        for (int i = 0; i < bytelength; i++ ) {
          
          byteholder = String.format("%8s", Integer.toBinaryString(memory[i + I]));
          ypos = wrapAround(ypos, i, 32);
          
          for (int j = 0; j < byteholder.length(); j++ ) {
            
            xpos = wrapAround(xpos, j, 72);
            
            if (byteholder.charAt(j) == '1' && gfx[xpos+j][ypos+i]) {
              V[15] = 1;
              gfx[xpos+j][ypos+i] = false;
            } else if (byteholder.charAt(j) == ' ' && !gfx[xpos+j][ypos+i]) {
              gfx[xpos+j][ypos+i] = false;
            } else if (byteholder.charAt(j) == ' ' && gfx[xpos+j][ypos+i]) {
              gfx[xpos+j][ypos+i] = true;
            } else if (byteholder.charAt(j) == '1' && !gfx[xpos+j][ypos+i]) {
              gfx[xpos+j][ypos+i] = true;
            } // end of if-else
          } // end of for
        } // end of for
        pc += 2;
        break;
      case 0xE000:
        switch (opcode & 0x00FF) {
          case 0x009E:
            int targE09E = ((opcode & 0x0F00) >> 8);
            if (keyList[V[targE09E]]) {
              pc += 4;
              keyList[V[targE09E]] = false;
            } else {
              pc += 2;
            } // end of if-else
            break;
          case 0x00A1: 
            int targE0A1 = ((opcode & 0x0F00) >> 8);
            if (!keyList[V[targE0A1]]) {
              pc += 4;
            } else {
              pc += 2;
              keyList[V[targE0A1]] = false;;
            } // end of if-else
            break;
        } // end of switch
        break;
      case 0xF000:
        switch (opcode & 0x00FF) {
          case 0x0007: 
            V[((opcode & 0x0F00) >> 8)] = dTime;
            pc += 2;
            break;
          case 0x000A:
            int targF00A = ((opcode & 0x0F00) >> 8);
            boolean keyPressed = false; 
            do {
              for (int i = 0; i < 16 ; i++ ) {
                if (keyList[i] = true) {
                  keyPressed = true;
                  V[targF00A] = i;
                }
              } // end of for
            } while (!keyPressed); // end of do-while
            pc += 2;
            break;
          case 0x0015: 
            dTime = V[((opcode & 0x0F00) >> 8)];
            pc += 2;
            break;
          case 0x0018: 
            sTime = V[((opcode & 0x0F00) >> 8)];
            pc += 2;
            break;
          case 0x001E: 
            I = I + V[((opcode & 0x0F00) >> 8)];
            pc += 2;
            break;
          case 0x0029: 
            I = V[((opcode & 0x0F00) >> 8)] * 5;
            pc += 2;
            break;
          case 0x0033: 
            String targF033 = String.format("%03d", V[((opcode & 0x0F00) >> 8)]);
            for (int i = 0; i < 3 ; i++ ) {
              memory[I+i] = Integer.parseInt(Character.toString(targF033.charAt(i)));
            } // end of for
            pc += 2;
            break;
          case 0x0055: 
            int targF055 = ((opcode & 0x0F00) >> 8);
            for (int i = 0; i <= targF055 ; i++ ) {
              memory[I+i] = V[i];
            } // end of for
            pc += 2;
            break;
          case 0x0065: 
            int targF065 = ((opcode & 0x0F00) >> 8);
            for (int i = 0; i <= targF065 ; i++ ) {
              V[i] = memory[I+i];
            } // end of for
            pc += 2;
            break;
        } // end of switch
    } // end of switch
    
    if (isRunning()) {
      try {
        Thread.sleep(cycleDelay);
      } catch(Exception e) {
        
      }
    }
    
    updateTimers();
    EPanel.repaint();
  }
    
  private void updateTimers() {
    if (dTime > 0 || sTime > 0) {
      if (dTime > 0) {
        if (dTime > 3) {
          dTime -= 3;
        } else {
          dTime = 0;
        } // end of if-else
      } else {
        if (sTime > 3) {
          sTime -= 3;
          Toolkit.getDefaultToolkit().beep();
        } else {
          sTime = 0;
        } // end of if-else
      } // end of if-else
      
    }
  }  
  
  private int wrapAround(int pos, int add, int max) {
    if ((pos + add) >= max) {
      pos = (pos + add) - max;
      pos = wrapAround(pos, add, max);
    }
    return pos;
  }
    
  private void initializeMemory() {
    //Load Font into Memory
    for (int i = 0; i < 80; i++ ) {
      memory[i] = chipFont[i];
    }
    
    //Load ROM into Memory at 0x200 (512)
    loadROM(ROMFile);
    
    for (int i = 0; i < romSize; i++ ) {
      memory[512 + i] = (ROMStorage[i] & 0xFF);
    } // end of for
  }
    
  private void loadROM(File rfile) {
    try {
      FileInputStream ROMReader = new FileInputStream(rfile);
      romSize = (int)ROMReader.getChannel().size();
      ROMStorage = new byte[romSize];
      ROMReader.read(ROMStorage);
      ROMReader.close();
    } catch(Exception e) {
      System.out.println("Error during ROM Read: " + e.toString());
      System.exit(1);
    }
  }
  public void EPanel_KeyPressed(KeyEvent evt) {
    for (int i = 0; i < 16 ; i++ ) {
      keyList[i] = false;
    } // end of for
    
    switch (evt.getKeyChar()) {
      case '1' : 
        keyList[0x1] = true;
        break;
      case '2' : 
        keyList[0x2] = true;
        break;
      case '3': 
        keyList[0x3] = true;
        break;
      case '4': 
        keyList[0xC] = true;
        break;
      case 'q': 
        keyList[0x4] = true;
        break;
      case 'w': 
        keyList[0x5] = true;
        break;
      case 'e': 
        keyList[0x6] = true;
        break;
      case 'r': 
        keyList[0xD] = true;
        break;
      case 'a': 
        keyList[0x7] = true;
        break;
      case 's': 
        keyList[0x8] = true;
        break;
      case 'd': 
        keyList[0x9] = true;
        break;
      case 'f': 
        keyList[0xE] = true;
        break;
      case 'y': 
        keyList[0xA] = true;
        break;
      case 'x': 
        keyList[0x0] = true;
        break;
      case 'c': 
        keyList[0xB] = true;
        break;
      case 'v': 
        keyList[0xF] = true;
        break;
    } // end of switch
    
  } // end of EmuCore_KeyPressed

  // Ende Methoden
}
