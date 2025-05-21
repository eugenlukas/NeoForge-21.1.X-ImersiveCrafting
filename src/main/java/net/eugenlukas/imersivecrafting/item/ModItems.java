package net.eugenlukas.imersivecrafting.item;

import net.eugenlukas.imersivecrafting.ImersiveCrafting;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ImersiveCrafting.MODID);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
