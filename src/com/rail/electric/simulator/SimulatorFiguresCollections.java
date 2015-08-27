package com.rail.electric.simulator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.geometry.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rail.electric.simulator.figures.ComplexCircuitFigure;
import com.rail.electric.simulator.figures.FlipComplexCircuitFigure;
import com.rail.electric.simulator.figures.FlipGroundWithResistFigure;
import com.rail.electric.simulator.figures.FlipMainTransformerFigure;
import com.rail.electric.simulator.figures.FlipThreePahseTransformerFigure;
import com.rail.electric.simulator.figures.GroundFigure;
import com.rail.electric.simulator.figures.GroundTransformerFigure;
import com.rail.electric.simulator.figures.GroundWithResistFigure;
import com.rail.electric.simulator.figures.HighVoltageLineFigure;
import com.rail.electric.simulator.figures.LeftGroundFigure;
import com.rail.electric.simulator.figures.LineFigure;
import com.rail.electric.simulator.figures.LowVoltageLineFigure;
import com.rail.electric.simulator.figures.MainSwitchFigure;
import com.rail.electric.simulator.figures.MainTransformerFigure;
import com.rail.electric.simulator.figures.RightGroundFigure;
import com.rail.electric.simulator.figures.SmallThreePhaseTransformerFigure;
import com.rail.electric.simulator.figures.StateFigure;
import com.rail.electric.simulator.figures.SwitchFigure;
import com.rail.electric.simulator.figures.ThreePahseTransformerFigure;
import com.rail.electric.simulator.figures.TwoPhaseTransformerFigure;
import com.rail.electric.simulator.figures.UpGroundFigure;
import com.rail.electric.simulator.helpers.CommHelper;
import com.rail.electric.simulator.helpers.DataTypeConverter;
import com.rail.electric.simulator.listeners.StateListener;
import com.rail.electric.simulator.listeners.ValidateSwitchListener;
import com.rail.electric.simulator.net.TeacherServer;

public class SimulatorFiguresCollections implements StateListener, ValidateSwitchListener {
	private final static Logger logger =  LoggerFactory.getLogger(TeacherServer.class);
	
	public static int SWTITCH_NUMBERS = 67;
	public static int LEDLINE_NUMBERS = 40;
	
	private static final byte BEGIN_BYTE = (byte)0xf5;
	private static final byte END_BYTE = (byte)0xfa;
	private static final byte CORRECT_PACKET_BYTE = (byte)0xf6;
	private static final byte READ_SWITCH_BYTE = (byte)0xf4;
	
	private static final int ALL_POWER_ON = (1<<0)|(1<<1);
	
	private static List<HashMap<Integer, List<Integer>>> relationShips;
	
	static {
		relationShips = Arrays.asList(
				new HashMap<Integer, List<Integer>>(),
				new HashMap<Integer, List<Integer>>());
		//#1 input
		relationShips.get(0).put(1, 	Arrays.asList(127));
		relationShips.get(0).put(127, 	Arrays.asList(2));
		relationShips.get(0).put(2, 	Arrays.asList(129));
		relationShips.get(0).put(129, 	Arrays.asList(3));
		relationShips.get(0).put(3, 	Arrays.asList(131));
		relationShips.get(0).put(131, 	Arrays.asList(4));
		relationShips.get(0).put(4, 	Arrays.asList(102,103));
		relationShips.get(0).put(102, 	Arrays.asList(5));
		relationShips.get(0).put(103, 	Arrays.asList(6));
		relationShips.get(0).put(6, 	Arrays.asList(105));
		relationShips.get(0).put(105, 	Arrays.asList(7));
		relationShips.get(0).put(7, 	Arrays.asList(107));
		relationShips.get(0).put(107, 	Arrays.asList(8));
		
		//#2 input
	}
	
	private FreeformLayer layer;	
	private byte[] switchStatus = new byte[SWTITCH_NUMBERS]; 
	private CommHelper commHelper = new CommHelper();
	private boolean isRunning = true;
	
	private List<Figure> figures;
	
	private Map<Integer, Figure> id2FigureMap = new HashMap<Integer, Figure>();
	
	SimulatorFiguresCollections(FreeformLayer layer) {
		this.layer = layer;
		init();
	}
	
