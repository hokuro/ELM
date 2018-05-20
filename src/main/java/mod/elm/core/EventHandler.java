package mod.elm.core;

import mod.elm.core.log.ModLog;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandler {
	@SubscribeEvent
	public void onLivingDeathEvent(LivingDeathEvent event) {
		if(event.getEntityLiving().world.isRemote) {
			return;
		}
//		if(event.getEntityLiving() instanceof EntitySquid || event.getEntityLiving() instanceof EntityGuardian) {
//			if(ModUtil.random(100) < 15) {
//				event.getEntityLiving().entityDropItem(new ItemStack(ItemCore.item_bladepiece,1+ModUtil.random(3),EnumBladePieceType.NIJI.getIndex()),0.0F);
//			}
//		}
	}

	@SubscribeEvent
	public void Load(net.minecraftforge.event.world.WorldEvent.Load event){
		ModLog.log().debug("Load");

	}

}
