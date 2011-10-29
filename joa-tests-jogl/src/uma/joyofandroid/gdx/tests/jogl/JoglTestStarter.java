/*******************************************************************************
 * basically copied from the gdx-test project
 ******************************************************************************/


package uma.joyofandroid.gdx.tests.jogl;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import uma.joyofandroid.gdx.tests.jogl.JoglTestStarter.TestList;
import uma.joyofandroid.gdx.tests.utils.JoaTest;
import uma.joyofandroid.gdx.tests.utils.JoaTests;

import com.badlogic.gdx.backends.jogl.JoglApplication;

public class JoglTestStarter {
	static class TestList extends JPanel {
		public TestList () {
			setLayout(new BorderLayout());

			final JList list = new JList(JoaTests.getNames());
			final JButton button = new JButton("Run Test");
			JScrollPane pane = new JScrollPane(list);

			DefaultListSelectionModel m = new DefaultListSelectionModel();
			m.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			m.setLeadAnchorNotificationEnabled(false);
			list.setSelectionModel(m);

			list.addMouseListener(new MouseAdapter() {
				public void mouseClicked (MouseEvent event) {
					if (event.getClickCount() == 2) button.doClick();
				}
			});

			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed (ActionEvent e) {
					String testName = (String)list.getSelectedValue();
					JoaTest test = JoaTests.newTest(testName);
					new JoglApplication(test, testName, 320, 480, test.needsGL20());
				}
			});

			add(pane, BorderLayout.CENTER);
			add(button, BorderLayout.SOUTH);
		}
	}

	public static void main (String[] argv) {
		JFrame frame = new JFrame("Joa - Jogl Test Launcher");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new TestList());
		frame.pack();
		frame.setSize(frame.getWidth(), 300);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
