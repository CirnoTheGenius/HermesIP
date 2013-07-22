package com.tenko.objs;

import javax.sound.midi.Sequencer;
import com.tenko.objs.MidiReciever;

public class MidiSeq {
	
	private Sequencer a;
	private MidiReciever b;
	
	public MidiSeq(Sequencer seq, MidiReciever midi){
		this.a = seq;
		this.b = midi;
	}
	
	public void close(){
		a.close();
		b.close();
	}
	
}