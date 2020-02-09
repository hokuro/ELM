package mod.elm.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.BooleanUtils;

import com.google.common.collect.Lists;

import mod.elm.block.BlockCore;
import mod.elm.core.Mod_Elm;
import mod.elm.item.ItemCore;
import mod.elm.item.parts.ab.IItemElmParts;
import mod.elm.sound.SoundCore;
import mod.elm.util.ModUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;

public class TileEntityMaergerPot extends TileEntity implements ITickableTileEntity, ITileEntityParameter, IInventory {
	public static final String NAME = "maegerpot";
	public static final int FIELD_BLOODCOUNT = 0;
	public static final int FIELD_ISRUN = 1;
	public static final int FIELD_TIMECNT = 2;
	public static final int MAX_BLOODCNT = 9;

	private final NonNullList<ItemStack> stacks = NonNullList.withSize(9, ItemStack.EMPTY);
	public static final int TIME_MAERGER = 1200;
	public static final int TIME_MELT = TIME_MAERGER/10;
	private int tick_count;
	private int blood_count;
	private boolean is_run;
	private ItemStack target_parts;

	public TileEntityMaergerPot(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
		tick_count = 0;
		blood_count = 0;
		is_run = false;
		target_parts = ItemStack.EMPTY;
	}

	public TileEntityMaergerPot() {
		this(Mod_Elm.RegistryEvents.MAERGERPOT);
	}

	@Override
	public void clear() {
		stacks.clear();
	}

	@Override
	public int getSizeInventory() {
		return stacks.size();
	}

