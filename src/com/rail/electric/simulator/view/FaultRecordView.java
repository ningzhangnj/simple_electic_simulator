package com.rail.electric.simulator.view;

import java.awt.Color;
import java.text.SimpleDateFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.jfree.ui.RectangleInsets;

import com.rail.electric.simulator.SimulatorMessages;

public class FaultRecordView extends AbstractView implements IView {
	
	private ViewForm form;
	private SashForm contentForm;
	
	public FaultRecordView(Composite parent, IView parentView) {
		super(parent, parentView);
		
		form = new ViewForm(parent, SWT.VERTICAL);	
		createToolBar(form);
		contentForm = new SashForm(form, SWT.VERTICAL);
		createVoltageChart(contentForm);
		createCurrentChart(contentForm);
			
		contentForm.setWeights(new int[] {1, 1});
		form.setContent(contentForm);
		
		rootControl = form;
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
	
	private static void createToolBar(ViewForm form) {
		final ToolBar toolBar = new ToolBar(form, SWT.FLAT|SWT.WRAP|SWT.CENTER);
		
		ToolItem itemSave = new ToolItem(toolBar, SWT.PUSH);
		itemSave.setText("导出数据");
	    Image icon = new Image(Display.getCurrent(), EquipmentConfigurationsView.class.getResourceAsStream("equipment/icons/save_edit.gif"));
	    itemSave.setImage(icon);
	    
	    form.setTopLeft(toolBar);		
	}
	
	private static void createVoltageChart(SashForm form) {
		final JFreeChart chart = createChart(createVoltageDataset(), "电压", "ms", "V");

		ChartComposite frame = new ChartComposite(form, SWT.NONE, chart, true);
		
		frame.setDisplayToolTips(true);
        frame.setHorizontalAxisTrace(false);
        frame.setVerticalAxisTrace(false);
	}
	
	private static void createCurrentChart(SashForm form) {
		final JFreeChart chart = createChart(createCurrentDataset(), "电流", "ms", "A");

		ChartComposite frame = new ChartComposite(form, SWT.NONE, chart, true);
		
		frame.setDisplayToolTips(true);
        frame.setHorizontalAxisTrace(false);
        frame.setVerticalAxisTrace(false);
	}

	private static JFreeChart createChart(XYDataset dataset, String title, String xLabel, String yLabel) {

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            title,
            xLabel,
            yLabel,
            dataset,            // data
            true,               // create legend?
            true,               // generate tooltips?
            false               // generate URLs?
        );

        chart.setBackgroundPaint(Color.white);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        
        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
        }
        
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("SSS"));
        
        return chart;

    }
        
    private static XYDataset createCurrentDataset() {

        TimeSeries s1 = new TimeSeries("Ia");
        TimeSeries s2 = new TimeSeries("Ic");
        for (int i = 0; i<100; i++) {
        	s1.add(new Millisecond(i, 0, 0, 0, 2 ,3, 2015), 2*Math.cos(Math.PI*i/10));
        	s2.add(new Millisecond(i, 0, 0, 0, 2 ,3, 2015), 2*Math.cos(Math.PI*i/10 + 2*Math.PI/3));
        }
        
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);
        dataset.addSeries(s2);
        
        return dataset;
    }
    
    private static XYDataset createVoltageDataset() {

        TimeSeries s1 = new TimeSeries("Uab");
        TimeSeries s2 = new TimeSeries("Uca");
        for (int i = 0; i<100; i++) {
        	s1.add(new Millisecond(i, 0, 0, 0, 2 ,3, 2015), 95*Math.cos(Math.PI*i/10));
        	s2.add(new Millisecond(i, 0, 0, 0, 2 ,3, 2015), 96*Math.cos(Math.PI*i/10 + 2*Math.PI/3));
        }
        
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);
        dataset.addSeries(s2);
        
        return dataset;
    }
    
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {
        final JFreeChart chart = createChart(createVoltageDataset(), "电压", "ms", "V");
        final Display display = new Display();
        Shell shell = new Shell(display);
        shell.setSize(1800, 1000);
        shell.setLayout(new FillLayout());
        shell.setText("Time series demo for jfreechart running with SWT");
        ChartComposite frame = new ChartComposite(shell, SWT.NONE, chart, true);
        frame.setDisplayToolTips(true);
        frame.setHorizontalAxisTrace(false);
        frame.setVerticalAxisTrace(false);
        
        ChartComposite frame1 = new ChartComposite(shell, SWT.NONE, chart, true);
        frame1.setDisplayToolTips(true);
        frame1.setHorizontalAxisTrace(false);
        frame1.setVerticalAxisTrace(false);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
    }
	
}
