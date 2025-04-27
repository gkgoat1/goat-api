package at.gkgo.api.mixin;

import at.gkgo.api.hook.BlockApiLookupHook;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.impl.lookup.block.BlockApiLookupImpl;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

@Mixin(BlockApiLookupImpl.class)
public class BlockApiLookupImplMixin<A,C> implements BlockApiLookupHook<A,C> {
    private final List<BlockApiLookup.BlockApiProvider<Function<A,A>,C>> appends = new CopyOnWriteArrayList<>();
    @Override
    public void registerAppend(BlockApiLookup.BlockApiProvider<Function<A, A>, C> append) {
        appends.addFirst(append);
    }
    @WrapMethod(method = "find")
    private @Nullable A refind(World world, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity blockEntity, C context, Operation<A> original){
        A old = original.call(world, pos, state, blockEntity, context);
        for(var a: appends){
            var f = a.find(world,pos,state,blockEntity,context);
            if(f != null) {
                old = f.apply(old);
            }
        }
        return old;
    }
}
