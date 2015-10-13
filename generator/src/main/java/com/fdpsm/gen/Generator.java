package com.fdpsm.gen;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class Generator {
    private static final String TARGET_FOLDER = "./app/src/main/java/";
    private static final int SCHEMA_VERSION = 1;

    public static final void main(String[] args) throws Throwable {
        Schema schema = new Schema(SCHEMA_VERSION, "com.fdpsm.exercise.dao");

        // for toString methods --> easier for using the ArrayAdapter
        schema.enableKeepSectionsByDefault();

        // defining the query entity
        Entity query = schema.addEntity("MovieQuery");
        query.setTableName("MOVIE_QUERY");
        query.addIdProperty();
        query.addStringProperty("searchText").notNull().index(); // setting an index
        query.addDateProperty("searchDate").notNull();


        // defining the query result entity
        Entity queryResult = schema.addEntity("MovieResult");
        queryResult.setTableName("MOVIE_RESULT");
        queryResult.addIdProperty();
        queryResult.addStringProperty("title").notNull().index();
        // this field is used to refer to the query within the query result table
        Property queryId = queryResult.addLongProperty("queryId").notNull().getProperty();


        // To-Many relationship, a query has multiple query results, the id is stored in the target of the To-Many relation
        query.addToMany(queryResult, queryId);
        // the following line of code allows accessing the query from the query result entity
        queryResult.addToOne(query, queryId);


        // defining the detail entity
        Entity detail = schema.addEntity("MovieDetail");
        detail.setTableName("MOVIE_DETAIL");
        detail.addIdProperty();
        detail.addStringProperty("title").unique().notNull().index(); // setting an index
        detail.addStringProperty("releaseDate");
        detail.addStringProperty("runtime");
        detail.addStringProperty("posterUrl");

        // we are generating the schema here
        new DaoGenerator().generateAll(schema, TARGET_FOLDER);
    }
}
