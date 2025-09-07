package com.xenonesis.womensafety.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.xenonesis.womensafety.data.dao.ContactDao
import com.xenonesis.womensafety.data.dao.LocationDao
import com.xenonesis.womensafety.data.dao.SosEventDao
import com.xenonesis.womensafety.data.model.Contact
import com.xenonesis.womensafety.data.model.LocationData
import com.xenonesis.womensafety.data.model.SosEvent
import com.xenonesis.womensafety.utils.Constants

@Database(
    entities = [Contact::class, SosEvent::class, LocationData::class],
    version = Constants.DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SosDatabase : RoomDatabase() {
    
    abstract fun contactDao(): ContactDao
    abstract fun sosEventDao(): SosEventDao
    abstract fun locationDao(): LocationDao
    
    companion object {
        @Volatile
        private var INSTANCE: SosDatabase? = null
        
        fun getDatabase(context: Context): SosDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SosDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                .addCallback(DatabaseCallback())
                .build()
                INSTANCE = instance
                instance
            }
        }
        
        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Pre-populate database with emergency service contacts
                INSTANCE?.let { database ->
                    // This will be executed in a background thread
                    database.contactDao()
                }
            }
        }
    }
}