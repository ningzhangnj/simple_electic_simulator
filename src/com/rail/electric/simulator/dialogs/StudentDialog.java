package com.rail.electric.simulator.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.rail.electric.simulator.SimulatorMessages;

public class StudentDialog extends TitleAreaDialog {

	private Text txtIp;
	private Text txtPort;

	private String ip;
	private String port;

	public StudentDialog(Shell parentShell) {
		super(parentShell);
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

	    return area;
	}

	private void createIp(Composite container) {
		Label lbtIp = new Label(container, SWT.NONE);
		lbtIp.setText("IP");
	
	    GridData dataIp = new GridData();
	    dataIp.grabExcessHorizontalSpace = true;
	    dataIp.horizontalAlignment = GridData.FILL;
	
	    txtIp = new Text(container, SWT.BORDER);
	    txtIp.setLayoutData(dataIp);
	}
	  
	private void createPort(Composite container) {
	    Label lbtPort = new Label(container, SWT.NONE);
	    lbtPort.setText(SimulatorMessages.Port_label);
	    
	    GridData dataPort = new GridData();
	    dataPort.grabExcessHorizontalSpace = true;
	    dataPort.horizontalAlignment = GridData.FILL;
	    txtPort = new Text(container, SWT.BORDER);
	    txtPort.setLayoutData(dataPort);
	}
		
	
	@Override
	protected boolean isResizable() {
		return true;
	}
	
	// save content of the Text fields because they get disposed
	// as soon as the Dialog closes
	private void saveInput() {
	    ip = txtIp.getText();
	    port = txtPort.getText();	
	}
	
	@Override
	protected void okPressed() {
	    saveInput();
	    super.okPressed();
	}	
	  
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, SimulatorMessages.OK_label, true);
	    createButton(parent, IDialogConstants.CANCEL_ID,
	    		SimulatorMessages.Cancel_label, false);
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
} 
