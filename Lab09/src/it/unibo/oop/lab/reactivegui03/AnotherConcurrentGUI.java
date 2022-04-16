package it.unibo.oop.lab.reactivegui03;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

//import it.unibo.oop.lab.reactivegui01.ConcurrentGUI.Agent;


public class AnotherConcurrentGUI extends JFrame {

    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final double WIDTH_PERC = 0.2;
    private static final double HEIGHT_PERC = 0.1;
    final JLabel display = new JLabel();
    private final JButton stop = new JButton("stop");
    private final JButton up = new JButton("up");
    private final JButton down = new JButton("down");
  
    
    public AnotherConcurrentGUI() {
        super();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int)(screenSize.getWidth() * WIDTH_PERC), (int)(screenSize.getHeight() * HEIGHT_PERC));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel p = new JPanel();
       
        p.add(display);
       
        p.add(up);
        p.add(down);
        p.add(stop);
        
        this.getContentPane().add(p);

        this.setVisible(true);
    
        
        final Agent agent = new Agent();
        final Agent2 a2 = new Agent2(stop,up,down,agent);
        new Thread(agent).start();
        new Thread(a2).start();
        
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
              
                agent.stopCounting();
            }
        });
        
        up.addActionListener(new ActionListener() {
            /**
             * event handler associated to action event on button stop.
             * 
             * @param e
             *            the action event that will be handled by this listener
             */
            @Override
            public void actionPerformed(final ActionEvent e) {
               
                agent.up();
            }
        });
        
        down.addActionListener(new ActionListener() {
            /**
             * event handler associated to action event on button stop.
             * 
             * @param e
             *            the action event that will be handled by this listener
             */
            @Override
            public void actionPerformed(final ActionEvent e) {
            
                agent.down();
            }
        });
        
        
    }

    
    
   private class Agent2 implements Runnable{

       
      JButton stop;
      JButton up;
      JButton down;
      Agent a;
      
    public Agent2(JButton stop, JButton up, JButton down,Agent a) {
        this.down = down;
        this.stop = stop;
        this.up = up;
        this.a = a;
    }
    
    
  
    @Override
    public void run() {
        
        try {
            Thread.sleep(10000);
            stop.setEnabled(false);
            down.setEnabled(false);
            up.setEnabled(false);
            a.stopCounting();
            
        } catch (InterruptedException e) {
       
            e.printStackTrace();
        }
        
    }
       
   }
    
    
   private class Agent implements Runnable{

    
    private volatile boolean stop;
    private volatile int counter;
    private int sgn = 1;
    
    public Agent() {
     
    }

    @Override
    public void run() {
    
        while(!this.stop) {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    @Override
                    public void run() {
                        // This will happen in the EDT: since i'm reading counter it needs to be volatile.
                        AnotherConcurrentGUI.this.display.setText(Integer.toString(Agent.this.counter));   
                    }
                });
                
                 this.counter = this.counter + this.sgn;
                 System.out.println(this.counter);
                 Thread.sleep(100);
                
            } catch (InvocationTargetException | InterruptedException e) {
              
                e.printStackTrace();
            }
        
            
    }
        
    }
    
    public void up() {
        if(this.sgn == -1)
        this.sgn= 1;
    
    }
    
    public void down() {
        if(this.sgn == 1)
        this.sgn= -1;
    
    }
    public void stopCounting() {
        this.stop = true;
    }
} 
    
}