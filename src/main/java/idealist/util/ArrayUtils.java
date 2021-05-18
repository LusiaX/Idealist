package idealist.util;

import java.lang.reflect.Array;

/**
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:11:35
 */
public abstract class ArrayUtils {
    public static final int INDEX_NOT_FOUND = -1;

    public static <T> boolean isNotEmpty(final T[] array) {
        return (array != null && array.length != 0);
    }

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    @SafeVarargs
    public static <T> T[] addAll(final T[] array1, final T... array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        }

        final Class<?> type1 = array1.getClass().getComponentType();
        @SuppressWarnings("unchecked") final T[] joinedArray = (T[]) Array.newInstance(type1, array1.length + array2.length);
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        try {
            System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        } catch (final ArrayStoreException ase) {
            final Class<?> type2 = array2.getClass().getComponentType();
            if (!type1.isAssignableFrom(type2)) {
                throw new IllegalArgumentException("Could not add " + type2.getName() + " object to "
                        + type1.getName() + " array.", ase);
            }
            throw ase; // No, so rethrow original
        }
        return joinedArray;
    }

    public static <T> T[] clone(final T[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    public static int[] addAll(final int[] array1, final int... array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        }
        final int[] joinedArray = new int[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    public static int[] clone(final int[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    public static <T> boolean contains(final T[] array, final T objectToFind) {
        return indexOf(array, objectToFind) != INDEX_NOT_FOUND;
    }

    public static <T> int indexOf(final T[] array, final T objectToFind) {
        return indexOf(array, objectToFind, 0);
    }

    public static <T> int indexOf(final T[] array, final T objectToFind, int startIndex) {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        if (objectToFind == null) {
            for (int i = startIndex; i < array.length; i++) {
                if (array[i] == null) {
                    return i;
                }
            }
        } else if (array.getClass().getComponentType().isInstance(objectToFind)) {
            for (int i = startIndex; i < array.length; i++) {
                if (objectToFind.equals(array[i])) {
                    return i;
                }
            }
        }
        return INDEX_NOT_FOUND;
    }
}
