package alex.com.livecurrencyconverter.app.components.app

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Inject

/**
 * Created by Alex Doub on 11/13/2019.
 */

@Module
class AppModule @Inject
constructor(val application: Application) {

    @Provides
    internal fun provideApplication(): Application {
        return application
    }

    @Provides
    internal fun provideContext(): Context {
        return application.applicationContext
    }
}

