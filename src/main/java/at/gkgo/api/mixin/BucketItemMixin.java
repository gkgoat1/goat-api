package at.gkgo.api.mixin;

import at.gkgo.api.component.PaletteSync;
import at.gkgo.api.util.Utils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BucketItem.class)
public class BucketItemMixin {
    @WrapOperation(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BucketItem;onEmptied(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/math/BlockPos;)V"))
    private void emitComponents(BucketItem instance, PlayerEntity player, World world, ItemStack stack, BlockPos pos, Operation<Void> original){
        for(var p: PaletteSync.viewUniversals().entrySet()){
            if(stack.contains(p.getKey())) {
                var k = world.getChunk(pos);
                var e = k.getAttachedOrCreate(p.getValue());
                e.put(pos, Utils.unsafeCoerce(stack.get(p.getKey())));
                k.setAttached(p.getValue(), Utils.unsafeCoerce(e));
            }
        }
        original.call(instance, player, world, stack, pos);
    }
}
