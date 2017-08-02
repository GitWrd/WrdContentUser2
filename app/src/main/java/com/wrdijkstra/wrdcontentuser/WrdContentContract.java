package com.wrdijkstra.wrdcontentuser2;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by WDijkstra on 27-Jun-17.
 */

public class WrdContentContract {
    protected static final String TABLE = "counters";

    public static final String AUTHORITY =
            "com.wrdijkstra.wrdcontent.provider";

    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY);

    public static final class Counters
            implements CommonColumns {

        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(
                        WrdContentContract.CONTENT_URI,
                        "counters");

        /**
         * The mime type of a directory of items.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/com.wrdijkstra.wrdcontent." + TABLE;

        /**
         * The mime type of a single item.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +
                        "/com.wrdijkstra.wrdcontent." + TABLE;

        /**
         * A projection of all columns
         * in the items table.
         */
        public static final String[] PROJECTION_ALL =
                {_ID, _COUNT, LABEL, LOCKED};

        /**
         * The default sort order for
         * queries containing ID fields.
         */
        public static final String SORT_ORDER_DEFAULT =
                _ID + " ASC";
    }

    /**
     * This interface defines common columns
     * found in multiple tables.
     */
    public static interface CommonColumns
            extends BaseColumns {
        String LABEL = "label";
        String LOCKED = "locked";

    }
}
