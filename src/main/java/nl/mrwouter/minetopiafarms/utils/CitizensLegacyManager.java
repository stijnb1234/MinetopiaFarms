package nl.mrwouter.minetopiafarms.utils;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.trait.MobType;
import net.citizensnpcs.api.util.Colorizer;
import net.citizensnpcs.npc.skin.SkinnableEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.lang.reflect.Method;
import java.util.UUID;

public class CitizensLegacyManager {
    private static boolean useNewAPI;
    private static CitizensLegacyManager instance = null;

    public static CitizensLegacyManager getInstance() {
        if (instance == null) {
            instance = new CitizensLegacyManager();
        }
        return instance;
    }

    public boolean isNewAPI() {
        return useNewAPI;
    }

    public void spawnNPC(String name, String skin, Location spawnLoc) throws Exception {
        name = Colorizer.parseColors(name);

        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);
        if (!useNewAPI) {
            npc.data().set(NPC.PLAYER_SKIN_UUID_METADATA, skin);

            npc.spawn(spawnLoc);

            ((SkinnableEntity) npc.getEntity()).setSkinName(Bukkit.getOfflinePlayer(UUID.fromString(skin)).getName());

            npc.despawn(DespawnReason.PENDING_RESPAWN);
            npc.setName(name);
            npc.spawn(spawnLoc);
        } else {
            if (!npc.hasTrait(MobType.class)) {
                npc.addTrait(MobType.class);
            }
            npc.getTrait(MobType.class).setType(EntityType.PLAYER); //Force the player type

            npc.spawn(spawnLoc);

            Class<? extends Trait> traitClass = (Class<? extends Trait>) Class.forName("net.citizensnpcs.trait.SkinTrait");

            if (!npc.hasTrait(traitClass)) npc.addTrait(traitClass);

            Method method = npc.getClass().getMethod("getTrait", traitClass);
            Object trait = method.invoke(npc, traitClass);

            Method skinMethod = trait.getClass().getMethod("setSkinName", String.class, boolean.class);
            skinMethod.invoke(trait, skin, true);
        }
    }

    static {
        try {
            Class.forName("net.citizensnpcs.trait.SkinTrait");
            useNewAPI = true;
        } catch (ClassNotFoundException ex) {
            useNewAPI = false;
        }
    }
}
