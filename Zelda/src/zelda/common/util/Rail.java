package zelda.common.util;

import zelda.common.geometry.Point;

public class Rail {
	public static final int TYPE_END      = 0;
	public static final int TYPE_STRAIGHT = 1;
	public static final int TYPE_CORNER   = 3;
	public static final Point[] TRACK_SOURCES =
			{new Point(6, 5),
			 new Point(7, 6), new Point(7, 5), // Straights
			 new Point(8, 6), new Point(9, 6), // Corners
			 new Point(9, 5), new Point(8, 5)};
	

	
	public static int getNextDir(int trackType, int currentDir) {
		if (isOfType(trackType, TYPE_STRAIGHT))
			return currentDir;
		if (isOfType(trackType, TYPE_CORNER)) {
			int[] inputs  = getInputs(trackType);
			int[] outputs = getOutputs(trackType);
			for (int i = 0; i < inputs.length; i++) {
				if (inputs[i] == currentDir)
					return outputs[1 - i];
			}
		}
		return -1;
	}
	
	public static int[] getInputs(int trackType) {
		if (isOfType(trackType, TYPE_STRAIGHT))
			return new int[] {trackType - 1, trackType + 1};
		if (isOfType(trackType, TYPE_CORNER))
			return new int[] {(trackType - 1) % 4, trackType % 4};
		else
			return new int[] {};
	}
	
	public static int[] getOutputs(int trackType) {
		int[] outputs = getInputs(trackType);
		if (isOfType(trackType, TYPE_CORNER)) {
			for (int i = 0; i < outputs.length; i++)
				outputs[i] = (outputs[i] + 2) % 4;
		}
		return outputs;
	}
	
	public static boolean isOfType(int trackType, int testType) {
		return (trackType == testType
				|| (trackType >= 1 && trackType <= 2
					&& testType == TYPE_STRAIGHT)
				|| (trackType >= 3 && trackType <= 6
					&& testType == TYPE_CORNER));
	}

	public static boolean hasOutput(int trackType, int dir) {
		if (trackType < 0)
			return false;
		int[] outputs = getOutputs(trackType);
		for (int i = 0; i < outputs.length; i++) {
			if (outputs[i] == dir)
				return true;
		}
		return false;
	}

	public static boolean hasInput(int trackType, int dir) {
		if (trackType < 0)
			return false;
		int[] inputs = getInputs(trackType);
		for (int i = 0; i < inputs.length; i++) {
			if (inputs[i] == dir)
				return true;
		}
		return false;
	}
}
