package at.gkgo.api.mixin;

import at.gkgo.api.component.PaletteSync;
import at.gkgo.api.util.Utils;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockItem.class)
public class BlockItemMixin {
    @Inject(method = "copyComponentsToBlockEntity", at = @At("HEAD"))
    private static void copyMoreComponents(World world, BlockPos pos, ItemStack stack, CallbackInfo ci){
        var k = world.getChunk(pos);
        for(var u: PaletteSync.viewUniversals().entrySet()){
            if(stack.contains(u.getKey())) {
                var a = k.getAttached(u.getValue());
                a.put(pos, Utils.unsafeCoerce(stack.get(u.getKey())));
                k.setAttached(u.getValue(),Utils.unsafeCoerce(a));
            }
        }
    }
}
