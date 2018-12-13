package com.cmcc.internalcontact.db;

import com.cmcc.internalcontact.model.db.PersonModel;
import com.cmcc.internalcontact.model.db.PersonModel_Table;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;

@Migration(version = 3, database = AppDataBase.class)
public class Mirgate_Person_2_3 extends AlterTableMigration<PersonModel> {
    public Mirgate_Person_2_3(Class<PersonModel> table) {
        super(table);
    }

    @Override
    public void onPreMigrate() {
        addColumn(SQLiteType.INTEGER, PersonModel_Table.userSort.getNameAlias().name());
    }
}
