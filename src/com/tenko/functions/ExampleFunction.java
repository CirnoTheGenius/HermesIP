package com.tenko.functions;

import org.bukkit.command.CommandSender;

import com.tenko.annotations.FunctionCommand;

public class ExampleFunction extends Function {

	@FunctionCommand(usage = "/waitwarmly <random words here>", requiredArgumentCount = 1, opOnly = true)
	public void waitWarmly(CommandSender cs, String[] args){
		cs.sendMessage("Here's your arguments:");
		cs.sendMessage(args);
	}

}