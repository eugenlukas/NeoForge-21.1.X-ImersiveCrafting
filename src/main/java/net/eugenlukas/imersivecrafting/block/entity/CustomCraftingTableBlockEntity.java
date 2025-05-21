package net.eugenlukas.imersivecrafting.block.entity;

import net.eugenlukas.imersivecrafting.entity.ModEntities;
import net.eugenlukas.imersivecrafting.entity.custom.CraftingSlotEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomCraftingTableBlockEntity extends BlockEntity {
    private final List<UUID> slotEntityUUIDs = new ArrayList<>();

    public CustomCraftingTableBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.CUSTOMCRAFTINGTABLEBLOCK_BE.get(), pos, blockState);
    }

    public void drops() {
        System.out.println("Custom crafting table drop behavior not inplementet!");
    }

    public void spawnCraftingSlots(Level level, BlockPos pos) {
        double slotSpacing = 0.2; // Abstand zwischen den Slots
        double startX = pos.getX() + 0.3;
        double startZ = pos.getZ() + 0.35;
        double y = pos.getY() + 0.95; // leicht über dem Block

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                double x = startX + col * slotSpacing;
                double z = startZ + row * slotSpacing;
                CraftingSlotEntity slotEntity = new CraftingSlotEntity(ModEntities.Custom_Crafting_Slot.get(), level);
                slotEntity.setPos(x, y, z);
                level.addFreshEntity(slotEntity);
                slotEntityUUIDs.add(slotEntity.getUUID());
            }
        }
    }

    public void deleteCraftingSlots() {
        System.out.println("List size: " + slotEntityUUIDs.size());
        for (UUID uuid : slotEntityUUIDs) {
            CraftingSlotEntity slot = (CraftingSlotEntity) ((ServerLevel) level).getEntity(uuid);
            slot.remove(Entity.RemovalReason.KILLED);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        // Save UUIDs as list of strings
        var uuidList = new net.minecraft.nbt.ListTag();
        for (UUID uuid : slotEntityUUIDs) {
            uuidList.add(net.minecraft.nbt.StringTag.valueOf(uuid.toString()));
        }
        tag.put("SlotEntityUUIDs", uuidList);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        slotEntityUUIDs.clear();

        if (tag.contains("SlotEntityUUIDs")) {
            var uuidList = tag.getList("SlotEntityUUIDs", net.minecraft.nbt.Tag.TAG_STRING);
            for (int i = 0; i < uuidList.size(); i++) {
                try {
                    slotEntityUUIDs.add(UUID.fromString(uuidList.getString(i)));
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid UUID string in NBT: " + uuidList.getString(i));
                }
            }
        }
    }
}
