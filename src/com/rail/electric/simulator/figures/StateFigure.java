package com.rail.electric.simulator.figures;

import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.swt.graphics.Image;

public abstract class StateFigure extends BaseFigure {
	protected boolean isOn = false;
	private Image icon_off;
	private Image icon_on;
	private StateListener listener;
	private int power = 0; //0 means no power, 1 means #1, 2 means #2
	private String label;
	
	StateFigure(int id, String label, Image icon_off, Image icon_on, int x, int y, int width, int height) {
		super(id, icon_off, x, y, width, height);
		this.label = label;
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
		setOn(!isOn);		
		
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
		if (isOn) {
			setIcon(icon_on);
		} else {
			setIcon(icon_off);
		}	
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	
	
}
