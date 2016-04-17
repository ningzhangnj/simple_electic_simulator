package com.rail.electric.simulator.view;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rail.electric.simulator.SimulatorMessages;
import com.rail.electric.simulator.util.SimulatorUtil;
import com.rail.electric.simulator.view.equipment.EquipmentConfigurationData;
import com.rail.electric.simulator.view.equipment.EquipmentConfigurations;
import com.rail.electric.simulator.view.equipment.EquipmentConfigurationsCellModifier;
import com.rail.electric.simulator.view.equipment.EquipmentConfigurationsTableLabelProvider;
import com.rail.electric.simulator.view.equipment.EquipmentListLabelProvider;

public class EquipmentConfigurationsView extends AbstractView implements IView {
	private final static Logger logger =  LoggerFactory.getLogger(EquipmentConfigurationsView.class);
	
	public static final String NAME = "Name";
	public static final String OLD_VALUE = "OldValue";
	public static final String NEW_VALUE = "NewValue";
    public static final String UNIT = "Unit";
    public static final String RANGE = "Range";
    public static final String RESOLUTION = "Resolution";

	public static final String[] PROPS = { NAME, OLD_VALUE, NEW_VALUE, RANGE, UNIT, RESOLUTION};
	
	private ViewForm form;
	private SashForm downForm;

	private Group group;
	private TableViewer tableViewer;
	private ListViewer listViewer;
	
	private ToolItem itemModify;
	private ToolItem itemSave;
	private ToolItem itemImport;
	private ToolItem itemExport;
	private ToolItem itemReset;
	
	private List<EquipmentConfigurations> equipConfs = new ArrayList<EquipmentConfigurations>();
	
	private boolean editable = false;
	
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
	
	private File getConfFile(String path) {
		if (Paths.get(path).isAbsolute()) {
			return new File(path);
		}
		
		StringBuilder dir = new StringBuilder();
        dir.append(System.getProperty("user.home"));
        dir.append(File.separator);
        dir.append(".simulator");
        dir.append(File.separator);
        String applicationDataPath = dir.toString();
        File f = new File(applicationDataPath);
        if (f.exists()) {
        	f.mkdirs();
        }
        
        File userConfFile = new File(applicationDataPath + path);
        return userConfFile;
	}
	
	private void importEquipmentConfigurations(EquipmentConfigurations equipConfs, String path) {
		File userConfFile = getConfFile(path);
		EquipmentConfigurations tempEquip = importimportEquipmentConfigurations(equipConfs.getName(), userConfFile);
		equipConfs.setConfigurationDatas(tempEquip.getConfigurationData());
	}
	
	private EquipmentConfigurations importimportEquipmentConfigurations(String name, File userConfFile) {
		BufferedReader reader = null;
        EquipmentConfigurations equip = new EquipmentConfigurations(name, userConfFile.getName());
        if (!userConfFile.exists()) {
        	InputStream input = EquipmentConfigurationsView.class.getResourceAsStream("equipment/" + userConfFile.getName());
        	reader = new BufferedReader(new InputStreamReader(input));
        } else {
        	try {
        		reader = new BufferedReader(new InputStreamReader(new FileInputStream(userConfFile), "UTF-8"));
			} catch (FileNotFoundException e) {
				logger.error("Failed to find configuration file {}, caused by {}", userConfFile.getName(), 
		         		   e.toString());
			} catch (UnsupportedEncodingException e) {
				logger.error("Unsupported encoding for configuration file {}, caused by {}", userConfFile.getName(), 
		         		   e.toString());
			}
        }
		
		String line;
		try {
			while((line = reader.readLine()) != null) {
				if (line.isEmpty()) {
					break;
				}
				
				if (!line.trim().isEmpty()) {
					String unicodeLine = SimulatorUtil.decodeUnicode(line.trim());
					String[] cols = unicodeLine.split(",");
					if (cols.length != PROPS.length) {
						throw new UnsupportedOperationException("Error when parsing default equipment configurations file. Path: " + userConfFile.getName() +
								". Line: " + line.trim());
					}
					
					equip.addConfigurationData(new EquipmentConfigurationData(cols[0], cols[1],
							cols[2], cols[3], 
							cols[4], cols[5]));
				}
			}
		} catch (IOException e) {
			logger.error("Failed to read user configuration file {}, caused by {}", userConfFile.getName(), 
	         		   e.toString());
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				logger.error("Failed to close buffered reader, caused by {}", e.toString());
			}
		}
		
