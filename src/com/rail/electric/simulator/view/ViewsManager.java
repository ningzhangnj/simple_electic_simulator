package com.rail.electric.simulator.view;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;

public class ViewsManager {
	private static ViewsManager instance = null;
	
	private Map<String, IView> viewCache = new HashMap<>();
	
	private ViewsManager() {}
	
	public static ViewsManager getInstance() {
		if (instance == null) {
			instance = new ViewsManager();
		}
		
		return instance;
	}
	
	public IView getView(String viewId, Composite parent, IView parentView) {
		if (!viewCache.containsKey(viewId) || viewCache.get(viewId) == null) {
			viewCache.put(viewId, constructView(viewId, parent, parentView));
		}
		
		return viewCache.get(viewId);
	}
	
	private IView constructView(String viewId, Composite parent, IView parentView) {
		IView view = null;
		switch(viewId) {
			case "cover":
				view = new CoverView(parent);
				break;
			case "connections":
				view = new ConnectionsView(parent, parentView);
				break;
			case "load":
				view = new LoadCurveView(parent, parentView);
				break;
			case "fault":
				view = new FaultRecordView(parent, parentView);
				break;
			case "equipment":
				view = new EquipmentConfigurationsView(parent, parentView);
				break;
			case "operations":
				view = new OperationsRecordView(parent, parentView);
				break;
			case "communication":
				view = new CommunicationStateView(parent, parentView);
				break;
			default:
				throw new UnsupportedOperationException("Invalid viewId: " + viewId);
		}
		
		return view;
	}
	
	public void disposeAllViews() {
		for (IView view : viewCache.values()) {
			view.dispose();
		}
	}
}
