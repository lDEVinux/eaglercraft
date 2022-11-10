package plugin;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ayunami2000.MapPacketCodec.PixelFormat;
import ayunami2000.MapPacketCodecBukkit;

public class CommandSampleMap implements CommandExecutor {
	
	public final EaglerSamplesPlugin plugin;
	
	public CommandSampleMap(EaglerSamplesPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg01, String arg1, String[] arg2) {
		if(!(arg0 instanceof Player)) {
			arg0.sendMessage(ChatColor.RED + "Internal Error: " + ChatColor.WHITE + "CommmandSender must be a Player");
			return false;
		}
		arg0.sendMessage(ChatColor.YELLOW + "Note: test packets are only sent to the player running this command");
		Player p = (Player)arg0;
		try {
			int mapId = -1;
			if(arg2.length >= 2) {
				mapId = Integer.parseInt(arg2[1]);
			}else {
				ItemStack i = p.getInventory().getItemInHand();
				if(i.getType() == Material.MAP) {
					mapId = (int)i.getDurability() & 0xFFFF;
				}
			}
			if(mapId != -1) {
				if(arg2.length == 1) {
					if(arg2[0].equalsIgnoreCase("get")) {
						arg0.sendMessage(ChatColor.GREEN + "Current map ID: " + ChatColor.WHITE + mapId);
						return true;
					}else if(arg2[0].equalsIgnoreCase("disable")) {
						MapPacketCodecBukkit pkt = new MapPacketCodecBukkit(mapId);
						pkt.sendDisablePacketToPlayer(p);
						arg0.sendMessage(ChatColor.GREEN + "Reset map: " + ChatColor.WHITE + mapId);
						return true;
					}else if(arg2[0].equalsIgnoreCase("set")) {
						arg0.sendMessage(ChatColor.RED + "Use: " + ChatColor.WHITE + "/samplemap set <file> [compress]");
						return true;
					}else {
						MapPacketCodecBukkit pkt = new MapPacketCodecBukkit(mapId);
						BufferedImage img = ImageIO.read(new File(arg2[0]));
						pkt.setPixels(img);
						pkt.sendNextPacketToPlayer(p);
						arg0.sendMessage(ChatColor.GREEN + "Wrote image " + ChatColor.WHITE + arg2[0] + ChatColor.GREEN + " to map " + ChatColor.WHITE + mapId);
						return true;
					}
				}else if(arg2.length == 2) {
					int j = Integer.parseInt(arg2[1]);
					if(arg2[0].equalsIgnoreCase("disable")) {
						MapPacketCodecBukkit pkt = new MapPacketCodecBukkit(j);
						pkt.sendDisablePacketToPlayer(p);
						arg0.sendMessage(ChatColor.GREEN + "Reset map: " + ChatColor.WHITE + j);
						return true;
					}
				}else if(arg2.length >= 3) {
					int j = Integer.parseInt(arg2[1]);
					MapPacketCodecBukkit pkt = new MapPacketCodecBukkit(j);
					BufferedImage img = ImageIO.read(new File(arg2[2]));
					if(arg2.length == 4 || arg2.length == 5) {
						if(arg2[3].equalsIgnoreCase("16bpp")) {
							pkt.pixelFormat(PixelFormat.R5_G6_B5);
							if(arg2.length == 5) {
								if(arg2[4].equalsIgnoreCase("true")) {
									pkt.deflate(true);
								}else {
									pkt.deflate(Integer.parseInt(arg2[4]));
								}
							}
						}else if(arg2[3].equalsIgnoreCase("24bpp")) {
							pkt.pixelFormat(PixelFormat.R8_G8_B8);
							if(arg2.length == 5) {
								if(arg2[4].equalsIgnoreCase("true")) {
									pkt.deflate(true);
								}else {
									pkt.deflate(Integer.parseInt(arg2[4]));
								}
							}
						}else if(arg2[3].equalsIgnoreCase("true")) {
							pkt.deflate(true);
						}else {
							pkt.deflate(Integer.parseInt(arg2[3]));
						}
					}
					pkt.setPixels(img);
					pkt.sendNextPacketToPlayer(p);
					arg0.sendMessage(ChatColor.GREEN + "Wrote image " + ChatColor.WHITE + arg2[2] + ChatColor.GREEN + " to map " + ChatColor.WHITE + mapId);
					return true;
				}
			}
		}catch(Throwable t) {
			arg0.sendMessage(ChatColor.RED + "Internal Error: " + ChatColor.WHITE + t.toString());
			t.printStackTrace();
		}
		return false;
	}

}
