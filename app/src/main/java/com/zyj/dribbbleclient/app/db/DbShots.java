package com.zyj.dribbbleclient.app.db;

import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.annotations.AutoIncrement;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Key;
import se.emilsjolander.sprinkles.annotations.Table;

@Table("shots")
public class DbShots extends Model {
    @Key
    @AutoIncrement
    @Column("id")
    private long id;

    @Column("type")
    public String type;

    @Column("body")
    public String body;

    public long getId() {
        return id;
    }
}
