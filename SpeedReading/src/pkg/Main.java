package pkg;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main extends JFrame implements ActionListener,ItemListener{
	
	private FrameDragListener frameDragListener;
	
	private JLabel msg;
	private JLabel speedlb;
	private JButton start;
	private JButton stop;
	private JButton pause;
	private JButton infobtn;
	private JScrollBar scrollbar;
	
	private JMenuBar menuBar;
	private JMenu setFontSize;
    private JMenuItem small;
    private JMenuItem medium;
    private JMenuItem large;
    private JMenu setFontStyle;
    private JMenuItem bold;
    private JMenuItem italic;
    private JMenuItem plain;
    private JMenuItem close;
    private JCheckBoxMenuItem  ontop;
	
	private Timer timer;

	private Font mainfont;
	
	private int counter;
	private int speed = 300;
	private double wps = 3.3; // words per second
	
	private int fontstyle = Font.BOLD;
	private int fontsize = 19;
	
	public static void main(String[] args){
		new Main();
	}
	
	public Main()
	{
		setMenu();
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SwingUtilities.updateComponentTreeUI(this);

		
		start();
	}
	
	public void start()
	{

	   msg = getmsgLabel();
	   speedlb = getSpeedLabel();
	   start = getStartButton();
	   pause = getPauseButton();
	   stop = getStopButton();
	   infobtn = getInfoButton();
	   scrollbar = getScrollBar();

	   add(msg);
	   add(speedlb);
	   add(start);
	   add(pause);
	   add(stop);
	   add(infobtn);
	   add(scrollbar);
	   
	   setFrameparam();
	   setListeners();

	}
	
	private void StartReading()
	{
		String data = getClipboard();
		
		if (isURL(data))
		{
			getContentFromUrl(data);
		}
		else
		{
			getContentFromString(data);
		}
	}
	
	private void getContentFromString(String data)
	{
		data = data.replace("." ," ");
		data = data.replace("," , " ");
		String[] words = data.split(" ");
		ActionListener listener = new ActionListener(){
	        public void actionPerformed(ActionEvent event){
	        	if (counter >= words.length){
	        		timer.stop();
	        	}
	        	else
	        	{ 
	        		getmsgLabel().setText(words[counter]);
	        		counter++;
	        	}
	        }
	    };
	    timer = new Timer(speed, listener);
	    timer.start();
	}
	
	private void getContentFromUrl(String url)
	{
		System.out.println("url");
	}
	
	private void Stop()
	{
		if (timer != null)
		{
			counter = 0;
			timer.stop();
		}
	}
	
	private void Pause()
	{
		if (timer != null)
		{
		timer.stop();
		}
	}
	
	private void Info()
	{
		
		String timeunit;
		String data = getClipboard();
		data = data.replace("." ," ");
		data = data.replace("," , " ");
		String[] words = data.split(" ");
		getmsgLabel().setFont(new Font("Serif", Font.PLAIN, 17));
		
		double readtime = words.length/wps;

		if (readtime > 60)
		{
			timeunit = "min";
			readtime = readtime/60;
		}
		else
		{
			timeunit = "sec";
		}
		
		String readtimestring = new DecimalFormat("##.##").format(readtime);
		
		getmsgLabel().setText("<html>Total words: "+ String.valueOf(words.length)+" <br> Read time: " + readtimestring + " " + timeunit + "<br>");
	}
	
	private String getClipboard()
	{
		String data = null;
		try {
			 data = (String) Toolkit.getDefaultToolkit()
					      .getSystemClipboard().getData(DataFlavor.stringFlavor);
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedFlavorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return data;
	}
	
	private static boolean isURL(String url) 
	{
	    try 
	    {
	        new URL(url);
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	private void setFrameparam()
	{
		   setLayout(null);
		   setPreferredSize(new Dimension(400, 200));
		   setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	       setLocation(500,500);
	       getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, new Color(211,209,209)));
	       setUndecorated(true);
	       setVisible(true); 
	       pack();
	       
	       frameDragListener = new FrameDragListener(this);
	       this.addMouseListener(frameDragListener);
	       this.addMouseMotionListener(frameDragListener);
	}
	
	private void setMenu(){
		
		menuBar = new JMenuBar();
		
		setFontSize = new JMenu("Font Size");
	    small = new JMenuItem("Small");
	    medium = new JMenuItem("Medium");
	    large = new JMenuItem("Large");
	    
	    setFontStyle = new JMenu("Font style");
	    bold = new JMenuItem("Bold");
	    italic = new JMenuItem("Italic");
	    plain = new JMenuItem("Plain");
	    
	    close = new JMenuItem("x");
	    ontop = new JCheckBoxMenuItem("On top");
	    ontop.setPreferredSize(new Dimension(50,20));
 
	    small.addActionListener(this);
	    medium.addActionListener(this);
	    large.addActionListener(this);
	    bold.addActionListener(this);
	    italic.addActionListener(this);
	    plain.addActionListener(this);
	    close.addActionListener(this);
	    ontop.addItemListener(this);
	    
	   
	    setFontSize.add(small);
	    setFontSize.add(medium);
	    setFontSize.add(large);
	    
	    setFontStyle.add(bold);
	    setFontStyle.add(italic);
	    setFontStyle.add(plain);
	    
	    
	    menuBar.add(setFontSize);
	    menuBar.add(setFontStyle);
	    menuBar.add(ontop);
	    close.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); 
	    menuBar.add(close);
	    
	    
	    this.setJMenuBar(menuBar);
	}
	
	private void setListeners()
	{
		getStartButton().addActionListener(new ActionListener()
	       {
	         public void actionPerformed(ActionEvent e)
	         {
	        	 getmsgLabel().setText("");
	           getmsgLabel().setFont(getFont());
	           StartReading();
	         }
	       });
		
		getStopButton().addActionListener(new ActionListener()
	       {
	         public void actionPerformed(ActionEvent e)
	         {
	           Stop();
	         }
	       });
		
		getPauseButton().addActionListener(new ActionListener()
	       {
	         public void actionPerformed(ActionEvent e)
	         {
	           Pause();
	         }
	       });
		
		getInfoButton().addActionListener(new ActionListener()
	       {
	         public void actionPerformed(ActionEvent e)
	         {
	           Info();
	         }
	       });
		
		getScrollBar().addAdjustmentListener(new AdjustmentListener() 
		{
			  public void adjustmentValueChanged(AdjustmentEvent evt) 
			  {
				    int value = evt.getValue();
				    wps = 300 - value*28;
				    speed = (int)(wps);
				    
				    wps = 1000/wps;
				    
				    String wpstring = String.valueOf(wps);
				    int index = wpstring.indexOf(".");
				    wpstring = wpstring.substring(0, index+2);

				    getSpeedLabel().setText(wpstring + " words/sec");
			  }
			  });    
		
	}
	
	public Font getFont(){
		mainfont = new Font("Serif", fontstyle, fontsize);
		return mainfont;
	}

	private JScrollBar getScrollBar(){
		if (scrollbar == null){
			scrollbar = new JScrollBar(JScrollBar.HORIZONTAL, 0 , 1,0 , 10);
			scrollbar.setBounds(25,130,250,15);
		}
		return scrollbar;
	}
	
	private JLabel getmsgLabel()
	{
		if (msg==null){
			msg = new JLabel("Copy text or URL");
		    msg.setBounds(50,0,200,130);
		    msg.setHorizontalAlignment(JLabel.CENTER);
		    msg.setVerticalAlignment(JLabel.CENTER);
		    msg.setFont(getFont());
		}
		return msg;
	}
	
	private JLabel getSpeedLabel()
	{
		if (speedlb==null){
			speedlb = new JLabel("3.3 words/sec");
			speedlb.setBounds(280,121,100,30);
			speedlb.setHorizontalAlignment(JLabel.CENTER);
			speedlb.setFont(new Font("Serif", Font.PLAIN, 16));
		}
		return speedlb;
	}
	
	private JButton getStartButton()
	{
		if (start == null){
			start = new JButton("Start");
			start.setBounds(290,13,80,20);
		}
		return start;
	}
	
	private JButton getPauseButton()
	{
		if (pause == null){
			pause = new JButton("Pause");
			pause.setBounds(290,38,80,20);
		}
		return pause;
	}
	
	private JButton getStopButton()
	{
		if (stop == null){
			stop = new JButton("Stop");
			stop.setBounds(290,63,80,20);
		}
		return stop;
	}
	
	private JButton getInfoButton()
	{
		if (infobtn == null){
			infobtn = new JButton("Info");
			infobtn.setBounds(290,88,80,20);
		}
		return infobtn;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		String action = e.getActionCommand();
		
		if (action.equals("x")){
			System.exit(0);
		}
		
		if (action.equals("Small")){
			fontsize = 15;
			getmsgLabel().setFont(getFont());
		}
		
		if (action.equals("Medium")){
			fontsize = 19;
			getmsgLabel().setFont(getFont());
		}
		
		if (action.equals("Large")){
			fontsize = 24;
			getmsgLabel().setFont(getFont());
		}
		
		if (action.equals("Bold")){
			fontstyle = Font.BOLD;
			getmsgLabel().setFont(getFont());
		}
		
		if (action.equals("Italic")){
			fontstyle = Font.ITALIC;
			getmsgLabel().setFont(getFont());
		}
		
		if (action.equals("Plain")){
			fontstyle = Font.PLAIN;
			getmsgLabel().setFont(getFont());
		}
				
	}

	@Override
	public void itemStateChanged(ItemEvent e) 
	{
		if (ontop.isSelected())
		{
			this.setAlwaysOnTop (true);
		}
		else
		{
			this.setAlwaysOnTop (false);
		}
	}

}
