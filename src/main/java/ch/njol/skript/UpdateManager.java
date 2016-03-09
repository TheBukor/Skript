/*
 *   This file is part of Skript.
 *
 *  Skript is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Skript is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 * Copyright 2011-2013 Peter Güttinger
 * 
 */

package ch.njol.skript;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.eclipse.jdt.annotation.Nullable;

/**
 * @author Peter Güttinger
 *
 */
public class UpdateManager {
		
	protected Double localVersion = 1.2;
	protected Double onlineVersion = 0.0;
	protected String downloadURL = "https://forums.skunity.com/t/4184";
	
	protected Double aLocalVersion = 1.9;
	protected Double aOnlineVersion = 0.0;
	protected String adownloadURL = "https://raw.githubusercontent.com/nfell2009/Skript/master/src/main/resources/aliases-english.sk";
	
	protected UpdateMode mode;
	
	protected enum UpdateMode {
		SKRIPT,
		ALIASES,
		BOTH
	}
	
	protected UpdateManager(UpdateMode mode) {
		this.mode = mode;
		setOnlineDetails();
		runUpdateCheck();
		startUpdateChecker();
	}
	
	@SuppressWarnings("null")
	protected void setOnlineDetails() {
		if(mode == UpdateMode.SKRIPT || mode == UpdateMode.BOTH) {
			try {
				URL url = new URL("http://nfell2009.uk/skript/version");
		      	Scanner scanner = new Scanner(url.openStream());
		      	String str = "";
		      	while (scanner.hasNext()) {
		          	str = str + scanner.next();
		      	}
		      	scanner.close();
		      	String[] temp = str.split("->");
		      	onlineVersion = Double.valueOf(temp[0]) != null ? Double.parseDouble(temp[0]) : 0.0;
		      	downloadURL = temp[1] != null ? temp[1] : "https://forums.skunity.com/t/4184";
		    } catch (IOException ex) {
		    	Skript.warning("An error occoured when trying to find the latest version information!");
		    	onlineVersion = 0.0;
		    }
		}
		if (mode == UpdateMode.ALIASES || mode == UpdateMode.BOTH) {
			try {
				URL url = new URL("https://tim740.github.io/aliasesVer");
		      	Scanner scanner = new Scanner(url.openStream());
		      	String str = "";
		      	while (scanner.hasNext()) {
		          	str = str + scanner.next();
		      	}
		      	scanner.close();
		      	aOnlineVersion = Double.valueOf(str) != null ? Double.parseDouble(str) : 0.0;
		    } catch (IOException ex) {
		    	Skript.warning("An error occoured when trying to find the latest version information for aliases!");
		    	onlineVersion = 0.0;
		    }
		}
	}
	
	protected void runUpdateCheck() {
		if(this.onlineVersion == 0.0) {
			Skript.warning("Failed to check for a new update from http://nfell2009.uk/skript/version! Returned value: " + onlineVersion);
		} else if(onlineVersion > localVersion) {
			Skript.info("A new version of Skript has been found. Skript " + onlineVersion + " has been released. It's recommended to try the latest version.");
			Skript.info("Download link: " + downloadURL);
		} else {
			Skript.warning("An unknown error occoured when attempting to get current version number");
		}
	}
	
	protected void startUpdateChecker() {
		Bukkit.getScheduler().runTaskAsynchronously(Skript.getInstance(), new Runnable(){

			@Override
			public void run() {
				runUpdateCheck();
			}
			
		});
	}
	
	
}
