package com.example.myapplication.viewmodle

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val MainActVM = module {
    viewModel { MainActivityVM(get()) }
}

val MatchActVM = module {
    viewModel { MatchActivityVM(get()) }
}

val ChatRoomActVM = module {
    viewModel { ChatRoomActivityVM(get()) }
}

val EventsActVM = module {
    viewModel { EventsActivityVM(get()) }
}

val MapsActVM = module {
    viewModel { MapsActivityVM(get()) }
}