package org.but.feec.csfd.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.but.feec.csfd.data.TitleRepository;
import org.but.feec.csfd.api.title.TitleBasicView;
import org.but.feec.csfd.api.title.TitleCreateView;
import org.but.feec.csfd.api.title.TitleDetailView;
import org.but.feec.csfd.api.title.TitleEditView;
import org.but.feec.csfd.api.title.TitleDeleteView;

import java.util.List;

public class TitleService {

    private TitleRepository titleRepository;

    public TitleService(TitleRepository titleRepository) {
        this.titleRepository = titleRepository;
    }

    public TitleDetailView getTitleDetailView(Long id) {
        return titleRepository.findTitleDetailedView(id);
    }

    public List<TitleBasicView> getTitlesBasicView() {
        return titleRepository.getTitlesBasicView();
    }

    public void createTitle(TitleCreateView titleCreateView) {
        // the following three lines can be written in one code line (only for more clear explanation it is written in three lines
        char[] originalPassword = titleCreateView.getPwd();
        char[] hashedPassword = hashPassword(originalPassword);
        titleCreateView.setPwd(hashedPassword);

        titleRepository.createTitle(titleCreateView);
    }

    public void editTitle(TitleEditView titleEditView) {
        titleRepository.editTitle(titleEditView);
    }

    public void deleteTitle(TitleDeleteView titleDeleteView) {
        titleRepository.deleteTitle(titleDeleteView);
    }

    private char[] hashPassword(char[] password) {
        return BCrypt.withDefaults().hashToChar(12, password);
    }

}