package net.eugenlukas.imersivecrafting.event;

import net.eugenlukas.imersivecrafting.ImersiveCrafting;
import net.eugenlukas.imersivecrafting.entity.ModEntities;
import net.eugenlukas.imersivecrafting.entity.client.CraftingSlotEntityModel;
import net.eugenlukas.imersivecrafting.entity.custom.CraftingSlotEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = ImersiveCrafting.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CraftingSlotEntityModel.LAYER_LOCATION, CraftingSlotEntityModel::createMainLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.Custom_Crafting_Slot.get(), CraftingSlotEntity.createMobAttributes().build());
    }
}
