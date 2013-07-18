package com.tenko.objs;

import javax.sound.midi.Sequencer;
import com.tenko.objs.MidiReciever;

public class MidiSeq {
	
	private Sequencer a;
	private MidiReciever b;
	
	public MidiSeq(Sequencer a, MidiReciever midi){
		this.a = a;
		this.b = midi;
	}
	
	public void close(){
		a.close();
		b.close();
	}
	
}