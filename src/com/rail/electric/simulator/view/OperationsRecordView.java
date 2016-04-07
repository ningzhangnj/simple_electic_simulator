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

public class OperationsRecordView extends AbstractView implements IView {
	private ViewForm form;
	private Group group;

	public OperationsRecordView(Composite parent, IView parentView) {
		super(parent, parentView);

		form = new ViewForm(parent, SWT.VERTICAL);	
		createToolBar(form);
		group = new Group (form, SWT.NULL);	
		group.setBounds(200, 100, 1400, 800);
		group.setText("操作记录");
		group.setFont(new Font(group.getDisplay(),"宋体", 20, SWT.BOLD));
		
		createOperationsTable(group);
		
		rootControl = form;
	}
	
	private static void createToolBar(ViewForm form) {
		final ToolBar toolBar = new ToolBar(form, SWT.FLAT|SWT.WRAP|SWT.CENTER|SWT.BORDER);
		
		ToolItem itemNew = new ToolItem(toolBar, SWT.PUSH);
		itemNew.setText("新建记录");
	    Image newIcon = new Image(Display.getCurrent(), OperationsRecordView.class.getResourceAsStream("operations/icons/new_wiz.gif"));
	    itemNew.setImage(newIcon);
	    
	    ToolItem itemOpen = new ToolItem(toolBar, SWT.PUSH);
	    itemOpen.setText("打开记录");
	    Image openIcon = new Image(Display.getCurrent(), OperationsRecordView.class.getResourceAsStream("operations/icons/pin_editor.gif"));
	    itemOpen.setImage(openIcon);
	    
	    ToolItem itemSave = new ToolItem(toolBar, SWT.PUSH);
	    itemSave.setText("保存记录");
	    Image saveIcon = new Image(Display.getCurrent(), OperationsRecordView.class.getResourceAsStream("operations/icons/save_edit.gif"));
	    itemSave.setImage(saveIcon);
	    
	    ToolItem itemPrint = new ToolItem(toolBar, SWT.PUSH);
	    itemPrint.setText("打印记录");
	    Image printIcon = new Image(Display.getCurrent(), OperationsRecordView.class.getResourceAsStream("operations/icons/print_edit.gif"));
	    itemPrint.setImage(printIcon);
	    
	    form.setTopLeft(toolBar);		
	}
	
	private static void createOperationsTable(Group group) {
		final Table table = new Table (group, 
				SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible (true);
		table.setLinesVisible (true);
		table.setFont(new Font(group.getDisplay(),"宋体", 16, SWT.BOLD));
		table.setBounds(200, 100, 1000, 600);
		TableColumn column1 = new TableColumn (table, SWT.CENTER);
		column1.setText("序号");
		column1.setWidth(100);
		TableColumn column2 = new TableColumn (table, SWT.CENTER);
		column2.setText("操作项目");
		column2.setWidth(600);
		TableColumn column3 = new TableColumn (table, SWT.CENTER);
		column3.setText("备注");
		column3.setWidth(300);
		for (int i=0; i<22; i++) {
			TableItem item = new TableItem (table, SWT.NULL);
			item.setText(new String[] {Integer.toString(i+1), "", ""});
		}
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
        OperationsRecordView view = new OperationsRecordView(shell, null);
        view.activate();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
    }
	
}
