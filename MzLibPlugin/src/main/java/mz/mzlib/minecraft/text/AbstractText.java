package mz.mzlib.minecraft.text;

import mz.mzlib.minecraft.VersionName;
import mz.mzlib.minecraft.wrapper.WrapMinecraftClass;
import mz.mzlib.minecraft.wrapper.WrapMinecraftFieldAccessor;
import mz.mzlib.util.wrapper.ListWrapped;
import mz.mzlib.util.wrapper.ListWrapper;
import mz.mzlib.util.wrapper.WrapperCreator;
import mz.mzlib.util.wrapper.WrapperObject;

import java.util.List;

@WrapMinecraftClass(
        {
                @VersionName(name = "net.minecraft.text.BaseText", end=1400),
                @VersionName(name = "net.minecraft.text.BaseText", begin=1400, end=1403),
                @VersionName(name = "net.minecraft.text.BaseText", begin=1403)
        })
public interface AbstractText extends WrapperObject,Text
{
    @WrapperCreator
    static AbstractText create(Object wrapped)
    {
        return WrapperObject.create(AbstractText.class, wrapped);
    }

    @WrapMinecraftFieldAccessor(@VersionName(name="style"))
    TextStyle getStyle();
    @WrapMinecraftFieldAccessor(@VersionName(name="style"))
    void setStyle(TextStyle value);

    default List<Text> getExtra()
    {
        return new ListWrapper<>(this.getExtra0(), Text::create);
    }
    default void setExtra(List<Text> value)
    {
        this.setExtra0(new ListWrapped<>(value, Text::create));
    }
    @WrapMinecraftFieldAccessor(@VersionName(name="siblings"))
    List<Object> getExtra0();
    @WrapMinecraftFieldAccessor(@VersionName(name="siblings"))
    void setExtra0(List<Object> value);
}
