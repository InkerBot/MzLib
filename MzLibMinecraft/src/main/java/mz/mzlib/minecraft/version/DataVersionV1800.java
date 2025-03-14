package mz.mzlib.minecraft.version;

import mz.mzlib.minecraft.VersionName;
import mz.mzlib.minecraft.wrapper.WrapMinecraftClass;
import mz.mzlib.minecraft.wrapper.WrapMinecraftMethod;
import mz.mzlib.util.wrapper.WrapperCreator;
import mz.mzlib.util.wrapper.WrapperObject;

@WrapMinecraftClass(@VersionName(name="net.minecraft.SaveVersion", begin=1800))
public interface DataVersionV1800 extends WrapperObject
{
    @WrapperCreator
    static DataVersionV1800 create(Object wrapped)
    {
        return WrapperObject.create(DataVersionV1800.class, wrapped);
    }
    
    @WrapMinecraftMethod(@VersionName(name="getId"))
    int getNumber();
}
