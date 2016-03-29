package com.rail.electric.simulator.view;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.rail.electric.simulator.SimulatorMessages;
import com.rail.electric.simulator.view.equipment.EquipmentConfigurationData;
import com.rail.electric.simulator.view.equipment.EquipmentConfigurations;
import com.rail.electric.simulator.view.equipment.EquipmentConfigurationsTableLabelProvider;
import com.rail.electric.simulator.view.equipment.EquipmentListLabelProvider;

public class EquipmentConfigurationsView extends AbstractView implements IView {
	
	private SashForm form;

	public EquipmentConfigurationsView(Composite parent, IView parentView) {
		super(parent, parentView);

		form = new SashForm(parent.getShell(), SWT.HORIZONTAL);		
		createLeftListViewer(form);
		createRightTableViewer(form);
		form.setWeights(new int[] {1, 3});
		rootControl = form;
	}
	
	private static void createLeftListViewer(SashForm form) {
		final ListViewer listViewer = new ListViewer (form, SWT.SINGLE);
		listViewer.setLabelProvider(new EquipmentListLabelProvider());
		listViewer.setContentProvider(new ArrayContentProvider());
		listViewer.setInput(new EquipmentConfigurations[] {new EquipmentConfigurations("LineProtection"), new EquipmentConfigurations("TransformerProtection")});
		
		listViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event)
			{
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				System.out.println("Selected: " + selection.getFirstElement());
			}
		});
		listViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event)
			{
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				System.out.println("Double Clicked: " + selection.getFirstElement());
			}
		});
	}
	
	private static void createRightTableViewer(SashForm form) {
		Group group = new Group (form, SWT.NULL);
		group.setText("Line Protection");
		//group.setBounds(50, 50, 1200, 600);
		group.setLayout(new FillLayout());
		group.setFont(new Font(form.getDisplay(),"宋体", 20, SWT.BOLD));
		
		final TableViewer tableViewer = new TableViewer (
				group, SWT.SINGLE | SWT.FULL_SELECTION);		
		final Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setFont(new Font(form.getDisplay(),"宋体", 16, SWT.NORMAL));
		//table.setBounds(50, 50, 1150, 550);

		String[] columnNames = new String[] {
				"Configuration Name", "Old Value", "New Value", "Unit", "Range", "Resolution"};
		int[] columnWidths = new int[] {
				300, 150, 150, 100, 300, 150};
		int[] columnAlignments = new int[] {
				SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.LEFT};
		for (int i = 0; i < columnNames.length; i++) {
			TableColumn tableColumn = 
				new TableColumn(table, columnAlignments[i]);
			tableColumn.setText(columnNames[i]);
			tableColumn.setWidth(columnWidths[i]);
		}

		EquipmentConfigurations equipmentConfigurations =  new EquipmentConfigurations("Line Protection");
		equipmentConfigurations.addConfigurationData(new EquipmentConfigurationData("name1", "oldValue1", "newValue1", "unit1", "range1", "resolution1"));
		equipmentConfigurations.addConfigurationData(new EquipmentConfigurationData("name2", "oldValue2", "newValue2", "unit2", "range2", "resolution2"));
		tableViewer.setLabelProvider(new EquipmentConfigurationsTableLabelProvider());
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setInput(equipmentConfigurations.getConfigurationData().toArray());
		
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
        shell.setLayout(new FillLayout());
        shell.setText("Equipment configurations");
        SashForm  form = new SashForm(shell, SWT.HORIZONTAL);		
		createLeftListViewer(form);
		createRightTableViewer(form);
		form.setWeights(new int[] {1, 3});
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
    }
	
}
