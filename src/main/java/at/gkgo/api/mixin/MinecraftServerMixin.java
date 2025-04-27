package at.gkgo.api.mixin;

import at.gkgo.api.util.ServerContext;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @WrapMethod(method = "runServer")
    private void rerun(Operation<Void> original){
        var old = ServerContext.server;
        ServerContext.server = (MinecraftServer) (Object)this;
        try{
            original.call();
        }finally {
            ServerContext.server = old;
        }
    }
}
