package mod.elm.core;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import mod.elm.block.BlockCore;
import mod.elm.config.MyModConfig;
import mod.elm.core.proxy.ClientProxy;
import mod.elm.core.proxy.CommonProxy;
import mod.elm.creative.ItemGroupElm;
import mod.elm.effect.EffectElm;
import mod.elm.entity.EntityElmBrain;
import mod.elm.entity.EntityElmFoot;
import mod.elm.entity.EntityElmHand;
import mod.elm.entity.EntityElmIntestine;
import mod.elm.entity.EntityElmStomach;
import mod.elm.entity.EntityWanderingPeople;
import mod.elm.entity.elmgolem.EntityElmGolem;
import mod.elm.event.MyModEventHandler;
import mod.elm.gui.GuiMaergerPot;
import mod.elm.inventory.ContainerMaergerPot;
import mod.elm.item.ItemCore;
import mod.elm.network.MessageHandler;
import mod.elm.render.RenderElmBrain;
import mod.elm.render.RenderElmFoot;
import mod.elm.render.RenderElmHand;
import mod.elm.render.RenderElmIntestine;
import mod.elm.render.RenderElmStomach;
import mod.elm.render.RenderTileEntityElmHead;
import mod.elm.render.RenderTileEntityMaergerPot;
import mod.elm.render.RenderWanderingPeople;
import mod.elm.sound.SoundCore;
import mod.elm.tileentity.TileEntityElmHead;
import mod.elm.tileentity.TileEntityMaergerPot;
import mod.elm.util.SpawnBiomeInfo;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntitySpawnPlacementRegistry.PlacementType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ObjectHolder;

@Mod(ModCommon.MOD_ID)
public class Mod_Elm {
	public static Random rnd = new Random();
	public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
	public static final ItemGroup GROUPELM = new ItemGroupElm("elmitem");
	public static final DamageSource DamageSourceBrain = new DamageSource("elm_blain").setDamageIsAbsolute();

	public Mod_Elm() {
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupCommon);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(ContainerType.class, this::onContainerRegistry);

        // aコンフィグ読み込み
    	ModLoadingContext.get().
        registerConfig(
        		net.minecraftforge.fml.config.ModConfig.Type.COMMON,
        		MyModConfig.spec);

    	BlockCore.init();
    	ItemCore.init();

    	// iメッセージ登録
    	MessageHandler.register();

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new MyModEventHandler());

