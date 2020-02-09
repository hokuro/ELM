package mod.elm.network;

import java.util.function.Supplier;

import mod.elm.entity.EntityWanderingPeople;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class Message_UpdateWanderingPeople {
	private CompoundNBT tag;
	private int entityid;
	public Message_UpdateWanderingPeople(CompoundNBT tagIn, int id){
		tag = tagIn;
		entityid = id;
	}


	public static void encode(Message_UpdateWanderingPeople pkt, PacketBuffer buf) {
		buf.writeCompoundTag(pkt.tag);
		buf.writeInt(pkt.entityid);
	}

	public static Message_UpdateWanderingPeople decode(PacketBuffer buf) {
		return new Message_UpdateWanderingPeople(buf.readCompoundTag(), buf.readInt());
	}

	public static class Handler {
		public static void handle(final Message_UpdateWanderingPeople pkt, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {
				Entity ent = Minecraft.getInstance().world.getEntityByID(pkt.entityid);
				if (ent instanceof EntityWanderingPeople) {
					((EntityWanderingPeople) ent).readAdditional(pkt.tag);
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
}
