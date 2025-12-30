package com.example.swipeclean.di

import androidx.room.Room
import com.example.swipeclean.data.local.AppDatabase
import com.example.swipeclean.data.local.doa.TrashDao
import com.example.swipeclean.data.repository.OnboardingRepository
import com.example.swipeclean.data.repository.PhotoRepositoryImpl
import com.example.swipeclean.data.repository.ThemeRepository
import com.example.swipeclean.domain.repository.PhotoRepository
import com.example.swipeclean.domain.usecase.DeletePhotoPermanentlyUseCase
import com.example.swipeclean.domain.usecase.GetPhotoAlbumsUseCase
import com.example.swipeclean.domain.usecase.GetPhotoGroupsUseCase
import com.example.swipeclean.domain.usecase.GetPhotosByGroupUseCase
import com.example.swipeclean.domain.usecase.GetTrashItemsUseCase
import com.example.swipeclean.domain.usecase.MovePhotoToTrashUseCase
import com.example.swipeclean.domain.usecase.RestorePhotoByUriUseCase
import com.example.swipeclean.domain.usecase.RestorePhotoUseCase
import com.example.swipeclean.ui.viewmodel.GroupViewModel
import com.example.swipeclean.ui.viewmodel.PhotoViewModel
import com.example.swipeclean.ui.viewmodel.SplashViewModel
import com.example.swipeclean.ui.viewmodel.ThemeViewModel
import com.example.swipeclean.ui.viewmodel.ArchiveViewModel
import com.example.swipeclean.utils.AdsManager
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "swipclean_db"
        ).fallbackToDestructiveMigration().build()
    }

    single<TrashDao> { get<AppDatabase>().trashDao() }

    single<PhotoRepository> { PhotoRepositoryImpl(androidContext(), get()) }
    single { OnboardingRepository(androidContext()) }
    single { ThemeRepository(androidContext()) }

    // Use cases
    single { GetPhotoGroupsUseCase(get()) }
    single { GetPhotosByGroupUseCase(get()) }
    single { MovePhotoToTrashUseCase(get()) }
    single { DeletePhotoPermanentlyUseCase(get()) }
    single { RestorePhotoUseCase(get()) }
    single { GetTrashItemsUseCase(get()) }
    single { RestorePhotoByUriUseCase(get()) }
    single { GetPhotoAlbumsUseCase(get()) }

    //Ads
    single {AdsManager(androidContext())}

        // ViewModels
    viewModel { PhotoViewModel(get(), get(), get(), get()) }
    viewModel { GroupViewModel(get(), get(), get()) }
    viewModel { ArchiveViewModel(get(), get(), get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { ThemeViewModel(get()) }
}
