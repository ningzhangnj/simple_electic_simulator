package com.rail.electric.simulator;

import java.io.File;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.ScalableFreeformLayeredPane;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rail.electric.simulator.SimulatorManager.WorkStatus;
import com.rail.electric.simulator.dialogs.StartStudentDialog;
import com.rail.electric.simulator.dialogs.StartTeacherDialog;
import com.rail.electric.simulator.helpers.DataTypeConverter;

public class SimulatorView {
	private final static Logger logger =  LoggerFactory.getLogger(SimulatorView.class);
	
	private static final int VIEW_WIDTH = 1800;	
	private static final int VIEW_HEIGHT = 1000;
	
	private static final List<Long> licenses = Arrays.asList(132837597590628L, 122178626107566L, 125099203868846L);
	
	private ScalableFreeformLayeredPane root;
	private FreeformLayer primary;
	private SimulatorManager simManager;
	
	private MenuItem	startTeacherMenuItem;
	private MenuItem	startStudentMenuItem;
	private MenuItem	stopMenuItem;
	private MenuItem	importMenuItem;
	
	
	private FigureCanvas createDiagram(Composite parent) {
		
		// Create a layered pane along with primary and connection layers
		root = new ScalableFreeformLayeredPane();
		root.setFont(parent.getFont());

		primary = new FreeformLayer();
		primary.setLayoutManager(new FreeformLayout());
		root.add(primary, "Primary");

		// Create the canvas and use it to show the root figure
		FigureCanvas canvas = new FigureCanvas(parent, SWT.DOUBLE_BUFFERED);
		canvas.setViewport(new FreeformViewport());
		canvas.setBackground(new Color(null, 204, 232,207));
		canvas.setContents(root);
		return canvas;
	}
	
	private void run() {
		Shell shell = new Shell(new Display());
		shell.setSize(VIEW_WIDTH, VIEW_HEIGHT);
		shell.setText(SimulatorMessages.Simulator_title); //$NON-NLS-1$
		shell.setLayout(new GridLayout());
		
		if (calculateLicense()) {
			FigureCanvas canvas = createDiagram(shell);
			canvas.setLayoutData(new GridData(GridData.FILL_BOTH));
			
			simManager = SimulatorManager.getInstance(this, primary,
				WorkStatus.IDLE);
					
			createMenuBar(shell);
					
			updateMenuItems();
			
			Display display = shell.getDisplay();
			shell.addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(DisposeEvent arg0) {
					simManager.deactivate();
				}
				
			});
			shell.open();
			while (!shell.isDisposed()) {
				while (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} else {
			MessageDialog.openError(shell, SimulatorMessages.ErrorDialog_title, SimulatorMessages.LicenseError_message);
		}		
	}
	
	private void createMenuBar(Shell shell) {

		// Create menu bar with "File" and "Zoom" menus
		final Menu menuBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);
		MenuItem operateMenuItem = new MenuItem(menuBar, SWT.CASCADE);
		operateMenuItem.setText(SimulatorMessages.Operate_menu); //$NON-NLS-1$
		Menu operateMenu = new Menu(shell, SWT.DROP_DOWN);
		operateMenuItem.setMenu(operateMenu);
		
		MenuItem connectionsMenuItem = new MenuItem(menuBar, SWT.CASCADE);
		connectionsMenuItem.setText(SimulatorMessages.Connections_menu); //$NON-NLS-1$
		Menu connectionsMenu = new Menu(shell, SWT.DROP_DOWN);
		connectionsMenuItem.setMenu(connectionsMenu);
		
		MenuItem zoomMenuItem = new MenuItem(menuBar, SWT.CASCADE);
		zoomMenuItem.setText(SimulatorMessages.Zoom_menu); //$NON-NLS-1$
		Menu zoomMenu = new Menu(shell, SWT.DROP_DOWN);
		zoomMenuItem.setMenu(zoomMenu);
		
		MenuItem aboutMenuItem = new MenuItem(menuBar, SWT.CASCADE);
		aboutMenuItem.setText(SimulatorMessages.About_menu); //$NON-NLS-1$
				
		// Create the File menu items
		createStartTeacherMenuItem(operateMenu, shell);
		createStartStudentMenuItem(operateMenu, shell);
		createStopMenuItem(operateMenu, shell);
		
		
		// Create import connections menu items.
		createImportConnectionsMenuItem(connectionsMenu, shell);
		
		// Create the "fixed" scale menu items
		createFixedZoomMenuItem(zoomMenu, "50%", 0.5);
		createFixedZoomMenuItem(zoomMenu, "100%", 1);
		createFixedZoomMenuItem(zoomMenu, "200%", 2);
		
		// Add "Scale to fit" menu item
		createScaleToFitMenuItem(zoomMenu);
		
