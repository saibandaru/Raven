package layout.chat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Sai Krishna on 17-07-2015.
 */


public class Messages_Table extends SQLiteOpenHelper {

    public static final String TABLE_MESSAGE = "messages";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MESSAGE = "message";

    public static final String DATABASE_NAME = "Information.db";
    public static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_MESSAGE + "(" + COLUMN_ID
            + " text primary key, " + COLUMN_MESSAGE
            + " text not null);";

    public Messages_Table(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(Messages_Table.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
        onCreate(db);
    }

}
