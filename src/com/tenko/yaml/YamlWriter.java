package com.tenko.yaml;

import java.util.List;

import com.tenko.FriendlyWall;

//Because I'm too lazy. Plus, I want to minimize code and narrow it down quicker.
public class YamlWriter {
	
	/**
	 * @param child - The element to add.
	 * @param parent - The list to add to.
	 * @return True if "parent" didn't already contain "child", false otherwise.
	 */
	public static boolean writeToList(String child, String parent){
		FriendlyWall fw = FriendlyWall.getPlugin();
		List<String> node = fw.getConfig().getStringList(parent);
		
		if(node.contains(child)){
			return false;
		}
		
		node.add(child);
		fw.getConfig().set(parent, node);
		fw.saveConfig();
		fw.reloadConfig();
		
		return true;
	}
	
	/**
	 * @param child - The element to remove.
	 * @param parent - The list to remove from.
	 * @return True if "parent" contained "child" and removed successfully, false otherwise.
	 */
	public static boolean removeFromList(String child, String parent){
		FriendlyWall fw = FriendlyWall.getPlugin();
		List<String> node = fw.getConfig().getStringList(parent);
		
		if(!node.remove(child)){
			return false;
		}
		
		fw.getConfig().set(parent, node);
		fw.saveConfig();
		fw.reloadConfig();
		
		return true;
	}
	
	public static List<String> getList(String listName){
		return FriendlyWall.getPlugin().getConfig().getStringList(listName);
	}
	
}
