package com.tenko.objs;

import java.util.Map;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;
import com.tenko.functions.Moosic;

public class MidiReciever implements Receiver {

	private Map<Integer, Integer> channelPatches;
	private Player plyr;

	public MidiReciever(Player plyr){
		this.plyr = plyr;
		this.channelPatches = Maps.newHashMap();
	}

	@Override
	public void close(){
		plyr = null;
	}

	@Override
	public void send(MidiMessage msg, long time) {
		if(msg instanceof ShortMessage){
			ShortMessage smsg = (ShortMessage)msg;

			int channel = smsg.getChannel();

			switch(smsg.getCommand()){
				case(ShortMessage.PROGRAM_CHANGE):{
					channelPatches.put(channel, smsg.getData1());
					break;
				}
	
				case(ShortMessage.NOTE_ON):{
					playNote(channel, smsg);
					break;
				}
			}
		}
	}

	public void playNote(int channel, ShortMessage msg){
		try {
			float pitch = (float)midiToPitch(msg);
			float volume = 10.0F * (msg.getData2() / 127.0f);
			if(Moosic.getTrack().containsKey(channel)){
				plyr.getWorld().playSound(plyr.getLocation(), Moosic.getTrack().get(channel), volume, pitch);
			} else {
				plyr.playSound(plyr.getLocation(), Moosic.getSound(), volume, pitch);
			}
			for(Entity p : plyr.getNearbyEntities(20, 20, 20)){
				if(p instanceof Player){
					if(Moosic.getTrack().containsKey(channel)){
						((Player)p).playSound(plyr.getLocation(), Moosic.getTrack().get(channel), volume, pitch);
						plyr.getWorld().playSound(plyr.getLocation(), Moosic.getTrack().get(channel), volume, pitch);
					} else {
						((Player)p).playSound(plyr.getLocation(), Moosic.getSound(), volume, pitch);
						plyr.playSound(plyr.getLocation(), Moosic.getSound(), volume, pitch);
					}
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public double midiToPitch(ShortMessage m){
		return Math.pow(2, (new Note((m.getData1() - 54 % 12) % 24).getId() - (byte)new Note(1, Tone.F, true).getId()) / 12.0);
	}

}