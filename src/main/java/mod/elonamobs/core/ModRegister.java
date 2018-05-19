package mod.fbd.core;

import mod.fbd.block.BlockCore;
import mod.fbd.entity.EntityBurret;
import mod.fbd.entity.ProfessionGunSmith;
import mod.fbd.entity.mob.EntityBladeSmith;
import mod.fbd.item.ItemCore;
import mod.fbd.item.ItemTamahagane.EnumTamahagane;
import mod.fbd.network.MessageBladeforgeSmeltingStart;
import mod.fbd.network.MessageCreateBlade;
import mod.fbd.network.MessageRepairBlade;
import mod.fbd.network.MessageSetRunAirPomp;
import mod.fbd.network.MessageSetRunBladeforge;
import mod.fbd.network.Message_BladeLevelUpdate;
import mod.fbd.world.village.ComponentVillageGunSmithHouse;
import mod.fbd.world.village.VillageCrationHandler;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import net.minecraftforge.fml.relauncher.Side;

public class ModRegister {
	public static void RegisterBlock(FMLPreInitializationEvent event) {
		BlockCore.register(event);
	}

	public static void RegisterItem(FMLPreInitializationEvent event){
		ItemCore.register(event);
	}

	public static void RegisterEntity(CommonProxy proxy){
		EntityRegistry.registerModEntity(EntityBladeSmith.loot, EntityBladeSmith.class, EntityBladeSmith.NAME, EntityBladeSmith.REGISTERID, Mod_FantomBlade.instance, 10, 1, false);
		EntityRegistry.registerModEntity(new ResourceLocation(ModCommon.MOD_ID + ":" + EntityBurret.NAME), EntityBurret.class, EntityBurret.NAME, EntityBurret.REGISTERID, Mod_FantomBlade.instance, 10, 1, false);

		// タイルエンティティはserverとclientで登録方法が違う為プロキシで分ける
		proxy.registerTileEntity();
	}

	public static void RegisterRender(CommonProxy proxy){
		// レンダーはクライアントの未登録
		proxy.registerRender();
	}

	public static void RegisterRecipe(){
		// 鉄
		GameRegistry.addSmelting(new ItemStack(ItemCore.item_tamahagane,1,EnumTamahagane.SECOND_GRADE.getIndex()),new ItemStack(Items.IRON_INGOT,4),0.1f);
		GameRegistry.addSmelting(new ItemStack(ItemCore.item_tamahagane,1,EnumTamahagane.LOWEST_GRADE.getIndex()),new ItemStack(Items.IRON_INGOT,1),0.1f);
	}

	public static void RegisterMessage(){
		Mod_FantomBlade.Net_Instance.registerMessage(MessageBladeforgeSmeltingStart.class, MessageBladeforgeSmeltingStart.class, ModCommon.MESID_BLADEFORGESTARTSMELTING, Side.SERVER);
		Mod_FantomBlade.Net_Instance.registerMessage(MessageSetRunAirPomp.class,MessageSetRunAirPomp.class,ModCommon.MESID_SETRUNAIRPOMP,Side.CLIENT);
		Mod_FantomBlade.Net_Instance.registerMessage(MessageSetRunBladeforge.class,MessageSetRunBladeforge.class,ModCommon.MESID_SETRUNBLADEFORGE,Side.CLIENT);
		Mod_FantomBlade.Net_Instance.registerMessage(MessageCreateBlade.class,MessageCreateBlade.class,ModCommon.MESID_CREATEBLADE,Side.SERVER);
		Mod_FantomBlade.Net_Instance.registerMessage(MessageRepairBlade.class,MessageRepairBlade.class,ModCommon.MESID_REPAIREBLADE,Side.SERVER);
		Mod_FantomBlade.Net_Instance.registerMessage(Message_BladeLevelUpdate.class,Message_BladeLevelUpdate.class,ModCommon.MESID_BLADELEVELUPDATE, Side.SERVER);
	}

	public static void RegisterSounds(){
		ForgeRegistries.SOUND_EVENTS.register(SoundManager.sound_bladesmith_hart);
		ForgeRegistries.SOUND_EVENTS.register(SoundManager.sound_bladesmith_damage);
		ForgeRegistries.SOUND_EVENTS.register(SoundManager.sound_bladesmith_dead);
		ForgeRegistries.SOUND_EVENTS.register(SoundManager.sound_bladesmith_work);
		ForgeRegistries.SOUND_EVENTS.register(SoundManager.sound_gun_gunshot);
		ForgeRegistries.SOUND_EVENTS.register(SoundManager.sound_gun_reload);
		ForgeRegistries.SOUND_EVENTS.register(SoundManager.sound_gun_noburret);
	}


