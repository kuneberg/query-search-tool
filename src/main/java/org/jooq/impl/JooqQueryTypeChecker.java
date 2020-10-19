package org.jooq.impl;

import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.SQLDialect;

import java.lang.reflect.Field;

public class JooqQueryTypeChecker {


    public boolean isUpdateFromQuery(String queryString) {
        try {
            Queries queries = DSL.using(SQLDialect.POSTGRES)
                    .parser()
                    .parse(queryString);

            Query query = queries.queries()[0];
            if (query instanceof UpdateImpl) {
                Query delegate = ((UpdateImpl) query).getDelegate();
                if (delegate instanceof UpdateQueryImpl) {
                    UpdateQueryImpl updateQuery = (UpdateQueryImpl) delegate;

                    Field fromField = UpdateQueryImpl.class.
                            getDeclaredField("from");
                    fromField.setAccessible(true);
                    TableList from = (TableList) fromField.get(updateQuery);
                    if (from != null && from.size() > 0) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return false;
    }

}
