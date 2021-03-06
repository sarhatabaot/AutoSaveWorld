package autosaveworld.zlibs.com.dropbox.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/*>>> import checkers.nullness.quals.Nullable; */
/*>>> import checkers.nullness.quals.NonNull; */

public class LangUtil
{
    public static RuntimeException mkAssert(String messagePrefix, Throwable cause)
    {
        RuntimeException ae = new RuntimeException(messagePrefix + ": " + cause.getMessage());
        ae.initCause(cause);
        return ae;
        // XXX: Using AssertionError doesn't work for some reason (maybe Android bug).  Trying
        // to call initCause results in "cause already initialized" exception.  Switching to
        // RuntimeException worked.
    }

    public static AssertionError badType(Object a)
    {
        String msg;
        if (a == null) {
            msg = "bad type: null";
        } else {
            msg = "bad type: " + a.getClass().getName();
        }
        return new AssertionError(msg);
    }

    public static <T> T[] arrayConcat(T[] a, T[] b)
    {
        if (a == null) throw new IllegalArgumentException("'a' can't be null");
        if (b == null) throw new IllegalArgumentException("'b' can't be null");
        /*@Nullable*/T[] rn = Arrays.copyOf(a, a.length + b.length);
        System.arraycopy(b, 0, rn, a.length, b.length);
        T[] r = rn;
        return r;
    }

    public static <T> boolean nullableEquals(/*@Nullable*/T a, /*@Nullable*/T b)
    {
        if (a == null) return (b == null);
        if (b == null) return false;
        return a.equals(b);
    }

    public static int nullableHashCode(/*@Nullable*/Object o)
    {
        if (o == null) return 0;
        return o.hashCode() + 1;
    }

    public static Date truncateMillis(/*@Nullable*/Date date) {
        if (date != null) {
            long time = date.getTime();
            return new Date(time - (time % 1000L));
        } else {
            return date;
        }
    }

    public static List<Date> truncateMillis(/*@Nullable*/List<Date> dates) {
        if (dates != null) {
            List<Date> truncated = new ArrayList<Date>(dates.size());
            for (Date date : dates) {
                long time = date.getTime();
                truncated.add(new Date(time - (time % 1000L)));
            }
            return truncated;
        } else {
            return dates;
        }
    }
}
