package com.myapplication.contentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.v4.database.DatabaseUtilsCompat;

import com.myapplication.base.Constants;
import com.myapplication.data.database.DatabaseHelper;

import java.io.IOException;

public class CitiesDBProvider extends ContentProvider {

    private DatabaseHelper mOpenHelper;
    private static final String UNKNOWN_URI_LOG = "Unknown URI ";
    private static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

        URI_MATCHER.addURI(Constants.CITIES_AUTHORITY, Constants.CITY_TABLE_NAME,
                Constants.CITIES);
        URI_MATCHER.addURI(Constants.CITIES_AUTHORITY, Constants.CITY_TABLE_NAME + "/#",
                Constants.CITIES_ID);
    }

    /*
     * 杩斿洖URI鍙傛暟瀵瑰簲鐨勬暟鎹殑MIME绫诲瀷
     *
     * @see android.content.ContentProvider#getType(android.net.Uri)
     */
    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case Constants.CITIES:
                return Constants.CITIES_CONTENT_TYPE;
            case Constants.CITIES_ID:
                return Constants.CITIES_CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException(UNKNOWN_URI_LOG + uri);
        }
    }

    /**
     * 鍙敤绗竴娆″畨瑁卆pk浼氳皟鍔╫nCreate()鏂规硶
     * @return
     */
    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        try {
            mOpenHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] finalSelectionArgs = selectionArgs;
        int matched = URI_MATCHER.match(uri);
        Uri regUri = uri;
        String finalSortOrder = sortOrder;
        String finalGrouping = null;
        String finalHaving = null;
        Cursor c;
        switch (matched) {
            case Constants.CITIES:
                qb.setTables(Constants.CITY_TABLE_NAME);

                break;
            case Constants.CITIES_ID:
                qb.setTables(Constants.CITY_TABLE_NAME);
                qb.appendWhere(Constants.CITY_ID + "=?");
                finalSelectionArgs = DatabaseUtilsCompat.appendSelectionArgs(
                        selectionArgs, new String[]{uri.getLastPathSegment()});
                break;
            default:
                break;
        }
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        c = qb.query(db, projection, selection, finalSelectionArgs,
                finalGrouping, finalHaving, finalSortOrder);

        c.setNotificationUri(getContext().getContentResolver(), regUri);
        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int matched = URI_MATCHER.match(uri);
        String matchedTable = null;
        Uri baseInsertedUri = null;
        switch (matched) {
            case Constants.CITIES:
            case Constants.CITIES_ID:
                matchedTable = Constants.CITY_TABLE_NAME;
                break;
            default:
                break;
        }
        if (matchedTable == null) {
            throw new IllegalArgumentException(UNKNOWN_URI_LOG + uri);
        }
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long rowId = db.insert(matchedTable, null, values);
        if (rowId >= 0) {
            Uri retUri = ContentUris.withAppendedId(baseInsertedUri, rowId);
            getContext().getContentResolver().notifyChange(retUri, null);
            return retUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String finalWhere;
        int count = 0;
        int matched = URI_MATCHER.match(uri);
        Uri regUri = uri;
        switch (matched) {
            case Constants.CITIES:
                count = db.delete(Constants.CITY_TABLE_NAME, selection,
                        selectionArgs);
                break;
            case Constants.CITIES_ID:
                // 鎷兼帴鍦板潃
                finalWhere = DatabaseUtilsCompat.concatenateWhere(Constants.CITY_ID
                        + "=" + ContentUris.parseId(uri), selection);
                count = db.delete(Constants.CITY_TABLE_NAME, finalWhere,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException(UNKNOWN_URI_LOG + uri);
        }
        getContext().getContentResolver().notifyChange(regUri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        String finalWhere;
        int matched = URI_MATCHER.match(uri);
        switch (matched) {
            case Constants.CITIES:
                count = db.update(Constants.CITY_TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case Constants.CITIES_ID:
                // 鎷兼帴鍦板潃
                finalWhere = DatabaseUtilsCompat.concatenateWhere(Constants.CITY_ID
                        + "=" + ContentUris.parseId(uri), selection);
                count = db.update(Constants.CITY_TABLE_NAME, values, finalWhere,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException(UNKNOWN_URI_LOG + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

}

