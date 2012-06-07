package gui;

import jade.gui.GuiEvent;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.omg.PortableInterceptor.USER_EXCEPTION;

import agents.UserAgent;

public class ChatFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	JTextField txtField;
	JTextArea backlog;
	JButton sendButton;
	BorderLayout layout;
	
	UserAgent myAgent;
	
	public ChatFrame(UserAgent ag) {
		super();
		myAgent = ag;		
				
		backlog = new JTextArea();
		backlog.setEditable(false);
		
		
		txtField = new JTextField();
		
		JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, backlog, txtField);
		pane.setDividerLocation(360);
		
		txtField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GuiEvent e = new GuiEvent(this, UserAgent.SEND_MESSAGE_EVENT);
				e.addParameter(myAgent.getLocalName() + " dit : " + txtField.getText());				
				txtField.setText("");
				backlog.setText(backlog.getText() + "\n" + e.getParameter(0));
				myAgent.postGuiEvent(e);
			}
		});

		this.getContentPane().add(pane);
		this.setTitle(myAgent.getName());
		this.setSize(400, 400);
		

		this.setLocation(600, 200);
		this.setVisible(true);		
	}


	public void addMessage(String parameter) {
		String contents = backlog.getText();
		contents += "\n" + parameter;
		backlog.setText(contents);		
	}

}
