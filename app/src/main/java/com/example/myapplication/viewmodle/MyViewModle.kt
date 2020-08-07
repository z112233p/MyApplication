package com.example.myapplication.viewmodle

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val MainActVM = module {
    viewModel { MainActivityVM(get()) }
}

