package com.worldofbuzz.order;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import com.worldofbuzz.dao.Data;



public class test
	{
	
	static Logger log = LogManager.getLogger(test.class);
	
	public static void main(String[] args)
		{
				File config = new File("config/config.properties");
				Properties prop = new Properties();
				InputStream input = null;
				FilenameFilter filter;
				List<File> dirTart;
				File dir;
				File out;
				BufferedReader stdInput;
				BufferedWriter stdOutput;
				String[] sor;
				String lines;
				String msg = "";
				OrderLine line;
				Data data;
				
				log.info("Program start.");
				try 
					{
					if (!config.exists())
						{
						log.error("Missing config.properties file!");
						}
					else //ha letezik a properties file
						{
						input = new FileInputStream(config);						
						prop.load(input); //config betoltes
						
						Configurator.setLevel(log.getName(), Level.getLevel(prop.getProperty("debug").toUpperCase())); //naplozas szint allitas
						
						filter = new FilenameFilter()  //szuro definialas
							{
								@Override
								public boolean accept(File dir, String name)
									{
									return name.endsWith(".csv");
									}
							};
						dir = new File(prop.getProperty("load_dir"));
						out = new File(prop.getProperty("save_dir"));
						dirTart = new ArrayList<File>(Arrays.asList(dir.listFiles(filter))); //csak a csv allomanyokat adja vissza
						
						data = new Data( prop.getProperty("db_user"), prop.getProperty("db_password"), prop.getProperty("db_host"), prop.getProperty("db_port"),log); // adatbazis kapcsolat inicializalas
						
						for (int i = 0; i < dirTart.size(); i++) // a konyvtarba levo csv listazasa
							{
							stdInput = new BufferedReader(new InputStreamReader(new FileInputStream(dirTart.get(i))));
							stdOutput = new BufferedWriter(new FileWriter(out.getAbsolutePath()+File.separator+dirTart.get(i).getName().replaceAll(".csv", "_response.csv")));

							while ((lines = stdInput.readLine()) != null) //soronkenti beolvasas
								{
								if (lines.length() > 12 && !lines.trim().startsWith("LineNumber"))
									{
									lines += "; "; // ha nincs datum akkor a vegere tezek meg egy ;-t
  								sor = lines.split(";"); //sor szetvagasa sozlopokra
  								if (sor.length < 12) //ha hiányoznak oszlopok 
  									{
  									msg = "Missing column(s)! ";
  									stdOutput.write(sor[0] + ";" + "ERROR;" + msg + "\n");
  									}
  								else //ha minden oszlop megvan
  									{
  									line = new OrderLine(sor[0],sor[1],sor[2],sor[3],sor[4],sor[5],sor[6],sor[7],sor[8],sor[9],sor[10],sor[11]); //sor letrehozas
  									msg = line.validate(); // oszlopok ellenorzese

  									log.info(line.toString());  									

  									if (msg.length() > 0) // ha nincsenek rendben az oszlopok
  										stdOutput.write(sor[0] + ";" + "ERROR;" + msg + "\n");
  									else
  										{
  										msg = data.validate(line.getiOrderId(), line.getiOrderItemId(),log); //ID-k ellenorzese
  										if (msg.length() > 0) // ha mar letezik valamelyik ID az adatbazisban
  											stdOutput.write(sor[0] + ";" + "ERROR;" + msg + "\n");
  										else
  											{
  											msg = data.insertOrder(line,log); //sorok beszurasa ay adatbazisba
  											if (msg.length() > 0) // ha hiba volt a beszuraskor
  												stdOutput.write(sor[0] + ";" + "ERROR;" + msg + "\n");
  											else // ha minden rendben 
  												stdOutput.write(sor[0] + ";" + "OK;\n");
  											}
  										}
  									}
									}
								}//while vege
							stdInput.close();
							stdOutput.close();
// az elkeszult response file feltoltese							
							if (sendData(prop.getProperty("ftp_host"),Integer.valueOf(prop.getProperty("ftp_port")),prop.getProperty("ftp_user"),prop.getProperty("ftp_password"),
											prop.getProperty("ftp_remote_dir") + "/" + dirTart.get(i).getName().replaceAll(".csv", "_response.csv"),
											out.getAbsolutePath()+File.separator+dirTart.get(i).getName().replaceAll(".csv", "_response.csv")) )
								{
								log.info("Upload succes!");
								}
							else
								log.error("Upload failed!");
							}
						data.disconnect(log); //adatbazis kapcsolat bontasa
						}//ha megvan a properties file vege
				} 
				catch ( IOException e1) 
					{
					// TODO Auto-generated catch block
					log.error(e1.getMessage());
					}
				
				}

	public static Boolean sendData(String host, Integer port, String user, String passw, String remoteDir, String local)
		{
		FTPClient client = new FTPClient();
		Boolean ret = true;
		FileInputStream inputStream;		
		
		try
			{
			client.connect(host,port);
			if (client.login(user, passw))
				{
				client.enterLocalPassiveMode();
				client.changeWorkingDirectory(remoteDir.substring(0,remoteDir.lastIndexOf("/")));
				client.setFileTransferMode(FTP.ASCII_FILE_TYPE);

				inputStream = new FileInputStream(local); //a feltolteni kivant allomany megnyitasa 
				ret = client.storeFile(remoteDir.substring(remoteDir.lastIndexOf("/")+1), inputStream); //feltoltes
				if (client.isConnected())
					{
					try
						{
						client.logout();
						client.disconnect();					
						} 
					catch (IOException e)
						{
						log.error(e.getMessage());
						ret = false;					
						}
					}
				inputStream.close();				
				}
			} 
		catch ( SocketException e )
			{
			log.error(e.getMessage());
			ret = false;
			}			
		catch ( IOException e )
			{
			log.error(e.getMessage());
			ret = false;
			} 


		return ret;
		}
	
		}
		

