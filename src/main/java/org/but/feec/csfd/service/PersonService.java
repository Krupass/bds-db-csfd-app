package org.but.feec.csfd.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.but.feec.csfd.api.PersonBasicView;
import org.but.feec.csfd.api.PersonCreateView;
import org.but.feec.csfd.api.PersonDetailView;
import org.but.feec.csfd.api.PersonEditView;
import org.but.feec.csfd.api.PersonDeleteView;
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

}