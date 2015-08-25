package com.rail.electric.simulator.dialogs;

import gnu.io.CommPortIdentifier;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.rail.electric.simulator.SimulatorMessages;
import com.rail.electric.simulator.helpers.CommHelper;
import com.rail.electric.simulator.model.TeacherWorkstation;
import com.rail.electric.simulator.model.TeacherWorkstation.WorkMode;

public class TeacherDialog extends TitleAreaDialog {

	private Text txtPort;
	private Combo combMode;
	private Combo combComPort;
	
	private String port;
	private WorkMode mode;
	private String comPort;
	
	public TeacherDialog(Shell parentShell) {
		super(parentShell);
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
	  	    
	    return area;
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
	    port = txtPort.getText();
	    comPort = combComPort.getText();
	    mode = WorkMode.getWorkMode(combMode.getSelectionIndex());
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

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public WorkMode getMode() {
		return mode;
	}

	public void setMode(WorkMode mode) {
		this.mode = mode;
	}

	public String getComPort() {
		return comPort;
	}

	public void setComPort(String comPort) {
		this.comPort = comPort;
	}
	
	
} 
