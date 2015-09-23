package com.zyj.dribbbleclient.app.db

import com.zyj.dribbbleclient.app.R
import se.emilsjolander.sprinkles.Query

/**
 * Created by zhaoyuanjie on 15/9/23.
 */
public object DataBase {

    public fun getShots(type: String): DbShots? {
        return Query.one(DbShots::class.java, R.raw.query_shots_with_type, type).get()
    }
}