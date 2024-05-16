package dev.alexcoss.universitygenerator.service.generator;

import dev.alexcoss.universitygenerator.dto.PersonDTO;
import dev.alexcoss.universitygenerator.enumerated.Role;
import dev.alexcoss.universitygenerator.service.factory.PersonFactory;
import dev.alexcoss.universitygenerator.service.factory.StudentFactory;
import dev.alexcoss.universitygenerator.service.factory.TeacherFactory;
import dev.alexcoss.universitygenerator.util.FileReader;
import dev.alexcoss.universitygenerator.util.exception.InvalidRoleException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PersonGenerator<T extends PersonDTO> {
    private final LoginPasswordGenerator loginPasswordGenerator;
    private final Map<Role, PersonFactory<T>> factoryMap;

    private final List<String> firstNamesList;
    private final List<String> lastNamesList;

    @Value("${data.person.amount}")
    private int personAmount;

    public PersonGenerator(LoginPasswordGenerator loginPasswordGenerator,
                           StudentFactory studentFactory,
                           TeacherFactory teacherFactory,
                           @Value("${file-paths.first-names}") String firstNamesPath,
                           @Value("${file-paths.last-names}") String lastNamesPath) {
        this.loginPasswordGenerator = loginPasswordGenerator;
        this.firstNamesList = readList(firstNamesPath);
        this.lastNamesList = readList(lastNamesPath);

        this.factoryMap = new HashMap<>();
        factoryMap.put(Role.STUDENT, (PersonFactory<T>) studentFactory);
        factoryMap.put(Role.TEACHER, (PersonFactory<T>) teacherFactory);
    }

    public List<T> generatePersons(Role role, int amount) {
        if (amount > 0)
            personAmount = amount;

        List<T> persons = new ArrayList<>();
        for (int i = 0; i < personAmount; i++) {
            persons.add(generatePerson(role));
        }
        return persons;
    }

    private T generatePerson(Role role) {
        String firstName = getRandomField(firstNamesList);
        String lastName = getRandomField(lastNamesList);
        String username = loginPasswordGenerator.generateStartingLogin(firstName, lastName);
        String password = loginPasswordGenerator.generateStartingPassword();

        PersonFactory<T> factory = factoryMap.get(role);
        if (factory == null)
            throw new InvalidRoleException("Invalid role: " + role);

        return factory.createPerson(firstName, lastName, username, password);
    }

    private String getRandomField(List<String> list) {
        Random random = new Random();
        return list.get(random.nextInt(list.size() - 1));
    }

    private List<String> readList(String path) {
        FileReader reader = new FileReader();
        return reader.fileRead(path, bufferedReader -> {
            List<String> list = new ArrayList<>();
            bufferedReader.lines().forEach(list::add);
            return list;
        });
    }
}
