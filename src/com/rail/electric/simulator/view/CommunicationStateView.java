package com.rail.electric.simulator.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.rail.electric.simulator.SimulatorMessages;

public class CommunicationStateView extends AbstractView implements IView {
	
	private Group group;

	public CommunicationStateView(Composite parent, IView parentView) {
		super(parent, parentView);

		group = new Group (parent, SWT.NULL);	
		group.setBounds(200, 100, 1400, 800);
		group.setText("Operations");
		group.setFont(new Font(group.getDisplay(),"宋体", 20, SWT.BOLD));
		
		createOperationsTable(group);
		
		rootControl = group;
	}
	
	
	private static void createOperationsTable(Group group) {
		final Table table = new Table (group, 
				SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible (true);
		table.setLinesVisible (true);
		table.setFont(new Font(group.getDisplay(),"宋体", 16, SWT.BOLD));
		table.setBounds(50, 50, 1000, 600);
		TableColumn column1 = new TableColumn (table, SWT.NULL);
		column1.setText("Name");
		column1.pack();
		TableColumn column2 = new TableColumn (table, SWT.NULL);
		column2.setText("Age");
		column2.pack();
		TableItem item1 = new TableItem (table, SWT.NULL);
		item1.setText(new String[] {"Dan", "38"});
		TableItem item2 = new TableItem (table, SWT.NULL);
		item2.setText(new String[] {"Eric", "39"});
		table.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				TableItem[] selected = table.getSelection();
				if (selected.length > 0) {
					System.out.println("Name: " + 
						selected[0].getText(0));
					System.out.println("Age: " + 
						selected[0].getText(1));
				}
			}
		});		
	}
		
	@Override
	public IView getParentView() {
		return parentView;
	}	
	
	@Override
	protected void createMenuBar(Shell shell) {
		final Menu menuBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);
				
		MenuItem returnMenuItem = new MenuItem(menuBar, SWT.CASCADE);
		returnMenuItem.setText(SimulatorMessages.Return_menu); //$NON-NLS-1$
		
		createReturnMenuItem(returnMenuItem, shell);
		
	}	
    
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {
        final Display display = new Display();
        Shell shell = new Shell(display);
        shell.setSize(1800, 1000);
        shell.setText("Operations record");
        Group group = new Group (shell, SWT.NULL);
        group.setBounds(200, 100, 1400, 800);
        group.setText("Operations");		
		group.setFont(new Font(group.getDisplay(),"宋体", 20, SWT.BOLD));
		createOperationsTable(group);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
    }
	
}