	public void init() {
		this.figures = Arrays.asList(
				//#1 High
				new SwitchFigure			(131,  "1111", 300, 150, 1),
				new HighVoltageLineFigure(3, 
						Arrays.asList(	new Point(322, 198),
										new Point(322, 230),
										new Point(304, 230)
									 ), 1),				
				new LeftGroundFigure		(130, "1111E", 240, 208, 1),
				new MainSwitchFigure		(129,  "111", 298, 230, 1),
				new HighVoltageLineFigure(2, 
						Arrays.asList(	new Point(368, 278),
										new Point(322, 278),
										new Point(322, 310),
										new Point(304, 310)
									 ), 1),	
				new	TwoPhaseTransformerFigure (0, 350, 278),
				new LeftGroundFigure		(128, "1113E", 240, 288, 1),
				new SwitchFigure			(127, "1113", 300, 310, 1),
				new HighVoltageLineFigure(1, 
						Arrays.asList(	new Point(322, 358),
										new Point(322, 400)
									 ), 1),
				new HighVoltageLineFigure(4, 
						Arrays.asList(	new Point(322, 150),
										new Point(322, 110),
										new Point(622, 110),
										new Point(622, 210),
										new Point(680, 210)
									), 1),	
				new ThreePahseTransformerFigure (102, "1001", 658, 210, 1),
				new HighVoltageLineFigure(5, 
						Arrays.asList(	new Point(698, 240),
										new Point(732, 240)
									 ), 1),	
				new GroundFigure		(101, "1001E", 708, 242, 1),
				new SwitchFigure			(103, "1011", 600, 210, 1),
				new HighVoltageLineFigure(6, 
						Arrays.asList(	new Point(622, 258),
										new Point(622, 290),
										new Point(604, 290)
									 ), 1),	
				new LeftGroundFigure		(104, "1011E",  540, 268, 1),
				new MainSwitchFigure		(105,  "101", 598, 290, 1),
				new HighVoltageLineFigure(7, 
						Arrays.asList(	new Point(622, 338),
										new Point(622, 370),
										new Point(604, 370)
									 ), 1),
				new LeftGroundFigure		(106, "1013E",  540, 348, 1),
				new SwitchFigure			(107,  "1013", 600, 370, 1),
				new HighVoltageLineFigure(8, 
						Arrays.asList(	new Point(622, 418),
										new Point(622, 450),
										new Point(604, 450)
									 ), 1),
				new LeftGroundFigure		(108, "1014E",  540, 428, 1),
				new MainTransformerFigure   (0,    588, 450),
									 
				new HighVoltageLineFigure(0, 
						Arrays.asList(	new Point(652, 470),
										new Point(680, 470)
									 ), 1),
				new ComplexCircuitFigure	(109, "1010", 680, 467, 1),
				
				//#1 Low
				new GroundTransformerFigure(0, 290, 486),
				new LowVoltageLineFigure(11, 
						Arrays.asList(	new Point(322, 550),
										new Point(322, 590)
									 ), 1),
				new MainSwitchFigure		(125,  "331", 298, 590, 1),
				new LowVoltageLineFigure(10, 
						Arrays.asList(	new Point(322, 638),
										new Point(322, 680)
									 ), 1),
				new UpGroundFigure			(126, "3311E",  360, 580, 1),
				new SwitchFigure			(124, "3311", 300, 680, 1),
				new LowVoltageLineFigure(0, 
						Arrays.asList(	new Point(622, 512),
										new Point(622, 590)
									), 1),
				new MainSwitchFigure		(110,  "301", 598, 590, 1),
				new LowVoltageLineFigure(16, 
						Arrays.asList(	new Point(622, 638),
										new Point(622, 680)
									), 1),
				new SwitchFigure			(112, "3011", 598, 680, 1),
				new GroundFigure			(111, "3011E", 536, 680, 1),
				new LowVoltageLineFigure(19, 
						Arrays.asList(	new Point(100, 780),
										new Point(850, 780)
									), 1),
				new SwitchFigure			(122, "3001", 200, 810, 1),
				new SwitchFigure			(119, "3131", 350, 810, 1),
				new SwitchFigure			(116, "3121", 500, 810, 1),
				new SwitchFigure			(113, "3111", 650, 810, 1),
				new LowVoltageLineFigure(9, 
						Arrays.asList(	new Point(222, 858),
										new Point(222, 874),
										new Point(172, 874),
										new Point(282, 874),
										new Point(282, 890)
									), 1),
				new LowVoltageLineFigure(12, 
						Arrays.asList(	new Point(372, 858),
										new Point(372, 890),
										new Point(372, 874),
										new Point(432, 874),
										new Point(432, 890)
									), 1),
				new LowVoltageLineFigure(14, 
						Arrays.asList(	new Point(522, 858),
										new Point(522, 890),
										new Point(522, 874),
										new Point(582, 874),
										new Point(582, 890)
									), 1),
				new LowVoltageLineFigure(18, 
						Arrays.asList(	new Point(672, 858),
										new Point(672, 890),
										new Point(672, 874),
										new Point(732, 874),
										new Point(732, 890)
									), 1),									
				new FlipGroundWithResistFigure (0, 140, 870),
				new SmallThreePhaseTransformerFigure(0, 200, 890),
				new SwitchFigure			(123, "3001E", 260, 890, 1),
				new SwitchFigure			(121, "3131E", 410, 890, 1),
				new SwitchFigure			(118, "3121E", 560, 890, 1),
				new SwitchFigure			(115, "3111E", 710, 890, 1),
				new MainSwitchFigure		(120,  "313", 350, 890, 1),
				new MainSwitchFigure		(117,  "312", 500, 890, 1),
				new MainSwitchFigure		(114,  "311", 650, 890, 1),
				new LowVoltageLineFigure(13, 
						Arrays.asList(	new Point(372, 938),
										new Point(372, 980)
									), 1),
				new LowVoltageLineFigure(15, 
						Arrays.asList(	new Point(522, 938),
										new Point(522, 980)
									), 1),
				new LowVoltageLineFigure(17, 
						Arrays.asList(	new Point(672, 938),
										new Point(672, 980)
									), 1),
				new SwitchFigure			(133, "3101", 800, 680, 1),
				new GroundFigure			(134, "3101E", 740, 680, 1),
				new MainSwitchFigure		(132,  "310", 800, 590, 3), //Special one
				new LowVoltageLineFigure(20, 
						Arrays.asList(	new Point(822, 638),
										new Point(822, 680),
										new Point(762, 680)
									), 1),
									
				//#2 High
				new SwitchFigure			(137,  "1121", 1454, 150, 2),
				new HighVoltageLineFigure(38, 
						Arrays.asList(	new Point(1478, 198),
										new Point(1478, 230),
										new Point(1492, 230)
									 ), 2),				
				new RightGroundFigure		(138, "1121E", 1492, 206, 2),
				new MainSwitchFigure		(139,  "112", 1456, 230, 2),
				new HighVoltageLineFigure(39, 
						Arrays.asList(	new Point(1428, 278),
										new Point(1478, 278),
										new Point(1478, 310),
										new Point(1492, 310)
									 ), 2),	
				new	TwoPhaseTransformerFigure (0, 1410, 278),
				new RightGroundFigure		(140, "1123E", 1492, 284, 2),
				new SwitchFigure			(141, "1123", 1456, 310, 2),
				new HighVoltageLineFigure(40, 
						Arrays.asList(	new Point(1478, 358),
										new Point(1478, 400)
									 ), 2),
				new HighVoltageLineFigure(37, 
						Arrays.asList(	new Point(1478, 150),
										new Point(1478, 110),
										new Point(1178, 110),
										new Point(1178, 210),
										new Point(1120, 210)
									), 2),	
				new FlipThreePahseTransformerFigure (166, "1002", 1100, 210, 2),
				new HighVoltageLineFigure(36, 
						Arrays.asList(	new Point(1102, 240),
										new Point(1068, 240)
									 ), 2),	
				new GroundFigure		(167, "1002E", 1048, 242, 2),
				new SwitchFigure			(165, "1021", 1156, 210, 2),
				new HighVoltageLineFigure(35, 
						Arrays.asList(	new Point(1178, 258),
										new Point(1178, 290),
										new Point(1192, 290)
									 ), 2),	
				new RightGroundFigure		(164, "1021E",  1192, 264, 2),
				new MainSwitchFigure		(163,  "102", 1156, 290, 2),
				new HighVoltageLineFigure(34, 
						Arrays.asList(	new Point(1178, 338),
										new Point(1178, 370),
										new Point(1192, 370)
									 ), 2),
				new RightGroundFigure		(162, "1023E",  1192, 344, 2),
				new SwitchFigure			(161,  "1023", 1156, 370, 2),
				new HighVoltageLineFigure(33, 
						Arrays.asList(	new Point(1178, 418),
										new Point(1178, 450),
										new Point(1192, 450)
									 ), 2),
				new RightGroundFigure		(160, "1024E",  1192, 424, 2),
				new FlipMainTransformerFigure   (0,    1148, 450),
									 
				new HighVoltageLineFigure(0, 
						Arrays.asList(	new Point(1148, 470),
										new Point(1120, 470)
									 ), 2),
				new FlipComplexCircuitFigure	(159, "1020", 1056, 467, 2),
				//#2 Low
				new GroundTransformerFigure(0, 1446, 486),
				new LowVoltageLineFigure(31, 
						Arrays.asList(	new Point(1478, 550),
										new Point(1478, 590)
									 ), 2),
				new MainSwitchFigure		(142,  "341", 1454, 590, 2),
				new LowVoltageLineFigure(30, 
						Arrays.asList(	new Point(1478, 638),
										new Point(1478, 680)
									 ), 2),
				new UpGroundFigure			(143, "3411E",  1400, 580, 2),
				new SwitchFigure			(144, "3411", 1454, 680, 2),
				new LowVoltageLineFigure(0, 
						Arrays.asList(	new Point(1178, 512),
										new Point(1178, 590)
									), 2),
				new MainSwitchFigure		(158,  "301", 1160, 590, 2),
				new LowVoltageLineFigure(25, 
						Arrays.asList(	new Point(1178, 638),
										new Point(1178, 680)
									), 2),
				new SwitchFigure			(156, "3021", 1158, 680, 2),
				new GroundFigure			(157, "3021E",1224, 680, 2),
				new LowVoltageLineFigure(22, 
						Arrays.asList(	new Point(1700, 780),
										new Point(950, 780)
									), 2),
				new SwitchFigure			(146, "3002", 1556, 810, 2),
				new SwitchFigure			(149, "3231", 1406, 810, 2),
				new SwitchFigure			(152, "3221", 1256, 810, 2),
				new SwitchFigure			(155, "3211", 1106, 810, 2),
				new LowVoltageLineFigure(32, 
						Arrays.asList(	new Point(1578, 858),
										new Point(1578, 874),
										new Point(1628, 874),
										new Point(1518, 874),
										new Point(1518, 890)
									), 2),
				new LowVoltageLineFigure(28, 
						Arrays.asList(	new Point(1428, 858),
										new Point(1428, 890),
										new Point(1428, 874),
										new Point(1368, 874),
										new Point(1368, 890)
									), 2),
				new LowVoltageLineFigure(26, 
						Arrays.asList(	new Point(1278, 858),
										new Point(1278, 890),
										new Point(1278, 874),
										new Point(1218, 874),
										new Point(1218, 890)
									), 2),
				new LowVoltageLineFigure(23, 
						Arrays.asList(	new Point(1128, 858),
										new Point(1128, 890),
										new Point(1128, 874),
										new Point(1068, 874),
										new Point(1068, 890)
									), 2),									
				new GroundWithResistFigure (0, 1630, 870),
				new SmallThreePhaseTransformerFigure(0, 1556, 890),
				new SwitchFigure			(145, "3002E", 1496, 890, 2),
				new SwitchFigure			(147, "3231E", 1346, 890, 2),
				new SwitchFigure			(151, "3221E", 1196, 890, 2),
				new SwitchFigure			(153, "3211E", 1046, 890, 2),
				new MainSwitchFigure		(148,  "323", 1406, 890, 2),
				new MainSwitchFigure		(150,  "322", 1256, 890, 2),
				new MainSwitchFigure		(154,  "321", 1106, 890, 2),
				new LowVoltageLineFigure(29, 
						Arrays.asList(	new Point(1428, 938),
										new Point(1428, 980)
									), 2),
				new LowVoltageLineFigure(27, 
						Arrays.asList(	new Point(1428, 938),
										new Point(1428, 980)
									), 2),
				new LowVoltageLineFigure(24, 
						Arrays.asList(	new Point(1128, 938),
										new Point(1128, 980)
									), 2),
				new SwitchFigure			(135, "3102", 1000, 680, 2),
				new GroundFigure			(136, "3102E", 1060, 680, 2),
				new LowVoltageLineFigure(21, 
						Arrays.asList(	new Point(822, 590),
										new Point(822, 540),
										new Point(1022, 540),
										new Point(1022, 680),
										new Point(1082, 680)
									), 2)
				);
	}
	
