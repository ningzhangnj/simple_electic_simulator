package com.rail.electric.simulator.view;


public interface IView {
	void activate();
	
	void deactivate();
	
	IView getParentView();
	
	void return2ParentView();
	
	void dispose();
}
