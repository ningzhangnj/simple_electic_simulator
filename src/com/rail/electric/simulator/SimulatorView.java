package com.rail.electric.simulator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.rail.electric.simulator.util.SimulatorUtil;
import com.rail.electric.simulator.view.CoverView;

public class SimulatorView {
	//private final static Logger logger =  LoggerFactory.getLogger(SimulatorView.class);
	
	private static final int VIEW_WIDTH = 1800;	
	private static final int VIEW_HEIGHT = 1000;
	
	private void run() {
		Shell shell = new Shell(new Display());
		shell.setSize(VIEW_WIDTH, VIEW_HEIGHT);
		shell.setText(SimulatorMessages.Simulator_title); //$NON-NLS-1$
		shell.setLayout(new GridLayout());
		
		if (SimulatorUtil.calculateLicense()) {
			/*Composite viewArea = new Composite(shell, SWT.NONE);
			viewArea.setLayout(new GridLayout());
			viewArea.setBounds(shell.getBounds());
			viewArea.setSize(shell.getSize());*/
			new CoverView(shell);			
			
			Display display = shell.getDisplay();
			shell.addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(DisposeEvent arg0) {
					//simManager.deactivate();
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
	
	public static void main(String[] args) {
		new SimulatorView().run();
	}

}
