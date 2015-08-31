package com.rail.electric.simulator.dialogs;

import gnu.io.CommPortIdentifier;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.rail.electric.simulator.SimulatorMessages;
import com.rail.electric.simulator.helpers.CommHelper;
import com.rail.electric.simulator.model.TeacherWorkstation;
import com.rail.electric.simulator.model.TeacherWorkstation.WorkMode;

public class StartTeacherDialog extends TitleAreaDialog {

	private Text txtPort;
	private Combo combMode;
	private Combo combComPort;
	
	private Text txtUsername;
	private Text txtPassword;	
		
	private TeacherWorkstation model;
	
	public StartTeacherDialog(Shell parentShell) {
		super(parentShell);
		model = new TeacherWorkstation();
	}

	@Override
	public void create() {
		super.create();
		setTitle(SimulatorMessages.TeacherDialog_title);
		setMessage(SimulatorMessages.TeacherDialog_message, IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);
	    
	    createPort(container);
	    createMode(container);
	    createComPort(container);
	    createUsername(container);
	    createPassword(container);
	  	    
	    return area;
	}

	private void createUsername(Composite container) {
		Label lbt = new Label(container, SWT.NONE);
		lbt.setText(SimulatorMessages.Username_label);
	
	    GridData dataIp = new GridData();
	    dataIp.grabExcessHorizontalSpace = true;
	    dataIp.horizontalAlignment = GridData.FILL;
	
	    txtUsername = new Text(container, SWT.BORDER | SWT.READ_ONLY);
	    txtUsername.setLayoutData(dataIp);
	    txtUsername.setText("admin");
	}
	
	private void createPassword(Composite container) {
		Label lbt = new Label(container, SWT.NONE);
		lbt.setText(SimulatorMessages.Password_label);
	
	    GridData dataIp = new GridData();
	    dataIp.grabExcessHorizontalSpace = true;
	    dataIp.horizontalAlignment = GridData.FILL;
	
	    txtPassword = new Text(container, SWT.PASSWORD | SWT.BORDER);
	    txtPassword.setLayoutData(dataIp);
	}
	
	private void createComPort(Composite container) {
		List<CommPortIdentifier> ports = new CommHelper().getComPorts();
		List<String> portNames = new ArrayList<String>();
		for (CommPortIdentifier port : ports) {
			portNames.add(port.getName());
		}
		Label lbt = new Label(container, SWT.NONE);
		lbt.setText(SimulatorMessages.CommPort_label);
	
	    combComPort = new Combo (container, SWT.READ_ONLY);
	    combComPort.setItems(portNames.toArray(new String[0]));
	    if (portNames.contains(model.getComPort())) {
	    	combComPort.setText(model.getComPort());
	    } else {
	    	combComPort.select(0);
	    }
	}
	
	private void createMode(Composite container) {
		Label lbt = new Label(container, SWT.NONE);
		lbt.setText(SimulatorMessages.Mode_label);
	
	    combMode = new Combo (container, SWT.READ_ONLY);
	    List<String> modeItems = new ArrayList<String>();
	    for (WorkMode mode : WorkMode.values()) {
	    	modeItems.add(mode.getLabel());
	    }
	    combMode.setItems(modeItems.toArray(new String[0]));
	    combMode.select(model.getMode().getIndex());
	}
	
	  
	private void createPort(Composite container) {
	    Label lbtPort = new Label(container, SWT.NONE);
	    lbtPort.setText(SimulatorMessages.Port_label);
	    
	    GridData dataPort = new GridData();
	    dataPort.grabExcessHorizontalSpace = true;
	    dataPort.horizontalAlignment = GridData.FILL;
	    txtPort = new Text(container, SWT.BORDER);
	    txtPort.setLayoutData(dataPort);
	    txtPort.setText(Integer.toString(model.getPort()));
	}
		
	
	@Override
	protected boolean isResizable() {
		return true;
	}
	
	// save content of the Text fields because they get disposed
	// as soon as the Dialog closes
	private boolean validateAndsaveInput() {
		if ("admin".equals(txtPassword.getText())) {
		    model.setPort(Integer.parseInt(txtPort.getText()));
		    model.setComPort(combComPort.getText());
		    model.setMode(WorkMode.getWorkMode(combMode.getSelectionIndex()));
		    model.save();
		    return true;
		} else return false;
	}
	
	@Override
	protected void okPressed() {
		if (validateAndsaveInput()) super.okPressed();
		else {
			MessageDialog.openError(getParentShell(), SimulatorMessages.ErrorPassword_title, SimulatorMessages.ErrorPassword_message);
			super.cancelPressed();
		}
	}	
	  
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, SimulatorMessages.OK_label, true);
	    createButton(parent, IDialogConstants.CANCEL_ID,
	    		SimulatorMessages.Cancel_label, false);
	}

	public int getPort() {
		return model.getPort();
	}

	public WorkMode getMode() {
		return model.getMode();
	}

	public String getComPort() {
		return model.getComPort();
	}
	
	public static void main (String[] args) {
		Shell shell = new Shell(new Display());
		StartTeacherDialog dialog = new StartTeacherDialog(shell);
		if (dialog.open() == Window.CANCEL) {
			
		}
		
	}
	
} 
