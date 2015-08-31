package com.rail.electric.simulator.dialogs;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.rail.electric.simulator.SimulatorMessages;

public class OperationInfoDialog extends TitleAreaDialog {
	private org.eclipse.swt.widgets.List lstOperation;		
	
	private String title;
	private String message;
	private List<String> operations;
	
	public OperationInfoDialog(Shell parentShell, String title, String message, List<String> operations) {
		super(parentShell);
		this.title = title;
		this.message = message;
		this.operations = operations;
	}

	@Override
	public void create() {
		super.create();
		setTitle(title);
		setMessage(this.message, IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);
	    
	    createOperationInfo(container);	    

	    return area;
	}
	
	private void createOperationInfo(Composite container) {
		Label lbt = new Label(container, SWT.NONE);
		lbt.setText(SimulatorMessages.OperationHistory_Label);
	
	    GridData dataIp = new GridData();
	    dataIp.grabExcessHorizontalSpace = true;
	    dataIp.horizontalAlignment = GridData.FILL;
	
	    lstOperation = new org.eclipse.swt.widgets.List(container, SWT.BORDER | SWT.READ_ONLY);
	    lstOperation.setLayoutData(dataIp);
	    lstOperation.setItems(operations.toArray(new String[0]));
	}
	
	  
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, SimulatorMessages.OK_label, true);    
	}
	
	public static void main (String[] args) {
		Shell shell = new Shell(new Display());
		OperationInfoDialog dialog = new OperationInfoDialog(shell, "Operation finished", "The student has passed the quiz.", Arrays.asList(
				"Turn off k1zzzzzz", "Turn On k2", "Turn off k1", "Turn On k2", "Turn off k1", "Turn On k2"));
		dialog.open();
		
	}
}
