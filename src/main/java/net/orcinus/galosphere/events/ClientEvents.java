package net.orcinus.galosphere.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.HorseRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.orcinus.galosphere.Galosphere;
import net.orcinus.galosphere.client.gui.CombustionTableScreen;
import net.orcinus.galosphere.client.model.SparkleModel;
import net.orcinus.galosphere.client.model.SterlingArmorModel;
import net.orcinus.galosphere.client.particles.AuraEmissionParticle;
import net.orcinus.galosphere.client.particles.AuraParticle;
import net.orcinus.galosphere.client.particles.providers.SilverBombProvider;
import net.orcinus.galosphere.client.particles.providers.WarpedProvider;
import net.orcinus.galosphere.client.renderer.SparkleRenderer;
import net.orcinus.galosphere.client.renderer.layer.BannerLayer;
import net.orcinus.galosphere.client.renderer.layer.HorseBannerLayer;
import net.orcinus.galosphere.init.CTBlocks;
import net.orcinus.galosphere.init.CTEntityTypes;
import net.orcinus.galosphere.init.CTItems;
import net.orcinus.galosphere.init.CTMenuTypes;
import net.orcinus.galosphere.init.CTModelLayers;
import net.orcinus.galosphere.init.CTParticleTypes;

@Mod.EventBusSubscriber(modid = Galosphere.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent 
    public static void onClientSetup(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(CTBlocks.ALLURITE_CLUSTER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(CTBlocks.LUMIERE_CLUSTER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(CTBlocks.MYSTERIA_VINES.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(CTBlocks.MYSTERIA_VINES_PLANTS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(CTBlocks.STIFFENED_ROOTS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(CTBlocks.STIFFENED_ROOTS_PLANTS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(CTBlocks.WARPED_ANCHOR.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(CTBlocks.FLUTTER_FROND.get(), RenderType.cutout());

        MenuScreens.register(CTMenuTypes.COMBUSTION_TABLE.get(), CombustionTableScreen::new);

    }

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        HorseRenderer horseRenderer = event.getRenderer(EntityType.HORSE);
        horseRenderer.addLayer(new HorseBannerLayer(horseRenderer));
        event.getSkins().forEach(skin -> {
            PlayerRenderer playerRenderer = event.getSkin(skin);
            playerRenderer.addLayer(new BannerLayer<>(playerRenderer));
        });
    }

    @SubscribeEvent
    public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CTModelLayers.SPARKLE, SparkleModel::createBodyLayer);
        event.registerLayerDefinition(CTModelLayers.STERLING_HELMET, SterlingArmorModel::createArmorLayer);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(CTEntityTypes.SPARKLE.get(), SparkleRenderer::new);
        event.registerEntityRenderer(CTEntityTypes.SIVLER_BOMB.get(), context -> new ThrownItemRenderer<>(context, 1.5F, false));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void init(ParticleFactoryRegisterEvent event) {
        ParticleEngine engine = Minecraft.getInstance().particleEngine;
        engine.register(CTParticleTypes.AURA_LISTENER.get(), AuraParticle.Provider::new);
        engine.register(CTParticleTypes.AURA_EMISSION.get(), AuraEmissionParticle.Provider::new);
        engine.register(CTParticleTypes.SILVER_BOMB.get(), new SilverBombProvider());
        engine.register(CTParticleTypes.WARPED.get(), WarpedProvider::new);
    }

}
