package com.tenko.functions;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.tenko.objs.MidiSeq;
import com.tenko.objs.MidiThread;

public class Moosic extends Function {

	private static Map<String, MidiSeq> storage;
	private static Sound instrumentSound = Sound.NOTE_PIANO;
	private List<MidiThread> musicThreads;
	private static Map<Integer, Sound> tracks;
	
	public Moosic(){
		storage = Maps.newHashMap();
		musicThreads = Lists.newArrayList();
		tracks = Maps.newHashMap();
	}

	@Override
	public boolean onCommand(CommandSender cs, Command c, String l, String[] args){
		if(cs.isOp()){
			if(c.getName().equalsIgnoreCase("moosic")){
				try {
					final Player plyr = (Player)cs;
					if(args.length > 0){
						if(storage.containsKey(plyr.getName())){
							closeStream(cs);
						}
						
						MidiThread thread = new MidiThread(plyr, args[0], ArrayUtils.contains(args, "l"));
						musicThreads.add(thread);
						thread.start();
						
						cs.sendMessage(ChatColor.BLUE + "Playing " + args[0]);
						return true;
					}
					cs.sendMessage(ChatColor.RED + "Wheres the argument, baka!?");
					return true;
				} catch (ClassCastException e){
					cs.sendMessage(ChatColor.RED + "You must be a player!");
					return true;
				}
			} else if(c.getName().equalsIgnoreCase("moosicstop")){
				closeStream(cs);
			} else if(c.getName().equalsIgnoreCase("moosicsound")){
				for(Sound s : Sound.values()){
					if(s.toString().trim().equalsIgnoreCase(args[0].trim())){
						instrumentSound = s;
						return true;
					}
				}
				cs.sendMessage(ChatColor.RED + "There wasn't a sound by that name!");
				return true;
			} else if(c.getName().equalsIgnoreCase("moosictrack")){
				try {
					int track = Integer.valueOf(args[0]);
					for(Sound s : Sound.values()){
						if(s.toString().trim().equalsIgnoreCase(args[1].trim())){
							tracks.put(track, s);
							cs.sendMessage(ChatColor.RED + "Set track " + track + "'s instrument to " + s);
							return true;
						}
					}
				} catch (ArrayIndexOutOfBoundsException e){
					cs.sendMessage(ChatColor.RED + "Please provide two arguments!");
				} catch (NumberFormatException e){
					cs.sendMessage(ChatColor.RED + "Use a number!");
				}
			}
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	public void closeStream(CommandSender cs){
		MidiSeq ms = storage.get(cs.getName());
		ms.close();
		storage.remove(cs);
		
		Iterator<MidiThread> iter = musicThreads.iterator();
		while(iter.hasNext()){
			Thread t = iter.next();
		    t.stop();
		    iter.remove();
		}
	}
	
	public static Map<String, MidiSeq> getStorage(){
		return storage;
	}
	
	public static Sound getSound(){
		return instrumentSound;
	}
	
	public static Map<Integer, Sound> getTrack(){
		return tracks;
	}
	
}
