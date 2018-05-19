package mod.fbd.core;

import mod.fbd.block.BlockCore;
import mod.fbd.core.log.ModLog;
import mod.fbd.item.ItemBladePiece.EnumBladePieceType;
import mod.fbd.item.ItemCore;
import mod.fbd.util.ModUtil;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandler {
	@SubscribeEvent
	public void onLivingDeathEvent(LivingDeathEvent event) {
		if(event.getEntityLiving().world.isRemote) {
			return;
		}
		if(event.getEntityLiving() instanceof EntitySquid || event.getEntityLiving() instanceof EntityGuardian) {
			if(ModUtil.random(100) < 15) {
				event.getEntityLiving().entityDropItem(new ItemStack(ItemCore.item_bladepiece,1+ModUtil.random(3),EnumBladePieceType.NIJI.getIndex()),0.0F);
			}
		}
	}

	@SubscribeEvent
	public void Load(net.minecraftforge.event.world.WorldEvent.Load event){
		ModLog.log().debug("Load");
		try{
			if (!event.getWorld().isRemote){
				LootTable lt = event.getWorld().getLootTableManager().getLootTableFromLocation(LootTableList.CHESTS_DESERT_PYRAMID);
				LootPool pool = lt.getPool("main");
				ModUtil.setPrivateValue(LootPool.class, pool, false, "isFrozen");
				if (pool.getEntry(BlockCore.block_bladealter.getRegistryName().toString()) == null){
					pool.addEntry(new LootEntryItem((new ItemStack(BlockCore.block_bladealter,1)).getItem(), 5, 0,
							new LootFunction[]{new SetCount(new LootCondition[]{}, new RandomValueRange(1,1))},
							new LootCondition[]{},BlockCore.block_bladealter.getRegistryName().toString()));
				}
				if (pool.getEntry(ItemCore.item_bladepiece.getRegistryName().toString()) == null){
					pool.addEntry(new LootEntryItem((new ItemStack(ItemCore.item_bladepiece,1,EnumBladePieceType.NIJI.getIndex())).getItem(), 15, 0,
							new LootFunction[]{new SetCount(new LootCondition[]{}, new RandomValueRange(1,4))},
							new LootCondition[]{},ItemCore.item_bladepiece.getRegistryName().toString()));
				}
				if (pool.getEntry(ItemCore.item_burret_assasination.getRegistryName().toString()) == null){
					pool.addEntry(new LootEntryItem(ItemCore.item_burret_assasination, 15, 0,
							new LootFunction[]{new SetCount(new LootCondition[]{}, new RandomValueRange(3,5))},
							new LootCondition[]{},ItemCore.item_burret_assasination.getRegistryName().toString()));
				}
				ModUtil.setPrivateValue(LootPool.class, pool, true, "isFrozen");

				lt = event.getWorld().getLootTableManager().getLootTableFromLocation(LootTableList.CHESTS_JUNGLE_TEMPLE);
				pool = lt.getPool("main");
				ModUtil.setPrivateValue(LootPool.class, pool, false, "isFrozen");
				if (pool.getEntry(BlockCore.block_bladealter.getRegistryName().toString()) == null){
					pool.addEntry(new LootEntryItem((new ItemStack(BlockCore.block_bladealter,1)).getItem(), 5, 0,
							new LootFunction[]{new SetCount(new LootCondition[]{}, new RandomValueRange(1,1))},
							new LootCondition[]{},BlockCore.block_bladealter.getRegistryName().toString()));
				}
				if (pool.getEntry(ItemCore.item_bladepiece.getRegistryName().toString()) == null){
					pool.addEntry(new LootEntryItem((new ItemStack(ItemCore.item_bladepiece,1,EnumBladePieceType.NIJI.getIndex())).getItem(), 15, 0,
							new LootFunction[]{new LootNiji(new LootCondition[]{}, new RandomValueRange(1,4))},
							new LootCondition[]{},ItemCore.item_bladepiece.getRegistryName().toString()));
				}
				if (pool.getEntry(ItemCore.item_burret_assasination.getRegistryName().toString()) == null){
					pool.addEntry(new LootEntryItem(ItemCore.item_burret_assasination, 15, 0,
							new LootFunction[]{new SetCount(new LootCondition[]{}, new RandomValueRange(3,5))},
							new LootCondition[]{},ItemCore.item_burret_assasination.getRegistryName().toString()));
				}
				ModUtil.setPrivateValue(LootPool.class, pool, true, "isFrozen");

				lt = event.getWorld().getLootTableManager().getLootTableFromLocation(LootTableList.CHESTS_IGLOO_CHEST);
				pool = lt.getPool("main");
				ModUtil.setPrivateValue(LootPool.class, pool, false, "isFrozen");
				if (pool.getEntry(ItemCore.item_burret_assasination.getRegistryName().toString()) == null){
					pool.addEntry(new LootEntryItem(ItemCore.item_burret_assasination, 15, 0,
							new LootFunction[]{new SetCount(new LootCondition[]{}, new RandomValueRange(3,5))},
							new LootCondition[]{},ItemCore.item_burret_assasination.getRegistryName().toString()));
				}
				ModUtil.setPrivateValue(LootPool.class, pool, true, "isFrozen");

				lt = event.getWorld().getLootTableManager().getLootTableFromLocation(LootTableList.CHESTS_NETHER_BRIDGE);
				pool = lt.getPool("main");
				ModUtil.setPrivateValue(LootPool.class, pool, false, "isFrozen");
				if (pool.getEntry(ItemCore.item_burret_assasination.getRegistryName().toString()) == null){
					pool.addEntry(new LootEntryItem(ItemCore.item_burret_assasination, 15, 0,
							new LootFunction[]{new SetCount(new LootCondition[]{}, new RandomValueRange(3,5))},
							new LootCondition[]{},ItemCore.item_burret_assasination.getRegistryName().toString()));
				}
				ModUtil.setPrivateValue(LootPool.class, pool, true, "isFrozen");

			}
		}catch(Exception ex){
			ModLog.log().error("Exception");
		}

	}

}
