package net.eugenlukas.imersivecrafting.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.eugenlukas.imersivecrafting.ImersiveCrafting;
import net.eugenlukas.imersivecrafting.entity.custom.CraftingSlotEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class CraftingSlotEntityRenderer extends MobRenderer<CraftingSlotEntity, CraftingSlotEntityModel<CraftingSlotEntity>> {
    public CraftingSlotEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new CraftingSlotEntityModel<>(context.bakeLayer(CraftingSlotEntityModel.LAYER_LOCATION)), 0f);
    }

    @Override
    public ResourceLocation getTextureLocation(CraftingSlotEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(ImersiveCrafting.MODID, "textures/entity/slot/slot.png");
    }

    @Override
    protected @Nullable RenderType getRenderType(CraftingSlotEntity entity, boolean bodyVisible, boolean translucent, boolean glowing) {
        return RenderType.entityTranslucent(getTextureLocation(entity));
    }

    @Override
    public void render(CraftingSlotEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.scale(1f, 1f, 1f);

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
