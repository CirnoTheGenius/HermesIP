package com.tenko.updater;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.UnknownDependencyException;

import com.tenko.FriendlyWall;

public class Updater {
	
	private static String urlDef = "";
	
	private static Thread updaterThread;
	
	public static void update(String pluginName){
		String urlS = urlDef + pluginName + ".jar";
		try {
			URL url = new URL(urlS);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			
			if(connection.getResponseCode() != 404){
				_update(urlS, connection);
			} else {
				Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[FriendlyWall - Updater] That isn't a plugin...");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private final static void _update(final String dlLink, final HttpURLConnection connection) throws IOException {
		updaterThread = new Thread(){
			@Override
			public void run(){
				try {
					Bukkit.broadcastMessage(ChatColor.RED + "A plugin will be updating in 10 seconds. Brace for impact!");
					Thread.sleep(20*10);
				    ReadableByteChannel rbc = Channels.newChannel(connection.getInputStream());
					File fp = new File(FriendlyWall.getPlugin().getDataFolder().getParent(), dlLink.substring(urlDef.length()));
				    FileOutputStream fos = new FileOutputStream(fp, false);
				    
					fos.getChannel().transferFrom(rbc, 0, Short.MAX_VALUE);
					fos.flush();
				    fos.close();
					rbc.close();
					System.out.println(dlLink.substring(urlDef.length(), dlLink.length()-4));
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "plm reload " + dlLink.substring(urlDef.length(), dlLink.length()-4));
					Bukkit.broadcastMessage(ChatColor.BLUE + "A plugin has updated and been loaded. If there's issues, tell a moderator.");
					//Make sure JVM says "r u wanna get ded"
					this.interrupt();
				} catch (UnknownDependencyException | SecurityException | IllegalArgumentException | IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		
		updaterThread.start();
	}
}
