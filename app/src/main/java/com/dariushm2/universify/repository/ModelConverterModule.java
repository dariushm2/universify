package com.dariushm2.universify.repository;

import com.dariushm2.universify.remote.NasaServices;

import dagger.Module;
import dagger.Provides;

@Module
public class ModelConverterModule {

    private NasaServices nasaServices;

    public ModelConverterModule(NasaServices nasaServices) {
        this.nasaServices = nasaServices;
    }

    @Provides
    ModelConverter provideModelConverter() {
        return new ModelConverter(nasaServices);
    }
}
