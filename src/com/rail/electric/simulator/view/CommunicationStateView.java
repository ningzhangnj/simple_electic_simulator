package com.rail.electric.simulator.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
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
	
	private void createToolBar(ViewForm form) {
		final ToolBar toolBar = new ToolBar(form, SWT.FLAT|SWT.WRAP|SWT.CENTER|SWT.BORDER);
	    
	    ToolItem itemPrint = new ToolItem(toolBar, SWT.PUSH);
	    itemPrint.setText("打印记录");
	    Image printIcon = new Image(Display.getCurrent(), CommunicationStateView.class.getResourceAsStream("operations/icons/print_edit.gif"));
	    itemPrint.setImage(printIcon);
	    
	    form.setTopLeft(toolBar);		
	}
	
	private void createOperationsTable(Group group) {
		TableViewer tableViewer = new TableViewer (
				group, SWT.SINGLE | SWT.FULL_SELECTION );		
		final Table table = tableViewer.getTable();
		
		table.setHeaderVisible (true);
		table.setLinesVisible (true);
		table.setFont(new Font(group.getDisplay(),"宋体", 20, SWT.BOLD));
		table.setBounds(350, 50, 700, 700);
		TableColumn column0 = new TableColumn (table, SWT.CENTER);
		column0.setWidth(0);	
		TableColumn column1 = new TableColumn (table, SWT.CENTER);
		column1.setText("设备名称");
		column1.setWidth(400);		
		TableColumn column2 = new TableColumn (table, SWT.CENTER);
		column2.setText("状态");
		column2.setWidth(280);		
				
		tableViewer.setLabelProvider(new CommunicationStateTableLabelProvider());		
		tableViewer.setContentProvider(new ArrayContentProvider());
		
		List<CommunicationState> states = new ArrayList<CommunicationState>();
		states.add(new CommunicationState("1#进线开关", true));
		states.add(new CommunicationState("1#主变110kV开关", true));
		states.add(new CommunicationState("1#所用变开关", true));
		states.add(new CommunicationState("1#主变35kV开关", true));
		states.add(new CommunicationState("35kV馈线1开关", true));
		states.add(new CommunicationState("35kV馈线3开关", true));
		states.add(new CommunicationState("35kV馈线5开关", true));
		states.add(new CommunicationState("2#进线开关", true));
		states.add(new CommunicationState("2#主变110kV开关", true));
		states.add(new CommunicationState("2#所用变开关", true));
		states.add(new CommunicationState("2#主变35kV开关", true));
		states.add(new CommunicationState("35kV馈线2开关", true));
		states.add(new CommunicationState("35kV馈线4开关", true));
		states.add(new CommunicationState("35kV馈线6开关", true));
		tableViewer.setInput(states.toArray());	
		
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
	
	private static class CommunicationStateTableLabelProvider extends LabelProvider implements ITableLabelProvider {	
		public static final Image CONNECTED_IMAGE = new Image(Display.getCurrent(),			
				CommunicationStateView.class.getResourceAsStream("communication/icons/connected.jpg"));
		public static final Image DISCONNECTED_IMAGE = new Image(Display.getCurrent(),			
				CommunicationStateView.class.getResourceAsStream("communication/icons/disconnected.jpg"));
		
		public Image getColumnImage(Object element, int columnIndex) {
			CommunicationState state = (CommunicationState) element;
			switch (columnIndex) {
				case 2:
					if (state.isConnected()) {
						return CONNECTED_IMAGE;
					} else {
						return DISCONNECTED_IMAGE;
					}	
				default :
					break;
			}
			
			return null;
		}
	
		public String getColumnText(Object element, int index) {
			CommunicationState state = (CommunicationState) element;
			switch (index) {
				case 1:
					return state.getName();	
				case 2:
					if (state.isConnected()) {
						return "连接";
					} else {
						return "断开";
					}	
				default :
					break;
			}
			return "";
		}
	}
	
	private static class CommunicationState {
		private String name;
		private boolean isConnected;
		
		public CommunicationState(String name, boolean isConnected) {
			super();
			this.name = name;
			this.isConnected = isConnected;
		}		

		public String getName() {
			return name;
		}

		public boolean isConnected() {
			return isConnected;
		}

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
