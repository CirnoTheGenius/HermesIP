package com.tenko.functions;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;

import org.bukkit.ChatColor;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.tenko.objs.MidiSeq;

public class Moosic extends Function {

	HashMap<String, MidiSeq> storage;

	@Override
	public boolean onCommand(final CommandSender cs, Command c, String l, final String[] args){
		if(cs.isOp()){
			if(c.getName().equalsIgnoreCase("moosic")){
				try {
					final Player plyr = (Player)cs;
					if(args.length > 0){
						new Thread(){
							@Override
							public void run(){
								try {
									Sequencer sq = MidiSystem.getSequencer(false);
									sq.setSequence(new URL(args[0]).openStream());
									sq.open();
									sq.setTempoFactor(1);

									MidiRecieve midi = new MidiRecieve(plyr);
									sq.getTransmitter().setReceiver(midi);
									sq.start();

									storage.put(cs.getName(), new MidiSeq(sq, midi));
								} catch (IOException | InvalidMidiDataException | MidiUnavailableException e) {
									e.printStackTrace();
								}
							}
						}.start();
					}
				} catch (ClassCastException e){
					cs.sendMessage(ChatColor.RED + "You must be a player!");
				}
			}
		} else if(c.getName().equalsIgnoreCase("stopmoosic")){
			try {
				storage.get(cs.getName()).close();
			} catch (Exception e){
				cs.sendMessage(ChatColor.RED + "what are you doing");
			}
		}
		return false;
	}

	public class MidiRecieve implements Receiver {

		private static final float VOLUME_RANGE = 10.0f;

		private final Map<Integer, Integer> channelPatches;
		private final byte BASE_NOTE = new Note(1, Tone.F, true).getId();
		private static final int MIDI_BASE_FSHARP = 54;

		private Player plyr;
		public MidiRecieve(Player p)
		{
			plyr = p;
			this.channelPatches = new HashMap<Integer, Integer>();
		}

		@Override
		public void send(MidiMessage m, long time)
		{
			if (m instanceof ShortMessage)
			{
				ShortMessage smessage = (ShortMessage) m;
				int chan = smessage.getChannel();

				switch (smessage.getCommand())
				{
				case ShortMessage.PROGRAM_CHANGE:
					int patch = smessage.getData1();
					channelPatches.put(chan, patch);
					break;

				case ShortMessage.NOTE_ON:
					this.playNote(smessage);
					break;

				case ShortMessage.NOTE_OFF:
					break;
				default:
					break;
				}
			}
		}

		public void playNote(ShortMessage message)
		{
			if (ShortMessage.NOTE_ON != message.getCommand()) return;

			float pitch = (float) midiToPitch(message);
			float volume = VOLUME_RANGE * (message.getData2() / 127.0f);

			Sound instrument = Sound.NOTE_PIANO;

			plyr.getWorld().playSound(plyr.getLocation(), instrument, volume, pitch);
		}

		@Override
		public void close()
		{
			channelPatches.clear();
		}


		public double noteToPitch(Note note)
		{
			double semitones = note.getId() - BASE_NOTE;
			return Math.pow(2.0, semitones / 12.0);
		}

		public Note midiToNote(ShortMessage smsg)
		{
			assert smsg.getCommand() == ShortMessage.NOTE_ON;
			int semitones = smsg.getData1() - MIDI_BASE_FSHARP % 12;
			return new Note(semitones % 24);
		}

		public double midiToPitch(ShortMessage smsg)
		{
			return noteToPitch(midiToNote(smsg));
		}
	}
}
