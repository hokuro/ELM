package mod.elm.entity.mob;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class EntityElmBase extends EntityTameable {
	/** Absoption効果をクライアント側へ転送するのに使う */
	protected static final DataParameter<Float> dataWatch_Absoption		= EntityDataManager.createKey(EntityElmBase.class, DataSerializers.FLOAT);
	public void DW_Absoption(Float value){
		this.dataManager.set(dataWatch_Absoption,value);
	}
	public Float DW_Absoption(){
		return this.dataManager.get(dataWatch_Absoption);
	}

	protected static final DataParameter<String> dataWatch_Texture		= EntityDataManager.createKey(EntityElmBase.class, DataSerializers.STRING);
	public void DW_Texture(String value){
		this.dataManager.set(dataWatch_Texture,value);
	}
	public String DW_Texture(){
		return this.dataManager.get(dataWatch_Texture);
	}

	// 利き腕
	protected static final DataParameter<Integer> dataWatch_DominamtArm	= EntityDataManager.createKey(EntityElmBase.class, DataSerializers.VARINT);
	public void DW_DominamtArm(Integer value){
		this.dataManager.set(dataWatch_DominamtArm,value);
	}
	public Integer DW_DominamtArm(){
		return this.dataManager.get(dataWatch_DominamtArm);
	}

	// アイテムの使用判定、腕毎(Integer)
	protected static final DataParameter<Integer> dataWatch_ItemUse		= EntityDataManager.createKey(EntityElmBase.class, DataSerializers.VARINT);
	public void DW_ItemUse(Integer value){
		this.dataManager.set(dataWatch_ItemUse,value);
	}
	public Integer DW_ItemUse(){
		return this.dataManager.get(dataWatch_ItemUse);
	}

	// 経験値
	protected static final DataParameter<Float> dataWatch_MaidExpValue		= EntityDataManager.createKey(EntityElmBase.class, DataSerializers.FLOAT);
	public void DW_MaidExpValue(Float value){
		this.dataManager.set(dataWatch_MaidExpValue,value);
	}
	public Float DW_MaidExpValue(){
		return this.dataManager.get(dataWatch_MaidExpValue);
	}

	// 種別
	protected static final DataParameter<String> dataWatch_MobKind = EntityDataManager.createKey(EntityElmBase.class, DataSerializers.STRING);
	protected void DW_MobKind(String value){
		this.dataManager.set(dataWatch_MobKind,value);
	}
	public String DW_MobKind(){
		return this.dataManager.get(dataWatch_MobKind);
	}

	protected IInventory inventory_items;
	protected IInventory inventory_equipment;



	public EntityElmBase(World worldIn) {
		super(worldIn);
	}


	@Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(dataWatch_Absoption,0.0F);
        this.dataManager.register(dataWatch_Texture,"");
        this.dataManager.register(dataWatch_DominamtArm,0);
        this.dataManager.register(dataWatch_ItemUse,0);
        this.dataManager.register(dataWatch_MaidExpValue,0.0F);
        String cls = EntityList.getEntityString(this);
        this.dataManager.register(dataWatch_MobKind,cls.toLowerCase());
    }

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return null;
	}

	@Override
    protected void updateAITasks()
    {
        super.updateAITasks();
    }


	@Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
    }


	@Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable(source))
        {
            return false;
        }
        else
        {
            return super.attackEntityFrom(source, amount);
        }
    }

	@Override
    public float getBlockPathWeight(BlockPos pos)
    {
        return this.world.getBlockState(pos.down()).getBlock() == this.spawnableBlock ? 10.0F : this.world.getLightBrightness(pos) - 0.5F;
    }


	@Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setFloat("dataWatch_Absoption", DW_Absoption());
        compound.setFloat("dataWatch_Texture", DW_Texture());
        compound.setFloat("dataWatch_DominamtArm", DW_DominamtArm());
        compound.setFloat("dataWatch_ItemUse", DW_ItemUse());
        compound.setFloat("dataWatch_MaidExpValue", DW_MaidExpValue());
        compound.setFloat("dataWatch_MobKind", DW_MobKind());
    }



	@Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
    }

	@Override
    public int getTalkInterval()
    {
        return 120;
    }

	@Override
    public boolean isBreedingItem(ItemStack stack)
    {
        return false;
    }

	@Override
    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        return super.processInteract(player, hand);
    }

	@Override
    public void setInLove(@Nullable EntityPlayer player)
    {

    }

	@Override
    @Nullable
    public EntityPlayerMP getLoveCause()
    {
    	return null;
    }

    @Override
    public boolean isInLove()
    {
        return false;
    }

    @Override
    public boolean canMateWith(EntityAnimal otherAnimal)
    {
       return false;
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id)
    {
    	super.handleStatusUpdate(id);
    }

    public void onDeath(DamageSource cause)
    {
        if (!this.world.isRemote && this.world.getGameRules().getBoolean("showDeathMessages") && this.getOwner() instanceof EntityPlayerMP)
        {
            this.getOwner().sendMessage(this.getCombatTracker().getDeathMessage());
        }

        super.onDeath(cause);
    }

    @Override
    public boolean isChild()
    {
        return false;
    }

    @Override
    public void setScaleForAge(boolean child)
    {
        this.setScale(1.0F);
    }


	@Override
    public double getYOffset()
    {
        return 0.14D;
    }

}

