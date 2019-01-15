package mod.elm.entity.passive;

import mod.elm.block.BlockImoutoHead.EnumImoutoHead;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityImoutoSnowman extends EntitySnowman {
	public static final DataParameter<Integer> ROTTEN = EntityDataManager.<Integer>createKey(EntitySnowman.class, DataSerializers.VARINT);
	public EnumImoutoHead getRotten(){
		return EnumImoutoHead.getFromIndex(dataManager.get(ROTTEN));
	}
	public void setRotten(EnumImoutoHead rotten){
		dataManager.set(ROTTEN, rotten.getValue());
	}

	public EntityImoutoSnowman(World worldIn) {
		this(worldIn,EnumImoutoHead.NORMAL);
	}

	public EntityImoutoSnowman(World worldIn,EnumImoutoHead rotten) {
		super(worldIn);
	}

	@Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(ROTTEN, 0);
    }

	@Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("rotten", getRotten().getValue());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        setRotten(EnumImoutoHead.getFromIndex(compound.getInteger("rotten")));
    }

}
