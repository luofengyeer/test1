package com.cmcc.internalcontact.db;

import com.cmcc.internalcontact.model.db.DepartModel;
import com.cmcc.internalcontact.model.db.DepartModel_Table;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;

@Migration(version = 3, database = AppDataBase.class)
public class Mirgate_depart_2_3 extends AlterTableMigration<DepartModel> {
    public Mirgate_depart_2_3(Class<DepartModel> table) {
        super(table);
    }

    @Override
    public void onPreMigrate() {
        addColumn(SQLiteType.INTEGER, DepartModel_Table.treeSort.getNameAlias().name());
    }
}
