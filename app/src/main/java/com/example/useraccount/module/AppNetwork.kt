package com.example.useraccount.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.example.useraccount.data.impl.FactsRepositoryImpl
import com.example.useraccount.data.impl.LanguageRepositoryImpl
import com.example.useraccount.data.impl.UserRepositoryImpl
import com.example.useraccount.data.local.dao.UserDao
import com.example.useraccount.data.local.UsersDatabase
import com.example.useraccount.data.local.dao.CurrentUserDao
import com.example.useraccount.data.network.ApiService
import com.example.useraccount.domain.repository.FactsRepository
import com.example.useraccount.domain.repository.LanguageRepository
import com.example.useraccount.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppNetwork {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://numbersapi.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): UsersDatabase {
        return Room.databaseBuilder(
            context,
            UsersDatabase::class.java,
            "user_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDatabaseDao(db: UsersDatabase): UserDao {
        return db.userDao()
    }

    @Provides
    @Singleton
    fun provideDatabaseCurrentUserDao(db: UsersDatabase): CurrentUserDao {
        return db.currentUserDao()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    private const val DATASTORE_NAME = "settings_prefs.preferences_pb"

    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = {
                context.dataStoreFile(DATASTORE_NAME)
            }
        )
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun provideUserRepositoryImpl(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun provideFactsRepositoryImpl(impl: FactsRepositoryImpl): FactsRepository

    @Binds
    @Singleton
    abstract fun provideLanguageRepositoryImpl(impl: LanguageRepositoryImpl): LanguageRepository

}