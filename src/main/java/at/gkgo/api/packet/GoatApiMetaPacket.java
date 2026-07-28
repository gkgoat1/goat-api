package at.gkgo.api.packet;

import at.gkgo.api.Goatapi;
import net.fabricmc.fabric.api.networking.v1.*;
import net.fabricmc.fabric.impl.networking.payload.PayloadHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import net.minecraft.util.ExtraCodecs;

import java.util.List;

public record GoatApiMetaPacket(byte[] data) implements CustomPacketPayload {
    public static final Identifier PAYLOAD_ID = Goatapi.id("meta");

    public static final CustomPacketPayload.Type<GoatApiMetaPacket> TYPE = new CustomPacketPayload.Type<>(PAYLOAD_ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, GoatApiMetaPacket> REGISTRY_CODEC = StreamCodec.composite(ByteBufCodecs.BYTE_ARRAY, GoatApiMetaPacket::data, GoatApiMetaPacket::new);

    public static final StreamCodec<FriendlyByteBuf, GoatApiMetaPacket> CODEC = StreamCodec.composite(ByteBufCodecs.BYTE_ARRAY, GoatApiMetaPacket::data, GoatApiMetaPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    static {
        PayloadTypeRegistry.clientboundPlay().register(TYPE, REGISTRY_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(TYPE, REGISTRY_CODEC);
        PayloadTypeRegistry.clientboundConfiguration().register(TYPE, CODEC);
        PayloadTypeRegistry.serverboundConfiguration().register(TYPE, CODEC);

        ServerConfigurationNetworking.registerGlobalReceiver(GoatApiMetaPacket.TYPE, (payload, context) -> {});
        ServerPlayNetworking.registerGlobalReceiver(GoatApiMetaPacket.TYPE, (payload, context) -> {});
    }
}