//		RegisterVillager();
//		RegisterVillagerTrade();

	}

    public static BiomeDictionary.Type[] toBiomeTypeArray(List<? extends String> strings) {
        BiomeDictionary.Type[] types = new BiomeDictionary.Type[strings.size()];
        for (int i = 0; i < strings.size(); i++) {
            String string = strings.get(i);
            types[i] = getType(string);
        }
        return types;
    }

    public static BiomeDictionary.Type getType(String name) {
        Map<String, BiomeDictionary.Type> byName = BiomeDictionary.Type.getAll().stream().collect(Collectors.toMap(BiomeDictionary.Type::getName, Function.identity()));
        name = name.toUpperCase();
        return byName.get(name);
    }
    private void setupCommon(FMLCommonSetupEvent event) {
    	List<SpawnBiomeInfo> info = MyModConfig.getInfo(EntityWanderingPeople.NAME);
    	for (SpawnBiomeInfo biome : info) {
    		biome.getBiome().getSpawns(EntityClassification.CREATURE).add(new SpawnListEntry(RegistryEvents.WANDERINGPEOPEL, biome.getWeight(), biome.getMin(), biome.getMax()));
    	}
    }

	private void doClientStuff(final FMLClientSetupEvent event) {
    	registRender();
    	guiHandler();
	}

	@OnlyIn(Dist.CLIENT)
	public void registRender(){
		RenderingRegistry.registerEntityRenderingHandler(EntityElmStomach.class, RenderElmStomach::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityElmIntestine.class, RenderElmIntestine::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityWanderingPeople.class, RenderWanderingPeople::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityElmHand.class, RenderElmHand::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityElmFoot.class, RenderElmFoot::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityElmBrain.class, RenderElmBrain::new);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityElmHead.class, new RenderTileEntityElmHead());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMaergerPot.class, new RenderTileEntityMaergerPot());
	}

	@OnlyIn(Dist.CLIENT)
	public void guiHandler(){
		ScreenManager.registerFactory(CONTAINER_MAERGERPOT, GuiMaergerPot::new);
	}

	@ObjectHolder(ModCommon.MOD_ID + ":" + ModCommon.CONTAINER_MAEGERPOT)
	public static ContainerType<ContainerMaergerPot> CONTAINER_MAERGERPOT;

    @SubscribeEvent
	public void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
		event.getRegistry().register(IForgeContainerType.create((wid, playerInv, extraData)->{
			int x = extraData.readInt();
			int y = extraData.readInt();
			int z = extraData.readInt();
			TileEntity ent = playerInv.player.world.getTileEntity(new BlockPos(x,y,z));
			if (ent instanceof TileEntityMaergerPot && !((TileEntityMaergerPot) ent).isRun()) {
				return new ContainerMaergerPot(wid, playerInv, ent);
			}
			return null;
		}).setRegistryName(ModCommon.MOD_ID + ":" + ModCommon.CONTAINER_MAEGERPOT));
	}

	@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
        	BlockCore.registerBlock(blockRegistryEvent);
        }

        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
            BlockCore.registerBlockItem(itemRegistryEvent);
            ItemCore.register(itemRegistryEvent);
        }


        public static EntityType<EntityWanderingPeople> WANDERINGPEOPEL;
        public static EntityType<EntityElmGolem> ELMGOLEM;
        public static EntityType<EntityElmStomach> STOMACH;
        public static EntityType<EntityElmIntestine> INTESTINE;
        public static EntityType<EntityElmHand> HAND;
        public static EntityType<EntityElmFoot> FOOT;
        public static EntityType<EntityElmBrain> BRAIN;
        @SubscribeEvent
        public static void onEntityRegistry(final RegistryEvent.Register<EntityType<?>> etRegistryEvent){
        	WANDERINGPEOPEL = EntityType.Builder.<EntityWanderingPeople>create(EntityWanderingPeople::new, EntityClassification.CREATURE)
        				.setTrackingRange(10).setUpdateInterval(1).setShouldReceiveVelocityUpdates(false).size(0.625F, 1.25F)
        				.setCustomClientFactory(EntityWanderingPeople::new).build(ModCommon.MOD_ID + ":" + EntityWanderingPeople.NAME);
        	WANDERINGPEOPEL.setRegistryName(new ResourceLocation(ModCommon.MOD_ID,EntityWanderingPeople.NAME));
        	etRegistryEvent.getRegistry().register(WANDERINGPEOPEL);
        	EntitySpawnPlacementRegistry.register(WANDERINGPEOPEL, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityWanderingPeople::spawan_place);

        	ELMGOLEM = EntityType.Builder.<EntityElmGolem>create(EntityElmGolem::new, EntityClassification.CREATURE)
    				.setTrackingRange(10).setUpdateInterval(1).setShouldReceiveVelocityUpdates(false).size(0.625F, 1.25F)
    				.setCustomClientFactory(EntityElmGolem::new).build(ModCommon.MOD_ID + ":" + EntityElmGolem.NAME);
        	ELMGOLEM.setRegistryName(new ResourceLocation(ModCommon.MOD_ID,EntityElmGolem.NAME));
        	etRegistryEvent.getRegistry().register(ELMGOLEM);

        	STOMACH = EntityType.Builder.<EntityElmStomach>create(EntityElmStomach::new, EntityClassification.MISC)
    				.setTrackingRange(10).setUpdateInterval(1).setShouldReceiveVelocityUpdates(false).size(0.5F, 0.5F)
    				.setCustomClientFactory(EntityElmStomach::new).build(ModCommon.MOD_ID + ":" + EntityElmStomach.NAME);
        	STOMACH.setRegistryName(new ResourceLocation(ModCommon.MOD_ID,EntityElmStomach.NAME));
        	etRegistryEvent.getRegistry().register(STOMACH);

        	INTESTINE = EntityType.Builder.<EntityElmIntestine>create(EntityElmIntestine::new, EntityClassification.MISC)
    				.setTrackingRange(10).setUpdateInterval(1).setShouldReceiveVelocityUpdates(false).size(0.5F, 0.5F)
    				.setCustomClientFactory(EntityElmIntestine::new).build(ModCommon.MOD_ID + ":" + EntityElmIntestine.NAME);
        	INTESTINE.setRegistryName(new ResourceLocation(ModCommon.MOD_ID,EntityElmIntestine.NAME));
        	etRegistryEvent.getRegistry().register(INTESTINE);


        	HAND = EntityType.Builder.<EntityElmHand>create(EntityElmHand::new, EntityClassification.MISC).immuneToFire()
    				.setTrackingRange(10).setUpdateInterval(1).setShouldReceiveVelocityUpdates(false).size(1.0F, 1.0F)
    				.setCustomClientFactory(EntityElmHand::new).build(ModCommon.MOD_ID + ":" + EntityElmHand.NAME);
        	HAND.setRegistryName(new ResourceLocation(ModCommon.MOD_ID,EntityElmHand.NAME));
        	etRegistryEvent.getRegistry().register(HAND);


        	FOOT = EntityType.Builder.<EntityElmFoot>create(EntityElmFoot::new, EntityClassification.MISC).immuneToFire()
    				.setTrackingRange(10).setUpdateInterval(1).setShouldReceiveVelocityUpdates(false).size(1.0F, 1.0F)
    				.setCustomClientFactory(EntityElmFoot::new).build(ModCommon.MOD_ID + ":" + EntityElmFoot.NAME);
        	FOOT.setRegistryName(new ResourceLocation(ModCommon.MOD_ID,EntityElmFoot.NAME));
        	etRegistryEvent.getRegistry().register(FOOT);

        	BRAIN = EntityType.Builder.<EntityElmBrain>create(EntityElmBrain::new, EntityClassification.MISC).immuneToFire()
    				.setTrackingRange(10).setUpdateInterval(1).setShouldReceiveVelocityUpdates(false).size(1.0F, 1.0F)
    				.setCustomClientFactory(EntityElmBrain::new).build(ModCommon.MOD_ID + ":" + EntityElmBrain.NAME);
        	BRAIN.setRegistryName(new ResourceLocation(ModCommon.MOD_ID,EntityElmBrain.NAME));
        	etRegistryEvent.getRegistry().register(BRAIN);
        }

    	public static TileEntityType<?> ELMHEAD;
    	public static TileEntityType<?> MAERGERPOT;
        @SubscribeEvent
        public static void onTERegistyr(final RegistryEvent.Register<TileEntityType<?>> teRegistryEvent){
        	ELMHEAD = TileEntityType.Builder.create(TileEntityElmHead::new, BlockCore.block_elmhead).build(null);
        	ELMHEAD.setRegistryName(ModCommon.MOD_ID, TileEntityElmHead.NAME);
        	teRegistryEvent.getRegistry().register(ELMHEAD);


        	MAERGERPOT = TileEntityType.Builder.create(TileEntityMaergerPot::new, BlockCore.block_maergerpot).build(null);
        	MAERGERPOT.setRegistryName(ModCommon.MOD_ID, TileEntityMaergerPot.NAME);
        	teRegistryEvent.getRegistry().register(MAERGERPOT);
        }


        public static final List<Effect> BLOOD_EFFECT = Lists.newArrayList();
        public static Effect EFFECT_BLOODCURSE_LIGHT;
        public static Effect EFFECT_BLOODCURSE_WATER;
        public static Effect EFFECT_BLOODCURSE_WARM;
        public static Effect EFFECT_BLOODCURSE_COLD;
        public static Effect EFFECT_BLOODREADOUT;
        public static Effect EFFECT_BLOODPARASITE;

        @SubscribeEvent
        public static void onEffect(final RegistryEvent.Register<Effect> event){
        	EFFECT_BLOODCURSE_LIGHT = new EffectElm(EffectType.HARMFUL,16726863)
        			.addAttributesModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "5a0e1389-9b4e-4cd8-9e84-2645778334c5", (double)-0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL)
					.addAttributesModifier(SharedMonsterAttributes.ATTACK_DAMAGE, "f3bce62d-afd0-4a37-8d94-d56194b862ea", 0.0D, AttributeModifier.Operation.ADDITION)
					.setRegistryName("elm_bloodcurse_light");
        	event.getRegistry().register(EFFECT_BLOODCURSE_LIGHT);

        	EFFECT_BLOODCURSE_WATER = new EffectElm(EffectType.HARMFUL,0x006881)
        			.addAttributesModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "b944e994-ef16-4a02-8b19-eb0bee817cb4", (double)-0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL)
					.addAttributesModifier(SharedMonsterAttributes.ATTACK_DAMAGE, "0c393c41-009b-460c-b50d-a05235a5bbe9", 0.0D, AttributeModifier.Operation.ADDITION)
					.setRegistryName("elm_bloodcurse_water");
        	event.getRegistry().register(EFFECT_BLOODCURSE_WATER);

        	EFFECT_BLOODCURSE_WARM = new EffectElm(EffectType.HARMFUL,0xCCC3A1)
        			.addAttributesModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "ec2397a5-706a-4a13-af29-1ad404a5b368", (double)-0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL)
					.addAttributesModifier(SharedMonsterAttributes.ATTACK_SPEED, "036aa4d5-a238-4076-bf6a-72b8b9d29e60", (double)-0.05F, AttributeModifier.Operation.MULTIPLY_TOTAL)
					.setRegistryName("elm_bloodcurse_warm");
        	event.getRegistry().register(EFFECT_BLOODCURSE_WARM);

        	EFFECT_BLOODCURSE_COLD = new EffectElm(EffectType.HARMFUL,0xCCC3A1)
        			.addAttributesModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "cda5ba4a-525f-4223-b4c9-ec94968c3193", (double)-0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL)
					.addAttributesModifier(SharedMonsterAttributes.ATTACK_SPEED, "f61d449e-a245-4cdf-a335-ba4bfed0ac26", (double)-0.05F, AttributeModifier.Operation.MULTIPLY_TOTAL)
					.setRegistryName("elm_bloodcurse_cold");
        	event.getRegistry().register(EFFECT_BLOODCURSE_COLD);

        	EFFECT_BLOODREADOUT = new EffectElm(EffectType.HARMFUL, 0xDC143C).setRegistryName("elm_bloodredout");
        	event.getRegistry().register(EFFECT_BLOODREADOUT);

        	EFFECT_BLOODPARASITE = new EffectElm(EffectType.NEUTRAL,0xB1C6AE)
					.setRegistryName("elm_bloodparasite");
        	event.getRegistry().register(EFFECT_BLOODPARASITE);
        	BLOOD_EFFECT.clear();
        	BLOOD_EFFECT.add(EFFECT_BLOODCURSE_LIGHT);
        	BLOOD_EFFECT.add(EFFECT_BLOODCURSE_WATER);
        	BLOOD_EFFECT.add(EFFECT_BLOODCURSE_WARM);
        	BLOOD_EFFECT.add(EFFECT_BLOODCURSE_COLD);
        	BLOOD_EFFECT.add(EFFECT_BLOODREADOUT);
        	BLOOD_EFFECT.add(EFFECT_BLOODPARASITE);
        }

        @SubscribeEvent
        public static void onSoundRegistyr(final RegistryEvent.Register<SoundEvent> soRegistryEvent){
        	SoundCore.register(soRegistryEvent);
        }
	}
}
