package mz.mzlib.minecraft.nbt;

import mz.mzlib.minecraft.VersionName;
import mz.mzlib.minecraft.wrapper.WrapMinecraftClass;
import mz.mzlib.minecraft.wrapper.WrapMinecraftFieldAccessor;
import mz.mzlib.util.wrapper.WrapperObject;
import mz.mzlib.util.wrapper.WrapConstructor;
import mz.mzlib.util.wrapper.WrapperCreator;

@WrapMinecraftClass({@VersionName(end = 1400, name = "net.minecraft.nbt.NbtLongArray"), @VersionName(begin = 1400, end = 1605, name = "net.minecraft.nbt.LongArrayTag"), @VersionName(begin = 1605, name = "net.minecraft.nbt.NbtLongArray")})
public interface NBTLongArray extends NBTElement
{
    @SuppressWarnings("deprecation")
    @WrapperCreator
    static NBTLongArray create(Object wrapped)
    {
        return WrapperObject.create(NBTLongArray.class, wrapped);
    }

    @WrapConstructor
    NBTLongArray staticNewInstance(long[] value);
    static NBTLongArray newInstance(long[] value)
    {
        return create(null).staticNewInstance(value);
    }

    @WrapMinecraftFieldAccessor(@VersionName(name = "value"))
    long[] getValue();
    @WrapMinecraftFieldAccessor(@VersionName(name = "value"))
    void setValue(long[] value);

    default long get(int index)
    {
        return this.getValue()[index];
    }

    default void set(int index, long value)
    {
        this.getValue()[index] = value;
    }
}
