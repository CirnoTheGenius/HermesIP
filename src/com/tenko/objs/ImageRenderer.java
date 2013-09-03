package com.tenko.objs;

import java.awt.Image;
import java.io.IOException;
import java.net.URI;

import javax.imageio.ImageIO;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class ImageRenderer extends MapRenderer {
	
	private boolean hasRendered;
	private final Image theImg;
	private Thread renderImageThread;
	
	public ImageRenderer(String url) throws IOException {
		super(false);
		hasRendered = false;
		theImg = MapPalette.resizeImage(ImageIO.read(URI.create(url).toURL().openStream()));
	}
	
	@Override
	public void render(MapView view, final MapCanvas canvas, Player plyr){
		if(!hasRendered && theImg != null && renderImageThread == null){
			renderImageThread = new Thread(){
				@Override
				public void run(){
					canvas.drawImage(0, 0, theImg);
				}
			};
			
			renderImageThread.start();
			hasRendered = true;
		}
	}

}
