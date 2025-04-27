package at.gkgo.api.util;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.entity.player.PlayerEntity;

public class Utils {
    public static int burn(ItemVariant itemVariant){
        return FuelRegistry.INSTANCE.get(itemVariant.getItem());
    }
    public static boolean isAdventure(PlayerEntity playerEntity){
        return !playerEntity.canModifyBlocks();
    }
    public static <T,U> U unsafeCoerce(T value){
        return (U)value;
    }
    private Utils(){

    }
}
