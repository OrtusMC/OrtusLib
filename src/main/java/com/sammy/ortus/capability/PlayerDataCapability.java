package com.sammy.ortus.capability;

import com.sammy.ortus.OrtusLib;
import com.sammy.ortus.network.SyncPlayerCapabilityDataPacket;
import com.sammy.ortus.network.SyncPlayerCapabilityDataServerPacket;
import com.sammy.ortus.network.interaction.UpdateLeftClickPacket;
import com.sammy.ortus.network.interaction.UpdateRightClickPacket;
import com.sammy.ortus.registry.OrtusPackets;
import com.sammy.ortus.systems.capability.OrtusCapability;
import com.sammy.ortus.systems.capability.OrtusCapabilityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.network.PacketDistributor;

import static com.sammy.ortus.registry.OrtusPackets.INSTANCE;

public class PlayerDataCapability implements OrtusCapability {

    //shove all player data here, use PlayerDataCapability.getCapability(player) to access data.

    public static Capability<PlayerDataCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public boolean hasJoinedBefore;
    public boolean rightClickHeld;
    public int rightClickTime;
    public boolean leftClickHeld;
    public int leftClickTime;


    public PlayerDataCapability() {
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerDataCapability.class);
    }

    public static void attachPlayerCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            final PlayerDataCapability capability = new PlayerDataCapability();
            event.addCapability(OrtusLib.prefix("player_data"), new OrtusCapabilityProvider<>(PlayerDataCapability.CAPABILITY, () -> capability));
        }
    }

    public static void playerJoin(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof Player player) {
            PlayerDataCapability.getCapability(player).ifPresent(capability -> capability.hasJoinedBefore = true);
            if (player instanceof ServerPlayer serverPlayer) {
                syncSelf(serverPlayer);
            }
        }
    }

    public static void syncPlayerCapability(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof Player player) {
            if (player.level instanceof ServerLevel) {
                syncTracking(player);
            }
        }
    }

    public static void playerTick(TickEvent.PlayerTickEvent event) {
        PlayerDataCapability.getCapability(event.player).ifPresent(c ->
        {
            c.rightClickTime = c.rightClickHeld ? c.rightClickTime + 1 : 0;
            c.leftClickTime = c.leftClickHeld ? c.leftClickTime + 1 : 0;
        });
    }

    public static void playerClone(PlayerEvent.Clone event) {
        event.getOriginal().revive();
        PlayerDataCapability.getCapability(event.getOriginal()).ifPresent(o -> PlayerDataCapability.getCapability(event.getPlayer()).ifPresent(c -> {
            c.deserializeNBT(o.serializeNBT());
        }));
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("firstTimeJoin", hasJoinedBefore);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        hasJoinedBefore = tag.getBoolean("firstTimeJoin");
    }

    public static void syncSelf(ServerPlayer player) {
        sync(player, PacketDistributor.PLAYER.with(() -> player));
    }

    public static void syncTrackingAndSelf(Player player) {
        sync(player, PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player));
    }

    public static void syncTracking(Player player) {
        sync(player, PacketDistributor.TRACKING_ENTITY.with(() -> player));
    }

    public static void sync(Player player, PacketDistributor.PacketTarget target) {
        getCapability(player).ifPresent(c -> INSTANCE.send(target, new SyncPlayerCapabilityDataPacket(player.getUUID(), c.serializeNBT())));
    }

    public static void syncServer(Player player) {
        getCapability(player).ifPresent(c -> INSTANCE.send(PacketDistributor.SERVER.noArg(), new SyncPlayerCapabilityDataServerPacket(player.getUUID(), c.serializeNBT())));
    }

    public static LazyOptional<PlayerDataCapability> getCapability(Player player) {
        return player.getCapability(CAPABILITY);
    }

    public static class ClientOnly {
        public static void clientTick(TickEvent.ClientTickEvent event) {
            Minecraft minecraft = Minecraft.getInstance();
            Player player = minecraft.player;
            PlayerDataCapability.getCapability(player).ifPresent(c -> {
                boolean left = minecraft.options.keyAttack.isDown();
                boolean right = minecraft.options.keyUse.isDown();
                if (left != c.leftClickHeld) {
                    c.leftClickHeld = left;
                    INSTANCE.send(PacketDistributor.SERVER.noArg(), new UpdateLeftClickPacket(c.leftClickHeld));
                }
                if (right != c.rightClickHeld) {
                    c.rightClickHeld = right;
                    INSTANCE.send(PacketDistributor.SERVER.noArg(), new UpdateRightClickPacket(c.rightClickHeld));
                }
            });
        }
    }
}