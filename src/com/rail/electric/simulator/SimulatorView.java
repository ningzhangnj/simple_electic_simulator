package com.rail.electric.simulator;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.ScalableFreeformLayeredPane;
import org.eclipse.draw2d.geometry.Rectangle;
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
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import com.rail.electric.simulator.dialogs.StartStudentDialog;
import com.rail.electric.simulator.dialogs.StartTeacherDialog;

public class SimulatorView {
	private ScalableFreeformLayeredPane root;
	private FreeformLayer primary;
	
	private static final int VIEW_WIDTH = 1800;	
	private static final int VIEW_HEIGHT = 1000;
	
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
		
		FigureCanvas canvas = createDiagram(shell);
		canvas.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		final SimulatorFiguresCollections sim = new SimulatorFiguresCollections(primary);
		sim.activate();
		
		createMenuBar(shell);
				
		
		Display display = shell.getDisplay();
		shell.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				sim.deactivate();
			}
			
		});
		shell.open();
		while (!shell.isDisposed()) {
			while (!display.readAndDispatch()) {
				display.sleep();
			}
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
		MenuItem zoomMenuItem = new MenuItem(menuBar, SWT.CASCADE);
		zoomMenuItem.setText(SimulatorMessages.Zoom_menu); //$NON-NLS-1$
		Menu zoomMenu = new Menu(shell, SWT.DROP_DOWN);
		zoomMenuItem.setMenu(zoomMenu);
		
		// Create the File menu items
		createStartTeacherMenuItem(operateMenu, shell);
		createStartStudentMenuItem(operateMenu, shell);
		createStopMenuItem(operateMenu, shell);
		
		// Create the "fixed" scale menu items
		createFixedZoomMenuItem(zoomMenu, "50%", 0.5);
		createFixedZoomMenuItem(zoomMenu, "100%", 1);
		createFixedZoomMenuItem(zoomMenu, "200%", 2);
		
		// Add "Scale to fit" menu item
		createScaleToFitMenuItem(zoomMenu);
	}
	
	private void createStartTeacherMenuItem(Menu menu, final Shell shell) {
		MenuItem menuItem = new MenuItem(menu, SWT.NULL);
		menuItem.setText(SimulatorMessages.StartTeacher_menu); //$NON-NLS-1$
		menuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				startTeacher(shell);
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
	}
	
	private void createStopMenuItem(Menu menu, final Shell shell) {
		MenuItem menuItem = new MenuItem(menu, SWT.NULL);
		menuItem.setText(SimulatorMessages.Stop_menu); //$NON-NLS-1$
		menuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				//TODO
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
	}
	
	private void createStartStudentMenuItem(Menu menu, final Shell shell) {
		MenuItem menuItem = new MenuItem(menu, SWT.NULL);
		menuItem.setText(SimulatorMessages.StartStudent_menu); //$NON-NLS-1$
		menuItem.addSelectionListener(new SelectionListener() {
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
	
	private void startTeacher(Shell shell) {
		StartTeacherDialog dialog = new StartTeacherDialog(shell);
		
		if (dialog.open() == Window.OK) {
			
		}
	}
	
	private void startStudent(Shell shell) {
		StartStudentDialog dialog = new StartStudentDialog(shell);
		
		if (dialog.open() == Window.OK) {
			
		}
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
	
	public static void main(String[] args) {
		new SimulatorView().run();
	}

}
