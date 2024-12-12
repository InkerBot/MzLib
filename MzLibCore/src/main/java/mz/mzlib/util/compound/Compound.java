package mz.mzlib.util.compound;

import mz.mzlib.asm.ClassWriter;
import mz.mzlib.asm.Opcodes;
import mz.mzlib.asm.tree.ClassNode;
import mz.mzlib.asm.tree.MethodNode;
import mz.mzlib.util.ClassUtil;
import mz.mzlib.util.ElementSwitcher;
import mz.mzlib.util.RuntimeUtil;
import mz.mzlib.util.asm.AsmUtil;
import mz.mzlib.util.wrapper.WrappedClassFinder;
import mz.mzlib.util.wrapper.WrappedClassFinderClass;
import mz.mzlib.util.wrapper.WrapperClassInfo;
import mz.mzlib.util.wrapper.WrapperObject;

import java.lang.annotation.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@WrappedClassFinderClass(Compound.Handler.class)
public @interface Compound
{
    class Handler implements WrappedClassFinder
    {
        @Override
        @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
        public Class<?> find(Class<? extends WrapperObject> wrapperClass, Annotation annotation) throws ClassNotFoundException
        {
            synchronized (wrapperClass)
            {
                String className = wrapperClass.getName() + "$0mzCompoundFianl";
                try
                {
                    return Class.forName(className, true, wrapperClass.getClassLoader());
                }
                catch (ClassNotFoundException ignored)
                {
                }
                className=AsmUtil.getType(className);
                ClassNode cn=new ClassNode();
                Set<Class<?>> superclasses = getSuperclasses(wrapperClass);
                Set<Class<?>> interfaces=superclasses.stream().filter(Class::isInterface).collect(Collectors.toSet());
                superclasses=superclasses.stream().filter(i->!i.isInterface()).collect(Collectors.toSet());
                Class<?> superclass=superclasses.stream().findFirst().orElse(Object.class);
                boolean flag=true;
                for(Class<?> i:superclasses)
                {
                    flag=true;
                    for(Class<?> j:superclasses)
                    {
                        if(!j.isAssignableFrom(i))
                            flag=false;
                    }
                    if(flag)
                    {
                        superclass=i;
                        break;
                    }
                }
                if(!flag)
                    throw new IllegalStateException("You can only extend a class.");
                cn.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, className, null, AsmUtil.getType(superclass), interfaces.stream().map(AsmUtil::getType).toArray(String[]::new));
                for(Method method:wrapperClass.getMethods())
                {
                    if(Modifier.isStatic(method.getModifiers())||!ElementSwitcher.isEnabled(method))
                        continue;
                    PropAccessor propAccessor=method.getDeclaredAnnotation(PropAccessor.class);
                    if(propAccessor!=null)
                    {
                        if(AsmUtil.getFieldNode(cn, propAccessor.value())==null)
                        {
                            Class<?> type;
                            switch(method.getParameterCount())
                            {
                                case 0:
                                    type = method.getReturnType();
                                    break;
                                case 1:
                                    type = method.getParameterTypes()[0];
                                    break;
                                default:
                                    throw new IllegalArgumentException("Too many args of "+method+".");
                            }
                            if(WrapperObject.class.isAssignableFrom(type))
                                type = WrapperObject.getWrappedClass(RuntimeUtil.cast(type));
                            cn.visitField(Opcodes.ACC_PUBLIC, propAccessor.value(), AsmUtil.getDesc(type), null, null).visitEnd();
                        }
                    }
                    CompoundOverride compoundOverride=method.getDeclaredAnnotation(CompoundOverride.class);
                    if(compoundOverride!=null)
                    {
                        Set<Method> wrapper = Arrays.stream(wrapperClass.getInterfaces()).map(Class::getMethods).flatMap(Arrays::stream).filter(j -> j.getName().equals(compoundOverride.value()) && Arrays.equals(j.getParameterTypes(), method.getParameterTypes())).collect(Collectors.toSet());
                        if(wrapper.isEmpty())
                            throw new IllegalStateException("Wrapper method not found: "+method);
                        Method tar=wrapper.iterator().next();
                        if(ElementSwitcher.isEnabled(tar))
                        {
                            tar = Objects.requireNonNull((Method)WrapperClassInfo.get(RuntimeUtil.cast(tar.getDeclaringClass())).wrappedMembers.get(tar), "Wrapped method of "+tar);
                            MethodNode mn = new MethodNode(Opcodes.ACC_PUBLIC, tar.getName(), AsmUtil.getDesc(tar), null, null);
                            mn.instructions.add(AsmUtil.insnVarLoad(Object.class, 0));
                            mn.instructions.add(AsmUtil.insnCreateWrapper(wrapperClass));
                            Class<?>[] tarParams = tar.getParameterTypes(), srcParams = method.getParameterTypes();
                            for(int i = 0, j = 1; i<tarParams.length; i++)
                            {
                                mn.instructions.add(AsmUtil.insnVarLoad(tarParams[i], j));
                                if(WrapperObject.class.isAssignableFrom(srcParams[i]))
                                {
                                    mn.instructions.add(AsmUtil.insnCast(Object.class, tarParams[i]));
                                    mn.instructions.add(AsmUtil.insnCreateWrapper(RuntimeUtil.<Class<? extends WrapperObject>>cast(srcParams[i])));
                                }
                                else
                                    mn.instructions.add(AsmUtil.insnCast(srcParams[i], tarParams[i]));
                                j += AsmUtil.getCategory(tarParams[i]);
                            }
                            mn.visitMethodInsn(Opcodes.INVOKEINTERFACE, AsmUtil.getType(wrapperClass), method.getName(), AsmUtil.getDesc(method), true);
                            if(WrapperObject.class.isAssignableFrom(method.getReturnType()))
                            {
                                mn.instructions.add(AsmUtil.insnGetWrapped());
                                mn.instructions.add(AsmUtil.insnCast(tar.getReturnType(), Object.class));
                            }
                            else
                                mn.instructions.add(AsmUtil.insnCast(tar.getReturnType(), method.getReturnType()));
                            mn.instructions.add(AsmUtil.insnReturn(tar.getReturnType()));
                            mn.visitEnd();
                            cn.methods.add(mn);
                        }
                    }
                }
                if(Delegator.class.isAssignableFrom(wrapperClass))
                {
                    String delegateField="mzlib$Delegate";
                    cn.visitField(Opcodes.ACC_PUBLIC,delegateField,AsmUtil.getDesc(Object.class),null,null).visitEnd();
                    MethodNode mn=new MethodNode(Opcodes.ACC_PUBLIC, "getDelegate", AsmUtil.getDesc(Object.class,new Class[0]), null, new String[0]);
                    mn.instructions.add(AsmUtil.insnVarLoad(Object.class, 0));
                    mn.visitFieldInsn(Opcodes.GETFIELD, className, delegateField, AsmUtil.getDesc(Object.class));
                    mn.instructions.add(AsmUtil.insnReturn(Object.class));
                    mn.visitEnd();
                    cn.methods.add(mn);
                    mn=new MethodNode(Opcodes.ACC_PUBLIC, "setDelegate", AsmUtil.getDesc(void.class, Object.class), null, new String[0]);
                    mn.instructions.add(AsmUtil.insnVarLoad(Object.class, 0));
                    mn.instructions.add(AsmUtil.insnVarLoad(Object.class, 1));
                    mn.visitFieldInsn(Opcodes.PUTFIELD, className, delegateField, AsmUtil.getDesc(Object.class));
                    mn.instructions.add(AsmUtil.insnReturn(void.class));
                    mn.visitEnd();
                    cn.methods.add(mn);
                    for(Method method:superclasses.stream().map(Class::getMethods).flatMap(Arrays::stream).collect(Collectors.toSet()))
                    {
                        String desc = AsmUtil.getDesc(method);
                        if(Modifier.isStatic(method.getModifiers()) || Modifier.isPrivate(method.getModifiers()) || Modifier.isFinal(method.getModifiers()) || cn.methods.stream().anyMatch(it-> Objects.equals(desc,it.desc)&&Objects.equals(method.getName(),it.name)))
                            continue;
                        mn=new MethodNode(Opcodes.ACC_PUBLIC, method.getName(), AsmUtil.getDesc(method), null, new String[0]);
                        mn.instructions.add(AsmUtil.insnVarLoad(Object.class, 0));
                        mn.visitFieldInsn(Opcodes.GETFIELD, className, delegateField, AsmUtil.getDesc(Object.class));
                        mn.instructions.add(AsmUtil.insnCast(method.getDeclaringClass(),Object.class));
                        int i=1;
                        for(Class<?> param:method.getParameterTypes())
                        {
                            mn.instructions.add(AsmUtil.insnVarLoad(param,i));
                            i+=AsmUtil.getCategory(param);
                        }
                        mn.visitMethodInsn(Modifier.isInterface(method.getModifiers())?Opcodes.INVOKEINTERFACE:Opcodes.INVOKEVIRTUAL, AsmUtil.getType(method.getDeclaringClass()), method.getName(), AsmUtil.getDesc(method), Modifier.isInterface(method.getModifiers()));
                        mn.instructions.add(AsmUtil.insnReturn(method.getReturnType()));
                        mn.visitEnd();
                        cn.methods.add(mn);
                    }
                }
                else
                {
                    for (Constructor<?> constructor : superclass.getDeclaredConstructors())
                    {
                        if (Modifier.isPrivate(constructor.getModifiers()))
                            continue;
                        MethodNode mn = new MethodNode(Opcodes.ACC_PUBLIC, "<init>", AsmUtil.getDesc(constructor), null, null);
                        mn.instructions.add(AsmUtil.insnVarLoad(Object.class, 0));
                        int i = 1;
                        for (Class<?> param : constructor.getParameterTypes())
                        {
                            mn.instructions.add(AsmUtil.insnVarLoad(param, i));
                            i += AsmUtil.getCategory(param);
                        }
                        mn.visitMethodInsn(Opcodes.INVOKESPECIAL, AsmUtil.getType(superclass), "<init>", AsmUtil.getDesc(constructor), false);
                        mn.instructions.add(AsmUtil.insnReturn(void.class));
                        mn.visitEnd();
                        cn.methods.add(mn);
                    }
                }
                ClassWriter cw=new ClassWriter(ClassWriter.COMPUTE_FRAMES|ClassWriter.COMPUTE_MAXS);
                cn.accept(cw);
                return ClassUtil.defineClass(wrapperClass.getClassLoader(), className, cw.toByteArray());
            }
        }

        public Set<Class<?>> getSuperclasses(Class<? extends WrapperObject> compoundClass)
        {
            if(!compoundClass.isAnnotationPresent(Compound.class))
                return Collections.singleton(WrapperObject.getWrappedClass(compoundClass));
            Set<Class<?>> result=new HashSet<>();
            for(Class<?> i:compoundClass.getInterfaces())
            {
                if(WrapperObject.class.isAssignableFrom(i))
                    result.addAll(this.getSuperclasses(RuntimeUtil.cast(i)));
            }
            return result;
        }
    }
}
