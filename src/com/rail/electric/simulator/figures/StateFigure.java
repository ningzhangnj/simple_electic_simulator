package com.rail.electric.simulator.figures;

import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.swt.graphics.Image;

public abstract class StateFigure extends BaseFigure {
	protected boolean isOn = false;
	private Image icon_off;
	private Image icon_on;
	private StateListener listener;
	private boolean hasPower = false;
	
	StateFigure(int id, Image icon_off, Image icon_on, int x, int y, int width, int height) {
		super(id, icon_off, x, y, width, height);
		this.icon_off = icon_off;
		this.icon_on = icon_on;
		this.addMouseListener(new MouseListener.Stub() {
			public void mousePressed(MouseEvent me) {
				if (me.button == 1) {
					switchState();
				}
				me.consume();
			}
		});
	}
	
	public void switchState() {
		isOn = !isOn;
		if (isOn) {
			setIcon(icon_on);
		} else {
			setIcon(icon_off);
		}	
		
		if (listener != null) {
			listener.onChange(getId(), hasPower&&isOn);
		}
	}
	
	public void registerStateListener(StateListener listener) {
		this.listener = listener;
	}

	public boolean isOn() {
		return isOn;
	}

	public void setOn(boolean isOn) {
		this.isOn = isOn;
	}

	public boolean isHasPower() {
		return hasPower;
	}

	public void setHasPower(boolean hasPower) {
		this.hasPower = hasPower;
	}
	
}
