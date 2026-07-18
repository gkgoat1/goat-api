package at.gkgo.api.packet;

import at.gkgo.api.Goatapi;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

import java.util.List;

public record GoatApiMetaPacket(byte[] data) implements CustomPacketPayload {
    public static final Identifier PAYLOAD_ID = Goatapi.id("meta");

    public static final CustomPacketPayload.Type<GoatApiMetaPacket> TYPE = new CustomPacketPayload.Type<>(PAYLOAD_ID);

    public static final StreamCodec<FriendlyByteBuf, GoatApiMetaPacket> CODEC = StreamCodec.composite(ByteBufCodecs.BYTE_ARRAY, GoatApiMetaPacket::data, GoatApiMetaPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    static {
        PayloadTypeRegistry.clientboundPlay().register(TYPE, CODEC);
        PayloadTypeRegistry.serverboundPlay().register(TYPE, CODEC);
        PayloadTypeRegistry.clientboundConfiguration().register(TYPE, CODEC);
        PayloadTypeRegistry.serverboundConfiguration().register(TYPE, CODEC);
    }
}
