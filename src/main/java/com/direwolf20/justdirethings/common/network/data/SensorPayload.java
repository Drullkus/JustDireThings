package com.direwolf20.justdirethings.common.network.data;

import com.direwolf20.justdirethings.JustDireThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SensorPayload(
        int senseTarget,
        boolean strongSignal,
        int senseCount,
        int equality
) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(JustDireThings.MODID, "sensor_packet");

    public SensorPayload(final FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readBoolean(), buffer.readInt(), buffer.readInt());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(senseTarget);
        buffer.writeBoolean(strongSignal);
        buffer.writeInt(senseCount);
        buffer.writeInt(equality);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
