package org.but.feec.csfd.service;

import org.but.feec.csfd.api.person.PersonBasicView;
import org.but.feec.csfd.api.person.PersonCreateView;
import org.but.feec.csfd.api.person.PersonDetailView;
import org.but.feec.csfd.api.person.PersonEditView;
import org.but.feec.csfd.api.person.PersonDeleteView;
import org.but.feec.csfd.api.title.TitleBasicView;
import org.but.feec.csfd.data.PersonRepository;

import java.util.List;

/**
 * Class representing business logic on top of the Persons
 */
public class PersonService {

    private PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public PersonDetailView getPersonDetailView(Long id) {
        return personRepository.findPersonDetailedView(id);
    }

    public List<PersonBasicView> getPersonsBasicView() {
        return personRepository.getPersonsBasicView();
    }

    public void createPerson(PersonCreateView personCreateView) {
        personRepository.createPerson(personCreateView);
    }

    public void editPerson(PersonEditView personEditView) {
        personRepository.editPerson(personEditView);
    }

    public void deletePerson(PersonDeleteView personDeleteView) {
        personRepository.deletePerson(personDeleteView);
    }

    public List<PersonBasicView> getPersonsFindView(String find, String choice) {
        return personRepository.getPersonFindView(find, choice);
    }

}