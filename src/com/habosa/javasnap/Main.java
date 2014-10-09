package com.habosa.javasnap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
	// test

	public static void main(String[] args) throws Exception {
		String user = pullUser();
		String pass = pullPass();
		//System.out.print(user);
		//System.out.print(pass);
		FileMaitenence testCleaner = new FileMaitenence("snaps//", 1);
		SnapBot test = new SnapBot(user, pass);
		while (true) {
			testCleaner.cleanupFolder();
			test.run();
			Thread.sleep(1000 * 60 * 3);
		}

	}

	public static String pullPass() throws IOException {
		File pass = new File("password.txt");
		String password = deserializeString(pass);
		return password;
	}

	public static String pullUser() throws IOException{
		File username = new File("username.txt");
		String user = deserializeString(username);
		return user;
	}

	public static String deserializeString(File file) throws IOException {
		int len;
		char[] chr = new char[4096];
		final StringBuffer buffer = new StringBuffer();
		final FileReader reader = new FileReader(file);
		try {
			while ((len = reader.read(chr)) > 0) {
				buffer.append(chr, 0, len);
			}
		} finally {
			reader.close();
		}
		return buffer.toString();
	}

}
