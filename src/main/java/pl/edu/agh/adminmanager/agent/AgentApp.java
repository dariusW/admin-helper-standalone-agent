package pl.edu.agh.adminmanager.agent;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AgentApp {

	private static Logger log = Logger.getLogger(AgentApp.class.getName());

	public static AgentApp running;

	private ApplicationContext context;

	private ContextController contextController;

	private ActionListener actionListener;

	private JFrame configFrame;

	private JButton save;

	private JButton cancel;

	private JPanel panel;

	private JScrollPane scrollPanel;

	private JPanel scrollContentPanel;

	private Map<JLabel, JTextField> map;

	/**
	 * @param args
	 */
	private AgentApp() {
		init();
	}

	private void init() {
		context = new ClassPathXmlApplicationContext("config/application.xml");

		contextController = (ContextController) context
				.getBean(ContextController.class);

		contextController.setContext(context);

		// tray icon
		if (SystemTray.isSupported()) {
			SystemTray systemTray = SystemTray.getSystemTray();
			Image image = Toolkit.getDefaultToolkit().getImage(
					AgentApp.class.getResource("/gfx/trayIcon.png"));

			MenuItem exit = new MenuItem("Exit");
			exit.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					contextController.close();

				}
			});

			MenuItem config = new MenuItem("Config");
			config.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					openConfigWindow();

				}

			});

			PopupMenu popupMenu = new PopupMenu();
			popupMenu.add(exit);
			popupMenu.add(config);

			TrayIcon trayIcon = new TrayIcon(image, "Admin manager", popupMenu);
			// trayIcon.addActionListener(new ActionListener() {
			//
			// public void actionPerformed(ActionEvent e) {
			//
			// }
			// });

			try {
				systemTray.add(trayIcon);
			} catch (AWTException e1) {
				log.error("Error creating trayIcon" + e1);
			}

		}

	}

	public static void main(String[] args) {
		AgentApp.run();
	}

	private static void run() {
		running = new AgentApp();

	}

	private void openConfigWindow() {
		if (configFrame == null) {
			configFrame = new JFrame("Admin Helper Agent - configure");
			configFrame.setSize(400, 200);
			configFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			configFrame.setLayout(new BorderLayout());
			actionListener = new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (e.getSource() == save) {
						Properties p = contextController.getConfigProperties();
						for (JLabel key : map.keySet()) {
							String keyValue = key.getText();
							String valueValue = map.get(key).getText();
							p.setProperty(keyValue, valueValue);
						}

						// TODO: handle save new config
						try {
							OutputStream propertyOut = new FileOutputStream(
									"config.properties");
							p.store(propertyOut,
									"Backup config stroed in config.properties.backup");
						} catch (FileNotFoundException e1) {
							log.error(e);
						} catch (IOException e1) {
							log.error("Prooperty save error: " + e1);
						}

						JOptionPane.showMessageDialog(configFrame,
								"You need to restarft application");
						configFrame.setVisible(false);

					} else if (e.getSource() == cancel) {
						configFrame.setVisible(false);
					}

				}
			};
			save = new JButton("Save");
			save.addActionListener(actionListener);
			cancel = new JButton("Cancel");
			cancel.addActionListener(actionListener);
			panel = new JPanel(new FlowLayout(10));
			panel.add(save);
			panel.add(cancel);

			scrollContentPanel = new JPanel();
			scrollContentPanel.setLayout(new BoxLayout(scrollContentPanel,
					BoxLayout.Y_AXIS));
			scrollPanel = new JScrollPane(scrollContentPanel);
			configFrame.add(panel, BorderLayout.SOUTH);
			configFrame.add(scrollPanel, BorderLayout.CENTER);

			map = new HashMap<JLabel, JTextField>();
		}

		scrollContentPanel.removeAll();
		for (Entry<Object, Object> item : contextController
				.getConfigProperties().entrySet()) {
			JLabel label = new JLabel(item.getKey().toString());
			JTextField field = new JTextField(item.getValue().toString());
			map.put(label, field);
			scrollContentPanel.add(label);
			scrollContentPanel.add(field);
		}

		configFrame.setVisible(true);

	}
}