		return equip;
	}
	
	private EquipmentConfigurations getEquipmentConfigurations(String name, String path) {        
        File userConfFile = getConfFile(path);
        return importimportEquipmentConfigurations(name, userConfFile);
    }
	
		
	private boolean saveEquipmentConfigurations(EquipmentConfigurations equipConfs) {
		return saveAsEquipmentConfigurations(equipConfs, equipConfs.getPath());
    }
	
	private boolean saveAsEquipmentConfigurations(EquipmentConfigurations equipConfs, String path) {
		File userConfFile = getConfFile(path);
		if (!userConfFile.exists()) {
			try {
                userConfFile.createNewFile();
            } catch (IOException e) {
               logger.error("Failed to create user configuration file {}, caused by {}", userConfFile.getName(), 
            		   e.toString());
               return false;
            }
		}
		
		BufferedOutputStream output = null;
		try {
			output = new BufferedOutputStream(new FileOutputStream(userConfFile));
			StringBuilder sb = new StringBuilder();
			for (EquipmentConfigurationData equipConfData : equipConfs.getConfigurationData()) {
				sb.append(equipConfData.getName());
				sb.append(",");
				sb.append(equipConfData.getNewValue()); // override old value
				sb.append(",");
				sb.append(equipConfData.getNewValue());
				sb.append(",");
				sb.append(equipConfData.getRange());
				sb.append(",");
				sb.append(equipConfData.getUnit());
				sb.append(",");
				sb.append(equipConfData.getResolution());
				sb.append("\n");
			}
			String confData = sb.toString();
			
			output.write(confData.getBytes("UTF-8"));
		} catch (FileNotFoundException e) {
			logger.error("Failed to find configuration file {}, caused by {}", userConfFile.getName(), 
	         		   e.toString());
		} catch (IOException e) {
			logger.error("Failed to write user configuration file {}, caused by {}", userConfFile.getName(), 
	         		   e.toString());
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
				logger.error("Failed to close buffered writer, caused by {}", e.toString());
			}
		}	
		
        return true;
    }

	
	private void createEquipmentConfigurations() {		
		EquipmentConfigurations equip1 = getEquipmentConfigurations("母联", "MotherLine.properties");		
		EquipmentConfigurations equip2 = getEquipmentConfigurations("电源进线一", "PowerInput1.properties");
		EquipmentConfigurations equip3 = getEquipmentConfigurations("电源进线二", "PowerInput2.properties");
		EquipmentConfigurations equip4 = getEquipmentConfigurations("馈出一", "FeedOutput1.properties");
		EquipmentConfigurations equip5 = getEquipmentConfigurations("馈出二", "FeedOutput2.properties");
		EquipmentConfigurations equip6 = getEquipmentConfigurations("馈出三", "FeedOutput3.properties");
		EquipmentConfigurations equip7 = getEquipmentConfigurations("馈出四", "FeedOutput4.properties");
		EquipmentConfigurations equip8 = getEquipmentConfigurations("馈出五", "FeedOutput5.properties");
		EquipmentConfigurations equip9 = getEquipmentConfigurations("馈出六", "FeedOutput6.properties");
		EquipmentConfigurations equip10 = getEquipmentConfigurations("1#主变", "Transformer1.properties");
		EquipmentConfigurations equip11 = getEquipmentConfigurations("2#主变", "Transformer2.properties");
		
		equipConfs.add(equip1);
		equipConfs.add(equip2);
		equipConfs.add(equip3);
		equipConfs.add(equip4);
		equipConfs.add(equip5);
		equipConfs.add(equip6);
		equipConfs.add(equip7);
		equipConfs.add(equip8);
		equipConfs.add(equip9);
		equipConfs.add(equip10);
		equipConfs.add(equip11);
	}
	
	private void createLeftListViewer(SashForm form) {
		listViewer = new ListViewer (form, SWT.SINGLE);
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
					updateToolBar();
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
				group, SWT.SINGLE | SWT.FULL_SELECTION );		
		final Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setFont(new Font(form.getDisplay(),"宋体", 16, SWT.NORMAL));
		table.setBounds(100, 100, 1150, 650);
		
		
		String[] columnNames = new String[] {
				"定值名称", "定值", "新定值", "整定范围", "量纲", "精度"};
		int[] columnWidths = new int[] {
				300, 150, 150, 300, 100, 150};
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
		
		CellEditor[] editors = new CellEditor[7];
	    editors[0] = new CheckboxCellEditor(table);
	    editors[1] = new TextCellEditor(table);
	    editors[2] = new TextCellEditor(table);
	    editors[3] = new TextCellEditor(table);
	    editors[4] = new TextCellEditor(table);
	    editors[5] = new TextCellEditor(table);
	    editors[6] = new TextCellEditor(table);

	    tableViewer.setColumnProperties(PROPS);
	    tableViewer.setCellModifier(new EquipmentConfigurationsCellModifier(tableViewer));
	    tableViewer.setCellEditors(editors);	    
	    setEditable(false);		
	}
	
	private void createToolBar(ViewForm form) {
		final ToolBar toolBar = new ToolBar(form, SWT.FLAT|SWT.WRAP|SWT.CENTER);
		
		itemModify = new ToolItem(toolBar, SWT.PUSH);
		itemModify.setText("修改定值");
	    Image modifyIcon = new Image(Display.getCurrent(), EquipmentConfigurationsView.class.getResourceAsStream("equipment/icons/editor_area.gif"));
	    itemModify.setImage(modifyIcon);
	    itemModify.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				setEditable(true);
			}
	    	
	    });
	    
		itemSave = new ToolItem(toolBar, SWT.PUSH);
		itemSave.setText("保存定值");
	    Image saveIcon = new Image(Display.getCurrent(), EquipmentConfigurationsView.class.getResourceAsStream("equipment/icons/save_edit.gif"));
	    itemSave.setImage(saveIcon);
	    itemSave.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				EquipmentConfigurations equipConfs = (EquipmentConfigurations)((IStructuredSelection)listViewer.getSelection()).getFirstElement();
				if (equipConfs != null) {	
					saveEquipmentConfigurations(equipConfs);
				}
				setEditable(false);				
			}	    	
	    });
	    
	    itemImport = new ToolItem(toolBar, SWT.PUSH);
	    itemImport.setText("导入定值");
	    Image importIcon = new Image(Display.getCurrent(), EquipmentConfigurationsView.class.getResourceAsStream("equipment/icons/import_wiz.gif"));
	    itemImport.setImage(importIcon);
	    itemImport.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				EquipmentConfigurations equipConfs = (EquipmentConfigurations)((IStructuredSelection)listViewer.getSelection()).getFirstElement();
				if (equipConfs != null) {	
					FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell());
					dialog.setFilterExtensions(new String[]{"*.properties"});
					String path = dialog.open();
					if (path != null) {
						importEquipmentConfigurations(equipConfs, path);
						tableViewer.setInput(equipConfs.getConfigurationData().toArray());
					}
				}							
			}	    	
	    });
	    
	    itemExport = new ToolItem(toolBar, SWT.PUSH);
	    itemExport.setText("导出定值");
	    Image exportIcon = new Image(Display.getCurrent(), EquipmentConfigurationsView.class.getResourceAsStream("equipment/icons/export_wiz.gif"));
	    itemExport.setImage(exportIcon);
	    itemExport.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				EquipmentConfigurations equipConfs = (EquipmentConfigurations)((IStructuredSelection)listViewer.getSelection()).getFirstElement();
				if (equipConfs != null) {	
					FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell());
					dialog.setFilterExtensions(new String[]{"*.properties"});
					String path = dialog.open();
					if (path != null) {
						saveAsEquipmentConfigurations(equipConfs, path);
					}
				}							
			}	    	
	    });
	    
	    itemReset = new ToolItem(toolBar, SWT.PUSH);
	    itemReset.setText("信号复归");
	    Image resetIcon = new Image(Display.getCurrent(), EquipmentConfigurationsView.class.getResourceAsStream("equipment/icons/refresh_nav.gif"));
	    itemReset.setImage(resetIcon);
	    itemReset.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) { }

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				EquipmentConfigurations equipConfs = (EquipmentConfigurations)((IStructuredSelection)listViewer.getSelection()).getFirstElement();
				if (equipConfs != null) {					
					MessageDialog.openInformation(Display.getCurrent().getActiveShell(), 
							SimulatorMessages.OperationFinished_Title, 
							equipConfs.getName() + "信号复位完成！");
				}				
			}
	    	
	    });
	    
	    form.setTopLeft(toolBar);		
	}
	
	private void setEditable(boolean flag) {
		editable = flag;
		updateToolBar();
		tableViewer.getTable().setEnabled(flag);
	}
	
	private void updateToolBar() {
		IStructuredSelection selection = (IStructuredSelection) listViewer.getSelection();
		Object obj = selection.getFirstElement(); 
		if (obj == null) {
			itemModify.setEnabled(false);
			itemSave.setEnabled(false);
			itemImport.setEnabled(false);
			itemExport.setEnabled(false);
			itemReset.setEnabled(false);
		} else {
			if (editable) {
				itemModify.setEnabled(false);
				itemSave.setEnabled(true);
				itemImport.setEnabled(true);
				itemExport.setEnabled(true);
				itemReset.setEnabled(true);
			} else {
				itemModify.setEnabled(true);
				itemSave.setEnabled(false);
				itemImport.setEnabled(false);
				itemExport.setEnabled(false);
				itemReset.setEnabled(true);
			}
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
