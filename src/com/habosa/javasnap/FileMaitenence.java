package com.habosa.javasnap;

import java.io.File;
import java.util.Date;

public class FileMaitenence {
	public String directory;
	private static int daysOld;
	public FileMaitenence(String dir, int cycleTime){
		directory = dir;
		daysOld = cycleTime;
	}
	public void cleanupFolder(){
		this.deleteOld();
		this.renameAll();
		
	}

	
	
	public void renameAll() {
		File dir = new File(directory);
		File[] directoryListing = dir.listFiles();
		int count = 1;

		if (directoryListing != null) {
			for (File snap : directoryListing) {
				snap.renameTo(new File(directory + count + ".jpg"));
				count++;
			}
		}
	}

	public void deleteOld() {
		File dir = new File(directory);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File snap : directoryListing) {
				long diff = new Date().getTime() - snap.lastModified();
				if (diff > daysOld * 24 * 60 * 60 * 1000) {
					snap.delete();
				}
			}
		}
	}
}
