package com.dariushm2.universify.repository;

import io.reactivex.Scheduler;

public interface BaseSchedulersProvider {

        Scheduler io();
        Scheduler ui();

}
