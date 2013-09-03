package com.tenko.objs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import com.google.common.io.Files;
import com.tenko.NovEngine.options.ResponseOption;
import com.tenko.functions.Function;
import com.tenko.functions.NPCs;
import com.tenko.npc.CraftMika;

public class NPCDataParserRunnable implements Runnable {

	private final NPCs instance;
	private final File parseFile;

	public NPCDataParserRunnable(NPCs i, File f){
		this.instance = i;
		this.parseFile = f;

		System.out.println("Created Parsee thread. Will begin paru'ing soon.");
	}

	private void checkFolders(){
		File dir = new File(Function.getFunctionDirectory("NPCs"), "Quotes");
		if(!dir.exists()){
			dir.mkdirs();
		}

		File dir2 = new File(Function.getFunctionDirectory("NPCs"), "NPCVNData");
		if(!dir2.exists()){
			dir2.mkdirs();
		}
	}

	@Override
	public void run(){
		System.out.println("Parsee thread starting. Paruparuparuparu...");
		checkFolders();

		String l;
		try(BufferedReader br = new BufferedReader(new FileReader(parseFile))){
			while((l = br.readLine()) != null){
				CraftMika mika = null;
				try {
					if(l.isEmpty()){
						continue;
					}

					String[] data = l.split(";");
					mika = CraftMika.createNPC(data[0], new Location(Bukkit.getWorld(data[1]), new Double(data[2]), new Double(data[3]), new Double(data[4])));
					instance.registerNPC(mika.getName(), mika);

					File oldQuoteData = new File(new File(Function.getFunctionDirectory("NPCs"), "Quotes"), mika.getName() + ".yml");
					if(oldQuoteData.exists()){
						System.out.println("Found old data. Converting...");
						convertData(oldQuoteData, mika.getName());
						System.out.println("Convert complete.");
					}

					File quoteData = new File(new File(Function.getFunctionDirectory("NPCs"), "Quotes"), mika.getName() + ".dat");
					if(quoteData.exists()){
						addQuotesFromFile(quoteData, mika);
					}

					File vnData = new File(new File(Function.getFunctionDirectory("NPCs"), "NPCVNData"), mika.getName() + ".dat");
					if(vnData.exists()){
						parseVNData(vnData, mika);
					}
				} catch (Exception e){
					e.printStackTrace();
					System.out.println("Bad data; line \"" + l + "\" appears to be invalid.");
					try {
						if(mika != null){
							mika.die();
						}
					} catch (Exception e1){
						//Done.
					}
					continue;
				}

			}
		} catch (IOException e){
			e.printStackTrace();
		}
	}


	private void convertData(File f, String npcName) throws IOException {
		FileConfiguration yaml = YamlConfiguration.loadConfiguration(f);
		List<String> things = yaml.getStringList("Quotes");
		File writeTo = new File(new File(Function.getFunctionDirectory("NPCs"), "Quotes"), npcName + ".dat");

		try(BufferedWriter writer = new BufferedWriter(new FileWriter(writeTo))){
			for(String s : things){
				writer.write(s);
				writer.newLine();
			}
		}

		f.delete();
	}

	private void addQuotesFromFile(File f, CraftMika npc) throws IOException {
		try(BufferedReader br = new BufferedReader(new FileReader(f))){
			String l;
			while((l = br.readLine()) != null){
				if(l.isEmpty()){
					continue;
				}

				npc.getQuotes().add(l);
			}
		}
	}

	private void parseVNData(File vnData, CraftMika mika) throws IOException {
		String l, type, question, answer;
		boolean mustConvert = false;
		try(BufferedReader br = new BufferedReader(new FileReader(vnData))){
			while((l = br.readLine()) != null){
				if(l.isEmpty()){
					continue;
				}

				try {
					String[] data = l.split(";");
					type = data[0];
					question = data[1];
					answer = data[2];

					if(type.equalsIgnoreCase("response")){
						mika.getOptions().add(new ResponseOption(question, answer));
					} else if(type.equalsIgnoreCase("more")){
						System.out.println("To be reimplemented.");
					} else if(type.equalsIgnoreCase("suboption")){
						mustConvert = true;
						break;
					} else {
						System.out.println("Not implemented.");
						continue;
					}
				} catch (Exception e){
					e.printStackTrace();
					System.out.println("Bad VN data; line \"" + l + "\" appears to be invalid.");
				}
			}
		}

		if(mustConvert){
			System.out.println("Found buggy suboptions. Making these a single layer now.");
			List<String> lines = Files.readLines(vnData, Charset.defaultCharset());
			try(BufferedWriter bw = new BufferedWriter(new FileWriter(vnData))){
				for(String s : lines){
					bw.write(s.replace("suboption;", ""));
				}
			}

			parseVNData(vnData, mika);
		}
	}

}
