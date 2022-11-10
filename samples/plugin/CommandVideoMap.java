package plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ayunami2000.VideoMapPacketCodecBukkit;

public class CommandVideoMap implements CommandExecutor {
	
	private VideoMapPacketCodecBukkit currentCodecInstance = null;

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(!(arg0 instanceof Player)) {
			arg0.sendMessage(ChatColor.RED + "Internal Error: " + ChatColor.WHITE + "CommmandSender must be a Player");
			return false;
		}
		if(arg3.length == 3 && arg3[0].equalsIgnoreCase("begin")) {
			try {
				List<int[]> mapRows = new ArrayList();
				double x = 0.0d;
				double y = 0.0d;
				double z = 0.0d;
				float volume = -1.0f;
				BufferedReader r = new BufferedReader(new FileReader(new File("video_map_config.txt")));
				String str;
				while((str = r.readLine()) != null) {
					str = str.trim();
					if(str.startsWith("#")) {
						continue;
					}else if(str.startsWith("x")) {
						int i = str.indexOf('=');
						if(i > 0) {
							x = Double.parseDouble(str.substring(i + 1).trim());
						}
					}else if(str.startsWith("y")) {
						int i = str.indexOf('=');
						if(i > 0) {
							y = Double.parseDouble(str.substring(i + 1).trim());
						}
					}else if(str.startsWith("z")) {
						int i = str.indexOf('=');
						if(i > 0) {
							z = Double.parseDouble(str.substring(i + 1).trim());
						}
					}else if(str.startsWith("volume")) {
						int i = str.indexOf('=');
						if(i > 0) {
							volume = Float.parseFloat(str.substring(i + 1).trim());
						}
					}else {
						try {
							String[] digits = str.split(",");
							int firstInt = Integer.parseInt(digits[0].trim());
							int[] newRow = new int[digits.length];
							newRow[0] = firstInt;
							for(int i = 1; i < digits.length; ++i) {
								newRow[i] = Integer.parseInt(digits[i].trim());
							}
							if(mapRows.size() > 0 && mapRows.get(0).length != newRow.length) {
								throw new IOException("All rows in map list must be the same length (" + mapRows.get(0).length + " != " + newRow.length + ")");
							}
							mapRows.add(newRow);
						}catch(NumberFormatException t) {
						}
					}
				}
				r.close();
				if(mapRows.size() > 0) {
					if(currentCodecInstance != null) {
						currentCodecInstance.disableVideoBukkit().send((Player)arg0);
						currentCodecInstance = null;
					}
					int[][] matrix = new int[mapRows.size()][mapRows.get(0).length];
					for(int i = 0, l = mapRows.size(); i < l; ++i) {
						for(int j = 0; j < matrix[i].length; ++j) {
							matrix[i][j] = mapRows.get(i)[j];
						}
					}
					currentCodecInstance = new VideoMapPacketCodecBukkit(matrix, x, y, z, volume);
					currentCodecInstance.beginPlaybackBukkit(arg3[1], true, arg3[2].indexOf('.') > 0 ? Float.parseFloat(arg3[2]) : Float.parseFloat(arg3[2] + ".0")).send((Player)arg0);
					arg0.sendMessage(ChatColor.GREEN + "Enabled video map, URL:" + ChatColor.WHITE + " " + arg3[1]);
					return true;
				}else {
					throw new IOException("No map rows were defined");
				}
			}catch(IOException ex) {
				arg0.sendMessage(ChatColor.RED + "Internal Error while reading \'video_map_config.txt\': " + ChatColor.WHITE + ex.toString());
			}
		}else if((arg3.length == 2 || arg3.length == 3) && arg3[0].equalsIgnoreCase("preload")) {
			int ttl = arg3.length == 3 ? Integer.parseInt(arg3[2]) * 1000 : 180 * 1000;
			VideoMapPacketCodecBukkit.bufferVideoBukkit(arg3[1], ttl).send((Player)arg0);
			arg0.sendMessage(ChatColor.GREEN + "Buffered video URL:" + ChatColor.WHITE + " " + arg3[1] + " " + ChatColor.GREEN + "for " + ChatColor.WHITE + (ttl / 1000) + ChatColor.GREEN + " seconds");
			return true;
		}else {
			if(arg3.length == 1 && arg3[0].equalsIgnoreCase("stop")) {
				if(currentCodecInstance != null) {
					currentCodecInstance.disableVideoBukkit().send((Player)arg0);
					currentCodecInstance = null;
					arg0.sendMessage(ChatColor.GREEN + "Disabled video map");
					return true;
				}else {
					arg0.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "no video is loaded");
				}
			}else if(arg3.length == 1 && arg3[0].equalsIgnoreCase("pause")) {
				if(currentCodecInstance != null) {
					currentCodecInstance.setPausedBukkit(true).send((Player)arg0);
					arg0.sendMessage(ChatColor.GREEN + "Paused video map");
					return true;
				}else {
					arg0.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "no video is loaded");
				}
			}else if(arg3.length == 1 && arg3[0].equalsIgnoreCase("resume")) {
				if(currentCodecInstance != null) {
					currentCodecInstance.setPausedBukkit(false).send((Player)arg0);
					arg0.sendMessage(ChatColor.GREEN + "Resumed video map");
					return true;
				}else {
					arg0.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "no video is loaded");
				}
			}else if((arg3.length == 1 || arg3.length == 2) && arg3[0].equalsIgnoreCase("loop")) {
				if(currentCodecInstance != null) {
					boolean gottaLoop = arg3.length == 1 || arg3[1].equalsIgnoreCase("true");
					currentCodecInstance.setLoopEnableBukkit(gottaLoop).send((Player)arg0);
					arg0.sendMessage(ChatColor.GREEN + (gottaLoop ? "Enabled video map loop" : "Disabled video map loop"));
					return true;
				}else {
					arg0.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "no video is loaded");
				}
			}else if(arg3.length == 1 && arg3[0].equalsIgnoreCase("move")) {
				if(currentCodecInstance != null) {
					Location l = ((Player)arg0).getLocation();
					currentCodecInstance.moveAudioSourceBukkit(l.getX(), l.getY(), l.getZ(), currentCodecInstance.getVolume()).send((Player)arg0);
					arg0.sendMessage(ChatColor.GREEN + "Repositioned audio source to " + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ());
					return true;
				}else {
					arg0.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "no video is loaded");
				}
			}
		}
		return false;
	}

}
