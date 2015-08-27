package com.rail.electric.simulator.figures;

import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;

import com.rail.electric.simulator.SimulatorMessages;
import com.rail.electric.simulator.dialogs.OperateConfirmationDialog;
import com.rail.electric.simulator.listeners.StateListener;
import com.rail.electric.simulator.listeners.ValidateSwitchListener;

public abstract class StateFigure extends BaseFigure {
	protected boolean isOn = false;
	private Image icon_off;
	private Image icon_on;
	private StateListener statelistener;
	private ValidateSwitchListener validateListener;
	private int power; //0 means no power, 1 means #1(1<<0), 2 means #2(1<<1)
	private String label;
	
	StateFigure(int id, String label, Image icon_off, Image icon_on, int x, int y, int width, int height, int initialPower) {
		super(id, icon_off, x, y, width, height);
		this.label = label;
		this.icon_off = icon_off;
		this.icon_on = icon_on;
		this.power = initialPower;
		setIcon(power>0?icon_on:icon_off);
		this.addMouseListener(new MouseListener.Stub() {
			public void mousePressed(MouseEvent me) {
				if (me.button == 1) {
					String message = SimulatorMessages.TurnOn_message;
					if (isOn) message = SimulatorMessages.TurnOff_message;
					OperateConfirmationDialog dialog = new OperateConfirmationDialog(null, 
							message + " " + getLabel(), true);
					if (dialog.open() == Window.OK) {
						String result = validateOperation();
						if (result == null)
							switchState();
					}
				}
				me.consume();
			}
		});
	}
	
	private String validateOperation() {
		if (validateListener != null) {
			return validateListener.validate(getId(), isOn());
		}
		return null;
	}
	
	public void switchState() {
		setOn(!isOn);		
		
		if (statelistener != null) {
			statelistener.onChange(getId(), isOn?power:0);
		}
	}
	
	public void registerStateListener(StateListener listener) {
		this.statelistener = listener;
	}
	
	public void clearStateListener() {
		this.statelistener = null;
	}

	public void registerValidateSwitchListener(ValidateSwitchListener listener) {
		this.validateListener = listener;
	}
	
	public void clearValidateSwitchListener() {
		this.validateListener = null;
	}
	
	public boolean isOn() {
		return isOn;
	}

	public void setOn(boolean isOn) {
		this.isOn = isOn;
		
		setIcon(power>0?icon_on:icon_off);			
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
