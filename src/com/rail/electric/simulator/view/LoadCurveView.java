package com.rail.electric.simulator.view;

import java.awt.Color;
import java.text.SimpleDateFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.jfree.ui.RectangleInsets;

import com.rail.electric.simulator.SimulatorMessages;

public class LoadCurveView extends AbstractView implements IView {
	
	private ChartComposite frame;

	public LoadCurveView(Composite parent, IView parentView) {
		super(parent, parentView);
		
		final JFreeChart chart = createChart(createDataset());

		frame = new ChartComposite(parent.getShell(), SWT.NONE, chart, true);
		
		frame.setDisplayToolTips(true);
        frame.setHorizontalAxisTrace(false);
        frame.setVerticalAxisTrace(false);
		
		rootControl = frame;
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

	private static JFreeChart createChart(XYDataset dataset) {

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            SimulatorMessages.LoadCurve_Title + "--2014-02-10",  // title
            SimulatorMessages.LoadCurveX_Label,   // x-axis label
            SimulatorMessages.LoadCurveY_Label,   // y-axis label
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
        axis.setDateFormatOverride(new SimpleDateFormat("HH"));
        
        return chart;

    }
    
    /**
     * Creates a dataset, consisting of two series of monthly data.
     *
     * @return The dataset.
     */
    private static XYDataset createDataset() {

        TimeSeries s1 = new TimeSeries(SimulatorMessages.LoadCurve_Name);
        s1.add(new Hour(0, 10, 2, 2014), 181.8);
        s1.add(new Hour(1, 10, 2, 2014), 167.3);
        s1.add(new Hour(2, 10, 2, 2014), 153.8);
        s1.add(new Hour(3, 10, 2, 2014), 167.6);
        s1.add(new Hour(4, 10, 2, 2014), 158.8);
        s1.add(new Hour(5, 10, 2, 2014), 148.3);
        s1.add(new Hour(6, 10, 2, 2014), 153.9);
        s1.add(new Hour(7, 10, 2, 2014), 142.7);
        s1.add(new Hour(8, 10, 2, 2014), 123.2);
        s1.add(new Hour(9, 10, 2, 2014), 131.8);
        s1.add(new Hour(10, 10, 2, 2014), 139.6);
        s1.add(new Hour(11, 10, 2, 2014), 142.9);
        s1.add(new Hour(12, 10, 2, 2014), 138.7);
        s1.add(new Hour(13, 10, 2, 2014), 137.3);
        s1.add(new Hour(14, 10, 2, 2014), 143.9);
        s1.add(new Hour(15, 10, 2, 2014), 139.8);
        s1.add(new Hour(16, 10, 2, 2014), 137.0);
        s1.add(new Hour(17, 10, 2, 2014), 132.8);
        s1.add(new Hour(18, 10, 2, 2014), 129.9);
        s1.add(new Hour(19, 10, 2, 2014), 131.7);
        s1.add(new Hour(20, 10, 2, 2014), 135.3);
        s1.add(new Hour(21, 10, 2, 2014), 138.9);
        s1.add(new Hour(22, 10, 2, 2014), 139.8);
        s1.add(new Hour(23, 10, 2, 2014), 141.0); 
        
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);
        
        return dataset;
    }
    
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {
        final JFreeChart chart = createChart(createDataset());
        final Display display = new Display();
        Shell shell = new Shell(display);
        shell.setSize(600, 300);
        shell.setLayout(new FillLayout());
        shell.setText("Time series demo for jfreechart running with SWT");
        ChartComposite frame = new ChartComposite(shell, SWT.NONE, chart, true);
        frame.setDisplayToolTips(true);
        frame.setHorizontalAxisTrace(false);
        frame.setVerticalAxisTrace(false);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
    }
	
}
