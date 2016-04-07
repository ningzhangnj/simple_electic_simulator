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
		EquipmentConfigurations equip1 = new EquipmentConfigurations("母联");
		equip1.addConfigurationData(new EquipmentConfigurationData("电压定值Udz1", "", "", "V", "2.00-120.00", "3.2"));
		equip1.addConfigurationData(new EquipmentConfigurationData("电压定值Udz2", "", "", "V", "2.00-120.00", "3.2"));
		equip1.addConfigurationData(new EquipmentConfigurationData("电压定值Udz3", "", "", "V", "2.00-120.00", "3.2"));
		equip1.addConfigurationData(new EquipmentConfigurationData("电压定值Udz4", "", "", "V", "2.00-120.00", "3.2"));
		equip1.addConfigurationData(new EquipmentConfigurationData("电压定值Udz5", "", "", "V", "2.00-120.00", "3.2"));
		equip1.addConfigurationData(new EquipmentConfigurationData("电压定值Udz6", "", "", "V", "2.00-120.00", "3.2"));
		equip1.addConfigurationData(new EquipmentConfigurationData("电流定值Idz1", "", "", "A", "0.02-100.00", "3.2"));
		equip1.addConfigurationData(new EquipmentConfigurationData("电流定值Idz2", "", "", "A", "0.02-100.00", "3.2"));
		equip1.addConfigurationData(new EquipmentConfigurationData("电流定值Idz3", "", "", "A", "0.02-100.00", "3.2"));
		equip1.addConfigurationData(new EquipmentConfigurationData("电流定值Idz4", "", "", "A", "0.02-100.00", "3.2"));
		equip1.addConfigurationData(new EquipmentConfigurationData("电流定值Idz5", "", "", "A", "0.02-100.00", "3.2"));
		equip1.addConfigurationData(new EquipmentConfigurationData("电流定值Idz6", "", "", "A", "0.02-100.00", "3.2"));
		equip1.addConfigurationData(new EquipmentConfigurationData("电流定值Idz7", "", "", "A", "0.02-100.00", "3.2"));
		equip1.addConfigurationData(new EquipmentConfigurationData("时间定值T1", "", "", "S", "0.00-60.00", "3.2"));
		equip1.addConfigurationData(new EquipmentConfigurationData("时间定值T2", "", "", "S", "0.00-60.00", "3.2"));
		equip1.addConfigurationData(new EquipmentConfigurationData("时间定值T3", "", "", "S", "0.00-60.00", "3.2"));
		equip1.addConfigurationData(new EquipmentConfigurationData("时间定值T4", "", "", "S", "0.00-60.00", "3.2"));
		equip1.addConfigurationData(new EquipmentConfigurationData("时间定值T5", "", "", "S", "0.00-60.00", "3.2"));
		equip1.addConfigurationData(new EquipmentConfigurationData("时间定值T6", "", "", "S", "0.00-60.00", "3.2"));
		equip1.addConfigurationData(new EquipmentConfigurationData("时间定值T7", "", "", "S", "0.00-60.00", "3.2"));
		equip1.addConfigurationData(new EquipmentConfigurationData("时间定值T8", "", "", "S", "0.00-60.00", "3.2"));
		equip1.addConfigurationData(new EquipmentConfigurationData("CT变比", "", "", "", "0.00-10.00", "3.2"));
		equip1.addConfigurationData(new EquipmentConfigurationData("PT变比", "", "", "", "0.01-10.00", "3.2"));
		
		EquipmentConfigurations equip2 = new EquipmentConfigurations("电源进线一");
		equip2.addConfigurationData(new EquipmentConfigurationData("控制字一", "", "", "", "0x0000-0xFFFF", "5.0"));
		equip2.addConfigurationData(new EquipmentConfigurationData("电压定值Udz1", "", "", "V", "2.00-120.00", "3.2"));
		equip2.addConfigurationData(new EquipmentConfigurationData("电压定值Udz2", "", "", "V", "2.00-120.00", "3.2"));
		equip2.addConfigurationData(new EquipmentConfigurationData("电压定值Udz3", "", "", "V", "2.00-120.00", "3.2"));
		equip2.addConfigurationData(new EquipmentConfigurationData("电压定值Udz4", "", "", "V", "2.00-120.00", "3.2"));
		equip2.addConfigurationData(new EquipmentConfigurationData("电压定值Udz5", "", "", "V", "2.00-120.00", "3.2"));
		equip2.addConfigurationData(new EquipmentConfigurationData("电压定值Udz6", "", "", "V", "2.00-120.00", "3.2"));
		equip2.addConfigurationData(new EquipmentConfigurationData("电流定值Idz1", "", "", "A", "0.02-100.00", "3.2"));
		equip2.addConfigurationData(new EquipmentConfigurationData("电流定值Idz2", "", "", "A", "0.02-100.00", "3.2"));
		equip2.addConfigurationData(new EquipmentConfigurationData("电流定值Idz3", "", "", "A", "0.02-100.00", "3.2"));
		equip2.addConfigurationData(new EquipmentConfigurationData("电流定值Idz4", "", "", "A", "0.02-100.00", "3.2"));
		equip2.addConfigurationData(new EquipmentConfigurationData("电流定值Idz5", "", "", "A", "0.02-100.00", "3.2"));
		equip2.addConfigurationData(new EquipmentConfigurationData("电流定值Idz6", "", "", "A", "0.02-100.00", "3.2"));
		equip2.addConfigurationData(new EquipmentConfigurationData("时间定值T1", "", "", "S", "0.00-60.00", "3.2"));
		equip2.addConfigurationData(new EquipmentConfigurationData("时间定值T2", "", "", "S", "0.00-60.00", "3.2"));
		equip2.addConfigurationData(new EquipmentConfigurationData("时间定值T3", "", "", "S", "0.00-60.00", "3.2"));
		equip2.addConfigurationData(new EquipmentConfigurationData("时间定值T4", "", "", "S", "0.00-60.00", "3.2"));
		equip2.addConfigurationData(new EquipmentConfigurationData("时间定值T5", "", "", "S", "0.00-60.00", "3.2"));
		equip2.addConfigurationData(new EquipmentConfigurationData("时间定值T6", "", "", "S", "0.00-60.00", "3.2"));		
		equip2.addConfigurationData(new EquipmentConfigurationData("CT变比", "", "", "", "0.00-10.00", "3.2"));
		equip2.addConfigurationData(new EquipmentConfigurationData("PT变比", "", "", "", "0.01-10.00", "3.2"));
		
		EquipmentConfigurations equip3 = new EquipmentConfigurations("电源进线二");
		equip3.addConfigurationData(new EquipmentConfigurationData("控制字一", "", "", "", "0x0000-0xFFFF", "5.0"));
		equip3.addConfigurationData(new EquipmentConfigurationData("控制字二", "", "", "", "0x0000-0xFFFF", "5.0"));
		equip3.addConfigurationData(new EquipmentConfigurationData("电压定值Udz1", "", "", "V", "2.00-120.00", "3.2"));
		equip3.addConfigurationData(new EquipmentConfigurationData("电压定值Udz2", "", "", "V", "2.00-120.00", "3.2"));
		equip3.addConfigurationData(new EquipmentConfigurationData("电压定值Udz3", "", "", "V", "2.00-120.00", "3.2"));
		equip3.addConfigurationData(new EquipmentConfigurationData("电压定值Udz4", "", "", "V", "2.00-120.00", "3.2"));
		equip3.addConfigurationData(new EquipmentConfigurationData("电压定值Udz5", "", "", "V", "2.00-120.00", "3.2"));
		equip3.addConfigurationData(new EquipmentConfigurationData("电压定值Udz6", "", "", "V", "2.00-120.00", "3.2"));
		equip3.addConfigurationData(new EquipmentConfigurationData("电流定值Idz1", "", "", "A", "0.02-100.00", "3.2"));
		equip3.addConfigurationData(new EquipmentConfigurationData("电流定值Idz2", "", "", "A", "0.02-100.00", "3.2"));
		equip3.addConfigurationData(new EquipmentConfigurationData("电流定值Idz3", "", "", "A", "0.02-100.00", "3.2"));
		equip3.addConfigurationData(new EquipmentConfigurationData("电流定值Idz4", "", "", "A", "0.02-100.00", "3.2"));
		equip3.addConfigurationData(new EquipmentConfigurationData("电流定值Idz5", "", "", "A", "0.02-100.00", "3.2"));
		equip3.addConfigurationData(new EquipmentConfigurationData("时间定值T1", "", "", "S", "0.00-60.00", "3.2"));
		equip3.addConfigurationData(new EquipmentConfigurationData("时间定值T2", "", "", "S", "0.00-60.00", "3.2"));
		equip3.addConfigurationData(new EquipmentConfigurationData("时间定值T3", "", "", "S", "0.00-60.00", "3.2"));
		equip3.addConfigurationData(new EquipmentConfigurationData("时间定值T4", "", "", "S", "0.00-60.00", "3.2"));
		equip3.addConfigurationData(new EquipmentConfigurationData("时间定值T5", "", "", "S", "0.00-60.00", "3.2"));
		equip3.addConfigurationData(new EquipmentConfigurationData("CT变比", "", "", "", "0.00-10.00", "3.2"));
		equip3.addConfigurationData(new EquipmentConfigurationData("PT变比", "", "", "", "0.01-10.00", "3.2"));
		
		EquipmentConfigurations equip4 = new EquipmentConfigurations("变压器");
		equip4.addConfigurationData(new EquipmentConfigurationData("控制字一", "", "", "", "0x0000-0xFFFF", "5.0"));
		equip4.addConfigurationData(new EquipmentConfigurationData("控制字二", "", "", "", "0x0000-0xFFFF", "5.0"));
		equip4.addConfigurationData(new EquipmentConfigurationData("电压定值Udz1", "", "", "V", "2.00-120.00", "3.2"));
		equip4.addConfigurationData(new EquipmentConfigurationData("电压定值Udz2", "", "", "V", "2.00-120.00", "3.2"));
		equip4.addConfigurationData(new EquipmentConfigurationData("电压定值Udz3", "", "", "V", "2.00-120.00", "3.2"));
		equip4.addConfigurationData(new EquipmentConfigurationData("电压定值Udz4", "", "", "V", "2.00-120.00", "3.2"));		
		equip4.addConfigurationData(new EquipmentConfigurationData("电流定值Idz1", "", "", "A", "0.02-100.00", "3.2"));
		equip4.addConfigurationData(new EquipmentConfigurationData("电流定值Idz2", "", "", "A", "0.02-100.00", "3.2"));
		equip4.addConfigurationData(new EquipmentConfigurationData("电流定值Idz3", "", "", "A", "0.02-100.00", "3.2"));		
		equip4.addConfigurationData(new EquipmentConfigurationData("时间定值T1", "", "", "S", "0.00-60.00", "3.2"));
		equip4.addConfigurationData(new EquipmentConfigurationData("时间定值T2", "", "", "S", "0.00-60.00", "3.2"));
		equip4.addConfigurationData(new EquipmentConfigurationData("时间定值T3", "", "", "S", "0.00-60.00", "3.2"));
		equip4.addConfigurationData(new EquipmentConfigurationData("时间定值T4", "", "", "S", "0.00-60.00", "3.2"));
		equip4.addConfigurationData(new EquipmentConfigurationData("时间定值T5", "", "", "S", "0.00-60.00", "3.2"));
		equip4.addConfigurationData(new EquipmentConfigurationData("CT变比", "", "", "", "0.00-10.00", "3.2"));
		equip4.addConfigurationData(new EquipmentConfigurationData("PT变比", "", "", "", "0.01-10.00", "3.2"));
		
		equipConfs.add(equip1);
		equipConfs.add(equip2);
		equipConfs.add(equip3);
		equipConfs.add(equip4);
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
		table.setBounds(100, 100, 1150, 650);
		
		
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
	    Image saveIcon = new Image(Display.getCurrent(), EquipmentConfigurationsView.class.getResourceAsStream("equipment/icons/save_edit.gif"));
	    itemSave.setImage(saveIcon);
	    
	    ToolItem itemImport = new ToolItem(toolBar, SWT.PUSH);
	    itemImport.setText("导入定值");
	    Image importIcon = new Image(Display.getCurrent(), EquipmentConfigurationsView.class.getResourceAsStream("equipment/icons/import_wiz.gif"));
	    itemImport.setImage(importIcon);
	    
	    ToolItem itemExport = new ToolItem(toolBar, SWT.PUSH);
	    itemExport.setText("导出定值");
	    Image exportIcon = new Image(Display.getCurrent(), EquipmentConfigurationsView.class.getResourceAsStream("equipment/icons/export_wiz.gif"));
	    itemExport.setImage(exportIcon);
	    
	    ToolItem itemFault = new ToolItem(toolBar, SWT.PUSH);
	    itemFault.setText("故障录波");
	    Image faulttIcon = new Image(Display.getCurrent(), EquipmentConfigurationsView.class.getResourceAsStream("equipment/icons/problems_view.gif"));
	    itemFault.setImage(faulttIcon);
	    
	    ToolItem itemZone = new ToolItem(toolBar, SWT.PUSH);
	    itemZone.setText("定值区号");
	    Image zoneIcon = new Image(Display.getCurrent(), EquipmentConfigurationsView.class.getResourceAsStream("equipment/icons/outline_co.gif"));
	    itemZone.setImage(zoneIcon);
	    
	    ToolItem itemControl = new ToolItem(toolBar, SWT.PUSH);
	    itemControl.setText("压板投退");
	    Image controlIcon = new Image(Display.getCurrent(), EquipmentConfigurationsView.class.getResourceAsStream("equipment/icons/pin_editor.gif"));
	    itemControl.setImage(controlIcon);
	    
	    ToolItem itemReset = new ToolItem(toolBar, SWT.PUSH);
	    itemReset.setText("信号复归");
	    Image resetIcon = new Image(Display.getCurrent(), EquipmentConfigurationsView.class.getResourceAsStream("equipment/icons/editor_area.gif"));
	    itemReset.setImage(resetIcon);
	    
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
