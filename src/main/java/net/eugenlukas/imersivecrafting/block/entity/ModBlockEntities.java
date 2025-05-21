package net.eugenlukas.imersivecrafting.block.entity;

import net.eugenlukas.imersivecrafting.ImersiveCrafting;
import net.eugenlukas.imersivecrafting.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ImersiveCrafting.MODID);

    public static final Supplier<BlockEntityType<CustomCraftingTableBlockEntity>> CUSTOMCRAFTINGTABLEBLOCK_BE =
            BLOCK_ENTITIES.register("craftingtableblock_be", () -> BlockEntityType.Builder.of(
                    CustomCraftingTableBlockEntity::new, ModBlocks.CUSTOMCRAFTINGTABLE.get()).build(null));

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
