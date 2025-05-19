package net.eugenlukas.imersivecrafting.entity;

import net.eugenlukas.imersivecrafting.ImersiveCrafting;
import net.eugenlukas.imersivecrafting.entity.custom.CraftingSlotEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, ImersiveCrafting.MODID);

    public static final Supplier<EntityType<CraftingSlotEntity>> Custom_Crafting_Slot =
            ENTITY_TYPES.register("customcraftingslot", () -> EntityType.Builder
                    .<CraftingSlotEntity>of(CraftingSlotEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f) // kleiner Cube
                    .build("customcraftingslot"));

    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }
}
