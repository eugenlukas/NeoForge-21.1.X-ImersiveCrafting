package net.eugenlukas.imersivecrafting.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.eugenlukas.imersivecrafting.block.entity.CustomCraftingTableBlockEntity;
import net.eugenlukas.imersivecrafting.entity.ModEntities;
import net.eugenlukas.imersivecrafting.entity.custom.CraftingSlotEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

import java.util.List;
import java.util.UUID;

public class CustomCraftingTableBlockEntityRenderer implements BlockEntityRenderer<CustomCraftingTableBlockEntity> {
    public CustomCraftingTableBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(CustomCraftingTableBlockEntity blockEntity, float pPartialTick, PoseStack poseStack, MultiBufferSource bufferSource, int pPackedLight, int pPackedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        List<UUID> slotEntitiyUUIDs = blockEntity.getSlots();

        for (UUID slotEntityUUID : slotEntitiyUUIDs) {
            CraftingSlotEntity slotEntity = (CraftingSlotEntity) ((ServerLevel) blockEntity.getLevel()).getEntity(slotEntityUUID);
            ItemStack stack = slotEntity.inventory.getStackInSlot(0);
            if (stack.isEmpty())
                continue;

            poseStack.pushPose();
            poseStack.translate(slotEntity.getX(), slotEntity.getY(), slotEntity.getZ());
            poseStack.scale(0.5f, 0.5f, 0.5f); // Optional scale
            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED,
                    getLightLevel(blockEntity.getLevel(), blockEntity.getBlockPos()),
                    OverlayTexture.NO_OVERLAY, poseStack, bufferSource, null, 0);

            poseStack.popPose();
        }
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
