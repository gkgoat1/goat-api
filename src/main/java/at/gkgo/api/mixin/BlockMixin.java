package at.gkgo.api.mixin;

import at.gkgo.api.component.PaletteFrame;
import at.gkgo.api.component.PaletteSync;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Block.class)
public class BlockMixin {
    @WrapOperation(method =  "dropExperienceWhenMined", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getBlockExperience(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;I)I"))
    private int redropExperienceBasedOnBlockComponents(ServerWorld world, ItemStack stack, int baseBlockExperience, Operation<Integer> original, @Local(argsOnly = true) BlockPos blockPos){
        baseBlockExperience = original.call(world,stack,baseBlockExperience);
        var e = PaletteFrame.ROOT.getFromFrame(world,blockPos, PaletteSync.ENCHANTMENTS);
        if(e != null){
            MutableFloat f = new MutableFloat(baseBlockExperience);
            for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : e.getEnchantmentEntries()) {
                ((RegistryEntry<Enchantment>)entry.getKey()).value().modifyBlockExperience(world,entry.getIntValue(),stack,f);
            }
            baseBlockExperience = f.intValue();
        }
        return baseBlockExperience;
    }
}
