package etf.pj2.cc.simulation;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.logging.Level;
import etf.pj2.cc.exceptions.CCLogger;
import interfaces.Locations;
import interfaces.WorkEntityInterface;


//Nekad radi, nekad ne radi


public class CoronaWatchService extends Thread implements WorkEntityInterface,Locations {
	
	Path pathToWatch=Paths.get(Locations.ROOT);
	WatchService watchService;
	ControlerControlGUI controlGUI;
	Integer startOrStop=1;
	Integer pause=0;
	
	public CoronaWatchService(ControlerControlGUI controlGUI)
	{
		this.controlGUI=controlGUI;
		try {
			watchService = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			CCLogger.cclogger(getClass().getName(), Level.INFO, e);
		}
	}
	
	@Override
	public void run()
	{
		System.out.println("WatchService started...");
		try {
			pathToWatch.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
		} catch (IOException e) {
			CCLogger.cclogger(getClass().getName(), Level.WARNING, e);
		}
		
		try {
		while(startOrStop!=0)
		{
			if(pause==0) {
			WatchKey key;
			
				while((key=watchService.take())!=null)
				{
		            for (WatchEvent<?> event : key.pollEvents()) {
		            	String changedFile=event.context().toString();
		            	if(changedFile.equals("FileWatcher.txt"))
							try {
								changeTextFields();
							} catch (IOException e) {
								CCLogger.cclogger(getClass().getName(), Level.WARNING, e);
							} 
		            }		            
		            key.reset();
				}
			}
			else
				Thread.sleep(1500);
		}
		Thread.sleep(150);
		}

		catch (InterruptedException e) {
			CCLogger.cclogger(getClass().getName(), Level.WARNING, e);
		}
	}

	@Override
	public Integer setStartorStop(Integer move) {
		startOrStop=move;
		return startOrStop;
	}
	
	public void changeTextFields() throws IOException
	{
		controlGUI.readAndChange();
	}

	@Override
	public Integer setPause(Integer pause) {
          this.pause=pause;
          return pause;
	}
}
