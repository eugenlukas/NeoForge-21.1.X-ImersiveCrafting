package net.eugenlukas.imersivecrafting.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class CraftingSlotEntity extends Animal {
    public final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return 1;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };

    public CraftingSlotEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return false;
    }

    @Override
    protected void registerGoals() {

    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected boolean isAffectedByFluids() {
        return false;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    public void clearContents() {
        inventory.setStackInSlot(0, ItemStack.EMPTY);
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;

        ItemStack stack = player.getItemInHand(hand);

        // --- Give item to entity ---
        if (inventory.getStackInSlot(0).isEmpty() && !stack.isEmpty()) {
            // Try to insert only one item (copy of 1)
            ItemStack toInsert = stack.copyWithCount(1);
            ItemStack remaining = inventory.insertItem(0, toInsert, false);

            // Only shrink if the item was accepted
            if (remaining.isEmpty()) {
                stack.shrink(1); // Remove 1 item from player's hand
                this.level().playSound(player, BlockPos.containing(vec), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 2f);
                return InteractionResult.SUCCESS;
            }

            // If insertion failed, do nothing
            return InteractionResult.PASS;
        }

        // --- Take item from entity ---
        else if (!inventory.getStackInSlot(0).isEmpty() && stack.isEmpty()) {
            ItemStack stackInEntity = inventory.getStackInSlot(0);
            if (!stackInEntity.isEmpty()) {
                player.setItemInHand(hand, stackInEntity.copy()); // Copy it so it's not the same reference
                clearContents();
                this.level().playSound(player, BlockPos.containing(vec), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 2f);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void tick() {
        super.tick();
        setNoGravity(true);
        setDeltaMovement(0, 0, 0);
        setPos(getX(), getY(), getZ());
    }
}
