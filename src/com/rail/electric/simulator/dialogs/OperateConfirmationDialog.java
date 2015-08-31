package com.rail.electric.simulator.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
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

public class OperateConfirmationDialog extends TitleAreaDialog {
	private Text txtUsername;
	private Text txtPassword;	
	
	private String message;
	private boolean isStudent;
	
	public OperateConfirmationDialog(Shell parentShell, String message, boolean isStudent) {
		super(parentShell);
		this.message = message;
		this.isStudent = isStudent;
	}

	@Override
	public void create() {
		super.create();
		setTitle(SimulatorMessages.OperateConfirmationDialog_title);
		setMessage(this.message, IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);
	    
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
	    if (isStudent) {	    	
	    	txtUsername.setText("user");
	    } else {
	    	txtUsername.setText("admin");
	    }
	}
	
	private void createPassword(Composite container) {
		Label lbt = new Label(container, SWT.NONE);
		lbt.setText(SimulatorMessages.Password_label);
	
	    GridData dataIp = new GridData();
	    dataIp.grabExcessHorizontalSpace = true;
	    dataIp.horizontalAlignment = GridData.FILL;
	
	    txtPassword = new Text(container, SWT.PASSWORD | SWT.BORDER);
	    txtPassword.setLayoutData(dataIp);
	    txtPassword.setFocus();
	}
	
	private boolean validatePassword() {
		if (isStudent && "user".equals(txtPassword.getText())) {	    	
	    	return true;
	    } else if ("admin".equals(txtPassword.getText())) {
	    	return true;
	    }
		return false;
	}
	
	@Override
	protected void okPressed() {
		boolean isPwdCorrect = validatePassword();
		if (!isPwdCorrect) {
			MessageDialog.openError(getParentShell(), SimulatorMessages.ErrorPassword_title, SimulatorMessages.ErrorPassword_message);
			super.cancelPressed();
		} else   super.okPressed();
	}	
	  
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, SimulatorMessages.OK_label, true);
	    createButton(parent, IDialogConstants.CANCEL_ID,
	    		SimulatorMessages.Cancel_label, false);
	}
	
	public static void main (String[] args) {
		Shell shell = new Shell(new Display());
		OperateConfirmationDialog dialog = new OperateConfirmationDialog(shell, "Operate on switch 1111.", true);
		dialog.open();
		
	}
}
