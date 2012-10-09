package pl.edu.agh.adminmanager.agent;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AgentApp {

	TrayIcon trayIcon;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"config/application.xml");

		final ContextController contextController = (ContextController) context
				.getBean(ContextController.class);

		contextController.setContext(context);
		
		//tray icon
		if(SystemTray.isSupported()){
			SystemTray systemTray = SystemTray.getSystemTray();
			Image image = Toolkit.getDefaultToolkit().getImage("trayIcon.png");
						
			
			MenuItem exit = new MenuItem("Exit");
			exit.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					contextController.close();
				
					
				}
			});
			

			PopupMenu popupMenu = new PopupMenu();
			popupMenu.add(exit);
			
			TrayIcon trayIcon = new TrayIcon(image,"Admin manager", popupMenu);
			trayIcon.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			
			try {
				systemTray.add(trayIcon);
			} catch (AWTException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
		}
		
	
	}

}
