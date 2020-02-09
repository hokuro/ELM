package mod.elm.network;

import mod.elm.core.ModCommon;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class MessageHandler {
	private static final String PROTOCOL_VERSION = Integer.toString(1);
	private static final SimpleChannel Handler = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation(ModCommon.MOD_ID,ModCommon.MOD_CHANEL))
			.clientAcceptedVersions(PROTOCOL_VERSION::equals)
			.serverAcceptedVersions(PROTOCOL_VERSION::equals)
			.networkProtocolVersion(() -> PROTOCOL_VERSION)
			.simpleChannel();

	public static void register() {
		int disc = 0;
		Handler.registerMessage(disc++, Message_UpdateWanderingPeople.class, Message_UpdateWanderingPeople::encode, Message_UpdateWanderingPeople::decode, Message_UpdateWanderingPeople.Handler::handle);
	}

	public static <MSG> void sendToServer(MSG message) {
		Handler.sendToServer(message);
	}

	public static <MSG> void sendToClient(MSG message, ServerPlayerEntity connection) {
		Handler.sendTo(message, connection.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
	}
}
