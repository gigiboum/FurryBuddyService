package furrybuddy.domain;

import Domain.Adopter;
import Domain.Pet;
import Domain.PetOwner;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class ApplicationState {

    private Map<UUID, Adopter> adopters;
    private Map<UUID, PetOwner> petOwners;
    private Map<UUID, Pet> pets;

    @PostConstruct
    public void init() {
        adopters = new HashMap<>();
        petOwners = new HashMap<>();
        pets = new HashMap<>();
        populateApplicationState();
    }

    private void populateApplicationState() {

        //Utils.testModeOn()

//        var Pepper =
    }

    public Pet addPet(Pet pet) {
        if (pet.getPetID() != null) {
            return addPet(pet.getPetID(), pet);
        }
        return addPet(UUID.randomUUID(), pet);
    }

    public Pet addPet(UUID petID, Pet pet) {
        pet.setPetID(petID);
        pets.put(petID, pet);
        return pet;
    }


}
