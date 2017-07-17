package minasedrak.squawker.provider;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by MinaSedrak on 7/16/2017.
 */

@Database(version = SquawkDatabase.VERSION)
public class SquawkDatabase {

    public static final int VERSION = 4;

    @Table(SquawkContract.class)
    public static final String SQUAWK_MESSAGES = "squawk_messages";
}
