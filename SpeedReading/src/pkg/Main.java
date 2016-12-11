package pkg;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Main extends JFrame{
	
	private JPanel panel;
	private JLabel msg;
	private JButton btn;
	private int counter;
	private Timer timer;
	
	public static void main(String[] args){
		new Main();
	}
	
	public Main()
	{
		start();
	}
	
	public void start()
	{

	   msg = getLabel();
	   btn = getButton();

	   add(msg);
	   add(btn);
	   
	   setFrameparam();
	   setListeners();

	}
	
	private void StartReading(String data)
	{
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
		JLabel test = getLabel();
		String[] words = data.split(" ");
		
 
		counter=0;
		ActionListener listener = new ActionListener(){
	        public void actionPerformed(ActionEvent event){
	        	
	        	if (counter >= words.length){
	        		timer.stop();
	        	}
	        	else
	        	{
	        	test.setText(words[counter]);
	        	counter++;
	        	}
	        }
	    };
	    timer = new Timer(100, listener);
	    timer.start();
 
	}
	
	private void getContentFromUrl(String url)
	{
		System.out.println("url");
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
	
	private void getClipboard()
	{
		String data;
		try {
			 data = (String) Toolkit.getDefaultToolkit()
					      .getSystemClipboard().getData(DataFlavor.stringFlavor);
			 
			 StartReading(data);
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
	}
	
	private void setFrameparam()
	{
		   setLayout(null);
		   setPreferredSize(new Dimension(400, 200));
		   setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	       setLocation(500,500);
	       setVisible(true); 
	       pack();
	}
	
	private void setListeners()
	{
		getButton().addActionListener(new ActionListener()
	       {
	         public void actionPerformed(ActionEvent e)
	         {
	           getClipboard();
	         }
	       });
	}
	
	private JLabel getLabel()
	{
		if (msg==null){
			msg = new JLabel("Copy text or URL and press Go");
		    msg.setBounds(30,60,200,30);
		    msg.setHorizontalAlignment(JLabel.CENTER);
		    msg.setFont(new Font("Serif", Font.BOLD, 19));
		}
		return msg;
	}
	
	private JButton getButton()
	{
		if (btn == null){
			btn = new JButton("Go");
			btn.setBounds(240,50,100,50);
		}
		return btn;
	}

}
