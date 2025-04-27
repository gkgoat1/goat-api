package at.gkgo.api.mixin;

import at.gkgo.api.component.PaletteSync;
import at.gkgo.api.util.Utils;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.component.ComponentChanges;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FluidBlock.class)
public class FluidBlockMixin {
    @WrapMethod(method = "tryDrainFluid")
    private ItemStack redrain(PlayerEntity player, WorldAccess world, BlockPos pos, BlockState state, Operation<ItemStack> original){
        var k = world.getChunk(pos);
        var a = ComponentChanges.builder();
        for(var e: PaletteSync.viewUniversals().entrySet()){
            var p = k.getAttachedOrCreate(e.getValue()).get(pos);
            if(p != null){
                a.add(e.getKey(),Utils.unsafeCoerce(p));
            }
        }
        var stack = original.call(player,world,pos,state);
        if(!stack.isEmpty()){
            stack.applyChanges(a.build());
        }
        return stack;
    }
}
