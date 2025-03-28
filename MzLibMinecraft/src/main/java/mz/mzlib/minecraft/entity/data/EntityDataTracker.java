package mz.mzlib.minecraft.entity.data;

import mz.mzlib.minecraft.VersionName;
import mz.mzlib.minecraft.VersionRange;
import mz.mzlib.minecraft.entity.Entity;
import mz.mzlib.minecraft.network.packet.s2c.play.PacketS2cEntityData;
import mz.mzlib.minecraft.wrapper.WrapMinecraftClass;
import mz.mzlib.minecraft.wrapper.WrapMinecraftFieldAccessor;
import mz.mzlib.minecraft.wrapper.WrapMinecraftInnerClass;
import mz.mzlib.minecraft.wrapper.WrapMinecraftMethod;
import mz.mzlib.util.wrapper.WrapConstructor;
import mz.mzlib.util.wrapper.WrapperCreator;
import mz.mzlib.util.wrapper.WrapperObject;

import java.util.function.Function;

@WrapMinecraftClass(@VersionName(name="net.minecraft.entity.data.DataTracker"))
public interface EntityDataTracker extends WrapperObject
{
    @WrapperCreator
    static EntityDataTracker create(Object wrapped)
    {
        return WrapperObject.create(EntityDataTracker.class, wrapped);
    }
    
    static EntityDataTracker newInstanceV_1903(Entity entity)
    {
        return create(null).staticNewInstanceV_1903(entity);
    }
    @VersionRange(end=1903)
    @WrapConstructor
    EntityDataTracker staticNewInstanceV_1903(Entity entity);
    
    @WrapMinecraftInnerClass(outer=EntityDataTracker.class, name={@VersionName(name="DataEntry", end=1400), @VersionName(name="Entry", begin=1400)})
    interface Entry extends WrapperObject, PacketS2cEntityData.Entry
    {
        @WrapperCreator
        static Entry create(Object wrapped)
        {
            return WrapperObject.create(Entry.class, wrapped);
        }
        
        static Entry newInstance(EntityDataType type, WrapperObject value)
        {
            return newInstance0(type, value.getWrapped());
        }
        
        static Entry newInstance0(EntityDataType type, Object value)
        {
            return Entry.create(null).staticNewInstance0(type, value);
        }
        
        @WrapConstructor
        Entry staticNewInstance0(EntityDataType type, Object value);
        
        @Override
        @WrapMinecraftMethod({@VersionName(name="method_12758", end=1400), @VersionName(name="getData", begin=1400)})
        EntityDataType getType();
        
        @Override
        @WrapMinecraftMethod({@VersionName(name="getValue", end=1400), @VersionName(name="get", begin=1400)})
        Object getValue0();
        
        @Override
        @WrapMinecraftMethod({@VersionName(name="setValue", end=1400), @VersionName(name="set", begin=1400)})
        void setValue0(Object value);
        
        @VersionRange(begin=1903)
        @WrapMinecraftMethod(@VersionName(name="toSerialized"))
        EntityDataV1903 toDataV1903();
        
        @Override
        default String toString0()
        {
            return PacketS2cEntityData.Entry.super.toString0();
        }
    }
    
    @VersionRange(begin=1903)
    @WrapMinecraftInnerClass(outer=EntityDataTracker.class, name=@VersionName(name="SerializedEntry"))
    interface EntityDataV1903 extends WrapperObject, PacketS2cEntityData.Entry
    {
        @WrapperCreator
        static EntityDataV1903 create(Object wrapped)
        {
            return WrapperObject.create(EntityDataV1903.class, wrapped);
        }
        
        @WrapMinecraftFieldAccessor(@VersionName(name="comp_1115"))
        int getIndex();
        
        @WrapMinecraftFieldAccessor(@VersionName(name="handler"))
        EntityDataHandler getHandler();
        
        @Override
        default EntityDataType getType()
        {
            return EntityDataType.newInstance(this.getIndex(), this.getHandler());
        }
        
        @Override
        @WrapMinecraftFieldAccessor(@VersionName(name="comp_1117"))
        Object getValue0();
        
        @Override
        @WrapMinecraftFieldAccessor(@VersionName(name="comp_1117"))
        void setValue0(Object value);
        
        default <T extends WrapperObject> T getValue(Function<Object, T> wrapperCreator)
        {
            return wrapperCreator.apply(getValue0());
        }
        
        @Override
        default String toString0()
        {
            return PacketS2cEntityData.Entry.super.toString0();
        }
    }
}
