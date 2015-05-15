/**
*Text genereted by Simple GUI Extension for BlueJ
*/
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.border.Border;
import javax.swing.*;


public class GUI_project extends JFrame {

	private JMenuBar menuBar;
	private JButton add;
	private JLabel name;
	private JLabel pointlabel;
	private JLabel points;
	private int score = 0;

	//Constructor 
	public GUI_project(){

		this.setTitle("GUI_project");
		this.setSize(500,400);
		//menu generate method
		generateMenu();
		this.setJMenuBar(menuBar);

		//pane with null layout
		JPanel contentPane = new JPanel(null);
		contentPane.setPreferredSize(new Dimension(500,400));
		contentPane.setBackground(new Color(192,192,192));


		add = new JButton();
		add.setBounds(148,221,200,35);
		add.setBackground(new Color(214,217,223));
		add.setForeground(new Color(0,0,0));
		add.setEnabled(true);
		add.setFont(new Font("sansserif",0,12));
		add.setText("Throw ball");
		add.setVisible(true);
		//Set action for button click
		//Call defined method
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				addPoint(evt);
			}
		});


		name = new JLabel();
		name.setBounds(144,147,200,35);
		name.setBackground(new Color(214,217,223));
		name.setForeground(new Color(0,0,0));
		name.setEnabled(true);
		name.setFont(new Font("sansserif",0,12));
		name.setText("Name: Mike Wazowski");
		name.setVisible(true);

		pointlabel = new JLabel();
		pointlabel.setBounds(145,187,90,35);
		pointlabel.setBackground(new Color(214,217,223));
		pointlabel.setForeground(new Color(0,0,0));
		pointlabel.setEnabled(true);
		pointlabel.setFont(new Font("sansserif",0,12));
		pointlabel.setText("Points: ");
		pointlabel.setVisible(true);

		points = new JLabel();
		points.setBounds(213,189,90,35);
		points.setBackground(new Color(214,217,223));
		points.setForeground(new Color(0,0,0));
		points.setEnabled(true);
		points.setFont(new Font("sansserif",0,12));
		points.setText("0");
		points.setVisible(true);

		//adding components to contentPane panel
		contentPane.add(add);
		contentPane.add(name);
		contentPane.add(pointlabel);
		contentPane.add(points);

		//adding panel to JFrame and seting of window position and close operation
		this.add(contentPane);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.pack();
		this.setVisible(true);
	}

	//Method actionPerformed for add
	private void addPoint (ActionEvent evt) {
	    score++;
	    points.setText("" + score);
	}

	//method for generate menu
	public void generateMenu(){
		menuBar = new JMenuBar();

		JMenu file = new JMenu("File");
		JMenu tools = new JMenu("Tools");
		JMenu help = new JMenu("Help");

		JMenuItem open = new JMenuItem("Open   ");
		JMenuItem save = new JMenuItem("Save   ");
		JMenuItem exit = new JMenuItem("Exit   ");
		JMenuItem preferences = new JMenuItem("Preferences   ");
		JMenuItem about = new JMenuItem("About   ");


		file.add(open);
		file.add(save);
		file.addSeparator();
		file.add(exit);
		tools.add(preferences);
		help.add(about);

		menuBar.add(file);
		menuBar.add(tools);
		menuBar.add(help);
	}



	 public static void main(String[] args){
		System.setProperty("swing.defaultlaf", "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new GUI_project();
			}
		});
	}

}