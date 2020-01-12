package com.dariushm2.universify.view.gallery;

import com.dariushm2.universify.model.frontend.GalleryListModel;

public interface GalleryDataEvents {

        void setUpAdapterAndView(GalleryListModel galleryListModel);

        void showErrorMessage(String error);

        void showLoadingIndicator();

}
