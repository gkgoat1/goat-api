package at.gkgo.api.component;

import at.gkgo.api.Goatapi;
import at.gkgo.api.util.ChunkBlockPosPalette;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.Identifier;

public class PaletteSync {
    private static BiMap<ComponentType<?>, AttachmentType<? extends ChunkBlockPosPalette<?>>> universals = HashBiMap.create();
    public static <T> AttachmentType<ChunkBlockPosPalette<T>> createComponentMap(Identifier id, Codec<T> codec, PacketCodec<RegistryByteBuf,T> packetCodec){
        return AttachmentRegistry.<ChunkBlockPosPalette<T>>builder().initializer(ChunkBlockPosPalette::new).persistent(ChunkBlockPosPalette.codec(codec)).syncWith(ChunkBlockPosPalette.packetCodec(packetCodec), AttachmentSyncPredicate.all()).buildAndRegister(id);
    }
    public static <T> void registerUniversal(ComponentType<T> componentType, AttachmentType<ChunkBlockPosPalette<T>> attachmentType){
        universals.put(componentType,attachmentType);
    }
    public static <T> ComponentType<T> getUniversalComponent(AttachmentType<ChunkBlockPosPalette<T>> attachmentType){
        return (ComponentType<T>) universals.inverse().get(attachmentType);
    }
    public static <T> AttachmentType<ChunkBlockPosPalette<T>>  getUniversalAttachment(ComponentType<T> componentType){
        return (AttachmentType<ChunkBlockPosPalette<T>>) universals.get(componentType);
    }
    public static BiMap<ComponentType<?>, AttachmentType<? extends ChunkBlockPosPalette<?>>> viewUniversals(){
        return universals;
    }
    public static AttachmentType<ChunkBlockPosPalette<ItemEnchantmentsComponent>> ENCHANTMENTS = createComponentMap(Identifier.of(Goatapi.MOD_ID,"enchantments"), ItemEnchantmentsComponent.CODEC,ItemEnchantmentsComponent.PACKET_CODEC);
    static{
        registerUniversal(DataComponentTypes.ENCHANTMENTS,ENCHANTMENTS);
    }
    public static void init(){

    }
}
