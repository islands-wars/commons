package fr.islandswars.commons.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Supplier;

/**
 * File <b>ReflectionUtil</b> located on fr.islandswars.commons.utils
 * ReflectionUtil is a part of commons.
 * <p>
 * Copyright (c) 2017 - 2024 Islands Wars.
 * <p>
 * commons is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <a href="http://www.gnu.org/licenses/">GNU license</a>.
 * <p>
 *
 * @author Jangliu, {@literal <jangliu@islandswars.fr>}
 * Created the 02/06/2024 at 20:37
 * @since 0.3
 */
public class ReflectionUtil {

    private static final Class[]  EMPTY_CLASS_ARRAY  = new Class[0];
    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    private ReflectionUtil() {
    }

    public static <T> T callMethod(Object instance, String method, Class<?>[] argsType, Object[] args) {
        return methodCall(findMethod(instance.getClass(), method, argsType), instance, args);
    }

    public static <T> T callStaticMethod(Class<?> clazz, String method, Class<?>[] argsType, Object[] args) {
        return methodCall(findMethod(clazz, method, argsType), null, args);
    }

    public static <T> T callStaticMethod(String clazz, String method, Class<?>[] argsType, Object[] args) {
        return methodCall(findMethod(getClass(clazz), method, argsType), null, args);
    }

    public static <T> T evilNewInstance(Class<T> clazz) {
        return classNewInstance(clazz);
    }

