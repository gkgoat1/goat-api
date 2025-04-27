package at.gkgo.api.mixin;

import at.gkgo.api.util.ServerPlayerEntityStorage;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @WrapMethod(method = "tick")
    private void reTick(Operation<Void> original){
        var old = ServerPlayerEntityStorage.playerEntity;
        ServerPlayerEntityStorage.playerEntity = (ServerPlayerEntity) (Object)this;
        try{
            original.call();
        }finally {
            ServerPlayerEntityStorage.playerEntity = old;
        }
    }
}
