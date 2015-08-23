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

import com.rail.electric.simulator.helpers.CommHelper;

public class TeacherDialog extends TitleAreaDialog {

	private Text txtPort;
	private Combo combMode;
	private Combo combComPort;
	private Text txtQuizNo;

	private String port;
	private String mode;
	private String comPort;
	private String quizNo;

	public TeacherDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void create() {
		super.create();
		setTitle("Input teacher machine info");
		setMessage("Please input teacher machine info to start work", IMessageProvider.INFORMATION);
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
	    createQuizNo(container); 
	    
	    return area;
	}

	private void createQuizNo(Composite container) {
		Label lbt = new Label(container, SWT.NONE);
		lbt.setText("Quiz No");
	
	    GridData gd = new GridData();
	    gd.grabExcessHorizontalSpace = true;
	    gd.horizontalAlignment = GridData.FILL;
	
	    txtQuizNo = new Text (container, SWT.BORDER);
	    txtQuizNo.setLayoutData(gd);
	}
	
	private void createComPort(Composite container) {
		List<CommPortIdentifier> ports = new CommHelper().getComPorts();
		List<String> portNames = new ArrayList<String>();
		for (CommPortIdentifier port : ports) {
			portNames.add(port.getName());
		}
		Label lbt = new Label(container, SWT.NONE);
		lbt.setText("Com Port");
	
	    combComPort = new Combo (container, SWT.READ_ONLY);
	    combComPort.setItems(portNames.toArray(new String[0]));
	    combComPort.setText("COM1");
	}
	
	private void createMode(Composite container) {
		Label lbt = new Label(container, SWT.NONE);
		lbt.setText("Mode");
	
	    combMode = new Combo (container, SWT.READ_ONLY);
	    combMode.setItems(new String[] {"PROXY", "RECV"});
	    combMode.setText("PROXY");
	}
	
	  
	private void createPort(Composite container) {
	    Label lbtPort = new Label(container, SWT.NONE);
	    lbtPort.setText("Port");
	    
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
	    quizNo = txtQuizNo.getText();
	    port = txtPort.getText();
	    comPort = combComPort.getText();
	    mode = combMode.getText();
	}
	
	@Override
	protected void okPressed() {
	    saveInput();
	    super.okPressed();
	}	
	  
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", true);
	    createButton(parent, IDialogConstants.CANCEL_ID,
	        IDialogConstants.CANCEL_LABEL, false);
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getComPort() {
		return comPort;
	}

	public void setComPort(String comPort) {
		this.comPort = comPort;
	}

	public String getQuizNo() {
		return quizNo;
	}

	public void setQuizNo(String quizNo) {
		this.quizNo = quizNo;
	}

	
} 
