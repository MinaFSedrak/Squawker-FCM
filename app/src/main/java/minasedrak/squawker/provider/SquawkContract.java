package minasedrak.squawker.provider;

import android.content.SharedPreferences;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by MinaSedrak on 7/16/2017.
 */

public class SquawkContract {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
    @AutoIncrement
    public static final String COLUMN_ID = "_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_AUTHOR = "author";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_AUTHOR_KEY = "authorKey";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_MESSAGE = "message";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    public static final String COLUMN_DATE = "date";

    // Topic keys as matching what is found in the database
    public static final String ASSER_KEY = "key_asser";
    public static final String JLIN_KEY = "key_jlin";
    public static final String LYLA_KEY = "key_lyla";
    public static final String NIKITA_KEY = "key_nikita";
    public static final String CEZANNE_KEY = "key_cezanne";
    public static final String TEST_ACCOUNT_KEY = "key_test";


    public static final String[] INSTRUCTOR_KEYS = {
            ASSER_KEY, JLIN_KEY, LYLA_KEY, NIKITA_KEY, CEZANNE_KEY
    };


    /**
     * Creates a SQLite SELECTION parameter that filters just the rows for the authors you are
     * currently following.
     */
    public static String createSelectionForCurrentFollowers(SharedPreferences sharedPreferences){

        StringBuilder  statement = new StringBuilder();

        statement.append(COLUMN_AUTHOR_KEY).append(" IN ('").append(TEST_ACCOUNT_KEY).append("'");

        for (String key: INSTRUCTOR_KEYS){
            if (sharedPreferences.getBoolean(key, false)){
                statement.append(",");
                statement.append("'").append(key).append("'");
            }
        }

        statement.append(")");
        return  statement.toString();
    }

}
