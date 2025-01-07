package mz.mzlib.minecraft.network.packet.s2c.play;

import mz.mzlib.minecraft.VersionName;
import mz.mzlib.minecraft.VersionRange;
import mz.mzlib.minecraft.network.packet.Packet;
import mz.mzlib.minecraft.wrapper.WrapMinecraftClass;
import mz.mzlib.minecraft.wrapper.WrapMinecraftFieldAccessor;
import mz.mzlib.util.wrapper.SpecificImpl;
import mz.mzlib.util.wrapper.WrapperCreator;
import mz.mzlib.util.wrapper.WrapperObject;

import java.util.List;

@WrapMinecraftClass(
        {
                @VersionName(name="net.minecraft.network.packet.s2c.play.EntitiesDestroyS2CPacket", end=1500),
                @VersionName(name="net.minecraft.client.network.packet.EntitiesDestroyS2CPacket", begin=1500, end=1502),
                @VersionName(name="net.minecraft.network.packet.s2c.play.EntitiesDestroyS2CPacket", begin=1502, end=1700),
                @VersionName(name="net.minecraft.network.packet.s2c.play.EntityDestroyS2CPacket", begin=1700, end=1701),
                @VersionName(name="net.minecraft.network.packet.s2c.play.EntitiesDestroyS2CPacket", begin=1701)
        })
public interface PacketS2cEntityDestroy extends Packet
{
    @WrapperCreator
    static PacketS2cEntityDestroy create(Object wrapped)
    {
        return WrapperObject.create(PacketS2cEntityDestroy.class, wrapped);
    }
    
    int[] getEntityIds();
    
    @SpecificImpl("getEntityIds")
    @VersionRange(end=1700)
    @WrapMinecraftFieldAccessor(@VersionName(name="entityIds"))
    int[] getEntityIdsV_1700();
    
    @VersionRange(begin=1700, end=1701)
    @WrapMinecraftFieldAccessor(@VersionName(name="entityId"))
    int getEntityIdV1700_1701();
    @SpecificImpl("getEntityIds")
    @VersionRange(begin=1700, end=1701)
    default int[] getEntityIdsV1700_1701()
    {
        return new int[] {this.getEntityIdV1700_1701()};
    }
    
    @VersionRange(begin=1701)
    @WrapMinecraftFieldAccessor(@VersionName(name="entityIds"))
    List<Integer> getEntityIds0V1701();
    @SpecificImpl("getEntityIds")
    @VersionRange(begin=1701)
    default int[] getEntityIdsV1701()
    {
        return getEntityIds0V1701().stream().mapToInt(Integer::intValue).toArray();
    }
}
