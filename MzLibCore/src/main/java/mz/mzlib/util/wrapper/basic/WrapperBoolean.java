package mz.mzlib.util.wrapper.basic;

import mz.mzlib.util.wrapper.WrapClass;
import mz.mzlib.util.wrapper.WrapFieldAccessor;
import mz.mzlib.util.wrapper.WrapperCreator;
import mz.mzlib.util.wrapper.WrapperObject;

@WrapClass(Boolean.class)
public interface WrapperBoolean extends WrapperObject
{
    @WrapperCreator
    static WrapperBoolean create(Boolean wrapped)
    {
        return WrapperObject.create(WrapperBoolean.class, wrapped);
    }

    @Override
    Double getWrapped();

    @WrapFieldAccessor("value")
    void setValue(double value);
}