	public void activate() {
		for (Figure figure : figures) {
			layer.add(figure);
			int id = 0;
			if (figure instanceof StateFigure) {
				id = ((StateFigure)figure).getId();		
				((StateFigure)figure).registerStateListener(this);
				((StateFigure)figure).registerValidateSwitchListener(this);
			} else if (figure instanceof LineFigure) {
				id = ((LineFigure)figure).getId();	
				((LineFigure)figure).registerStateListener(this);
			}
			
			if (id > 0) {
				id2FigureMap.put(id, figure);				
			}
		}
		/*commHelper.open("COM1");
		((LineFigure)id2FigureMap.get(1)).setOn(true);
		((LineFigure)id2FigureMap.get(2)).setOn(true);
		((LineFigure)id2FigureMap.get(3)).setOn(true);
		((LineFigure)id2FigureMap.get(4)).setOn(true);
		((LineFigure)id2FigureMap.get(5)).setOn(true);
		((LineFigure)id2FigureMap.get(6)).setOn(true);
		((LineFigure)id2FigureMap.get(7)).setOn(true);
		((LineFigure)id2FigureMap.get(8)).setOn(true);
		switchStatus[1] = 1;
		switchStatus[2] = 1;
		switchStatus[4] = 1;
		switchStatus[6] = 1;
		switchStatus[26] = 1;
		switchStatus[28] = 1;
		switchStatus[30] = 1;
		((StateFigure)id2FigureMap.get(127)).setHasPower(true);
		((StateFigure)id2FigureMap.get(129)).setHasPower(true);
		((StateFigure)id2FigureMap.get(131)).setHasPower(true);
		((StateFigure)id2FigureMap.get(102)).setHasPower(true);
		((StateFigure)id2FigureMap.get(103)).setHasPower(true);
		((StateFigure)id2FigureMap.get(105)).setHasPower(true);
		((StateFigure)id2FigureMap.get(107)).setHasPower(true);
		((StateFigure)id2FigureMap.get(127)).setOn(true);
		((StateFigure)id2FigureMap.get(129)).setOn(true);
		((StateFigure)id2FigureMap.get(131)).setOn(true);
		((StateFigure)id2FigureMap.get(102)).setOn(true);
		((StateFigure)id2FigureMap.get(103)).setOn(true);
		((StateFigure)id2FigureMap.get(105)).setOn(true);
		((StateFigure)id2FigureMap.get(107)).setOn(true);
		
		readSwitchStatus();*/
	}

