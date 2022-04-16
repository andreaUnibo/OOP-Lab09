package it.unibo.oop.lab.reactivegui02;

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



public class ConcurrentGUI  extends JFrame {

    
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
  
    
    public ConcurrentGUI() {
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
        System.out.println("ciao");
        this.setVisible(true);
    
        
        final Agent agent = new Agent();
        new Thread(agent).start();
        
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                // Agent should be final
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
                // Agent should be final
                agent.reverse();
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
                // Agent should be final
                agent.reverse();
            }
        });
        
  
    }


   private class Agent implements Runnable{

    
    private volatile boolean stop;
    private volatile int counter;
    private int sgn = 1;
    
    public Agent() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void run() {
    
        while(!this.stop) {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    @Override
                    public void run() {
                        // This will happen in the EDT: since i'm reading counter it needs to be volatile.
                        ConcurrentGUI.this.display.setText(Integer.toString(Agent.this.counter));   
                    }
                });
                
                 this.counter = this.counter + this.sgn;
                 System.out.println(this.counter);
                 Thread.sleep(100);
                
            } catch (InvocationTargetException | InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        
            
    }
        
    }
    
    public void reverse() {
        this.sgn= -1 * this.sgn;
    
    }
    public void stopCounting() {
        this.stop = true;
    }
} 
    
}