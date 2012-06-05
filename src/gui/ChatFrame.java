package gui;

import jade.gui.GuiEvent;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.omg.PortableInterceptor.USER_EXCEPTION;

import agents.UserAgent;

public class ChatFrame extends JFrame implements PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	JTextField txtField;
	JTextArea backlog;
	JButton sendButton;
	BorderLayout layout;
	
	UserAgent myAgent;
	
	public ChatFrame(UserAgent ag) {
		super();
		myAgent = ag;
		myAgent.addPropertyChangeListener(this);
	
		layout = new BorderLayout(3, 1);
		this.setLayout(layout);
		
		backlog = new JTextArea();
		backlog.setEditable(false);
		this.add(backlog, BorderLayout.NORTH);
		
		txtField = new JTextField();
		this.add(txtField, BorderLayout.CENTER);
		
		txtField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GuiEvent e = new GuiEvent(this, UserAgent.SEND_MESSAGE_EVENT);
				e.addParameter(myAgent.getLocalName() + "dit : " + txtField.getText());
				System.out.println(txtField.getText());
				txtField.setText("");
				myAgent.postGuiEvent(e);
			}
		});

				
		this.setTitle(myAgent.getName());
		this.setSize(400, 400);
		

		this.setLocation(600, 200);

		
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);		
	}
		
	
	public void propertyChange(PropertyChangeEvent ev) {
		String propname = ev.getPropertyName();

			String contents = backlog.getText();
			contents += "\n" + (String) ev.getNewValue();
			backlog.setText(contents);
		}

}
