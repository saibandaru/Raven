package com.example.saikrishna.raven.DataBase.Tables.Table_Interface;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Sai Krishna on 8/4/2015.
 */
@ParseClassName("Repository")
public class Repository_Parse extends ParseObject {
    public ParseFile getPP() {return getParseFile("Default_ProfilePic");}

        public void setWholeMessage(ParseFile pp){
            put("Default_ProfilePic",pp);

        }
        public static ParseQuery<Repository_Parse> getQuery() {
            return ParseQuery.getQuery(Repository_Parse.class);
        }
    }