package com.ricardothecoder.yac.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ColorUtil
{
	public static Color getTextureColor(int[] dataArray)
	{
		short[] allRedStored = new short[dataArray.length];
		short[] allGreenStored = new short[dataArray.length];
		short[] allBlueStored = new short[dataArray.length];

		for (int pixelIndex = 0; pixelIndex < dataArray.length; pixelIndex++) {
			allRedStored[pixelIndex] = (short) (dataArray[pixelIndex] >> 16 & 255);
			allGreenStored[pixelIndex] = (short) (dataArray[pixelIndex] >> 8 & 255);
			allBlueStored[pixelIndex] = (short) (dataArray[pixelIndex] & 255);
		}

		int aggregateRed = 0;
		int aggregateGreen = 0;
		int aggregateBlue = 0;

		for (int colourIndex = 0; colourIndex < dataArray.length; colourIndex++) {
			aggregateRed += allRedStored[colourIndex];
			aggregateGreen += allGreenStored[colourIndex];
			aggregateBlue += allBlueStored[colourIndex];
		}

		short meanRed;
		short meanGreen;
		short meanBlue;

		meanRed = (short) (aggregateRed / (allRedStored.length > 0 ? allRedStored.length : 1));
		meanGreen = (short) (aggregateGreen / (allGreenStored.length > 0 ? allGreenStored.length : 1));
		meanBlue = (short) (aggregateBlue / (allBlueStored.length > 0 ? allBlueStored.length : 1));

		return new Color(meanRed, meanGreen, meanBlue, 128);
	}

	public static Color getTextureColor(int[][] data2dArray) 
	{
		List<Integer> list = new ArrayList<Integer>();

		for (int[] aData2dArray : data2dArray) {
			for (int anAData2dArray : aData2dArray) {
				list.add(anAData2dArray);
			}
		}

		int[] data1dArray = new int[list.size()];

		for (int dataIndex = 0; dataIndex < data1dArray.length; dataIndex++) {
			data1dArray[dataIndex] = list.get(dataIndex);
		}

		return getTextureColor(data1dArray);
	}
	
	public static int getRGBInteger(int r, int g, int b)
	{
		return new Color(r, g, b).getRGB();
	}
}
