package com.rail.electric.simulator;

import static com.rail.electric.simulator.SimulatorManager.BEGIN_BYTE;
import static com.rail.electric.simulator.SimulatorManager.END_BYTE;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import com.rail.electric.simulator.figures.SimulatorLabel;
import com.rail.electric.simulator.figures.SmallThreePhaseTransformerFigure;
import com.rail.electric.simulator.figures.StateFigure;
import com.rail.electric.simulator.figures.SwitchFigure;
import com.rail.electric.simulator.figures.ThreePahseTransformerFigure;
import com.rail.electric.simulator.figures.TwoPhaseTransformerFigure;
import com.rail.electric.simulator.figures.UpGroundFigure;
import com.rail.electric.simulator.helpers.DataTypeConverter;
import com.rail.electric.simulator.listeners.StateListener;
import com.rail.electric.simulator.listeners.ValidateSwitchListener;
import com.rail.electric.simulator.model.StateModel;
import com.rail.electric.simulator.model.StateSequenceModel;

public class SimulatorFiguresCollections implements StateListener, ValidateSwitchListener {
	private final static Logger logger =  LoggerFactory.getLogger(SimulatorFiguresCollections.class);
	
	public static final int SWITCH_NUMBERS = 67;
	public static final int LEDLINE_NUMBERS = 40;
	public static final int SWITCH_OFFSET = 101;	
	
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
		relationShips.get(0).put(107, 	Arrays.asList(8,51,53));
		relationShips.get(0).put(8, 	Arrays.asList(110));
		relationShips.get(0).put(110, 	Arrays.asList(16));
		relationShips.get(0).put(16, 	Arrays.asList(112));
		relationShips.get(0).put(112, 	Arrays.asList(19));
		relationShips.get(0).put(19, 	Arrays.asList(124,133,122,119,116,113));
		relationShips.get(0).put(124, 	Arrays.asList(10));
		relationShips.get(0).put(10, 	Arrays.asList(125));
		relationShips.get(0).put(125, 	Arrays.asList(11));
		relationShips.get(0).put(122, 	Arrays.asList(9));
		relationShips.get(0).put(119, 	Arrays.asList(12));
		relationShips.get(0).put(12, 	Arrays.asList(120));
		relationShips.get(0).put(120, 	Arrays.asList(13));
		relationShips.get(0).put(116, 	Arrays.asList(14));
		relationShips.get(0).put(14, 	Arrays.asList(117));
		relationShips.get(0).put(117, 	Arrays.asList(15));
		relationShips.get(0).put(113, 	Arrays.asList(18));
		relationShips.get(0).put(18, 	Arrays.asList(114));
		relationShips.get(0).put(114, 	Arrays.asList(17));
		relationShips.get(0).put(133, 	Arrays.asList(20));
		relationShips.get(0).put(20, 	Arrays.asList(132));
		relationShips.get(0).put(132, 	Arrays.asList(21));
		relationShips.get(0).put(21, 	Arrays.asList(135));
		relationShips.get(0).put(135, 	Arrays.asList(22));
		relationShips.get(0).put(22, 	Arrays.asList(156,144,155,152,149,146));
		relationShips.get(0).put(156, 	Arrays.asList(25));
		relationShips.get(0).put(144, 	Arrays.asList(30));
		relationShips.get(0).put(30, 	Arrays.asList(142));
		relationShips.get(0).put(142, 	Arrays.asList(31));
		relationShips.get(0).put(155, 	Arrays.asList(23));
		relationShips.get(0).put(23, 	Arrays.asList(154));
		relationShips.get(0).put(154, 	Arrays.asList(24));
		relationShips.get(0).put(152, 	Arrays.asList(26));
		relationShips.get(0).put(26, 	Arrays.asList(150));
		relationShips.get(0).put(150, 	Arrays.asList(27));
		relationShips.get(0).put(149, 	Arrays.asList(28));
		relationShips.get(0).put(28, 	Arrays.asList(148));
		relationShips.get(0).put(148, 	Arrays.asList(29));
		relationShips.get(0).put(146, 	Arrays.asList(32));
		
		
		//#2 input
		relationShips.get(1).put(40, 	Arrays.asList(141));
		relationShips.get(1).put(141, 	Arrays.asList(39));
		relationShips.get(1).put(39, 	Arrays.asList(139));
		relationShips.get(1).put(139, 	Arrays.asList(38));
		relationShips.get(1).put(38, 	Arrays.asList(137));
		relationShips.get(1).put(137, 	Arrays.asList(37));
		relationShips.get(1).put(37, 	Arrays.asList(165,166));
		relationShips.get(1).put(166, 	Arrays.asList(36));
		relationShips.get(1).put(165, 	Arrays.asList(35));
		relationShips.get(1).put(35, 	Arrays.asList(163));
		relationShips.get(1).put(163, 	Arrays.asList(34));
		relationShips.get(1).put(34, 	Arrays.asList(161));
		relationShips.get(1).put(161, 	Arrays.asList(33,52,54));
		relationShips.get(1).put(33, 	Arrays.asList(158));
		relationShips.get(1).put(158, 	Arrays.asList(25));
		relationShips.get(1).put(25, 	Arrays.asList(156));
		relationShips.get(1).put(156, 	Arrays.asList(22));		
		relationShips.get(1).put(22, 	Arrays.asList(135,144,155,152,149,146));
		relationShips.get(1).put(144, 	Arrays.asList(30));
		relationShips.get(1).put(30, 	Arrays.asList(142));
		relationShips.get(1).put(142, 	Arrays.asList(31));
		relationShips.get(1).put(155, 	Arrays.asList(23));
		relationShips.get(1).put(23, 	Arrays.asList(154));
		relationShips.get(1).put(154, 	Arrays.asList(24));
		relationShips.get(1).put(152, 	Arrays.asList(26));
		relationShips.get(1).put(26, 	Arrays.asList(150));
		relationShips.get(1).put(150, 	Arrays.asList(27));
		relationShips.get(1).put(149, 	Arrays.asList(28));
		relationShips.get(1).put(28, 	Arrays.asList(148));
		relationShips.get(1).put(148, 	Arrays.asList(29));
		relationShips.get(1).put(146, 	Arrays.asList(32));
		relationShips.get(1).put(135, 	Arrays.asList(21));
		relationShips.get(1).put(21, 	Arrays.asList(132));
		relationShips.get(1).put(132, 	Arrays.asList(20));
		relationShips.get(1).put(20, 	Arrays.asList(133));
		relationShips.get(1).put(133, 	Arrays.asList(19));
		relationShips.get(1).put(19, 	Arrays.asList(124,112,122,119,116,113));
		relationShips.get(1).put(124, 	Arrays.asList(10));
		relationShips.get(1).put(10, 	Arrays.asList(125));
		relationShips.get(1).put(125, 	Arrays.asList(11));
		relationShips.get(1).put(122, 	Arrays.asList(9));
		relationShips.get(1).put(119, 	Arrays.asList(12));
		relationShips.get(1).put(12, 	Arrays.asList(120));
		relationShips.get(1).put(120, 	Arrays.asList(13));
		relationShips.get(1).put(116, 	Arrays.asList(14));
		relationShips.get(1).put(14, 	Arrays.asList(117));
		relationShips.get(1).put(117, 	Arrays.asList(15));
		relationShips.get(1).put(113, 	Arrays.asList(18));
		relationShips.get(1).put(18, 	Arrays.asList(114));
		relationShips.get(1).put(114, 	Arrays.asList(17));
		relationShips.get(1).put(112, 	Arrays.asList(16));
		
	}
	
	private FreeformLayer layer;
	private SimulatorManager manager;
	private byte[] switchStatus = new byte[SWITCH_NUMBERS]; 
	
	private List<Figure> figures;
	
	private Map<Integer, Figure> id2FigureMap = new HashMap<Integer, Figure>();
	
	private StateSequenceModel initState;
	private StateSequenceModel operationSeq;
	private int operationIndex;
	private String quizName;
	private List<String> operationList;
	private String operationScore = "100";
	
	SimulatorFiguresCollections(FreeformLayer layer, SimulatorManager manager) {
		this.manager = manager;
		this.layer = layer;
	}
	
	public void init() {
		initState = null;
		operationSeq = null;
		quizName = null;
		
		this.figures = Arrays.asList(
				//#1 High
				new SwitchFigure			(131,  "1111", 300, 150, 1),
				new SimulatorLabel			("1111", 340, 150),
				new HighVoltageLineFigure(3, 
						Arrays.asList(	new Point(322, 198),
										new Point(322, 230),
										new Point(304, 230)
									 ), 1),				
				new LeftGroundFigure		(130, "1111E", 240, 208, 1),
				new SimulatorLabel			("1111", 200, 208),
				new MainSwitchFigure		(129,  "111", 298, 230, 1),
				new SimulatorLabel			("111", 340, 230),
				new HighVoltageLineFigure(2, 
						Arrays.asList(	new Point(368, 278),
										new Point(322, 278),
										new Point(322, 310),
										new Point(304, 310)
									 ), 1),	
				new	TwoPhaseTransformerFigure (0, 350, 278),
				new LeftGroundFigure		(128, "1113E", 240, 288, 1),
				new SimulatorLabel			("1113E", 200, 288),
				new SwitchFigure			(127, "1113", 300, 310, 1),
				new SimulatorLabel			("1113", 340, 330),
				new HighVoltageLineFigure(1, 
						Arrays.asList(	new Point(322, 358),
										new Point(322, 400)
									 ), 1),
				new SimulatorLabel			("110Kv1#" + SimulatorMessages.InputLine_Label, 280, 410, 80, 12),
				new HighVoltageLineFigure(4, 
						Arrays.asList(	new Point(322, 150),
										new Point(322, 110),
										new Point(622, 110),
										new Point(622, 210),
										new Point(680, 210)
									), 1),	
				new ThreePahseTransformerFigure (102, "1001", 658, 210, 1),
				new SimulatorLabel			("1001", 680, 210),
				new HighVoltageLineFigure(5, 
						Arrays.asList(	new Point(698, 240),
										new Point(732, 240)
									 ), 1),	
				new GroundFigure		(101, "1001E", 708, 242, 1),
				new SimulatorLabel			("1001E", 748, 242),
				new SwitchFigure			(103, "1011", 600, 210, 1),
				new SimulatorLabel			("1011", 560, 210),
				new HighVoltageLineFigure(6, 
						Arrays.asList(	new Point(622, 258),
										new Point(622, 290),
										new Point(604, 290)
									 ), 1),	
				new LeftGroundFigure		(104, "1011E",  540, 268, 1),
				new SimulatorLabel			("1011E", 500, 268),
				new MainSwitchFigure		(105,  "101", 598, 290, 1),
				new SimulatorLabel			("101", 638, 290),
				new HighVoltageLineFigure(7, 
						Arrays.asList(	new Point(622, 338),
										new Point(622, 370),
										new Point(604, 370)
									 ), 1),
				new LeftGroundFigure		(106, "1013E",  540, 348, 1),
				new SimulatorLabel			("1013E", 500, 348),
				new SwitchFigure			(107,  "1013", 600, 370, 1),
				new SimulatorLabel			("1013", 640, 370),
				new HighVoltageLineFigure(8, 
						Arrays.asList(	new Point(622, 418),
										new Point(622, 450),
										new Point(604, 450)
									 ), 1),
				new LeftGroundFigure		(108, "1014E",  540, 428, 1),
				new SimulatorLabel			("1014E", 500, 428),
				new MainTransformerFigure   (0,    588, 450),
				new SimulatorLabel			("1#" + SimulatorMessages.MainTransformer_Label, 540, 470),					 
				new HighVoltageLineFigure(53, 
						Arrays.asList(	new Point(652, 470),
										new Point(680, 470)
									 ), 1),
				new ComplexCircuitFigure	(109, "1010", 680, 467, 1),
				new SimulatorLabel			("1010", 720, 467),
				
				//#1 Low
				new LowVoltageLineFigure(51, 
						Arrays.asList(	new Point(622, 512),
										new Point(622, 590)
									 ), 1),
				new GroundTransformerFigure(0, 290, 486),
				new LowVoltageLineFigure(11, 
						Arrays.asList(	new Point(322, 550),
										new Point(322, 590)
									 ), 1),
				new MainSwitchFigure		(125,  "331", 298, 590, 1),
				new SimulatorLabel			("331", 258, 590),
				new LowVoltageLineFigure(10, 
						Arrays.asList(	new Point(382, 638),
										new Point(322, 638),
										new Point(322, 680)
									 ), 1),
				new UpGroundFigure			(126, "3311E",  360, 578, 1),
				new SimulatorLabel			("3311E", 400, 578),
				new SwitchFigure			(124, "3311", 300, 680, 1),
				new SimulatorLabel			("3311", 340, 680),
				new MainSwitchFigure		(110,  "301", 598, 590, 1),
				new SimulatorLabel			("301", 638, 590),
				new LowVoltageLineFigure(16, 
						Arrays.asList(	new Point(622, 638),
										new Point(622, 680),
										new Point(558, 680)
									), 1),
				new SwitchFigure			(112, "3011", 598, 680, 1),
				new SimulatorLabel			("3011", 638, 680),
				new GroundFigure			(111, "3011E", 536, 680, 1),
				new SimulatorLabel			("3011E", 496, 680),
				new LowVoltageLineFigure(19, 
						Arrays.asList(	new Point(100, 780),
										new Point(222, 780),
										new Point(222, 810),
										new Point(222, 780),
										new Point(322, 780),
										new Point(322, 728),
										new Point(322, 780),
										new Point(372, 780),
										new Point(372, 810),
										new Point(372, 780),
										new Point(522, 780),
										new Point(522, 810),
										new Point(522, 780),
										new Point(620, 780),
										new Point(620, 720),
										new Point(620, 780),
										new Point(672, 780),
										new Point(672, 810),
										new Point(672, 780),
										new Point(822, 780),
										new Point(822, 728),
										new Point(822, 780),
										new Point(850, 780)
									), 1),
				new SimulatorLabel			("35kvI" + SimulatorMessages.MotherLine_Label, 100, 760, 80, 12),
				new SwitchFigure			(122, "3001", 200, 810, 1),
				new SimulatorLabel			("3001", 240, 810),
				new SwitchFigure			(119, "3131", 350, 810, 1),
				new SimulatorLabel			("3131", 390, 810),
				new SwitchFigure			(116, "3121", 500, 810, 1),
				new SimulatorLabel			("3121", 540, 810),
				new SwitchFigure			(113, "3111", 650, 810, 1),
				new SimulatorLabel			("3111", 690, 810),
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
				new SmallThreePhaseTransformerFigure(0, 198, 874),
				new GroundFigure			(123, "3001E", 260, 890, 1),
				new SimulatorLabel			("3001E", 300, 890),
				new GroundFigure			(121, "3131E", 410, 890, 1),
				new SimulatorLabel			("3131E", 450, 890),
				new GroundFigure			(118, "3121E", 560, 890, 1),
				new SimulatorLabel			("3121E", 600, 890),
				new GroundFigure			(115, "3111E", 710, 890, 1),
				new SimulatorLabel			("3111E", 750, 890),
				new MainSwitchFigure		(120,  "313", 348, 890, 1),
				new SimulatorLabel			("313", 388, 890),
				new MainSwitchFigure		(117,  "312", 498, 890, 1),
				new SimulatorLabel			("312", 538, 890),
				new MainSwitchFigure		(114,  "311", 648, 890, 1),
				new SimulatorLabel			("311", 688, 890),
				new LowVoltageLineFigure(13, 
						Arrays.asList(	new Point(372, 938),
										new Point(372, 980)
									), 1),
				new SimulatorLabel			("35kv" + SimulatorMessages.FeedBackLine_Label + "5", 362, 990),
				new LowVoltageLineFigure(15, 
						Arrays.asList(	new Point(522, 938),
										new Point(522, 980)
									), 1),
				new SimulatorLabel			("35kv" + SimulatorMessages.FeedBackLine_Label + "5", 512, 990),
				new LowVoltageLineFigure(17, 
						Arrays.asList(	new Point(672, 938),
										new Point(672, 980)
									), 1),
				new SimulatorLabel			("35kv" + SimulatorMessages.FeedBackLine_Label + "1", 662, 990),
				new SwitchFigure			(133, "3101", 800, 680, 1),
				new SimulatorLabel			("3101", 840, 680),
				new GroundFigure			(134, "3101E", 740, 680, 1),
				new SimulatorLabel			("3101E", 700, 680),
				new MainSwitchFigure		(132,  "310", 798, 590, 3), //Special one
				new SimulatorLabel			("310", 838, 590),
				new LowVoltageLineFigure(20, 
						Arrays.asList(	new Point(822, 638),
										new Point(822, 680),
										new Point(762, 680)
									), 1),
									
				//#2 High
				new SwitchFigure			(137,  "1121", 1454, 150, 2),
				new SimulatorLabel			("1121", 1414, 150),
				new HighVoltageLineFigure(38, 
						Arrays.asList(	new Point(1478, 198),
										new Point(1478, 230),
										new Point(1492, 230)
									 ), 2),				
				new RightGroundFigure		(138, "1121E", 1492, 206, 2),
				new SimulatorLabel			("1121E", 1532, 206),
				new MainSwitchFigure		(139,  "112", 1454, 230, 2),
				new SimulatorLabel			("112", 1414, 230),
				new HighVoltageLineFigure(39, 
						Arrays.asList(	new Point(1428, 278),
										new Point(1478, 278),
										new Point(1478, 310),
										new Point(1492, 310)
									 ), 2),	
				new	TwoPhaseTransformerFigure (0, 1410, 278),
				new RightGroundFigure		(140, "1123E", 1492, 284, 2),
				new SimulatorLabel			("1123E", 1532, 284),
				new SwitchFigure			(141, "1123", 1454, 310, 2),
				new SimulatorLabel			("1123", 1414, 330),
				new HighVoltageLineFigure(40, 
						Arrays.asList(	new Point(1478, 358),
										new Point(1478, 400)
									 ), 2),
				new SimulatorLabel			("110Kv2#" + SimulatorMessages.InputLine_Label, 1458, 410, 80, 12),
				new HighVoltageLineFigure(37, 
						Arrays.asList(	new Point(1478, 150),
										new Point(1478, 110),
										new Point(1178, 110),
										new Point(1178, 210),
										new Point(1120, 210)
									), 2),	
				new FlipThreePahseTransformerFigure (166, "1002", 1100, 210, 2),
				new SimulatorLabel			("1002", 1060, 210),
				new HighVoltageLineFigure(36, 
						Arrays.asList(	new Point(1102, 240),
										new Point(1068, 240)
									 ), 2),	
				new GroundFigure		(167, "1002E", 1048, 242, 2),
				new SimulatorLabel			("1002E", 1008, 242),
				new SwitchFigure			(165, "1021", 1156, 210, 2),
				new SimulatorLabel			("1021", 1116, 210),
				new HighVoltageLineFigure(35, 
						Arrays.asList(	new Point(1178, 258),
										new Point(1178, 290),
										new Point(1192, 290)
									 ), 2),	
				new RightGroundFigure		(164, "1021E",  1192, 264, 2),
				new SimulatorLabel			("1021E", 1232, 264),
				new MainSwitchFigure		(163,  "102", 1154, 290, 2),
				new SimulatorLabel			("102", 1114, 290),
				new HighVoltageLineFigure(34, 
						Arrays.asList(	new Point(1178, 338),
										new Point(1178, 370),
										new Point(1192, 370)
									 ), 2),
				new RightGroundFigure		(162, "1023E",  1192, 344, 2),
				new SimulatorLabel			("1023E", 1232, 344),
				new SwitchFigure			(161,  "1023", 1154, 370, 2),
				new SimulatorLabel			("1023", 1114, 370),
				new HighVoltageLineFigure(33, 
						Arrays.asList(	new Point(1178, 418),
										new Point(1178, 450),
										new Point(1192, 450)
									 ), 2),
				new RightGroundFigure		(160, "1024E",  1192, 424, 2),
				new SimulatorLabel			("1024E", 1232, 424),
				new FlipMainTransformerFigure   (0,    1148, 450),
				new SimulatorLabel			("2#" + SimulatorMessages.MainTransformer_Label, 1188, 470),									 
				new HighVoltageLineFigure(54, 
						Arrays.asList(	new Point(1148, 470),
										new Point(1120, 470)
									 ), 2),
				new FlipComplexCircuitFigure	(159, "1020", 1056, 467, 2),
				new SimulatorLabel			("1020", 1016, 467),
				//#2 Low
				new LowVoltageLineFigure(52, 
						Arrays.asList(	new Point(1178, 512),
										new Point(1178, 590)
									 ), 2),
				new GroundTransformerFigure(0, 1446, 486),
				new LowVoltageLineFigure(31, 
						Arrays.asList(	new Point(1478, 550),
										new Point(1478, 590)
									 ), 2),
				new MainSwitchFigure		(142,  "341", 1454, 590, 2),
				new SimulatorLabel			("341", 1494, 590),
				new LowVoltageLineFigure(30, 
						Arrays.asList(	new Point(1422, 638),
										new Point(1478, 638),
										new Point(1478, 680)
									 ), 2),
				new UpGroundFigure			(143, "3411E",  1400, 578, 2),
				new SimulatorLabel			("3411E", 1360, 578),
				new SwitchFigure			(144, "3411", 1454, 680, 2),
				new SimulatorLabel			("3411", 1494, 680),
				new MainSwitchFigure		(158,  "302", 1154, 590, 2),
				new SimulatorLabel			("302", 1194, 590),
				new LowVoltageLineFigure(25, 
						Arrays.asList(	new Point(1178, 638),
										new Point(1178, 680),
										new Point(1246, 680)
									), 2),
				new SwitchFigure			(156, "3021", 1156, 680, 2),
				new SimulatorLabel			("3021", 1116, 710),
				new GroundFigure			(157, "3021E",1224, 680, 2),
				new SimulatorLabel			("3021E", 1264, 680),
				new LowVoltageLineFigure(22, 
						Arrays.asList(	new Point(1700, 780),
										new Point(1578, 780),										
										new Point(1578, 810),
										new Point(1578, 780),
										new Point(1478, 780),
										new Point(1478, 728),
										new Point(1478, 780),
										new Point(1428, 780),
										new Point(1428, 810),
										new Point(1428, 780),
										new Point(1278, 780),
										new Point(1278, 810),
										new Point(1278, 780),
										new Point(1178, 780),
										new Point(1178, 728),
										new Point(1178, 780),
										new Point(1128, 780),
										new Point(1128, 810),
										new Point(1128, 780),
										new Point(1022, 780),
										new Point(1022, 728),
										new Point(1022, 780),
										new Point(1000, 780)
									), 2),
				new SimulatorLabel			("35kvII" + SimulatorMessages.MotherLine_Label, 1620, 760, 80, 12),
				new SwitchFigure			(146, "3002", 1554, 810, 2),
				new SimulatorLabel			("3002", 1594, 810),
				new SwitchFigure			(149, "3231", 1404, 810, 2),
				new SimulatorLabel			("3231", 1444, 810),
				new SwitchFigure			(152, "3221", 1254, 810, 2),
				new SimulatorLabel			("3221", 1294, 810),
				new SwitchFigure			(155, "3211", 1104, 810, 2),
				new SimulatorLabel			("3211", 1144, 810),
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
				new GroundWithResistFigure (0, 1628, 870),
				new SmallThreePhaseTransformerFigure(0, 1554, 874),
				new GroundFigure			(145, "3002E", 1496, 890, 2),
				new SimulatorLabel			("3002E", 1456, 890),
				new GroundFigure			(147, "3231E", 1346, 890, 2),
				new SimulatorLabel			("3231E", 1306, 890),
				new GroundFigure			(151, "3221E", 1196, 890, 2),
				new SimulatorLabel			("3221E", 1156, 890),
				new GroundFigure			(153, "3211E", 1046, 890, 2),
				new SimulatorLabel			("3211E", 1006, 890),
				new MainSwitchFigure		(148,  "323", 1404, 890, 2),
				new SimulatorLabel			("323", 1364, 890),
				new MainSwitchFigure		(150,  "322", 1254, 890, 2),
				new SimulatorLabel			("322", 1214, 890),
				new MainSwitchFigure		(154,  "321", 1104, 890, 2),
				new SimulatorLabel			("321", 1064, 890),
				new LowVoltageLineFigure(29, 
						Arrays.asList(	new Point(1428, 938),
										new Point(1428, 980)
									), 2),
				new SimulatorLabel			("35kv" + SimulatorMessages.FeedBackLine_Label + "6", 1418, 990),
				new LowVoltageLineFigure(27, 
						Arrays.asList(	new Point(1278, 938),
										new Point(1278, 980)
									), 2),
				new SimulatorLabel			("35kv" + SimulatorMessages.FeedBackLine_Label + "4", 1268, 990),
				new LowVoltageLineFigure(24, 
						Arrays.asList(	new Point(1128, 938),
										new Point(1128, 980)
									), 2),
				new SimulatorLabel			("35kv" + SimulatorMessages.FeedBackLine_Label + "2", 1118, 990),
				new SwitchFigure			(135, "3102", 1000, 680, 2),
				new SimulatorLabel			("3102", 960, 680),
				new GroundFigure			(136, "3102E", 1060, 680, 2),
				new SimulatorLabel			("3102E", 1100, 680),
				new LowVoltageLineFigure(21, 
						Arrays.asList(	new Point(822, 590),
										new Point(822, 540),
										new Point(1022, 540),
										new Point(1022, 680),
										new Point(1082, 680)
									), 2)
				);
		
		for (Figure figure : figures) {
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
		((StateFigure)id2FigureMap.get(132)).setOn(false);
		
		activate();
	}
	
	public void activate() {
		for (Figure figure : figures) {
			layer.add(figure);	
			
			if (figure instanceof StateFigure) {
				StateFigure stateFigure = ((StateFigure)figure);
				int id = stateFigure.getId();
				if (stateFigure.isOn()) {
					switchStatus[id - SWITCH_OFFSET] = 1;
				} else {
					switchStatus[id - SWITCH_OFFSET] = 0;
				}
			}
		}
	}

	public void deactivate() {
		layer.removeAll();
	}
	
	@Override
	public void onChange(int id, int state) {		
		chainReact(id, state);	
		
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
								LineFigure childState = (LineFigure)childFigure;
								if (childState.getPower() != (1<<relationshipIndex) && childState.getPower() > 0) return;
								LineFigure childLine = (LineFigure)childFigure;
								childLine.setPower(state);
								iterateChildren(relationshipIndex, child, state);
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
									iterateChildren(relationshipIndex, child, childState.getPower());
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
				if (index <= LEDLINE_NUMBERS && index > 0) {
					result[index] = 1;
				}				
			}
		}
		return result;
	}
	
	public int getSwtichChangeId(byte[] input) {
		if (input.length != SWITCH_NUMBERS) return -1;
		for (int i=0; i<SWITCH_NUMBERS; i++) {
			if (input[i] != switchStatus[i]) {
					switchStatus[i] = input[i];
					logger.debug("Swtich {}(id:{}) status changed to {}. ", ((StateFigure)id2FigureMap.get(i+SWITCH_OFFSET)).getLabel(), 
							i+SWITCH_OFFSET, switchStatus[i]);
					return i;
			}
		}
		
		return -1;
	}
	
	public static void main(String[] args) {
		//SimulatorFiguresCollections inst = new SimulatorFiguresCollections(null, null);
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
		if (manager.validate(id, state) < 0) return SimulatorMessages.ForbiddenOperation_message;
		return null;
	}
	
	// Fail:-1, Correct:0, Finish: 1
	public int validate(byte[] switchBytes) {
		int id = switchBytes[0] + SWITCH_OFFSET;
		boolean isOn = (switchBytes[1] == 1)?true:false;
		if (id2FigureMap.containsKey(id)) {
			Figure stateFigure = id2FigureMap.get(id);
			if (stateFigure instanceof StateFigure) {
				String message = isOn?SimulatorMessages.TurnOff_message:SimulatorMessages.TurnOn_message;
				operationList.add( message + " " + ((StateFigure)stateFigure).getLabel());
				
			}
		}		
		boolean isMatched = false;
		List<StateModel> currentStates = operationSeq.getSeqs().get(operationIndex);
		if (currentStates.size() > 0) {
			for (StateModel state : currentStates) {
				if (state.getId() == id && state.isOn() != isOn) {
					currentStates.remove(state);
					isMatched = true;
					if (currentStates.isEmpty()) operationIndex ++;
					break;
				}
			}
		}
		if (!isMatched) {
			operationScore = "0";
			return -1;
		}
		else if (operationIndex >= operationSeq.getSeqs().size()) {
			return 1;
		}
		return 0;
	}
	
	public List<String> getOperationList() {
		return operationList;
	}
	
	public String getOperationScore() {
		return operationScore;
	}
	
	public void importConnections(File connectionsFile) {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(connectionsFile));
			initState = StateSequenceModel.parseStateString(props.getProperty("init", ""));
			operationSeq = StateSequenceModel.parseStateString(props.getProperty("operation"));
			operationIndex = 0;
			operationList = new ArrayList<String>();
			operationScore = "100";
			quizName = connectionsFile.getName();
			updatInitState(getInitStateBytes());
			manager.sendLineStatus();
		} catch (IOException e) {
			logger.error("Failed to load connections ini file {},  caused by {}", connectionsFile.getName(), e.toString());
		}
	}
	
	private byte[] getQuizNameBytes() {
		if (quizName == null) {
			quizName = "Unknown";
		}		
		try {
			int lastIndex = quizName.lastIndexOf(".");
			if (lastIndex > 0) return quizName.substring(0, lastIndex).getBytes("UTF-8");
			return quizName.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("UTF-8 charset is not supported. Caused by {}", e.toString());
		}
		return null;
	}
	
	private byte[] getInitStateBytes() {
		List<Byte> result = new ArrayList<Byte>();
		for (List<StateModel> stateList : initState.getSeqs()) {
			for (StateModel state : stateList) {
				result.add((byte)(state.getId()-SWITCH_OFFSET));
				result.add(state.isOn()?(byte)1:(byte)0);
			}
		}
		byte[] bytes = new byte[result.size()];
		for (int i=0; i<bytes.length; i++) {
			bytes[i] = result.get(i);
		}
		return bytes;
	}
	
	public void writeInitStateBytes(DataOutputStream output, byte head) {
		byte[] initBytes = getInitStateBytes();
		if (initBytes.length <= 0) return;
		try {
			output.writeInt(initBytes.length + 1);
			output.writeByte(head);
			output.write(initBytes);
			logger.debug("Send out init state bytes: {}", DataTypeConverter.bytesToHex(initBytes));
			output.flush();
		} catch (IOException e) {
			logger.error("Failed to send out init states: {}. caused by {}", DataTypeConverter.bytesToHex(initBytes),
					e.toString());
		}	
	}
	
	public void writeQuizNameBytes(DataOutputStream output, byte head) {
		byte[] nameBytes = getQuizNameBytes();
		try {
			output.writeInt(nameBytes.length + 1);
			output.writeByte(head);
			output.write(nameBytes);
			logger.debug("Send out quiz name bytes: {}", DataTypeConverter.bytesToHex(nameBytes));
			output.flush();
		} catch (IOException e) {
			logger.error("Failed to send out quiz name: {}. caused by {}", DataTypeConverter.bytesToHex(nameBytes),
					e.toString());
		}		
	}	
	
	public void updatInitState(byte[] initStateBytes) {
		for (int i=0; i<initStateBytes.length;) {
			int id = initStateBytes[i] + SWITCH_OFFSET;
			i++;
			if (id2FigureMap.containsKey(id)) {
				Figure stateFigure = id2FigureMap.get(id);
				if (stateFigure instanceof StateFigure) {
					boolean isOn = initStateBytes[i] == 1?true:false;
					if (isOn != ((StateFigure)stateFigure).isOn()) {
						logger.debug("Will update switch {}:{} init status to {}.", ((StateFigure)stateFigure).getLabel(), 
								id, isOn?"On":"Off");
						((StateFigure)stateFigure).switchState();
					}
				}
			}
			i++;
		}		
		
	}
	
	public void updateSwitchStatus(byte[] switchBytes) {
		int id = switchBytes[0] + SWITCH_OFFSET;
		if (id2FigureMap.containsKey(id)) {
			Figure stateFigure = id2FigureMap.get(id);
			if (stateFigure instanceof StateFigure) {
				boolean isOn = switchBytes[1] == 1?true:false;
				if (isOn == ((StateFigure)stateFigure).isOn()) {
					logger.debug("Will update switch {}:{} status to {}.", ((StateFigure)stateFigure).getLabel(), 
							id, isOn?"Off":"On");
					((StateFigure)stateFigure).switchState();
				}
			}
		}
	}
	
	public void updateChain(int pos) {
		if (id2FigureMap.containsKey(pos+SWITCH_OFFSET)) {
			Figure stateFigure = id2FigureMap.get(pos+SWITCH_OFFSET);
			if (stateFigure instanceof StateFigure) {
				boolean isOn = switchStatus[pos] == 1?true:false;
				if (isOn != ((StateFigure)stateFigure).isOn()) {
					logger.debug("Will update switch {}:{} chain to {}.", ((StateFigure)stateFigure).getLabel(), 
							pos+SWITCH_OFFSET, isOn?"On":"Off");
					((StateFigure)stateFigure).switchState();
				}
			}			
		}		
	}
	
	public String checkSwitchStatus(byte[] input) {
		if (input.length != switchStatus.length) {
			return SimulatorMessages.ErrorSwitchReadLength_message;
		}
		
		for (int i=0; i<input.length; i++) {
			if(input[i] != switchStatus[i]) {
				logger.info("Switch {} status does not match. Scanned: {}, Expected: {}.", i + SWITCH_OFFSET, input[i], switchStatus[i]);
				return String.format(SimulatorMessages.ErrorSwitchRead_message, i + SWITCH_OFFSET, input[i], switchStatus[i]);
			}
		}
		return null;
	}
}
