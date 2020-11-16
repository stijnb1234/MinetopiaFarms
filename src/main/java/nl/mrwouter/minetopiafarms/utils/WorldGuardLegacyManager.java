package nl.mrwouter.minetopiafarms.utils;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;

public class WorldGuardLegacyManager {

	private static String wgVerStr = null;
	private static WorldGuardLegacyManager instance = null;

	public static WorldGuardLegacyManager getInstance() {
		if (instance == null) {
			instance = new WorldGuardLegacyManager();
		}
		return instance;
	}

	public WorldGuardPlugin getWorldGuard() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		if (!(plugin instanceof WorldGuardPlugin)) {
			return null;
		}
		return (WorldGuardPlugin) plugin;
	}

	public RegionManager getRegionManager(World w) {
		if (getWgVer().contains("7.")) {
			try {
				Class<?> wgClass = Class.forName("com.sk89q.worldguard.WorldGuard");

				Object instance = wgClass.getDeclaredMethod("getInstance").invoke(null);
				Class<?> wgInstanceClass = instance.getClass();

				Object platform = wgInstanceClass.getDeclaredMethod("getPlatform").invoke(instance);
				Class<?> wgPlatformClass = platform.getClass();

				Object regionContainer = wgPlatformClass.getDeclaredMethod("getRegionContainer").invoke(platform);
				Class<?> wgRegionContainer = regionContainer.getClass();

				return (RegionManager) wgRegionContainer.getSuperclass()
						.getMethod("get", com.sk89q.worldedit.world.World.class)
						.invoke(regionContainer, new BukkitWorld(w));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			return getWorldGuard().getRegionManager(w);
		}
		return null;
	}

	public Object getBlockVectorV3(Location loc) throws Exception {
		Class<?> blockVector3 = Class.forName("com.sk89q.worldedit.math.BlockVector3");

		Method blockVectorAt = blockVector3.getDeclaredMethod("at", double.class, double.class, double.class);
		return blockVectorAt.invoke(null, loc.getX(), loc.getY(), loc.getZ());
	}

	public FlagRegistry getFlagRegistry() {
		if (getWgVer().contains("7.")) {
			try {
				Class<?> wgClass = Class.forName("com.sk89q.worldguard.WorldGuard");

				Object instance = wgClass.getDeclaredMethod("getInstance").invoke(null);
				Class<?> wgInstanceClass = instance.getClass();
				Method declaredMethod = wgInstanceClass.getDeclaredMethod("getFlagRegistry");
				return (FlagRegistry) declaredMethod.invoke(instance);
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		} else {
			return getWorldGuard().getFlagRegistry();
		}
	}
	
	public ApplicableRegionSet getApplicableRegionSet(Location loc) {
		if (getWgVer().contains("7.")) {
			try {
				RegionManager mngr = getRegionManager(loc.getWorld());

				Class<?> blockVector3 = Class.forName("com.sk89q.worldedit.math.BlockVector3");

				Method applicableRegions = mngr.getClass().getDeclaredMethod("getApplicableRegions", blockVector3);

				Method blockVectorAt = blockVector3.getDeclaredMethod("at", double.class, double.class, double.class);
				Object blockVector = blockVectorAt.invoke(null, loc.getX(), loc.getY(), loc.getZ());

				Object regionSet = applicableRegions.invoke(mngr, blockVector);
				
				return (ApplicableRegionSet) regionSet;
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		} else {
			return getRegionManager(loc.getWorld()).getApplicableRegions(new Vector(loc.getX(), loc.getY(), loc.getZ()));
		}
	}


	public String getWgVer() {
		if (wgVerStr == null) {
			wgVerStr = Bukkit.getPluginManager().getPlugin("WorldGuard").getDescription().getVersion();
		}
		return wgVerStr;
	}
}