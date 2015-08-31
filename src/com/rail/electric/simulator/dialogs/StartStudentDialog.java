package com.rail.electric.simulator.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.rail.electric.simulator.SimulatorMessages;
import com.rail.electric.simulator.model.StudentWorkstation;

public class StartStudentDialog extends TitleAreaDialog {

	private Text txtIp;
	private Text txtPort;
	
	private Text txtUsername;
	private Text txtPassword;	
	
	private StudentWorkstation model;

	public StartStudentDialog(Shell parentShell) {
		super(parentShell);
		model = new StudentWorkstation();
	}

	@Override
	public void create() {
		super.create();
		setTitle(SimulatorMessages.StudentDialog_title);
		setMessage(SimulatorMessages.StudentDialog_message, IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);

	    createIp(container);
	    createPort(container);
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
	    txtUsername.setText("user");
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

	private void createIp(Composite container) {
		Label lbtIp = new Label(container, SWT.NONE);
		lbtIp.setText("IP");
	
	    GridData dataIp = new GridData();
	    dataIp.grabExcessHorizontalSpace = true;
	    dataIp.horizontalAlignment = GridData.FILL;
	
	    txtIp = new Text(container, SWT.BORDER );
	    txtIp.setLayoutData(dataIp);
	    txtIp.setText(model.getIp());
	}
	  
	private void createPort(Composite container) {
	    Label lbtPort = new Label(container, SWT.NONE);
	    lbtPort.setText(SimulatorMessages.Port_label);
	    
	    GridData dataPort = new GridData();
	    dataPort.grabExcessHorizontalSpace = true;
	    dataPort.horizontalAlignment = GridData.FILL;
	    
	    txtPort = new Text(container, SWT.BORDER );
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
		if ("user".equals(txtPassword.getText())) {			
			model.setIp(txtIp.getText());
			model.setPort(Integer.parseInt(txtPort.getText()));	
			model.save();
			return true;
		} else return false;
	}
	
	@Override
	protected void okPressed() {
	    if (validateAndsaveInput())  super.okPressed();
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

	public String getIp() {
		return model.getIp();
	}


	public int getPort() {
		return model.getPort();
	}
	
	public static void main (String[] args) {
		Shell shell = new Shell(new Display());
		StartStudentDialog dialog = new StartStudentDialog(shell);
		if (dialog.open() == Window.CANCEL) {
			
		}
		
	}
} 
