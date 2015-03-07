import java.io.*;
import java.net.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * 
 * @author RISHABH KUMAR
 * @author DEVANG GAUR
 *
 */
public class Server implements ActionListener         // THE SERVER
{
    ServerSocket ss;
    Socket s;
    DataOutputStream dout;
    DataInputStream din;
    ArrayList al = new ArrayList();
    JFrame f1, f2;
    JButton b1, b3;
    JTextField tf;
    JLabel l1, l2;
        
    public Server()
    {
	f1 = new JFrame("Secure Log-in");
	l1 = new JLabel("Password :");
	l1.setBounds(50, 150, 125, 50);
	f1.add(l1);
	tf = new JTextField();
	tf.setBounds(200, 150, 150, 50);
	f1.add(tf);
	b1 = new JButton("Submit");
	b1.setBounds(175, 250, 100, 50);
	b1.addActionListener(this);
	f1.add(b1);
	f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	f1.setSize(400, 400);
	f1.setLayout(null);
	f1.setVisible(true);

	f2 = new JFrame("Server Management");
	l2 = new JLabel("Welcome to the Server");
	l2.setBounds(150, 50, 300, 50);
	f2.add(l2);
	b3 = new JButton("Stop");
	b3.setBounds(200, 200, 100, 50);
	b3.addActionListener(this);
	f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	f2.add(b3);
	f2.setSize(400, 400);
	f2.setLayout(null);
	f2.setVisible(false);

	try
	    {
		ss = new ServerSocket(21);
		while(true)
		    {
			s = ss.accept();
			System.out.println("Client Connected");
			al.add(s);
			Server_Thread tt = new Server_Thread(s, al);
			Thread t = new Thread(tt);
			t.start();
		    }
	    }
	catch(Exception e)
	    {
		System.out.println(e);
	    }
    }

    public void actionPerformed(ActionEvent ee)
    {
	if(ee.getSource() == b1)
	    {
		String ss = tf.getText();
		if(ss.equals("naruto"))
		    {
			f1.setVisible(false);
			f2.setVisible(true);
			
		    }
		else
		    {
			tf.setText("No! You're Wrong!");
		    }
	    }
	
	if(ee.getSource() == b3)
	    {
		System.exit(0);
	    }
    }

    public static void main(String[] args)
    {
	new Server();
    }
}

class Server_Thread implements Runnable
{
    Socket s;
    ArrayList al;
       
    public Server_Thread(Socket s, ArrayList al)
    {
	this.s = s;
	this.al = al;
    }
    
    public void run()
    {
	String s1 = "";
	try
	    {
		DataInputStream din = new DataInputStream(s.getInputStream());
		do
		    {
			s1 = din.readUTF();
			System.out.println(s1);
			
			if(!s1.equals("stop"))
			    {
				broadcastMessage(s1);
			    }
			else
			    {
				DataOutputStream dout = new DataOutputStream(s.getOutputStream());
				dout.writeUTF(s1);
				dout.flush();
				//name.remove(naam);
				System.out.println("Client Disconnected");
			    }
		    }
		while(!s1.equals("stop"));
	    }
	catch(Exception e)
	    {
		System.out.println(e);
	    }
    }

    public void broadcastMessage(String s2)
    {
	Iterator i = al.iterator();
	while(i.hasNext())
	    {
		try
		    {
			Socket s = (Socket)i.next();
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			dout.writeUTF(s2);
			dout.flush();
		    }
		catch(Exception e)
		    {
			System.out.println(e);
		    }
	    }
    }
}
