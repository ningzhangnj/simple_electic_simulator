package com.rail.electric.simulator.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.rail.electric.simulator.SimulatorMessages;

public class CommunicationStateView extends AbstractView implements IView {
	private ViewForm form;
	private Group group;

	public CommunicationStateView(Composite parent, IView parentView) {
		super(parent, parentView);

		form = new ViewForm(parent, SWT.VERTICAL);	
		createToolBar(form);
		group = new Group (form, SWT.NULL);	
		group.setBounds(200, 100, 1400, 800);
		group.setText("通信状态");
		group.setFont(new Font(group.getDisplay(),"宋体", 24, SWT.BOLD));
		
		createOperationsTable(group);
		
		rootControl = form;
	}
	
	private static void createToolBar(ViewForm form) {
		final ToolBar toolBar = new ToolBar(form, SWT.FLAT|SWT.WRAP|SWT.CENTER|SWT.BORDER);
	    
	    ToolItem itemPrint = new ToolItem(toolBar, SWT.PUSH);
	    itemPrint.setText("打印记录");
	    Image printIcon = new Image(Display.getCurrent(), CommunicationStateView.class.getResourceAsStream("operations/icons/print_edit.gif"));
	    itemPrint.setImage(printIcon);
	    
	    form.setTopLeft(toolBar);		
	}
	
	private static void createOperationsTable(Group group) {
		final Table table = new Table (group, 
				SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible (true);
		table.setLinesVisible (true);
		table.setFont(new Font(group.getDisplay(),"宋体", 20, SWT.BOLD));
		table.setBounds(200, 100, 1000, 600);
		TableColumn column1 = new TableColumn (table, SWT.CENTER);
		column1.setText("设备名称");
		column1.setWidth(300);
		TableColumn column2 = new TableColumn (table, SWT.CENTER);
		column2.setText("IP地址");
		column2.setWidth(400);
		TableColumn column3 = new TableColumn (table, SWT.CENTER);
		column3.setText("状态");
		column3.setWidth(300);
		
		TableItem item1 = new TableItem (table, SWT.NULL);
		item1.setText(new String[] {"母联", "192.168.1.5", "连接"});
		
		TableItem item2 = new TableItem (table, SWT.NULL);
		item2.setText(new String[] {"电源进线一", "192.168.1.7", "断开"});
		
		TableItem item3 = new TableItem (table, SWT.NULL);
		item3.setText(new String[] {"电源进线二", "192.168.1.8", "断开"});
		
		TableItem item4 = new TableItem (table, SWT.NULL);
		item4.setText(new String[] {"变压器", "192.168.1.13", "连接"});
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
        shell.setText("Communication State");
        CommunicationStateView view = new CommunicationStateView(shell, null);
        view.activate();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
    }
	
}
