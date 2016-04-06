package com.rail.electric.simulator.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
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
import com.rail.electric.simulator.view.equipment.EquipmentConfigurationData;
import com.rail.electric.simulator.view.equipment.EquipmentConfigurations;
import com.rail.electric.simulator.view.equipment.EquipmentConfigurationsTableLabelProvider;
import com.rail.electric.simulator.view.equipment.EquipmentListLabelProvider;

public class EquipmentConfigurationsView extends AbstractView implements IView {
	
	private ViewForm form;
	private SashForm downForm;

	private Group group;
	private TableViewer tableViewer;
	
	private List<EquipmentConfigurations> equipConfs = new ArrayList<EquipmentConfigurations>();
	
	public EquipmentConfigurationsView(Composite parent, IView parentView) {
		super(parent, parentView);

		form = new ViewForm(parent, SWT.VERTICAL);	
		//form.setLayoutData(new FillLayout());
		createToolBar(form);
		downForm = new SashForm(form, SWT.HORIZONTAL);
		createEquipmentConfigurations();
		createLeftListViewer(downForm);
		createRightTableViewer(downForm);
		
		downForm.setWeights(new int[] {1, 3});
		form.setContent(downForm);
		
		rootControl = form;
	}
	
	private void createEquipmentConfigurations() {
		EquipmentConfigurations equip1 = new EquipmentConfigurations("LineProtection");
		equip1.addConfigurationData(new EquipmentConfigurationData("name1", "oldValue1", "newValue1", "unit1", "range1", "resolution1"));
		equip1.addConfigurationData(new EquipmentConfigurationData("name2", "oldValue2", "newValue2", "unit2", "range2", "resolution2"));
		
		EquipmentConfigurations equip2 = new EquipmentConfigurations("TransformerProtection");
		equip2.addConfigurationData(new EquipmentConfigurationData("name3", "oldValue3", "newValue3", "unit3", "range3", "resolution3"));
		equip2.addConfigurationData(new EquipmentConfigurationData("name4", "oldValue4", "newValue4", "unit4", "range4", "resolution4"));
		
		equipConfs.add(equip1);
		equipConfs.add(equip2);
	}
	
	private void createLeftListViewer(SashForm form) {
		final ListViewer listViewer = new ListViewer (form, SWT.SINGLE);
		listViewer.getList().setFont(new Font(form.getDisplay(),"宋体", 16, SWT.NORMAL));
		listViewer.setLabelProvider(new EquipmentListLabelProvider());
		listViewer.setContentProvider(new ArrayContentProvider());
		listViewer.setInput(equipConfs.toArray());
		
		listViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event)
			{
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				Object obj = selection.getFirstElement(); 
				if (obj instanceof EquipmentConfigurations) {
					EquipmentConfigurations equip = (EquipmentConfigurations) obj;
					group.setText(equip.getName());
					tableViewer.setInput(equip.getConfigurationData().toArray());
				}
			}
		});		
	}
	
	private void createRightTableViewer(SashForm form) {
		group = new Group (form, SWT.NULL);
		group.setBounds(100, 50, 1200, 600);
		//group.setLayout(new FillLayout());
		group.setFont(new Font(form.getDisplay(),"宋体", 20, SWT.BOLD));
		
		tableViewer = new TableViewer (
				group, SWT.SINGLE | SWT.FULL_SELECTION);		
		final Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setFont(new Font(form.getDisplay(),"宋体", 16, SWT.NORMAL));
		table.setBounds(50, 100, 1150, 650);
		
		
		String[] columnNames = new String[] {
				"定值名称", "定值", "新定值", "量纲", "整定范围", "精度"};
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

		tableViewer.setLabelProvider(new EquipmentConfigurationsTableLabelProvider());
		tableViewer.setContentProvider(new ArrayContentProvider());
		
	}
	
	private static void createToolBar(ViewForm form) {
		final ToolBar toolBar = new ToolBar(form, SWT.FLAT|SWT.WRAP|SWT.CENTER);
		
		ToolItem itemSave = new ToolItem(toolBar, SWT.PUSH);
		itemSave.setText("保存定值");
	    Image icon = new Image(Display.getCurrent(), EquipmentConfigurationsView.class.getResourceAsStream("equipment/icons/save_edit.gif"));
	    itemSave.setImage(icon);
	    
	    form.setTopLeft(toolBar);
		
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
        EquipmentConfigurationsView view = new EquipmentConfigurationsView(shell, null);
        view.activate();
		
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
    }
	
}