	public void deactivate() {
		layer.removeAll();
		commHelper.close();
	}
	
	@Override
	public void onChange(int id, int state) {
		chainReact(id, state);	
		
		//sendLineStatus();		
	}
	
	private void sendLineStatus() {
		byte[] result = getLedLineBytes();
		logger.debug("Line status: " + DataTypeConverter.bytesToHex(result));
		for (int i=0; i<3; i++) {
			commHelper.writeBytes(result);
			byte[] response = commHelper.readBytes(1);
			logger.debug("Response of line status: " + DataTypeConverter.bytesToHex(response));
			if (response[0] == CORRECT_PACKET_BYTE) break;
		}		
	}
	
	private void readSwitchStatus() {
		while (isRunning) {
			byte[] result = commHelper.readBytes(1);
			if (result[0] != BEGIN_BYTE ) {
				commHelper.readBytes(1);
			}
			byte[] scanSwitchStatus = commHelper.readBytes(SWTITCH_NUMBERS);
			logger.debug("Scanned swtich status is: {}", DataTypeConverter.bytesToHex(scanSwitchStatus));
			int pos = getSwtichChangeId(scanSwitchStatus);
			if (pos < 0) continue;
			logger.debug("Swtich {}(id:{}) status changed to {}. ", ((StateFigure)id2FigureMap.get(pos+101)).getLabel(), 
					pos+101, switchStatus[pos]);
			byte[] endByte = commHelper.readBytes(1);
			logger.debug("End byte of switch status is: {}", DataTypeConverter.bytesToHex(endByte));
			commHelper.writeBytes(new byte[]{CORRECT_PACKET_BYTE});
			
			chainReact(pos+101, switchStatus[pos] == 1?((StateFigure)id2FigureMap.get(pos+101)).getPower():0);
			sendLineStatus();
		}
	}
	
