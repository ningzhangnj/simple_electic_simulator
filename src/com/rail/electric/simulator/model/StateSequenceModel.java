package com.rail.electric.simulator.model;

import java.util.ArrayList;
import java.util.List;

public class StateSequenceModel {
	private List<List<StateModel>> seqs = new ArrayList<List<StateModel>>();
	
	public void addStates(List<StateModel> stateModels) {
		seqs.add(stateModels);
	}
	
	public static StateSequenceModel parseStateString(String input) {
		StateSequenceModel stateSequenceModel = new StateSequenceModel();
		String[] seqStates = input.split(";");
		for (String seqState : seqStates) {
			String[] parallelStates = seqState.split(",");
			List<StateModel> stateModels = new ArrayList<StateModel>();
			for (String parallelState : parallelStates) {
				stateModels.add(extractStateModel(parallelState));
			}
			stateSequenceModel.addStates(stateModels);
		}
		return stateSequenceModel;
	}
	
	private static StateModel extractStateModel(String input) {
		String[] states = input.split(":");
		int id = Integer.parseInt(states[0]);
		boolean state = "1".equals(states[1])?true:false;
		return new StateModel(id, state);
	}
	
	public static void main(String[] args) {
		StateSequenceModel stateSequenceModel = StateSequenceModel.parseStateString("1:1,101:0,201:1;51:1,52:0;127:1");
		System.out.println(stateSequenceModel);
	}
}
