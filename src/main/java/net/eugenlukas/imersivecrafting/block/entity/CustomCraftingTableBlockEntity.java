package net.eugenlukas.imersivecrafting.block.entity;

import net.eugenlukas.imersivecrafting.entity.ModEntities;
import net.eugenlukas.imersivecrafting.entity.custom.CraftingSlotEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        System.out.println("spawn slots");
        double slotSpacing = 0.2; // Distance between the slots
        double startX = pos.getX() + 0.3;
        double startZ = pos.getZ() + 0.3;
        double y = pos.getY() + 0.85; // Little over the block

        int i = 1;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                double x = startX + col * slotSpacing;
                double z = startZ + row * slotSpacing;
                CraftingSlotEntity slotEntity = new CraftingSlotEntity(ModEntities.Custom_Crafting_Slot.get(), level);
                slotEntity.setPos(x, y, z);
                //slotEntity.setCustomName(Component.literal(Integer.toString(i)));
                //slotEntity.setCustomNameVisible(true);
                level.addFreshEntity(slotEntity);
                slotEntityUUIDs.add(slotEntity.getUUID());
                i++;
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

    public void tryCraft() {
        if (level.isClientSide()) return; // only run on the server side

        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            CraftingSlotEntity slotEntity = (CraftingSlotEntity) ((ServerLevel) level).getEntity(slotEntityUUIDs.get(i));
            ItemStack stack = slotEntity.inventory.getStackInSlot(0);
            if (stack != null)
                items.add(i, slotEntity.inventory.getStackInSlot(0));
        }

        CraftingInput input = CraftingInput.of(3, 3, items);

        Optional<RecipeHolder<CraftingRecipe>> recipe = level.getServer().getRecipeManager()
                .getRecipeFor(RecipeType.CRAFTING, input, level);

        if (recipe.isPresent()) {
            ItemStack result = recipe.get().value().assemble(input, level.registryAccess());

            BlockPos dropPos = this.getBlockPos();
            double x = dropPos.getX();
            double y = dropPos.getY();
            double z = dropPos.getZ() + 0.95f;

            ItemEntity itemEntity = new ItemEntity(level, x, y, z, result.copy());
            itemEntity.setDefaultPickUpDelay();

            level.addFreshEntity(itemEntity);

            for (UUID uuid : slotEntityUUIDs) {
                CraftingSlotEntity slotEntity = (CraftingSlotEntity) ((ServerLevel) level).getEntity(uuid);
                slotEntity.clearContents();
            }
        }
        else System.out.println("No recipe found!");
    }

    public List<UUID> getSlots() {
        return slotEntityUUIDs;
    }
}