	private void chainReact(int id, int state) {
		iterateChildren( 0, id, state);
		iterateChildren( 1, id, state);
	}
	
	private void iterateChildren(int relationshipIndex, int id, int state) {
		Map<Integer, List<Integer>> relationShip = relationShips.get(relationshipIndex);
		if (relationShip.containsKey(id)) {
			List<Integer> children = relationShip.get(id);
			if (id2FigureMap.containsKey(id)) {
				Figure parentFigure = id2FigureMap.get(id);
				if (parentFigure instanceof StateFigure) {
					for (Integer child : children) {
						if (id2FigureMap.containsKey(child)) {
							Figure childFigure = id2FigureMap.get(child);
							if (childFigure instanceof LineFigure) {
								LineFigure childLine = (LineFigure)childFigure;
								childLine.setPower(state);
								chainReact(child, state);
							}
						}				
					}
				} else if (parentFigure instanceof LineFigure) {
					for (Integer child : children) {
						if (id2FigureMap.containsKey(child)) {
							Figure childFigure = id2FigureMap.get(child);
							if (childFigure instanceof StateFigure) {
								StateFigure childState = (StateFigure)childFigure;
								if (state == 0) {
									if (childState.getPower() == (1<<relationshipIndex)) {
										childState.setPower(state);
									} else if (childState.getPower() > 0){
										childState.setPower(childState.getPower() - (1<<relationshipIndex));
									}									
								} else {
									childState.setPower(state | childState.getPower());
								} 
								if (childState.isOn()) {
									chainReact(child, childState.getPower());
								}
							}
						}				
					}
				}
			}
		}
	}
	
