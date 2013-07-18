package com.tenko.objs;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.tenko.functions.Moosic;

public class MidiThread extends Thread {

	private final Player plyr;
	private final boolean loop;
	private final String url;

	public MidiThread(Player plyr, String url, boolean loop){
		this.plyr = plyr;
		this.loop = loop;
		this.url = url;
	}

	@Override
	public void run(){
		try {
			Sequencer sq = MidiSystem.getSequencer(false);
			URLConnection con = new URL(url).openConnection();
		    
		    sq.setSequence(new BufferedInputStream(con.getInputStream()));
			sq.setTempoFactor(1);
			sq.open();	

			MidiReciever midi = new MidiReciever(plyr);
			sq.getTransmitter().setReceiver(midi);
			Moosic.getStorage().put(plyr.getName(), new MidiSeq(sq, midi));

			if(loop){
				sq.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
			}

			sq.start();
		} catch (Exception e) {
			plyr.sendMessage(ChatColor.RED + "Somehow, the MIDI playback failed!");
			plyr.sendMessage(ChatColor.RED + "Failed with " + e.getCause());
			e.printStackTrace();
		}
	}



}