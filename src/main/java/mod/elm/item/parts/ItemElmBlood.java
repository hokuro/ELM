package mod.elm.item.parts;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import mod.elm.core.Mod_Elm;
import mod.elm.item.parts.ab.ItemElmParts;
import mod.elm.util.ModUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemElmBlood extends ItemElmParts {
	public static final int DEFAULT_EFFECT_NUM = 3;

	public ItemElmBlood(Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		PlayerEntity playerentity = entityLiving instanceof PlayerEntity ? (PlayerEntity)entityLiving : null;
		if (playerentity == null || !playerentity.abilities.isCreativeMode) {
			stack.shrink(1);
		}

		if (!worldIn.isRemote) {
			this.getPartTargetEntity(stack).ifPresent(etype->{
				// i設定されているエフェクトを取り出す
				List<EffectInstance> effects = getEffects(stack);

				// i即時回復のレベルを指定する
				MobEntity mob = (MobEntity)etype.create(worldIn);
				int level = Math.round(mob.getMaxHealth()/2);
				Effect iheal;
				if (entityLiving.getCreatureAttribute() != CreatureAttribute.UNDEAD) {
					// i即時回復
					iheal = Effects.INSTANT_HEALTH;
				} else {
					// iアンデットなら即時ダメージで回復させる
					iheal = Effects.INSTANT_DAMAGE;
				}
				// i即時回復発動
				iheal.affectEntity(entityLiving, entityLiving, entityLiving, level, 2);

				// i確率でおなかを壊す
				if (ModUtil.randomF() < 0.4) {
					EffectInstance hunger;
					if (entityLiving.isPotionActive(Effects.HUNGER)) {
						// i既におなかを壊している場合悪化する
						EffectInstance hunger2 = entityLiving.getActivePotionEffect(Effects.HUNGER);
						hunger = new EffectInstance(Effects.HUNGER, 60 * 20 + hunger2.getDuration(), hunger2.getAmplifier()+1);
					} else {
						 hunger = new EffectInstance(Effects.HUNGER, 60 * 20, 0);
					}
					entityLiving.addPotionEffect(hunger);
				}

				// iポーション効果発動
				effects.forEach(additional -> {
					if (entityLiving.isPotionActive(additional.getPotion())) {
						// i既に発生済みの効果の場合効力アップ
						EffectInstance active = entityLiving.getActivePotionEffect(additional.getPotion());
						entityLiving.addPotionEffect(new EffectInstance(additional.getPotion(), active.getDuration() + additional.getDuration(), active.getAmplifier() + additional.getAmplifier()));
					} else {
						entityLiving.addPotionEffect(additional);
					}
				});

				// iアンデッドまたは昆虫の血液の場合一定確率で毒に侵される
				if (((mob.getCreatureAttribute() == CreatureAttribute.UNDEAD) || (mob.getCreatureAttribute() == CreatureAttribute.ARTHROPOD)) && ModUtil.randomF() < 0.05F) {
					// i発症中の場合再発症しない
					if (!entityLiving.isPotionActive(Effects.POISON)) {
						entityLiving.addPotionEffect(new EffectInstance(Effects.POISON, 0));
					}
				}else if (mob instanceof AnimalEntity && mob.getCreatureAttribute() == CreatureAttribute.WATER && ModUtil.randomF() < 0.1) {
					// i海の動物の血の場合イルカの好意又は水中呼吸又はコンジットパワーが付く(同じものが発動している場合効果時間を延ばす)
					Effect seeEffect;
					int duration = 0;
					if (ModUtil.randomF() < 0.2) {
						seeEffect = Effects.CONDUIT_POWER;
						duration = 30 * 20;
					} else if (ModUtil.randomF() < 0.3) {
						seeEffect = Effects.DOLPHINS_GRACE;
						duration = 60 * 20;
					} else {
						// iさウイ中呼吸
						seeEffect = Effects.WATER_BREATHING;
						duration = 90 * 20;
					}
					if(entityLiving.isPotionActive(seeEffect)) {
						EffectInstance ei = entityLiving.getActivePotionEffect(seeEffect);
						entityLiving.addPotionEffect(new EffectInstance(seeEffect, duration + ei.getDuration(), ei.getAmplifier()));
					} else {
						entityLiving.addPotionEffect(new EffectInstance(seeEffect, duration, 0));
					}
				}
			});

			// i血液による特別な呪い効果をつける
			int idx = Math.round((ModUtil.randomF() * 1000)) % Mod_Elm.RegistryEvents.BLOOD_EFFECT.size();
			Effect blood_curse = Mod_Elm.RegistryEvents.BLOOD_EFFECT.get(idx);
			// i呪い発動中の場合呪いがさらに強くなる
			if(entityLiving.isPotionActive(blood_curse)) {
				EffectInstance ei = entityLiving.getActivePotionEffect(blood_curse);
				if (blood_curse == Mod_Elm.RegistryEvents.EFFECT_BLOODPARASITE) {
					// i寄生虫の場合生まれるまでの時間を1/10短縮(1分未満にはならない)
					int duration = ei.getDuration();
					if (duration > 66) {
						duration = Math.round(duration * 0.9F);
					}
					EffectInstance parasite = new EffectInstance(Mod_Elm.RegistryEvents.EFFECT_BLOODPARASITE, duration,0);
					parasite.setCurativeItems(new ArrayList<ItemStack>());
					entityLiving.addPotionEffect(parasite);
				}else {
					entityLiving.addPotionEffect(new EffectInstance(blood_curse, 10 * 60 * 20 + ei.getDuration(), ei.getAmplifier() + 1));
				}
			} else {
				if (blood_curse == Mod_Elm.RegistryEvents.EFFECT_BLOODPARASITE) {
					// i寄生虫の場合20分
					EffectInstance parasite = new EffectInstance(blood_curse, 20 * 60 * 20, 0);
					parasite.setCurativeItems(new ArrayList<ItemStack>());
					entityLiving.addPotionEffect(parasite);
				} else {
					entityLiving.addPotionEffect(new EffectInstance(blood_curse, 10 * 60 * 20, 0));
				}
			}
		}
		return stack;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 64;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.DRINK;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		playerIn.setActiveHand(handIn);
		return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		return I18n.format(this.getTranslationKey()) + "'s " + this.getPartTargetName(stack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
	}

	private List<EffectInstance> getEffects(ItemStack stack){
		// i設定されているエフェクトを取り出す
		List<EffectInstance> effects = PotionUtils.getEffectsFromStack(stack);
		if (effects.size() == 0) {
			// i無ければ設定する
			effects.addAll(setRandomPotionEffect(stack,DEFAULT_EFFECT_NUM));
		}
		return effects;
	}


	private static final List<Vec3i> durationTable = new ArrayList<Vec3i>(){
		{add(new Vec3i(0,15,30*20));}
		{add(new Vec3i(15,45,90*20));}
		{add(new Vec3i(45,70,120*20));}
		{add(new Vec3i(70,90,180*20));}
		{add(new Vec3i(90,100,300*20));}
	};

	private static final List<Vec3i> levelTable =  new ArrayList<Vec3i>(){
		{add(new Vec3i(0,50,0));}
		{add(new Vec3i(50,75,1));}
		{add(new Vec3i(75,90,2));}
		{add(new Vec3i(90,100,3));}
	};
	public static List<EffectInstance> setRandomPotionEffect(ItemStack stack, int cnt) {
		List<EffectInstance> effects = Lists.newArrayList();
		// i指定した個数の良性エフェクトを選択する
		for (int i = 0; i < cnt; i++) {
			ModUtil.resetPreviousRand();
			Vec3i key = durationTable.stream().filter(k -> k.getX() <= ModUtil.getPrevisiousRand(100) && ModUtil.getPrevisiousRand(100) < k.getY()).findFirst().get();
			int duration = key.getZ();


			ModUtil.resetPreviousRand();
			key = levelTable.stream().filter(k -> k.getX() <= ModUtil.getPrevisiousRand(100) && ModUtil.getPrevisiousRand(100) < k.getY()).findFirst().get();
			int level = key.getZ();

			List<Effect> ef = Registry.EFFECTS.stream().filter(e -> e.isBeneficial() && !e.isInstant()).collect(Collectors.toList());
			effects.add(new EffectInstance(ef.get(ModUtil.random(ef.size())),duration,level));
		}
		// iアイテムに良性エフェクトを設定する
		PotionUtils.appendEffects(stack, effects);
		return effects;
	}
}
