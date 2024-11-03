package furrybuddy.domain;

import Domain.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

@ApplicationScoped
public class ApplicationState {

    private Map<UUID, Adopter> adopters;
    private Map<UUID, PetOwner> petOwners;
    private Map<UUID, Pet> pets;
    private Map<UUID, User> users;

    @PostConstruct
    public void init() {
        adopters = new TreeMap<>();
        petOwners = new TreeMap<>();
        pets = new TreeMap<>();
        users = new TreeMap<>();

        populateApplicationState();
    }

    // PET
    // CREATE
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

    // READ
    public Pet getPet(UUID petID) {
        return pets.get(petID);
    }

    public Map<UUID, Pet> getAllPets() {
        return pets;
    }

    // UPDATE
    public boolean setPet(UUID petID, Pet pet) {
        var thePet = pets.get(petID);
        if (thePet == null) {
            return false;
        }
//        thePet.replaceWith(pet);
        return true;
    }

    // DELETE
    public boolean removePet(UUID petID) {
        var pet = pets.get(petID);
        if (pet == null) {
            return false;
        }
        pets.remove(petID);
        return true;
    }

    // PET OWNER
    // CREATE
    public PetOwner addPetOwner(PetOwner petOwner) {
        if (petOwner.getUserID() != null) {
            return addPetOwner(petOwner.getUserID(), petOwner);
        }
        return addPetOwner(UUID.randomUUID(), petOwner);
    }

    public PetOwner addPetOwner(UUID userID, PetOwner petOwner) {
        var email = petOwner.getEmail();
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("PetOwner email is null or empty");
        }
        if (users.containsKey(email)) {
            throw new IllegalArgumentException("PetOwner email already exists");
        }
        if (petOwner.getPassword() == null || petOwner.getPassword().isBlank()) {
            throw new IllegalArgumentException("PetOwner password is null or empty");
        }

        petOwner.setUserID(userID);
        petOwners.put(userID, petOwner);
        users.put(userID, petOwner);
        return petOwner;
    }

    // READ
    public PetOwner getPetOwner(UUID userID) {
        return petOwners.get(userID);
    }

    public Map<UUID, PetOwner> getAllPetOwners() {
        return petOwners;
    }

    //UPDATE
    public boolean setPetOwner(UUID userID, PetOwner petOwner) {
        var thePetOwner = petOwners.get(petOwner.getUserID());
        if (thePetOwner == null) {
            return false;
        }
//        thePetOwner.replaceWith(petOwner);
        return true;
    }

    //DELETE
    public boolean removePetOwner(UUID petOwnerID) {
        var petOwner = petOwners.get(petOwnerID);
        if (petOwner == null) {
            return false;
        }
        petOwners.remove(petOwnerID);
        return true;
    }

    // ADOPTER
    // CREATE
    public Adopter addAdopter(Adopter adopter) {
        if (adopter.getUserID() != null) {
            return addAdopter(adopter.getUserID(), adopter);
        }
        return addAdopter(UUID.randomUUID(), adopter);
    }

    public Adopter addAdopter(UUID userID, Adopter adopter) {
        var email = adopter.getEmail();
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Adopter email is null or empty");
        }
        if (users.containsKey(email)) {
            throw new IllegalArgumentException("Adopter email already exists");
        }
        if (adopter.getPassword() == null || adopter.getPassword().isBlank()) {
            throw new IllegalArgumentException("adopter password is null or empty");
        }

        adopter.setUserID(userID);
        adopters.put(userID, adopter);
        users.put(userID, adopter);
        return adopter;
    }

    // READ
    public Adopter getAdopter(UUID userID) {
        return adopters.get(userID);
    }

    public Map<UUID, Adopter> getAllAdopters() {
        return adopters;
    }

    //UPDATE
    public boolean setAdopter(UUID userID, Adopter adopter) {
        var theAdopter = adopters.get(adopter.getUserID());
        if (theAdopter == null) {
            return false;
        }
//        theAdopter.replaceWith(adopter);
        return true;
    }

    //DELETE
    public boolean removeAdopter(UUID adopterID) {
        var adopter = adopters.get(adopterID);
        if (adopter == null) {
            return false;
        }
        adopters.remove(adopterID);
        return true;
    }

    // create objects
    private void populateApplicationState() {

        // create some pets
        var pepper = addPet(UUID.fromString("2b7da5cb"),
                new Pet("Pepper",
                        "Dog",
                        "Labrador",
                        false,
                        Pet.Gender.FEMALE,
                        "Cute and friendly",
                        "Playful",
                        "black",
                        false,
                        true,
                        true,
                        true,
                        2,
                        200.0,
                        Pet.Status.AVAILABLE,
                        true,
                        true,
                        "None"));

        var nala = addPet(UUID.fromString("f3b7d1b1"),
                new Pet("Nala",
                        "Dog",
                        "Shih-tzu",
                        true,
                        Pet.Gender.FEMALE,
                        "Cheerful dog",
                        "Independant",
                        "beige",
                        true,
                        true,
                        true,
                        true,
                        11,
                        250.0,
                        Pet.Status.AVAILABLE,
                        true,
                        true,
                        "Cyst on back"));

        var simba = addPet(UUID.fromString("b8d0c81d"),
                new Pet("Simba",
                        "Dog",
                        "Shih-tzu",
                        true,
                        Pet.Gender.MALE,
                        "Cheerful",
                        "Clingy",
                        "beige",
                        true,
                        true,
                        true,
                        true,
                        10,
                        250.0,
                        Pet.Status.AVAILABLE,
                        true,
                        true,
                        "Nearly blind"));

        // pet owner
        var alice = new PetOwner(
                "alice@gmail.com",
                "password123",
                "Alice",
                "Gold",
                new Location(
                        "Paris",
                        "75000",
                        "Champs-elysee"
                ),
                User.Role.PET_OWNER
        );

        // Adopter
        var bob = new Adopter(
                "bob@gmail.com",
                "1234",
                "Bob",
                "Sinclar",
                new Location(
                        "Manhattan",
                        "12345",
                        "5th Avenue"
                ),
                User.Role.ADOPTER
        );

        var advertisement = new Advertisement(pepper,alice, pepper.getDescription(),alice.getLocation(), Advertisement.Status.AVAILABLE);

        var request = new AdoptionRequest(bob, advertisement, AdoptionRequest.Status.PENDING);
        request.getAdopter();

    }
}