	public static void RegisterEvent(){
		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	public static final ResourceLocation VILLAGER_GUNSMITH_NAME = new ResourceLocation(ModCommon.MOD_ID,"villagergunsmith");
	public static final ResourceLocation VILLAGER_GUNSMITH_TEXTURE = new ResourceLocation(ModCommon.MOD_ID,"textures/entity/villagergunsmith.png");
	public static final ResourceLocation VILLAGER_GUNSMITHZOMBIE_TEXTURE = new ResourceLocation(ModCommon.MOD_ID,"textures/entity/villagergunsmithzombi.png");
	public static VillagerProfession gunSmith;
	public static void RegisterVillager() {
		gunSmith = new ProfessionGunSmith(VILLAGER_GUNSMITH_NAME.toString(), VILLAGER_GUNSMITH_TEXTURE.toString(), VILLAGER_GUNSMITHZOMBIE_TEXTURE.toString());
		VillagerCareer carry_gunsmith = new VillagerCareer(gunSmith, "villager.gunsmith");
		carry_gunsmith.addTrade(1, new EntityVillager.ITradeList[]{
				new EntityVillager.EmeraldForItems(ItemCore.item_steel, new EntityVillager.PriceInfo(8,12)),
				new EntityVillager.EmeraldForItems(Items.IRON_INGOT, new EntityVillager.PriceInfo(12,16)),
				new EntityVillager.ListItemForEmeralds(Items.IRON_INGOT, new EntityVillager.PriceInfo(-4,-6)),
				new EntityVillager.ListItemForEmeralds(ItemCore.item_steel, new EntityVillager.PriceInfo(-2,-6))
		});
		carry_gunsmith.addTrade(2, new EntityVillager.ITradeList[]{
				new EntityVillager.ListItemForEmeralds(ItemCore.item_revolver, new EntityVillager.PriceInfo(-30,-40)),
				new EntityVillager.ListItemForEmeralds(ItemCore.item_autmatic, new EntityVillager.PriceInfo(-30,-40)),
				new EntityVillager.ListItemForEmeralds(ItemCore.item_cartridge, new EntityVillager.PriceInfo(-10,-15))
		});
		carry_gunsmith.addTrade(3, new EntityVillager.ITradeList[]{
				new EntityVillager.ListItemForEmeralds(new ItemStack(BlockCore.block_guncustomizer), new EntityVillager.PriceInfo(-40,-64))
		});
		VillagerCareer carry_burretsmith = new VillagerCareer(gunSmith, "villager.burretsmith");
		carry_burretsmith.addTrade(1, new EntityVillager.ITradeList[]{
				new EntityVillager.EmeraldForItems(Items.GUNPOWDER, new EntityVillager.PriceInfo(6,10)),
				new EntityVillager.ListItemForEmeralds(Items.GUNPOWDER, new EntityVillager.PriceInfo(-4,-6)),
				new EntityVillager.ListItemForEmeralds(ItemCore.item_burret, new EntityVillager.PriceInfo(-12,-24))
		});
		carry_burretsmith.addTrade(2, new EntityVillager.ITradeList[]{
				new EntityVillager.EmeraldForItems(ItemCore.item_gunpwoder_flame, new EntityVillager.PriceInfo(6,10)),
				new EntityVillager.EmeraldForItems(ItemCore.item_burret_drain, new EntityVillager.PriceInfo(6,10)),
				new EntityVillager.EmeraldForItems(ItemCore.item_burret_teleport, new EntityVillager.PriceInfo(6,10)),
				new EntityVillager.EmeraldForItems(ItemCore.item_burret_explode, new EntityVillager.PriceInfo(6,10)),
		});
		carry_burretsmith.addTrade(3, new EntityVillager.ITradeList[]{
				new EntityVillager.ListItemForEmeralds(ItemCore.item_burret_teleport, new EntityVillager.PriceInfo(-12,-24)),
				new EntityVillager.ListItemForEmeralds(ItemCore.item_burret_explode, new EntityVillager.PriceInfo(-12,-24)),
				new EntityVillager.ListItemForEmeralds(ItemCore.item_burret_draw, new EntityVillager.PriceInfo(-12,-24)),
				new EntityVillager.ListItemForEmeralds(ItemCore.item_burret_flame, new EntityVillager.PriceInfo(-12,-24))
		});
		carry_burretsmith.addTrade(4, new EntityVillager.ITradeList[]{
				new EntityVillager.ListItemForEmeralds(ItemCore.item_burret_thunder, new EntityVillager.PriceInfo(-12,-24)),
				new EntityVillager.ListItemForEmeralds(ItemCore.item_burret_drain, new EntityVillager.PriceInfo(-12,-24)),
				new EntityVillager.ListItemForEmeralds(ItemCore.item_burret_blow, new EntityVillager.PriceInfo(-12,-24))
		});
		carry_burretsmith.addTrade(5, new EntityVillager.ITradeList[]{
				new EntityVillager.ListItemForEmeralds(ItemCore.item_burret_golem, new EntityVillager.PriceInfo(-8,-12)),
				new EntityVillager.ListItemForEmeralds(ItemCore.item_burret_snowman, new EntityVillager.PriceInfo(-8,-12))
		});
		((ProfessionGunSmith)gunSmith).init(carry_gunsmith,carry_burretsmith);
		ForgeRegistries.VILLAGER_PROFESSIONS.register(gunSmith);
		VillagerRegistry.instance().registerVillageCreationHandler(new VillageCrationHandler());
		//MapGenStructureIO.registerStructure(ComponentVillageGunSmithHouseStart.class, "VCHGS");
		MapGenStructureIO.registerStructureComponent(ComponentVillageGunSmithHouse.class,"VCHGS");
	}




}
