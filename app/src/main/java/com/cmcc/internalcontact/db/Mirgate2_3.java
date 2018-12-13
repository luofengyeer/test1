package com.cmcc.internalcontact.db;

import com.cmcc.internalcontact.model.db.DepartPersonModel;
import com.cmcc.internalcontact.model.db.DepartPersonModel_Table;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;

@Migration(version = 3, database = AppDataBase.class)
public class Mirgate2_3 extends AlterTableMigration<DepartPersonModel> {
    public Mirgate2_3(Class<DepartPersonModel> table) {
        super(table);
    }

    @Override
    public void onPreMigrate() {
        addColumn(SQLiteType.INTEGER, DepartPersonModel_Table.udSort.getNameAlias().name());
    }
}
