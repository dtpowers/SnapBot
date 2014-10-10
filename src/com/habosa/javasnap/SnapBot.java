package com.habosa.javasnap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.text.SimpleDateFormat;

public class SnapBot {
	private static String user;
	private static String pass;
	private static Snapchat snapchat;

	public SnapBot(String username, String password) {
		user = username;
		pass = password;

	}

	// TODO write program to rename all files to 1-x
	// TODO purge old files
	public void run() throws Exception {
		// get new snaps
		if (snapchat != null) {
			snapchat.refresh();
			System.out.print("refreshed!\n");
		} else {
			log();
		}
		Snap[] snaps = snapchat.getSnaps();

		// add friends
		addFriends(snaps);
		// parse users to respond to
		ArrayList<String> users = getUsers(snaps);
		// download snaps locally and mark as viewed
		storeSnaps(snaps);
		// respond to snaps
		respond(users);
		// delete snaps over 24 hours old
		System.out.print("Done!\n");
	}

	private static void respond(ArrayList<String> users) {
		List<String> recipients = new ArrayList<String>();
		Random rand = new Random();
		File file = new File("snaps");
		File tempPhoto;
		File[] photos = file.listFiles();
		for (int i = 0; i < users.size(); i++) {
			tempPhoto = photos[rand.nextInt(photos.length)];
			recipients.add(users.get(i));
			if (tempPhoto.getName().contains("jpg")) {
				snapchat.sendSnap(tempPhoto, recipients, false, false, 7);
				recipients.remove(0);

			} else {
				snapchat.sendSnap(tempPhoto, recipients, true, false, 7);
				recipients.remove(0);
			}

		}

	}

	private static void respondold(ArrayList<String> users) {

		List<String> recipients = new ArrayList<String>();
		String responseSnap;
		Random rand = new Random();
		int randomPhoto;
		int numPhotos = getNumPhotos();

		for (int i = 0; i < users.size(); i++) {

			randomPhoto = rand.nextInt(numPhotos) + 1;
			// check if file is jpg or mp4, send appropriate
			File f = new File("snaps\\" + randomPhoto + ".jpg");
			if (f.exists() && !f.isDirectory()) {
				responseSnap = "snaps\\" + randomPhoto + ".jpg";
				recipients.add(users.get(i));
				File image = new File(responseSnap);
				snapchat.sendSnap(image, recipients, false, false, 7);
				recipients.remove(0);
			} else {
				responseSnap = "snaps\\" + randomPhoto + ".mp4";
				recipients.add(users.get(i));
				File image = new File(responseSnap);
				snapchat.sendSnap(image, recipients, true, false, 7);
				recipients.remove(0);
			}

		}
	}

	public static int getNumPhotos() {
		File dir = new File("snaps\\");
		File[] directoryListing = dir.listFiles();
		return directoryListing.length;
	}

	private static void storeSnaps(Snap[] snaps) throws Exception {
		// see how many files exsist in directory currently
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		int count = getNumPhotos();
		for (int i = 0; i < snaps.length; i++) {
			if (snaps[i].isDownloadable()) {
				count++;
				String extension = ".jpg";
				if (!snaps[i].isImage()) {
					extension = ".mp4";
				}
				byte[] snapBytes = snapchat.getSnap(snaps[i]);
				File snapFile = new File("snaps\\" + timeStamp + count + extension);
				FileOutputStream snapOs = new FileOutputStream(snapFile);
				snapOs.write(snapBytes);
				snapOs.close();
				snapchat.setSnapFlags(snaps[i], true, false, false);
			}
		}
	}

	public static ArrayList<String> getUsers(Snap[] snaps) {
		ArrayList<String> users = new ArrayList<String>();
		for (int i = 0; i < snaps.length; i++) {
			if (snaps[i].isDownloadable()) {
				users.add(snaps[i].getSender());
			}
		}
		return users;
	}

	public static void log() {
		System.out.println("Logging in...");
		snapchat = Snapchat.login(user, pass);
		if (snapchat != null) {
			System.out.println("Logged in.");
		} else {
			System.out.println("Failed to log in.");
			return;
		}

	}

	public static void addFriends(Snap[] snaps) {
		Friend[] friends = snapchat.getFriends();
		for (int i = 0; i < snaps.length; i++) {
			String sender = snaps[i].getSender();
			// if sender isnt already a friend, add as friend
			if (!Arrays.asList(friends).contains(sender)) {
				snapchat.addFriend(sender);
			}
		}

	}

	public static void sendSnap(String username, String recipient,
			String filename) throws FileNotFoundException {

		// Get file
		File file = new File(filename);

		// Try sending it
		List<String> recipients = new ArrayList<String>();
		recipients.add(recipient);

		// Send and print
		System.out.println("Sending...");
		boolean postStory = false; // set as true to make this your story as
									// well...

		// TODO(samstern): User-specified time, not automatically 10 seconds
		boolean result = snapchat.sendSnap(file, recipients, false, postStory,
				10);
		if (result) {
			System.out.println("Sent.");
		} else {
			System.out.println("Could not send.");
		}
	}

	public static void setStory(String username, String filename)
			throws FileNotFoundException {

		boolean video = false; // TODO(liamcottle) upload video snaps from
								// command line.
		// Get file
		File file = new File(filename);

		// Send and print
		System.out.println("Setting...");
		boolean postStory = false; // set as true to make this your story as
									// well...

		// TODO(samstern): User-specified time, not automatically 10 seconds
		boolean result = snapchat.sendStory(file, video, 10, "My Story");
		if (result) {
			System.out.println("Set.");
		} else {
			System.out.println("Could not set.");
		}
	}

}
