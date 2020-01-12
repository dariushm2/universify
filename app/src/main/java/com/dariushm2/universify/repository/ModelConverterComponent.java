package com.dariushm2.universify.repository;



import dagger.Component;

@Component(modules = ModelConverterModule.class)
public interface ModelConverterComponent {

    void inject(GalleryPresenter galleryPresenter);
}
