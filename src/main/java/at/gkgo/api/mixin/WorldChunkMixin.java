package at.gkgo.api.mixin;

import at.gkgo.api.component.PaletteSync;
import at.gkgo.api.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldChunk.class)
public class WorldChunkMixin {
    @Inject(method = "setBlockState", at = @At("RETURN"))
    private void resetBlockState(BlockPos pos, BlockState state, boolean moved, CallbackInfoReturnable<BlockState> cir){
        WorldChunk x = (WorldChunk) (Object)this;
        for(var p: PaletteSync.viewUniversals().entrySet()){
            var a = x.getAttachedOrCreate(p.getValue());
            a.remove(pos);
            x.setAttached(p.getValue(), Utils.unsafeCoerce(a));
        }
    }
}
