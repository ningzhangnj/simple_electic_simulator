package com.rail.electric.simulator.view;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public abstract class AbstractView implements IView {
	protected Composite parent;
	
	protected IView parentView;
	
	protected Control rootControl;
	
	public AbstractView(Composite parent, IView parentView) {
		super();
		this.parent = parent;
		this.parentView = parentView;
	}
	
	@Override
	public void activate() {	
		rootControl.setBounds(parent.getClientArea());
		rootControl.setVisible(true);
		
		createMenuBar(parent.getShell());
	}
	
	@Override
	public void deactivate() {
		rootControl.setVisible(false);
	}
	
	@Override
	public void return2ParentView() {
		deactivate();
		getParentView().activate();
	}
	
	@Override
	public void dispose() {
		deactivate();
	}
	
	protected abstract void createMenuBar(Shell shell) ;
	
	protected void createReturnMenuItem(MenuItem menuItem, final Shell shell) {		
		menuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				return2ParentView();
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
	}
}
