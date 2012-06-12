package gui;

import jade.gui.GuiEvent;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import agents.UserAgent;

public class ChatFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static int FRAME_WIDTH = 250;
	private static int FRAME_HEIGHT = 250;
	private static int frameNum = 0;
	private static int numFramePerRow = 2;
	JTextField txtField, txtFieldDestName;
	JTextArea backlog;
	JButton sendButton;
	BorderLayout layout;
	
	UserAgent myAgent;
	
	public ChatFrame(UserAgent ag) {
		super();
		myAgent = ag;		
		setLayout(new BorderLayout());
		initialize();
//		frameNum++;
	}
	
	private void initialize() {
		backlog = new JTextArea();
		backlog.setAutoscrolls(true);
		backlog.setEditable(false);
		JScrollPane scrollArea = new JScrollPane(backlog);
		txtField = new JTextField();
//		JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, backlog, txtField);
//		pane.setDividerLocation(360);
		
		add(txtField, BorderLayout.NORTH);
		
		txtFieldDestName = new JTextField();
		add(txtFieldDestName, BorderLayout.SOUTH);
		txtFieldDestName.setText(myAgent.interlocuteur);
		
		add(scrollArea, BorderLayout.CENTER);
		
		txtField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GuiEvent e = new GuiEvent(this, UserAgent.SEND_MESSAGE_EVENT);
				e.addParameter("< " + myAgent.getLocalName() + " : " + txtField.getText());
				e.addParameter(txtFieldDestName.getText());	
				backlog.setText(backlog.getText() + "\n> " + txtFieldDestName.getText() + " : " + txtField.getText());
				txtField.setText("");
				myAgent.postGuiEvent(e);
			}
		});

//		this.getContentPane().add(pane);
		this.setTitle(myAgent.getLocalName());
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);

		int newX = 850+(frameNum%numFramePerRow)*FRAME_WIDTH;
		int newY = (frameNum/numFramePerRow)*FRAME_HEIGHT;
		frameNum++;
		System.out.println("x : " + newX);
		System.out.println("y: " + newY);
		System.out.println();
		this.setLocation(newX, newY);
		this.setVisible(true);		
	}


	public void addMessage(String parameter) {
		String contents = backlog.getText();
		contents += "\n" + parameter;
		backlog.setText(contents);		
	}

}
