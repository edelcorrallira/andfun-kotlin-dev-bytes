/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.devbyteviewer.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface VideoDao {
    @Query("select * from databasevideo")
    fun getVideos(): LiveData<List<DatabaseVideo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg videos: DatabaseVideo)
}

/**
 * This class is our actual database class.
 *
 * To establish we take 3 steps:
 *
 * 1) We extend [RoomDatabase]
 * 2) We add an abstract DAO val ([VideoDao])
 * 3) We add the Database annotation and introduce two parameters: entities (list of entities
 * the database holds) and the version number (we're starting at 1)
 */
@Database(entities = [DatabaseVideo::class], version = 1)
abstract class VideosDatabase : RoomDatabase() {
    /**
     * Instance of DAO needed to gain access.
     */
    abstract val videoDao: VideoDao
}

/**
 * Singleton instance of the DATABASE initialized in get method.
 */
private lateinit var INSTANCE: VideosDatabase

/**
 * Retrieves database object instance singleton. Initializes if not yet initialized.
 */
fun getDatabase(context: Context): VideosDatabase {
    synchronized(VideosDatabase::class.java) {
        // Check if needed to create within synchronized for thread safety
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    VideosDatabase::class.java,
                    "videos").build()
        }
    }
    return INSTANCE
}