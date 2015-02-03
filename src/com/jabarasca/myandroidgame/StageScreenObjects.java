package com.jabarasca.myandroidgame;

import java.util.ArrayList;
import java.util.List;
import java.lang.Runnable;
import java.lang.Thread;
import java.io.InputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.content.Context;
import android.content.res.AssetManager;

public class StageScreenObjects {
	
	/*Each side of the screen has the values: 1-Top, 2-Right, 3-Bottom, 4-Left*/
	private static List<Integer> screenPath = new ArrayList<Integer>();
	/*Pair values is Y coordinates. Unpaired values is X coordinates.*/
	public List<Integer> objectCoordinates = new ArrayList<Integer>();
	public int objectQuantity = 0;
	public int objectRadius = 0;
	public String bitmapFilePath;
	private Context context;
	private String attributesTextFile;
	
	public StageScreenObjects(Context context, String textFileAttrPath) {
		this.context = context;
		this.setupObjAttrFromFile(textFileAttrPath);
	}
	
	public void setScreenPath(int pathValue) {
		StageScreenObjects.screenPath.add(pathValue);
	}
	
	public int getScreenPath(int index) {
		return StageScreenObjects.screenPath.get(index);
	}
	
	/*Sets the objects attributes based on text file. All text files should end with empty new line.*/
	private void setupObjAttrFromFile(String filePath) throws IllegalArgumentException {
		AssetManager assetManager = context.getAssets();
		
		//Reading text file contents and setting in String.
		try {
			InputStream input = assetManager.open(filePath);
			attributesTextFile = IOUtils.toString(input, "UTF-8");
			input.close();
		}catch(IOException e) {
			e.printStackTrace();
		}

		if(!attributesTextFile.contains("quantity:") || !attributesTextFile.contains("radius:") || !attributesTextFile.contains("bitmapFilePath:")) {
			throw new IllegalArgumentException("Error on arguments specified on: "+filePath);
		}
		
		if(!attributesTextFile.endsWith("\n")) {
			throw new IllegalArgumentException("Text file should end with new line");
		}
		
		//Getting quantity value.
		int beginIndex = attributesTextFile.indexOf("quantity:") + "quantity:".length();
		int endIndex = attributesTextFile.indexOf("\n", beginIndex);
		char[] buffer = new char[endIndex - beginIndex];
		attributesTextFile.getChars(beginIndex, endIndex, buffer, 0);
		try {
			this.objectQuantity = Integer.parseInt(new String(buffer));
		}catch(NumberFormatException e) {
			e.printStackTrace();
		}
			
		//Getting radius value.
		beginIndex = attributesTextFile.indexOf("radius:") + "radius:".length();
		endIndex = attributesTextFile.indexOf("\n", beginIndex);
		buffer = new char[endIndex - beginIndex];
		attributesTextFile.getChars(beginIndex, endIndex, buffer, 0);
		this.objectRadius = Integer.parseInt(new String(buffer));
		try {
			this.objectRadius = Integer.parseInt(new String(buffer));
		}catch(NumberFormatException e) {
			e.printStackTrace();
		}
		
		//Getting bitmapFilePath.
		beginIndex = attributesTextFile.indexOf("bitmapFilePath:") + "bitmapFilePath:".length();
		endIndex = attributesTextFile.indexOf("\n", beginIndex);
		if(endIndex - beginIndex == 0) {
			throw new IllegalArgumentException("Empty bitmap filePath on: "+filePath);
		}
		buffer = new char[endIndex - beginIndex];
		attributesTextFile.getChars(beginIndex, endIndex, buffer, 0);
		this.bitmapFilePath = new String(buffer);
		
		
		//Getting the objects coordinates.
		for(int i = 1; i <= this.objectQuantity; i++) {
			String coordinateString = "coordinate"+i+":";
			int startIndexOfCoordinate = attributesTextFile.indexOf(coordinateString);
			beginIndex = startIndexOfCoordinate + coordinateString.length();
			endIndex = attributesTextFile.indexOf("\n", beginIndex);
			if(endIndex - beginIndex == 0 || startIndexOfCoordinate == -1) {
				throw new IllegalArgumentException("Error on coordinate on: "+filePath);
			}
			buffer = new char[endIndex - beginIndex];
			attributesTextFile.getChars(beginIndex, endIndex, buffer, 0);
			String[] coordinatesValues = new String(buffer).split(",");
			for(String coordValue : coordinatesValues) {
				this.objectCoordinates.add(Integer.parseInt(coordValue));
			}
		}
		
	}//End setupObjAttrFromFile().
}
