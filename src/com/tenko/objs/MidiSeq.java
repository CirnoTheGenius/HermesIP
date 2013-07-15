package com.tenko.objs;

import javax.sound.midi.Sequencer;
import com.tenko.functions.Moosic.MidiRecieve;

public class MidiSeq {
	
	private Sequencer a;
	private MidiRecieve b;
	
	public MidiSeq(Sequencer a, MidiRecieve b){
		this.a = a;
		this.b = b;
	}
	
	public void close(){
		a.close();
		b.close();
	}
	
}
