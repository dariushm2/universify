package com.dariushm2.universify.Gallery;

import com.dariushm2.universify.model.frontend.GalleryListModel;
import com.dariushm2.universify.remote.NasaServices;
import com.dariushm2.universify.repository.ModelConverter;
import com.dariushm2.universify.repository.BaseSchedulersProvider;
import com.dariushm2.universify.repository.GalleryPresenter;
import com.dariushm2.universify.repository.TestSchedulersProvider;
import com.dariushm2.universify.view.gallery.GalleryDataEvents;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import io.reactivex.Flowable;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GalleryPresenterTest {


    private GalleryPresenter galleryPresenter;
    @Mock
    private GalleryDataEvents events;
    @Mock
    private NasaServices nasaServices;
    @Mock
    private ModelConverter converter;
    private BaseSchedulersProvider testSchedulersProvider;


    private static final String QUERY = "Universe";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        //converter = new ModelConverter(nasaServices);
        testSchedulersProvider = new TestSchedulersProvider();
        GalleryPresenter.init(nasaServices, events, converter, testSchedulersProvider);
        galleryPresenter = GalleryPresenter.getInstance();
        galleryPresenter.setSearchQuery(QUERY);

    }


    @Test
    public void showLoadingIndicatorTest() {
        Flowable<GalleryListModel> flowable = Flowable.just(new GalleryListModel(false, true, null, null));

        when(converter.fetchImages()).thenReturn(flowable);

        assertEquals(converter.fetchImages(), flowable);

        galleryPresenter.getNextImages();

        verify(events, times(2)).showLoadingIndicator();
    }

    @Test
    public void showErrorMessageTest() {
        Flowable<GalleryListModel> flowable = Flowable.just(new GalleryListModel(true, false, "Something went wrong!", null));

        when(converter.fetchImages()).thenReturn(flowable);

        assertEquals(converter.fetchImages(), flowable);

        galleryPresenter.getNextImages();
        verify(events).showLoadingIndicator();
    }

    @Test
    public void setUpAdapterAndView() {
        Flowable<GalleryListModel> flowable = Flowable.just(new GalleryListModel(false, false, null, new ArrayList<>()));

        when(converter.fetchImages()).thenReturn(flowable);

        assertEquals(converter.fetchImages(), flowable);

        galleryPresenter.getNextImages();
        verify(events).showLoadingIndicator();
    }
}
