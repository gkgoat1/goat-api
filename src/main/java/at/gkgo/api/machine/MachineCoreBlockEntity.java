package at.gkgo.api.machine;

import at.gkgo.api.component.PaletteFrame;
import at.gkgo.api.component.PaletteSyncedBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class MachineCoreBlockEntity extends PaletteSyncedBlockEntity {
    public MachineCoreBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, PaletteFrame frame) {
        super(type, pos, state,frame);
    }
}
