import java.io.*;
import java.net.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
/**
 * 
 * @author RISHABH KUMAR
 * @author DEVANG GAUR
 */
public class Client implements ActionListener                          // THE CLIENT
{
    String global;
    Socket s;
    DataOutputStream dout;
    DataInputStream din;
    JFrame f1, f2;
    JButton b1, b2, b3;
    JTextField tf1;
    JLabel l1, l2, l3;
    JTextArea tf2, tf4, tf3;

    Client()
    {
	f1 = new JFrame("Registration");
	l1 = new JLabel("Name:");
	l1.setBounds(50, 200, 100, 50);
	f1.add(l1);
	tf1 = new JTextField();
	tf1.setBounds(200, 200, 150, 50);
	f1.add(tf1);
	b1 = new JButton("Login");
	b1.setBounds(250, 400, 100, 50);
	f1.add(b1);
	b1.addActionListener(this);
	f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	f1.setSize(500, 500);
	f1.setLayout(null);
	f1.setVisible(true);
	
	f2 = new JFrame("Public Chat");
	l2 = new JLabel();
	l2.setBounds(50, 50, 200, 50);
	f2.add(l2);
	tf2 = new JTextArea();
	tf2.setBounds(50, 150, 500, 350);
	f2.add(tf2);
	tf3 = new JTextArea();
	tf3.setBounds(50, 550, 400, 100);
	f2.add(tf3);
	b2 = new JButton("Send");
	b2.setBounds(500, 575, 100, 50);
	f2.add(b2);
	b2.addActionListener(this);
	b3 = new JButton("Log Out");
	b3.setBounds(650, 50, 100, 50);
	f2.add(b3);
	b3.addActionListener(this);
	tf4 = new JTextArea();
	tf4.setBounds(600, 150, 150, 400);
	f2.add(tf4);
	l3 = new JLabel("People Connected : ");
	l3.setBounds(600, 100, 150, 50);
	f2.add(l3);
	f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	f2.setSize(800, 800);
	f2.setLayout(null);
	f2.setVisible(false);
	
	try
	    {
		s = new Socket("192.168.0.101", 21);
		din = new DataInputStream(s.getInputStream());
		dout = new DataOutputStream(s.getOutputStream());
		clientChat();
	    }				       
	catch(Exception e)
	    {
		System.out.println(e);
	    }
    }

    public void clientChat() throws IOException
    {
	Client_Thread tt = new Client_Thread(din, this);
	Thread t = new Thread(tt);
	t.start();
    }

    public void actionPerformed(ActionEvent e) 
    {
	if(e.getSource() == b1)
	    {
		
		String s1 = "Welcome " + tf1.getText();
		try
		    {
			DataOutputStream d = new DataOutputStream(s.getOutputStream());
			d.writeUTF("-> " + tf1.getText());
			d.flush();
		    }
		catch(Exception e1)
		    {
			System.out.println(e1);
		    }
		l2.setText(s1);
		f1.setVisible(false);
		f2.setVisible(true);
	    }
	if(e.getSource() == b3)
	    {
		try
		    {
			dout.writeUTF("stop");
			dout.flush();
		    }
		catch(Exception e1)
		    {
			System.out.println(e1);
		    }
		System.exit(0);
	    }
	if(e.getSource() == b2)
	    {
		String s2 = tf1.getText() +">> " + tf3.getText();
		try
		    {
			dout.writeUTF(s2);
			dout.flush();
		    }
		catch(Exception e1)
		    {
			System.out.println(e1);
		    }
		tf3.setText("");
	    }
    }



    public static void main(String[] args)
    {
	new Client();
    }
}


class Client_Thread implements Runnable
{
    DataInputStream din;
    Client t;
    Client_Thread(DataInputStream din, Client t)
    {
	this.din = din;
	this.t = t;
    }
    
    public void run()
    {
	String global = "";
	String ss = "";
	do
	    {
		try
		    {
			ss = din.readUTF();
			global = global + "\n" + ss;
			t.tf2.setText(global);
		    }
		catch(Exception e)
		    {
			System.out.println(e);
		    }
	    }
	while(!ss.equals("stop"));
    }
}