	public byte[] getLedLineBytes() {
		byte[] result = new byte[LEDLINE_NUMBERS+2];
		result[0] = BEGIN_BYTE;
		result[LEDLINE_NUMBERS+1] = END_BYTE;
		
		for (Entry<Integer, Figure> entryId2Figure : id2FigureMap.entrySet()) {
			if (entryId2Figure.getValue() instanceof LineFigure && ((LineFigure)entryId2Figure.getValue()).getPower()>0) {
				int index = entryId2Figure.getKey().intValue();
				if (index < LEDLINE_NUMBERS && index > 0) {
					result[index] = 1;
				}				
			}
		}
		return result;
	}
	
	public int getSwtichChangeId(byte[] input) {
		if (input.length != SWTITCH_NUMBERS) return -1;
		for (int i=0; i<SWTITCH_NUMBERS; i++) {
			if (input[i] != switchStatus[i]) {
					switchStatus[i] = input[i];
					return i;
			}
		}
		
		return -1;
	}
	
	public static void main(String[] args) {
		SimulatorFiguresCollections inst = new SimulatorFiguresCollections(null);
		/*ByteBuffer bb = ByteBuffer.allocate(16);
		bb.putLong(0, 0x0002L);
		bb.putLong(8, 0x0100L);
		System.out.println("bytes: " + DataTypeConverter.bytesToHex(bb.array()));
		int a = inst.getBitPostion(bb.array());
		System.out.println("Pos: " + a);*/
	}

	@Override
	public String validate(int id, boolean state) {
		if (id2FigureMap.containsKey(id)) {
			Figure stateFigure = id2FigureMap.get(id);
			if (stateFigure instanceof StateFigure) {
				if ((((StateFigure)stateFigure).getPower()&ALL_POWER_ON) == ALL_POWER_ON) {
					return SimulatorMessages.ForbiddenOperation_message;
				}
			}
		}
		return null;
	}
	
	public void importConnections(File connectionsFile) {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(connectionsFile));
			String initState = props.getProperty("init");
			String operationSeq = props.getProperty("operation");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
