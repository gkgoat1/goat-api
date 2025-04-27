package at.gkgo.api.mixin;


import at.gkgo.api.component.PaletteSync;
import at.gkgo.api.util.Utils;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class BlockStateMixin {
    @Shadow public abstract Block getBlock();

    @ModifyReturnValue(method = "getDroppedStacks", at = @At("RETURN"))
    private List<ItemStack> redropStacks(List<ItemStack> original, @Local(argsOnly = true) LootContextParameterSet.Builder builder){

        var o = builder.get(LootContextParameters.ORIGIN);
        var op = new BlockPos((int) Math.floor(o.getX()), (int) Math.floor(o.getY()), (int) Math.floor(o.getZ()));

        var w = builder.getWorld();
        var k = w.getChunk(op);

        for(var s: original){
            if(s.getItem() == getBlock().asItem()) {
                for(var e: PaletteSync.viewUniversals().entrySet()){
                    var p = k.getAttachedOrCreate(e.getValue()).get(op);
                    if(p != null){
                        s.set(e.getKey(), Utils.unsafeCoerce(p));
                    }
                }
            }
        }

        return original;
    }
}
