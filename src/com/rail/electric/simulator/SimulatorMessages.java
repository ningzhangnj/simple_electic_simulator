package com.rail.electric.simulator;

import org.eclipse.osgi.util.NLS;

public class SimulatorMessages extends NLS {
	public static String Simulator_title;
	public static String LicenseError_message;
	public static String Operate_menu;
	public static String StartTeacher_menu;
	public static String StartStudent_menu;
	public static String Stop_menu;
	public static String Zoom_menu;
	public static String Scale2Fit_menu;
	public static String Connections_menu;
	public static String ImportConnections_menu;
	public static String About_menu;
	public static String Return_menu;
	public static String Manufacturer_name;
	public static String About_title;
	public static String Version_label;
	
	public static String TeacherDialog_title;
	public static String TeacherDialog_message;
	public static String StudentDialog_title;
	public static String StudentDialog_message;
	public static String OperateConfirmationDialog_title;
	public static String ErrorPassword_title;
	public static String ErrorPassword_message;
	public static String ErrorDialog_title;
	public static String InfoDialog_title;
	public static String QuestionDialog_title;
	
	public static String Port_label;
	public static String CommPort_label;
	public static String Mode_label;
	public static String OK_label;
	public static String Cancel_label;
	public static String Username_label;
	public static String Password_label;
	public static String Score_label;
	
	public static String Mode_StudentTeacherSimulator_Item;
	public static String Mode_StudentTeacher_Item;
	public static String Mode_TeacherSimulator_Item;
	
	public static String TurnOn_message;
	public static String TurnOff_message;
	
	public static String ForbiddenOperation_message;
	
	public static String ConectionRefused_message;
	public static String WrongOperation_message;
	public static String QuizPassedStudent_message;
	public static String QuizPassedTeacher_message;
	public static String OpenCommError_message;
	public static String ReadyToStartQuiz_message;
	public static String ErrorSwitchStatus_message;
	public static String ErrorSwitchReadLength_message;
	public static String ErrorSwitchRead_message;
	
	public static String InputLine_Label;
	public static String FeedBackLine_Label;
	public static String MotherLine_Label;
	public static String MainTransformer_Label;
	
	public static String OperationHistory_Label;
	public static String OperationFinished_Title;
	
	static {
		NLS.initializeMessages(
				"com.rail.electric.simulator.messages", SimulatorMessages.class); //$NON-NLS-1$
	}
}
