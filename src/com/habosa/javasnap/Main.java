package com.habosa.javasnap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Main {
	

	public static void main(String[] args) throws Exception {
		while(true){
		FileMaitenence testCleaner = new FileMaitenence("snaps//", 1);
		SnapBot test = new SnapBot("user", "pass");
		testCleaner.cleanupFolder();
		test.run();
	    Thread.sleep(1000 * 60 * 1);
		}
		
	}

	
}
