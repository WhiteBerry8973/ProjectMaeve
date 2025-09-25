package UiAddmin;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import FactoryMethodPattern.Coffee;


public class Jframeadmin extends JFrame {
      
    
    Container cp ;
    JLabel Password , Username , headline, Conpass;
    JTextField t1 ,t2,t3 ;
    JButton b1,b2 ;
   
    public Jframeadmin(){
    
    JFrame f = new JFrame("Admin");
    Container cp = f.getContentPane();
    cp.setLayout(null);
    
  
    //JTextField t = new JTextField(30);

        headline = new JLabel("MAEVE COFFEE") ;
        Username = new JLabel("Username : ");
        Password = new JLabel("Password : ");
        Conpass  = new JLabel("Confirm Password : ");
        t1 = new JTextField(20);
        t2 = new JTextField(20);
        t3 = new JTextField(20);
        b1 = new JButton("Login");
        b2 = new JButton("Guest");  
        

        headline.setBounds(155,25,170,25);
        Username.setBounds(75, 75, 70, 25);
        Password.setBounds(80, 115, 70, 25);
        Conpass.setBounds(35, 155, 170, 25);
        t1.setBounds(150, 75, 180, 25);
        t2.setBounds(150, 115, 180, 25);
        t3.setBounds(150,155,180,25);
        b1.setBounds(120, 200, 80, 25);
        b2.setBounds(225, 200, 80, 25);
        
        cp.add(Username); cp.add(t1);
        cp.add(Password); cp.add(t2);
        cp.add(Conpass); cp.add(t3);
        cp.add(b1) ; cp.add(b2);
        cp.add(headline);
    


    f.setSize (440,300);
    f.setVisible(true);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    
    }
}