		createAboutMenuItem(aboutMenuItem, shell);
	}
	
	private void createAboutMenuItem(MenuItem menuItem, final Shell shell) {		
		menuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				MessageDialog.openInformation(shell, SimulatorMessages.About_title, SimulatorMessages.Version_label + ": V1.0\n"
						+ SimulatorMessages.Manufacturer_name);
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
	}
	
	private void createImportConnectionsMenuItem(Menu menu, final Shell shell) {
		importMenuItem = new MenuItem(menu, SWT.NULL);
		importMenuItem.setText(SimulatorMessages.ImportConnections_menu); //$NON-NLS-1$
		importMenuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				importConnections(shell);
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
	}
	
	private void createStartTeacherMenuItem(Menu menu, final Shell shell) {
		startTeacherMenuItem = new MenuItem(menu, SWT.NULL);
		startTeacherMenuItem.setText(SimulatorMessages.StartTeacher_menu); //$NON-NLS-1$
		startTeacherMenuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				startTeacher(shell);
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});		
	}
	
	private void createStopMenuItem(Menu menu, final Shell shell) {
		stopMenuItem = new MenuItem(menu, SWT.NULL);
		stopMenuItem.setText(SimulatorMessages.Stop_menu); //$NON-NLS-1$
		stopMenuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				stop();
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});		
	}
	
	private void createStartStudentMenuItem(Menu menu, final Shell shell) {
		startStudentMenuItem = new MenuItem(menu, SWT.NULL);
		startStudentMenuItem.setText(SimulatorMessages.StartStudent_menu); //$NON-NLS-1$
		startStudentMenuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				startStudent(shell);
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
	}
	
	private void createFixedZoomMenuItem(Menu menu, String text, final double scale) {
		MenuItem menuItem;
		menuItem = new MenuItem(menu, SWT.NULL);
		menuItem.setText(text);
		menuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				root.setScale(scale);
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
	}

	private void createScaleToFitMenuItem(Menu menu) {
		MenuItem menuItem = new MenuItem(menu, SWT.NULL);
		menuItem.setText(SimulatorMessages.Scale2Fit_menu); //$NON-NLS-1$
		menuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				scaleToFit();
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
	}
	
	public void updateMenuItems() {
		if (simManager != null) {
			switch (simManager.getStatus()) {
				case IDLE:
					startTeacherMenuItem.setEnabled(true);
					startStudentMenuItem.setEnabled(true);
					stopMenuItem.setEnabled(false);
					importMenuItem.setEnabled(false);
					break;
				case RUNNING_TEACHER:
					startTeacherMenuItem.setEnabled(false);
					startStudentMenuItem.setEnabled(false);
					stopMenuItem.setEnabled(true);
					importMenuItem.setEnabled(true);;
					break;
				case RUNNING_STUDENT:
					startTeacherMenuItem.setEnabled(false);
					startStudentMenuItem.setEnabled(false);
					stopMenuItem.setEnabled(true);
					importMenuItem.setEnabled(false);
					break;
				default:
					break;
			}
		}
	}
	
	private void stop() {		
		simManager.deactivate();
		updateMenuItems();
		
	}
	
	private void startTeacher(Shell shell) {
		
		StartTeacherDialog dialog = new StartTeacherDialog(shell);
		
		if (dialog.open() == Window.OK) {
			simManager.setStatus(WorkStatus.RUNNING_TEACHER);
			simManager.startTeacher(dialog.getPort(), dialog.getMode(), dialog.getComPort());
		}
		updateMenuItems();
	}
	
	private void importConnections(Shell shell) {
		FileDialog dialog = new FileDialog(shell);
		dialog.setFilterExtensions(new String[]{"*.ini"});
		String path = dialog.open();
		if (path != null) {
			simManager.importConnections(new File(path));
		}
	}
	
	private void startStudent(Shell shell) {
		StartStudentDialog dialog = new StartStudentDialog(shell);
		
		if (dialog.open() == Window.OK) {
			simManager.setStatus(WorkStatus.RUNNING_STUDENT);
			simManager.startStudent(dialog.getIp(), dialog.getPort());
		}
		updateMenuItems();		
	}
	
	private void scaleToFit() {
		FreeformViewport viewport = (FreeformViewport) root.getParent();
		Rectangle viewArea = viewport.getClientArea();
		
		root.setScale(1);
		Rectangle extent = root.getFreeformExtent().union(0, 0);
		
		double wScale = ((double) viewArea.width) / extent.width;
		double hScale = ((double) viewArea.height) / extent.height;
		double newScale = Math.min(wScale, hScale);
		
		root.setScale(newScale);
	}
	
	private boolean calculateLicense() {		
		try {			
			for(Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
               e.hasMoreElements(); )
			{
				NetworkInterface ni = e.nextElement();
				byte[] mac = ni.getHardwareAddress();
				if (mac == null || mac.length != 6) continue;
				logger.debug("Get mac address is : " + DataTypeConverter.bytesToHex(mac));				
				long result = (convertByte2Long(mac[0])<<8) + convertByte2Long(mac[1])
						+ (convertByte2Long(mac[2])<<24) + (convertByte2Long(mac[3])<<16) 
						+ (convertByte2Long(mac[4])<<40) + (convertByte2Long(mac[5])<<32);
				logger.debug("Computed license result is: " + result);
				if (licenses.contains(result)) return true;
			}			
		} catch (SocketException e) {
			logger.error("Critial error: can not get network info.");
		}
		
		return false;
	}
	
	private long convertByte2Long(byte b) {
		return b&0xff;
	}
	
	public static void main(String[] args) {
		new SimulatorView().run();
	}

}
