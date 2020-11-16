package nl.mrwouter.minetopiafarms.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import nl.minetopiasdb.api.API;
import nl.mrwouter.minetopiafarms.Main;
import nl.mrwouter.minetopiafarms.utils.Utils;

public class NPCClickListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onNPCRightClick(NPCRightClickEvent e) {
		Player clicker = e.getClicker();
		NPC clicked = e.getNPC();
		if (Main.getMessage("NPC.Name").equals(clicked.getName())) {

			double paymentAmount = 0;
			for (ItemStack item : clicker.getInventory().getContents()) {
				if (item != null) {
					if (item.getType() == Material.BEETROOT) {
						if (clicker.getInventory().removeItem(item).size() == 0)
							paymentAmount = paymentAmount
									+ (Main.getPlugin().getConfig().getDouble("TerugverkoopPrijs.Boer.BEETROOTS")
											* item.getAmount());
					} else if (item.getType() == XMaterial.WHEAT.parseMaterial()) {
						if (clicker.getInventory().removeItem(item).size() == 0)
							paymentAmount = paymentAmount
									+ (Main.getPlugin().getConfig().getDouble("TerugverkoopPrijs.Boer.WHEAT")
											* item.getAmount());
					} else if (item.getType() == Utils.getMelonMaterial()) {
						if (clicker.getInventory().removeItem(item).size() == 0)
							paymentAmount = paymentAmount
									+ (Main.getPlugin().getConfig().getDouble("TerugverkoopPrijs.Boer.MELON")
											* item.getAmount());
					} else if (item.getType() == XMaterial.PUMPKIN.parseMaterial()) {
						if (clicker.getInventory().removeItem(item).size() == 0)
							paymentAmount = paymentAmount
									+ (Main.getPlugin().getConfig().getDouble("TerugverkoopPrijs.Boer.PUMPKIN")
											* item.getAmount());
					} else if (item.getType() == Utils.getCarrotItem()) {
						if (clicker.getInventory().removeItem(item).size() == 0)
							paymentAmount = paymentAmount
									+ (Main.getPlugin().getConfig().getDouble("TerugverkoopPrijs.Boer.CARROTS")
											* item.getAmount());
					} else if (item.getType() == Utils.getPotatoItem()) {
						if (clicker.getInventory().removeItem(item).size() == 0)
							paymentAmount = paymentAmount
									+ (Main.getPlugin().getConfig().getDouble("TerugverkoopPrijs.Boer.POTATOES")
											* item.getAmount());
					}
				}
			}

			for (ItemStack item : clicker.getInventory().getContents()) {
				if (item != null) {
					if (item.getType() == XMaterial.COAL_ORE.parseMaterial()) {
						if (clicker.getInventory().removeItem(item).size() == 0)
							paymentAmount = paymentAmount
									+ (Main.getPlugin().getConfig().getDouble("TerugverkoopPrijs.Mijnwerker.COAL_ORE")
											* item.getAmount());
					} else if (item.getType() == XMaterial.IRON_ORE.parseMaterial()) {
						if (clicker.getInventory().removeItem(item).size() == 0)
							paymentAmount = paymentAmount
									+ (Main.getPlugin().getConfig().getDouble("TerugverkoopPrijs.Mijnwerker.IRON_ORE")
											* item.getAmount());
					} else if (item.getType() == XMaterial.EMERALD_ORE.parseMaterial()) {
						if (clicker.getInventory().removeItem(item).size() == 0)
							paymentAmount = paymentAmount + (Main.getPlugin().getConfig()
									.getDouble("TerugverkoopPrijs.Mijnwerker.EMERALD_ORE") * item.getAmount());
					} else if (item.getType() == XMaterial.GOLD_ORE.parseMaterial()) {
						if (clicker.getInventory().removeItem(item).size() == 0)
							paymentAmount = paymentAmount
									+ (Main.getPlugin().getConfig().getDouble("TerugverkoopPrijs.Mijnwerker.GOLD_ORE")
											* item.getAmount());
					} else if (item.getType() == XMaterial.LAPIS_ORE.parseMaterial()) {
						if (clicker.getInventory().removeItem(item).size() == 0)
							paymentAmount = paymentAmount
									+ (Main.getPlugin().getConfig().getDouble("TerugverkoopPrijs.Mijnwerker.LAPIS_ORE")
											* item.getAmount());
					} else if (item.getType() == XMaterial.REDSTONE_ORE.parseMaterial()) {
						if (clicker.getInventory().removeItem(item).size() == 0)
							paymentAmount = paymentAmount + (Main.getPlugin().getConfig()
									.getDouble("TerugverkoopPrijs.Mijnwerker.REDSTONE_ORE") * item.getAmount());
					} else if (item.getType() == XMaterial.DIAMOND_ORE.parseMaterial()) {
						if (clicker.getInventory().removeItem(item).size() == 0)
							paymentAmount = paymentAmount + (Main.getPlugin().getConfig()
									.getDouble("TerugverkoopPrijs.Mijnwerker.DIAMOND_ORE") * item.getAmount());
					}
				}
			}

			for (ItemStack item : clicker.getInventory().getContents()) {
				if (item != null) {
					if (item.getType().toString().contains("LOG")) {
						if (clicker.getInventory().removeItem(item).size() == 0)
							paymentAmount = paymentAmount + (item.getAmount()
									* Main.getPlugin().getConfig().getInt("TerugverkoopPrijs.Houthakker"));
					}
				}
			}

			API.getEcon().depositPlayer(clicker, paymentAmount);
			clicker.sendMessage(
					Main.getMessage("GeldBetaald").replaceAll("<Bedrag>", Utils.formatMoney(paymentAmount)));
			API.updateScoreboard(clicker);

		}
	}
}