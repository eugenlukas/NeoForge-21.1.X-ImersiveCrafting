package net.eugenlukas.imersivecrafting.event;

import net.eugenlukas.imersivecrafting.ImersiveCrafting;
import net.eugenlukas.imersivecrafting.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = ImersiveCrafting.MODID, bus = EventBusSubscriber.Bus.GAME)
public class BlockPlaceHandler {
    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        Level world = (Level) event.getLevel();
        BlockPos pos = event.getPos();
        BlockState placedBlock = event.getPlacedBlock();

        if (placedBlock.getBlock() == Blocks.CRAFTING_TABLE) {
            BlockState customBlockState = ModBlocks.CUSTOMCRAFTINGTABLE.get().defaultBlockState();

            world.setBlock(pos, customBlockState, 3);
        }
    }
}
