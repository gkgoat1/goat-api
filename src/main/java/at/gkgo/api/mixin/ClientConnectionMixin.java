package at.gkgo.api.mixin;

import at.gkgo.api.util.ServerPlayerEntityStorage;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @WrapMethod(method = "handlePacket")
    private static void wrapPacket(Packet<?> packet, PacketListener listener, Operation<Void> original){
        if(listener instanceof ServerPlayNetworkHandler handler){
            var old = ServerPlayerEntityStorage.playerEntity;
            ServerPlayerEntityStorage.playerEntity = handler.player;
            try{
                original.call(packet,listener);
            }finally {
                ServerPlayerEntityStorage.playerEntity = old;
            }
        }else{
            original.call(packet,listener);
        }
    }
}
