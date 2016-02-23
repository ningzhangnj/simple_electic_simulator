package com.rail.electric.simulator.figures;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.swt.graphics.Image;

import com.rail.electric.simulator.listeners.FigureClickListener;

public abstract class ClickableFigure extends BaseFigure {
	protected List<FigureClickListener> clickListeners = new ArrayList<>();

	ClickableFigure(int id, Image icon, int x, int y, int width, int height) {
		super(id, icon, x, y, width, height);
		this.addMouseListener(new MouseListener.Stub() {
			public void mousePressed(MouseEvent me) {
				for (FigureClickListener clickListener : clickListeners) {
					clickListener.onClick(this.getClass().getName());
				}
			}
		});
	}

	public void addClickListener(FigureClickListener clickListener) {
		clickListeners.add(clickListener);
	}
	
	public void removeClickListener(FigureClickListener clickListener) {
		clickListeners.remove(clickListener);
	}
	
}
