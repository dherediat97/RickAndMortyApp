package com.dherediat97.rickandmorty.di


import com.dherediat97.rickandmorty.domain.repository.CharacterRepository
import com.dherediat97.rickandmorty.domain.repository.EpisodeRepository
import com.dherediat97.rickandmorty.presentation.characterdetail.CharacterDetailViewModel
import com.dherediat97.rickandmorty.presentation.characterlist.CharacterListViewModel
import com.dherediat97.rickandmorty.presentation.charactersearch.CharacterSearchViewModel
import com.dherediat97.rickandmorty.presentation.episodelist.EpisodeListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoriesModule = module {
    single { CharacterRepository() }
    single { EpisodeRepository() }
}
val viewModelsModule = module {
    viewModel {
        CharacterListViewModel(get())
    }
    viewModel {
        CharacterDetailViewModel(get())
    }
    viewModel {
        EpisodeListViewModel(get())
    }
    viewModel {
        CharacterSearchViewModel()
    }
}