package org.but.feec.csfd.service;

import org.but.feec.csfd.api.dummy.*;
import org.but.feec.csfd.api.title.TitleBasicView;
import org.but.feec.csfd.data.DummyRepository;

import java.util.List;

public class DummyService {

    private DummyRepository dummyRepository;

    public DummyService(DummyRepository dummyRepository) {
        this.dummyRepository = dummyRepository;
    }

    public List<DummyBasicView> getDummyBasicView() {
        return dummyRepository.getDummyBasicView();
    }

    public void createString(DummyBasicView dummyBasicView) {
        dummyRepository.createString(dummyBasicView);
    }

    public void addNdelString(DummyBasicView dummyBasicView) {
        dummyRepository.addNdelString(dummyBasicView);
    }

    public List<DummyBasicView> getDummyFindView(String find) {
        return dummyRepository.getDummyFindView(find);
    }

}