    private static <T> T classNewInstance(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new ReflectionException(e);
        }
    }

    public static <T> T evilNewInstance(String clazz) {
        return classNewInstance(getClass(clazz));
    }

    public static <T> Class<T> getClass(String clazz) {
        return findClass(clazz);
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<T> findClass(String name) {
        try {
            return (Class<T>) Class.forName(name);
        } catch (ReflectiveOperationException e) {
            throw new ReflectionException(e);
        }
    }

    public static <T> ConstructorAccessor<T> getConstructorAccessor(Class<T> clazz, Class<?>... args) {
        return getConstructorAccessor(findConstructor(clazz, args));
    }

    public static <T> ConstructorAccessor<T> getConstructorAccessor(Constructor<T> constructor) {
        constructor.setAccessible(true); // Disable Accessible check -- Faster
        return () -> constructor;
    }

    private static <T> Constructor<T> findConstructor(Class<T> clazz, Class<?>[] argsType) {
        try {
            return clazz.getDeclaredConstructor(argsType);
        } catch (ReflectiveOperationException e) {
            throw new ReflectionException(e);
        }
    }

    public static <T> ConstructorAccessor<T> getConstructorAccessor(String clazz, Class<?>... args) {
        return getConstructorAccessor(findConstructor(getClass(clazz), args));
    }

    public static <T> FieldAccessor<T> getCountFieldOfTypeAccessor(Class<?> clazz, Class<?> type, int count) {
        return getFieldAccessor(countFieldOfType(clazz, type, count));
    }

    public static <T> FieldAccessor<T> getCountFieldOfTypeAccessor(Class<?> clazz, String type, int count) {
        return getCountFieldOfTypeAccessor(clazz, getClass(type), count);
    }

    public static <T> FieldAccessor<T> getCountFieldOfTypeAccessor(String clazz, Class<?> type, int count) {
        return getCountFieldOfTypeAccessor(getClass(clazz), type, count);
    }

    public static <T> FieldAccessor<T> getCountFieldOfTypeAccessor(String clazz, String type, int count) {
        return getCountFieldOfTypeAccessor(getClass(clazz), getClass(type), count);
    }

    public static <T> T getCountValueOfType(Object instance, Class<?> type, int count) {
        return fieldGet(countFieldOfType(instance.getClass(), type, count), instance);
    }

    public static <T> T getCountValueOfType(Object instance, String type, int count) {
        return fieldGet(countFieldOfType(instance.getClass(), getClass(type), count), instance);
    }

    public static <T> FieldAccessor<T> getFieldAccessor(Class<?> clazz, String name) {
        return getFieldAccessor(findField(clazz, name));
    }

    public static <T> FieldAccessor<T> getFieldAccessor(String clazz, String name) {
        return getFieldAccessor(findField(getClass(clazz), name));
    }

    public static <T> FieldAccessor<T> getFieldAccessor(Field field) {
        field.setAccessible(true); // Disable Accessible check -- Faster
        return () -> field;
    }

    public static <T> FieldAccessor<T> getFirstFieldOfTypeAccessor(Class<?> clazz, Class<?> type) {
        return getCountFieldOfTypeAccessor(clazz, type, 0);
    }

    public static <T> FieldAccessor<T> getFirstFieldOfTypeAccessor(Class<?> clazz, String type) {
        return getCountFieldOfTypeAccessor(clazz, getClass(type), 0);
    }

    public static <T> FieldAccessor<T> getFirstFieldOfTypeAccessor(String clazz, Class<?> type) {
        return getCountFieldOfTypeAccessor(getClass(clazz), type, 0);
    }

    public static <T> FieldAccessor<T> getFirstFieldOfTypeAccessor(String clazz, String type) {
        return getCountFieldOfTypeAccessor(getClass(clazz), getClass(type), 0);
    }

    public static <T> T getFirstValueOfType(Object instance, Class<?> type) {
        return fieldGet(countFieldOfType(instance.getClass(), type, 0), instance);
    }

    public static <T> T getFirstValueOfType(Object instance, String type) {
        return fieldGet(countFieldOfType(instance.getClass(), getClass(type), 0), instance);
    }

    public static <T> MethodAccessor<T> getMethodAccessor(Class<?> clazz, String name, Class<?>... args) {
        return getMethodAccessor(findMethod(clazz, name, args));
    }

    public static <T> MethodAccessor<T> getMethodAccessor(String clazz, String name, Class<?>... args) {
        return getMethodAccessor(findMethod(getClass(clazz), name, args));
    }

    public static <T> MethodAccessor<T> getMethodAccessor(Method method) {
        method.setAccessible(true); // Disable Accessible check -- Faster
        return () -> method;
    }

    public static String getPackage(String clazz) {
        int index = clazz.lastIndexOf('.');
        return index > 0 ? clazz.substring(0, index) : ""; //Empty string if default package
    }

    public static <T> T getStaticValue(Class<?> clazz, String field) {
        return fieldGet(findField(clazz, field), null);
    }

    public static <T> T getStaticValue(String clazz, String field) {
        return fieldGet(findField(getClass(clazz), field), null);
    }

    public static <T> T getValue(Object instance, String field) {
        return fieldGet(findField(instance.getClass(), field), instance);
    }

    public static <T> T newInstance(Class<T> clazz, Class<?>[] argsType, Object[] args) {
        return constructorNewInstance(findConstructor(clazz, argsType), args);
    }

    private static <T> T constructorNewInstance(Constructor<T> method, Object[] args) {
        try {
            method.setAccessible(true);
            return method.newInstance(args);
        } catch (ReflectiveOperationException e) {
            throw new ReflectionException(e);
        }
    }

    public static <T> T newInstance(String clazz, Class<?>[] argsType, Object[] args) {
        return constructorNewInstance(findConstructor(getClass(clazz), argsType), args);
    }

    public static <T> T newInstance(Class<T> clazz) {
        return constructorNewInstance(findConstructor(clazz, EMPTY_CLASS_ARRAY), EMPTY_OBJECT_ARRAY);
    }

    public static <T> T newInstance(String clazz) {
        return constructorNewInstance(findConstructor(getClass(clazz), EMPTY_CLASS_ARRAY), EMPTY_OBJECT_ARRAY);
    }

    public static void setField(Object instance, String field, Object value) {
        fieldSet(findField(instance.getClass(), field), instance, value);
    }

    public static void setFirstValueOfType(Object instance, Class<?> type, Object value) {
        fieldSet(countFieldOfType(instance.getClass(), type, 0), instance, value);
    }

    public static void setFirstValueOfType(Object instance, String type, Object value) {
        fieldSet(countFieldOfType(instance.getClass(), getClass(type), 0), instance, value);
    }

    public static void setStaticField(Class<?> clazz, String field, Object value) {
        fieldSet(findField(clazz, field), null, value);
    }

    public static void setStaticField(String clazz, String field, Object value) {
        fieldSet(findField(getClass(clazz), field), null, value);
    }

    private static Field countFieldOfType(Class<?> clazz, Class<?> type, int count) {
        return countFieldOfType(clazz, type, count, clazz);
    }

    private static Field countFieldOfType(Class<?> clazz, Class<?> type, int count, Class<?> search) {
        var index = 0;
        for (var field : search.getDeclaredFields()) {
            if (field.getType() == type) {
                if (index == count) {
                    field.setAccessible(true);
                    return field;
                }
                index++;
            }
        }

        Class<?> superClass = search.getSuperclass();
        if (superClass != null) return countFieldOfType(clazz, type, count, superClass);
        throw new ReflectionException("Cannot find a field with packet " + type + " in " + clazz + '.');
    }

    @SuppressWarnings("unchecked")
    private static <T> T fieldGet(Field field, Object inst) {
        try {
            field.setAccessible(true);
            return (T) field.get(inst);
        } catch (ReflectiveOperationException e) {
            throw new ReflectionException(e);
        }
    }

    private static void fieldSet(Field field, Object inst, Object value) {
        try {
            field.setAccessible(true);
            field.set(inst, value);
        } catch (ReflectiveOperationException e) {
            throw new ReflectionException(e);
        }
    }

    private static Field findField(Class<?> clazz, String name) {
        return findField(clazz, name, clazz);
    }

    private static Field findField(Class<?> clazz, String name, Class<?> search) {
        Field[] fields = search.getDeclaredFields();
        for (var field : fields)
            if (field.getName().equals(name)) return field;

        Class<?> superClass = search.getSuperclass();

        if (superClass != null) return findField(clazz, name, superClass);

        throw new ReflectionException("Cannot find field " + name + " in " + clazz);
    }

    private static Method findMethod(Class<?> clazz, String name, Class<?>[] argsType) {
        return findMethod(clazz, name, argsType, clazz);
    }

    private static Method findMethod(Class<?> clazz, String name, Class<?>[] argsType, Class<?> search) {
        Method[] methods = search.getDeclaredMethods();
        for (var method : methods)
            if (method.getName().equals(name) && Arrays.equals(argsType, method.getParameterTypes()) && (!search.isInterface() || method.isDefault())) //Default methods in interfaces are fine
                return method;

        var     superClass = search.getSuperclass();
        Class[] interfaces = search.getInterfaces();

        for (var interf : interfaces)
            findMethod(interf, name, argsType); //Find default methods in interfaces

        if (superClass != null) return findMethod(clazz, name, argsType, superClass);

        throw new ReflectionException("Cannot find field " + name + " in " + clazz);
    }

    @SuppressWarnings("unchecked")
    private static <T> T methodCall(Method method, Object inst, Object[] args) {
        try {
            method.setAccessible(true);
            return (T) method.invoke(inst, args);
        } catch (ReflectiveOperationException e) {
            throw new ReflectionException(e);
        }
    }

    @FunctionalInterface
    public interface FieldAccessor<T> extends Supplier<Field> {

        @SuppressWarnings("unchecked")
        default T get(Object instance) {
            try {
                return (T) get().get(instance);
            } catch (ReflectiveOperationException e) {
                throw new ReflectionException(e);
            }
        }

        default Class<?> getDeclaringClass() {
            return get().getDeclaringClass();
        }

        @SuppressWarnings("unchecked")
        default Class<T> getType() {
            return (Class<T>) get().getType();
        }

        default boolean has(Class<?> target) {
            return get().getDeclaringClass().isAssignableFrom(target);
        }

        default void set(Object instance, T value) {
            try {
                get().set(instance, value);
            } catch (ReflectiveOperationException e) {
                throw new ReflectionException(e);
            }
        }
    }

    @FunctionalInterface
    public interface MethodAccessor<T> extends Supplier<Method> {

        default Class<?> getDeclaringClass() {
            return get().getDeclaringClass();
        }

        default Class<?>[] getExceptionTypes() {
            return get().getExceptionTypes();
        }

        default Class<?>[] getParameterTypes() {
            return get().getParameterTypes();
        }

        @SuppressWarnings("unchecked")
        default Class<T> getReturnType() {
            return (Class<T>) get().getReturnType();
        }

        default boolean has(Class<?> target) {
            return get().getDeclaringClass().isAssignableFrom(target);
        }

        @SuppressWarnings("unchecked")
        default T invoke(Object instance, Object... args) {
            try {
                return (T) get().invoke(instance, args);
            } catch (ReflectiveOperationException e) {
                throw new ReflectionException(e);
            }
        }
    }


    @FunctionalInterface
    public interface ConstructorAccessor<T> extends Supplier<Constructor<T>> {

        default Class<?> getDeclaringClass() {
            return get().getDeclaringClass();
        }

        default Class<?>[] getExceptionTypes() {
            return get().getExceptionTypes();
        }

        default Class<?>[] getParameterTypes() {
            return get().getParameterTypes();
        }

        default boolean has(Class<?> target) {
            return get().getDeclaringClass().isAssignableFrom(target);
        }

        default T newInstance(Object... args) {
            try {
                return get().newInstance(args);
            } catch (ReflectiveOperationException e) {
                throw new ReflectionException(e);
            }
        }
    }

    public static final class ReflectionException extends RuntimeException {

        private ReflectionException() {
        }

        private ReflectionException(String arg0) {
            super(arg0);
        }

        private ReflectionException(String arg0, Throwable arg1) {
            super(arg0, arg1);
        }

        public ReflectionException(Throwable arg0) {
            super(arg0);
        }
    }
}

