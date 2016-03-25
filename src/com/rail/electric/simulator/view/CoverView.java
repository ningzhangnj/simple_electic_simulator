package com.rail.electric.simulator.view;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.ScalableFreeformLayeredPane;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import com.rail.electric.simulator.SimulatorMessages;
import com.rail.electric.simulator.figures.ConnectionsEntryFigure;
import com.rail.electric.simulator.figures.LoadCurveFigure;
import com.rail.electric.simulator.listeners.FigureClickListener;

public class CoverView extends AbstractView implements IView {
	
	private ScalableFreeformLayeredPane root;
	private FreeformLayer primary;
	
	private FigureCanvas canvas;
			
	public CoverView(Composite parent) {
		super(parent, null);
		
		createDiagram(parent);
		
		rootControl = canvas;
		
		loadCover();
	}
	
	private void loadCover() {
		//primary.add(new CoverFigure(0, 210, 200));
		ConnectionsEntryFigure connectionsEntryFigure = new ConnectionsEntryFigure(0, 400, 400);
		connectionsEntryFigure.addClickListener(new FigureClickListener() {

			@Override
			public void onClick(String id) {
				deactivate();
				ViewsManager.getInstance().getView("connections", parent, CoverView.this).activate();
			}
			
		});
		LoadCurveFigure loadCurveyFigure = new LoadCurveFigure(0, 800, 400);
		loadCurveyFigure.addClickListener(new FigureClickListener() {

			@Override
			public void onClick(String id) {
				deactivate();
				ViewsManager.getInstance().getView("load", parent, CoverView.this).activate();
			}
			
		});
		primary.add(connectionsEntryFigure);
		primary.add(loadCurveyFigure);
	}	
	
	private void createDiagram(Composite parent) {
		
		// Create a layered pane along with primary and connection layers
		root = new ScalableFreeformLayeredPane();
		root.setFont(parent.getFont());
		
		primary = new FreeformLayer();
		primary.setLayoutManager(new FreeformLayout());	
		root.add(primary, "Primary");

		// Create the canvas and use it to show the root figure
		canvas = new FigureCanvas(parent, SWT.NONE);
		canvas.setViewport(new FreeformViewport());
		canvas.setBackground(new Color(null, 204, 232, 207));		
		canvas.setContents(root);
		canvas.setLayoutData(new GridData(GridData.FILL_BOTH));	
	}
	
	protected void createMenuBar(Shell shell) {
		final Menu menuBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);
		MenuItem aboutMenuItem = new MenuItem(menuBar, SWT.CASCADE);
		aboutMenuItem.setText(SimulatorMessages.About_menu); //$NON-NLS-1$
		createAboutMenuItem(aboutMenuItem, shell);
	}
	
	private void createAboutMenuItem(MenuItem menuItem, final Shell shell) {		
		menuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				MessageDialog.openInformation(shell, SimulatorMessages.About_title, SimulatorMessages.Version_label + ": V1.0\n"
						+ SimulatorMessages.Manufacturer_name);
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
	}
	
	@Override
	public IView getParentView() {
		return null;
	}

}