	@Override
	public boolean isEmpty() {
		for(ItemStack item : stacks) {
			if (!stacks.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.stacks.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.stacks, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.stacks, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.stacks.set(index, stack);
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	public ItemStack addItemStack(ItemStack item) {
		ItemStack over = item.copy();
		for (int i = 0; i < stacks.size(); i++) {
			if (stacks.get(i).isEmpty()) {
				ItemStack newItem = over.copy();
				int count = Math.min(this.getInventoryStackLimit(), over.getCount());
				newItem.setCount(count);
				stacks.set(i, newItem);
				over.shrink(count);
				if (over.isEmpty() || over.getCount() <= 0) {
					over = ItemStack.EMPTY;
				}
			} else {
				ItemStack inv = stacks.get(i).copy();
				if (inv.getItem() == over.getItem() &&					// i同じアイテム
						inv.getTag().equals(over.getTag()) &&			// iタグまで同じ
						inv.getCount() < inv.getMaxStackSize() && 		// i最大スタック未満
						inv.getCount() < this.getInventoryStackLimit()) // iインベントリに余裕あり
				{
					int maxCount = Math.min(inv.getMaxStackSize(), this.getInventoryStackLimit());
					int count = Math.min(over.getCount(), maxCount - inv.getCount());
					stacks.get(i).grow(count);
					over.shrink(count);
					if (over.isEmpty() || over.getCount() <= 0) {
						over = ItemStack.EMPTY;
					}
				}
			}
			if (over == ItemStack.EMPTY) {
				break;
			}
		}
		return over;
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		if (!this.is_run) {
			return player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
		}
		return false;
	}

	@Override
	public int getField(int id) {
		int value = 0;
		switch(id) {
		case FIELD_BLOODCOUNT:
			value = this.blood_count;
			break;
		case FIELD_ISRUN:
			value = BooleanUtils.toInteger(this.is_run);
			break;
		case FIELD_TIMECNT:
			value = this.tick_count;
			break;
		}
		return value;
	}

	@Override
	public void setField(int id, int value) {
		switch(id) {
		case FIELD_BLOODCOUNT:
			this.blood_count = value;
			break;
		case FIELD_ISRUN:
			this.is_run = BooleanUtils.toBoolean(value);
			break;
		case FIELD_TIMECNT:
			this.tick_count = value;
			break;
		}
	}

	@Override
	public int getFieldCount() {
		return 3;
	}

	@Override
	public void tick() {
		if (is_run) {
			tick_count--;
			if (0 >= tick_count) {
				// i完成
				this.is_run = false;
				blood_count = 0;
				this.clear();
				if (!this.world.isRemote) {
					// i成果物を放り出す
					ModUtil.spawnItemStack(this.world, this.pos.getX(), this.pos.getY() + 1, this.pos.getZ(), target_parts, Mod_Elm.rnd);
					target_parts = ItemStack.EMPTY;
				}
				playSound(POT_SOUND.SOUND_FINISH);
			} else if ((tick_count % TIME_MELT) == 0) {
				if (!this.world.isRemote) {
					// i具剤をひとつ溶かす
					List<ItemStack> collect = stacks.parallelStream().filter(itm -> !itm.isEmpty()).collect(Collectors.toList());
					if (collect.size() > 0) {
						// iランダムにアイテムを消す
						collect.remove(ModUtil.random(collect.size()));
						// iアイテム再設定
						stacks.clear();
						collect.forEach(itm ->{
							addItemStack(itm);
						});
					}
				}
				if (blood_count % (20+ModUtil.random(40)) == 0) {
					playSound(POT_SOUND.SOUND_MAERGED);
				}
				blood_count--;
			}

			if(this.world.isRemote) {
				// iパーティクルとサウンド
				for (int i = 0; i < 3; i++) {
					float rndx = this.getPos().getX() + 0.5F + MathHelper.clamp(ModUtil.randomF() - 0.5F, -0.3F, 0.3F);
					float rndy = this.getPos().getY() + 1.0F + ModUtil.randomF() - 0.5F;
					float rndz = this.getPos().getZ() + 0.5F + MathHelper.clamp(ModUtil.randomF() - 0.5F, -0.3F, 0.3F);
					world.addParticle(ParticleTypes.SMOKE, rndx, rndy, rndz, 0F, 0F ,0F);
				}

				if (((tick_count % 20) == 0) && ModUtil.randomF() < 0.4F) {
					playSound(POT_SOUND.SOUND_MAERGED);
					world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS, 1.0F, 1.0F, true);
				}
			}
		}
	}

	@Override
    public void read(CompoundNBT compound) {
		super.read(compound);

		this.tick_count = compound.getInt("tick_count");
		this.blood_count = compound.getInt("blood_count");
		this.is_run = compound.getBoolean("is_run");
		CompoundNBT parts = (CompoundNBT)compound.get("parts");
		this.target_parts = ItemStack.read(parts);

		this.stacks.clear();
		ItemStackHelper.loadAllItems(compound, this.stacks);
    }

	@Override
    public CompoundNBT write(CompoundNBT compound) {
		compound = super.write(compound);

		 compound.putInt("tick_count", this.tick_count);
		 compound.putInt("blood_count", this.blood_count);
		 compound.putBoolean("is_run", this.is_run);
		 CompoundNBT parts = new CompoundNBT();
		 target_parts.write(parts);
		 compound.put("parts", parts);

		ItemStackHelper.saveAllItems(compound, stacks);
        return compound;
    }

	@Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT cp = super.getUpdateTag();
        return this.write(cp);
    }

	@Override
    public void handleUpdateTag(CompoundNBT tag) {
		super.handleUpdateTag(tag);
		this.read(tag);
    }

	@Override
	public SUpdateTileEntityPacket getUpdatePacket()  {
        CompoundNBT CompoundNBT = new CompoundNBT();
        return new SUpdateTileEntityPacket(this.pos, 1,  this.write(CompoundNBT));
    }

	public ItemStack addBlood(ItemStack blood) {
		ItemStack ret = blood.copy();
		if (!this.isRun() && this.blood_count < MAX_BLOODCNT) {
			this.blood_count++;
			ret.shrink(1);
			if (blood.isEmpty() || blood.getCount() <= 0) {
				ret = ItemStack.EMPTY;
			}
			playSound(POT_SOUND.SOUND_BLOODIN);
		}else {
			playSound(POT_SOUND.SOUND_BLOODOVER);
		}
		return ret;
	}

	public int getBloodCount() {
		return this.blood_count;
	}

	public boolean isRun() {
		return this.is_run;
	}

	public boolean start() {
		if (is_run || blood_count < MAX_BLOODCNT) {
			playSound(POT_SOUND.SOUND_FAIL);
			return false;
		}
		target_parts = ItemStack.EMPTY;
		String targetName = "";
		// iターゲットの名前を取得
		List<String> targets = stacks.stream().filter(parts -> {
			return (parts.getItem() instanceof IItemElmParts && (parts.getItem() != ItemCore.item_meet && parts.getItem() != ItemCore.item_bone && parts.getItem() != ItemCore.item_skin));
		}).map(parts -> {
			return ((IItemElmParts)parts.getItem()).getPartsTarget(parts);
		}).distinct().collect(Collectors.toList());
		// iターゲットがないもしくは複数設定されている場合作成できない
		if (targets.size() != 1) {
			playSound(POT_SOUND.SOUND_FAIL);
			return false;
		}

		// iレシピを検索して最初の一個を採用
		Optional<Recipie> recipie =  RECIPIE.stream().filter(r ->{
			List<ItemStack> work = Lists.newArrayList();
			work.addAll(stacks);
			long cnt = r.getMaterials().stream().filter(mt -> {
				// iスロットの内容と違うアイテムを除外
				if (mt == work.get(0).getItem()) {
					work.remove(0);
					return true;
				}
				return false;
			}).count();

			// i要素が全部残っていればOK
			if (cnt == 9) {
				return true;
			}
			return false;
		}).findFirst();

		if (recipie.isPresent()) {
			// iアイテム生成
			target_parts = recipie.get().getResultItem().getDefaultInstance();
			// iターゲットを指定
			((IItemElmParts)recipie.get().resultItem).setPartsTarget(target_parts, targets.get(0));
			this.is_run = true;
			this.tick_count = TIME_MAERGER;
		} else {
			this.is_run = false;
			this.tick_count = 0;
		}

		playSound(is_run?POT_SOUND.SOUND_START:POT_SOUND.SOUND_FAIL);
		return is_run;
	}

	public static class Recipie {
		private final Item resultItem;
		private final NonNullList<Item> materialItem = NonNullList.withSize(9, Items.AIR);

		public Recipie(Item result, Item... parts) {
			if (parts.length <= 9) {
				resultItem = result;
				for (int i = 0; i < parts.length; i++) {
					materialItem.set(i, parts[i]);
				}
			}else {
				throw new IllegalArgumentException("over 9 parts");
			}
		}

		public Item getResultItem() {return resultItem;}
		public NonNullList<Item> getMaterials(){
			return materialItem;
		}

		public boolean check(int idx, Item item) {
			if (materialItem.get(idx) == item) {
				return true;
			}
			return false;
		}
	}
	private static final List<Recipie> RECIPIE = new ArrayList<Recipie>() {
		{add(new Recipie(BlockCore.item_elmhead,ItemCore.item_bone, ItemCore.item_brain, ItemCore.item_bone,
				 								ItemCore.item_eye,  ItemCore.item_meet , ItemCore.item_eye,
				 								ItemCore.item_skin, ItemCore.item_meet,  ItemCore.item_skin));}
		{add(new Recipie(ItemCore.item_elm_arm, ItemCore.item_skin, ItemCore.item_meet, ItemCore.item_hand,
				 								ItemCore.item_skin, ItemCore.item_bone, ItemCore.item_meet,
				 								ItemCore.item_bone, ItemCore.item_skin, ItemCore.item_meet));}
		{add(new Recipie(ItemCore.item_elm_arm, ItemCore.item_hand, ItemCore.item_meet, ItemCore.item_skin,
				 								ItemCore.item_meet, ItemCore.item_bone, ItemCore.item_skin,
				 								ItemCore.item_meet, ItemCore.item_skin, ItemCore.item_bone));}
		{add(new Recipie(ItemCore.item_elm_leg, ItemCore.item_bone, ItemCore.item_meet, ItemCore.item_skin,
			     								ItemCore.item_meet, ItemCore.item_bone, ItemCore.item_skin,
			     								ItemCore.item_meet, ItemCore.item_skin, ItemCore.item_foot));}
		{add(new Recipie(ItemCore.item_elm_leg, ItemCore.item_skin, ItemCore.item_meet, ItemCore.item_bone,
				 								ItemCore.item_skin, ItemCore.item_bone, ItemCore.item_meet,
				 								ItemCore.item_foot, ItemCore.item_skin, ItemCore.item_meet));}
		{add(new Recipie(ItemCore.item_elm_body,ItemCore.item_lung, ItemCore.item_stmach, ItemCore.item_hart,
				 								ItemCore.item_bone, ItemCore.item_intestine, ItemCore.item_bone,
				 								ItemCore.item_skin, ItemCore.item_uterus, ItemCore.item_skin));}
		{add(new Recipie(ItemCore.item_elm_body,ItemCore.item_lung, ItemCore.item_stmach, ItemCore.item_hart,
				 								ItemCore.item_bone, ItemCore.item_intestine, ItemCore.item_bone,
				 								ItemCore.item_skin, ItemCore.item_meet, ItemCore.item_skin));}
	};

	protected void playSound(POT_SOUND se) {
		this.world.playSound(null, this.pos, se.getSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
	}

	public static enum POT_SOUND{
		// i開始音楽、実行失敗音楽、実行中音楽、終了音楽、血液投入音楽、血液一杯音楽
		SOUND_START(SoundCore.SOUND_BLOCK_START_MAERGERPOT),
		SOUND_FAIL(SoundCore.SOUND_BLOCK_MISS_MAERGERPOT),
		SOUND_MAERGED(SoundCore.SOUND_BLOCK_MAERGED_MAERGERPOT),
		SOUND_FINISH(SoundCore.SOUND_BLOCK_FINISH_MAERGERPOT),
		SOUND_BLOODIN(SoundCore.SOUND_BLOCK_BLOODIN_MAERGERPOT),
		SOUND_BLOODOVER(SoundCore.SOUND_BLOCK_BLOODOVER_MAERGERPOT);

		private SoundEvent sound;
		private POT_SOUND(SoundEvent se) {
			sound = se;
		}

		public SoundEvent getSound() {return sound;}
	}
}
