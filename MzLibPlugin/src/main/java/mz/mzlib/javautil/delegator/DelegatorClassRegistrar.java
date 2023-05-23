package mz.mzlib.javautil.delegator;

import mz.mzlib.javautil.Instance;
import mz.mzlib.javautil.RuntimeUtil;
import mz.mzlib.module.IRegistrar;
import mz.mzlib.module.MzModule;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DelegatorClassRegistrar implements IRegistrar<Class<? extends Delegator>>, Instance
{
	public static DelegatorClassRegistrar instance=new DelegatorClassRegistrar();
	
	public Map<Class<? extends Delegator>,DelegatorClassRegistration> registrations=new ConcurrentHashMap<>();
	
	@Override
	public Class<Class<? extends Delegator>> getType()
	{
		return RuntimeUtil.forceCast(Class.class);
	}
	@Override
	public boolean isRegistrable(Class<? extends Delegator> object)
	{
		return Delegator.class.isAssignableFrom(object);
	}
	
	@Override
	public void register(MzModule module,Class<? extends Delegator> object)
	{
		registrations.put(object,new DelegatorClassRegistration(object));
	}
	@Override
	public void unregister(MzModule module,Class<? extends Delegator> object)
	{
		registrations.remove(object);
	}
}
