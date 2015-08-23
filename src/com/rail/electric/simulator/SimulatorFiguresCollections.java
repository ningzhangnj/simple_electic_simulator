package com.rail.electric.simulator;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.geometry.Point;

import com.rail.electric.simulator.figures.ComplexCircuitFigure;
import com.rail.electric.simulator.figures.GroundFigure;
import com.rail.electric.simulator.figures.HighVoltageLineFigure;
import com.rail.electric.simulator.figures.LeftGroundFigure;
import com.rail.electric.simulator.figures.LineFigure;
import com.rail.electric.simulator.figures.MainSwitchFigure;
import com.rail.electric.simulator.figures.MainTransformerFigure;
import com.rail.electric.simulator.figures.StateFigure;
import com.rail.electric.simulator.figures.StateListener;
import com.rail.electric.simulator.figures.SwitchFigure;
import com.rail.electric.simulator.figures.ThreePahseTransformerFigure;
import com.rail.electric.simulator.helpers.DataTypeConverter;

public class SimulatorFiguresCollections implements StateListener {
	private static List<Figure> figures;
	public static Map<Integer, List<Integer>> relationShip;
	public static Map<Integer, Figure> id2FigureMap = new HashMap<Integer, Figure>();
	public static int SWTITCH_NUMBERS = 67;
	public static int LEDLINE_NUMBERS = 40;
	
	static {
		figures = Arrays.asList(
				
				new SwitchFigure			(131,  300, 150),
				new HighVoltageLineFigure(3, 
						Arrays.asList(	new Point(322, 198),
										new Point(322, 230)
									 )),				
				new LeftGroundFigure		(130,  200, 192),
				new MainSwitchFigure		(129,  298, 230),
				new HighVoltageLineFigure(2, 
						Arrays.asList(	new Point(322, 278),
										new Point(322, 310)
									 )),				
				new LeftGroundFigure		(128,  200, 272),
				new SwitchFigure			(127,  300, 310),
				new HighVoltageLineFigure(1, 
						Arrays.asList(	new Point(322, 358),
										new Point(322, 400)
									 )),
				new HighVoltageLineFigure(4, 
						Arrays.asList(	new Point(322, 150),
										new Point(322, 110),
										new Point(622, 110),
										new Point(622, 210),
										new Point(680, 210)
									)),	
				new ThreePahseTransformerFigure (102, 658, 210),
				new HighVoltageLineFigure(5, 
						Arrays.asList(	new Point(698, 240),
										new Point(732, 240)
									 )),	
				new GroundFigure		(101,  708, 242),
				new SwitchFigure			(103,  600, 210),
				new HighVoltageLineFigure(6, 
						Arrays.asList(	new Point(622, 258),
										new Point(622, 290)
									 )),	
				new LeftGroundFigure		(104,  500, 252),
				new MainSwitchFigure		(105,  598, 290),
				new HighVoltageLineFigure(7, 
						Arrays.asList(	new Point(622, 338),
										new Point(622, 370)
									 )),
				new LeftGroundFigure		(106,  500, 332),
				new SwitchFigure			(107,  600, 370),
				new HighVoltageLineFigure(8, 
						Arrays.asList(	new Point(622, 418),
										new Point(622, 450)
									 )),
				new LeftGroundFigure		(108,  500, 412),
				new MainTransformerFigure   (0,    588, 450),
									 
				new HighVoltageLineFigure(0, 
						Arrays.asList(	new Point(652, 470),
										new Point(680, 470)
									 )),
				new ComplexCircuitFigure	(109, 680, 467)
			);	
		
		relationShip = new HashMap<>();
		relationShip.put(1, 	Arrays.asList(127));
		relationShip.put(127, 	Arrays.asList(2));
		relationShip.put(2, 	Arrays.asList(129));
		relationShip.put(129, 	Arrays.asList(3));
		relationShip.put(3, 	Arrays.asList(131));
		relationShip.put(131, 	Arrays.asList(4));
		relationShip.put(4, 	Arrays.asList(102,103));
		relationShip.put(102, 	Arrays.asList(5));
		relationShip.put(103, 	Arrays.asList(6));
		relationShip.put(6, 	Arrays.asList(105));
		relationShip.put(105, 	Arrays.asList(7));
		relationShip.put(7, 	Arrays.asList(107));
		relationShip.put(107, 	Arrays.asList(8));
		
	}
	
	private FreeformLayer layer;	
	
	SimulatorFiguresCollections(FreeformLayer layer) {
		this.layer = layer;
	}
	
	public void activate() {
		for (Figure figure : figures) {
			layer.add(figure);
			int id = 0;
			if (figure instanceof StateFigure) {
				id = ((StateFigure)figure).getId();		
				((StateFigure)figure).registerStateListener(this);
			} else if (figure instanceof LineFigure) {
				id = ((LineFigure)figure).getId();	
				((LineFigure)figure).registerStateListener(this);
			}
			
			if (id > 0) {
				id2FigureMap.put(id, figure);				
			}
		}
		((LineFigure)id2FigureMap.get(1)).switchState();
	}

	@Override
	public void onChange(int id, boolean state) {
		chainReact(id, state);	
		byte[] result = getLedLineBytes();
		System.out.println("bytes: " + DataTypeConverter.bytesToHex(result));
	}
	
	private void chainReact(int id, boolean state) {
		if (relationShip.containsKey(id)) {
			List<Integer> children = relationShip.get(id);
			for (Integer child : children) {
				if (id2FigureMap.containsKey(child)) {
					Figure childFigure = id2FigureMap.get(child);
					if (childFigure instanceof StateFigure) {
						StateFigure childState = (StateFigure)childFigure;
						childState.setHasPower(state);
						
						chainReact(child, childState.isHasPower()&&childState.isOn());
					} else if (childFigure instanceof LineFigure) {
						LineFigure childLine = (LineFigure)childFigure;
						childLine.setOn(state);
						chainReact(child, state);
					}
				}
				
			}
		}
	}
	
	public byte[] getLedLineBytes() {
		ByteBuffer bb = ByteBuffer.allocate(8);
		long result = 0;
		for (Entry<Integer, Figure> entryId2Figure : id2FigureMap.entrySet()) {
			if (entryId2Figure.getValue() instanceof LineFigure && ((LineFigure)entryId2Figure.getValue()).isOn()) {
				int shiftValue = entryId2Figure.getKey().intValue();
				if (shiftValue < LEDLINE_NUMBERS) {
					result |= 1<<shiftValue;
				}				
			}
		}
		bb.putLong(result);
		return bb.array();
	}
	
	public int getBitPostion(byte[] input) {
		ByteBuffer bb = ByteBuffer.allocate(16);
		bb.put(input);
		long lowValue = bb.getLong(8);
		for (int i=1; i<=63; i++) {
			if ((lowValue&(1<<i)) !=0 ) {
				return i;
			}
		}
		long highValue = bb.getLong(0);
		for (int i=0; i<=(SWTITCH_NUMBERS-64); i++) {
			if ((highValue&(1<<i)) !=0 ) {
				return i+64;
			}
		}
		return 0;
	}
	
	public static void main(String[] args) {
		SimulatorFiguresCollections inst = new SimulatorFiguresCollections(null);
		ByteBuffer bb = ByteBuffer.allocate(16);
		bb.putLong(0, 0x0002L);
		bb.putLong(8, 0x0100L);
		System.out.println("bytes: " + DataTypeConverter.bytesToHex(bb.array()));
		int a = inst.getBitPostion(bb.array());
		System.out.println("Pos: " + a);
	}
}
