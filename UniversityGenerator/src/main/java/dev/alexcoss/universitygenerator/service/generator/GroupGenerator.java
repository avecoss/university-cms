package dev.alexcoss.universitygenerator.service.generator;

import dev.alexcoss.universitygenerator.dto.GGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class GroupGenerator {
    @Value("${data.group.amount}")
    private int groupCount;
    @Value("${data.group.quantity_characters}")
    private int quantityCharacters;
    @Value("${data.group.quantity_numbers}")
    private int quantityNumbers;
    @Value("${data.group.char_first}")
    private char charFirst;
    @Value("${data.group.char_last}")
    private char charLast;

    private final Random random = new Random();

    public List<GGroup> generateGroupList(int amount) {
        if (amount > 0)
            groupCount = amount;

        List<GGroup> groupList = new ArrayList<>();

        for (int i = 0; i < groupCount; i++) {
            GGroup group = generateRandomName();
            groupList.add(group);
        }

        return groupList;
    }

    private GGroup generateRandomName() {
        StringBuilder nameBuilder = new StringBuilder();

        for (int i = 0; i < quantityCharacters; i++) {
            nameBuilder.append(generateRandomCharacter());
        }

        nameBuilder.append('-');

        for (int i = 0; i < quantityNumbers; i++) {
            nameBuilder.append(generateRandomDigit());
        }

        GGroup group = new GGroup();
        group.setName(nameBuilder.toString());

        return group;
    }

    private int generateRandomDigit() {
        return random.nextInt(10);
    }

    private char generateRandomCharacter() {
        return (char) (charFirst + random.nextInt(charLast - charFirst + 1));
    }
}
