package at.gkgo.api;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Goatapi implements ModInitializer {
	public static final String MOD_ID = "goat-api";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	public static AttachmentType<ChunkBlockPosPalette<DataComponentPatch>> PALETTE = AttachmentRegistry.<ChunkBlockPosPalette<DataComponentPatch>>builder().initializer(ChunkBlockPosPalette::new).persistent(ChunkBlockPosPalette.codec(DataComponentPatch.CODEC)).syncWith(ChunkBlockPosPalette.packetCodec(DataComponentPatch.STREAM_CODEC), (a, b) -> true).buildAndRegister(id("block_components"));


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
	}
}